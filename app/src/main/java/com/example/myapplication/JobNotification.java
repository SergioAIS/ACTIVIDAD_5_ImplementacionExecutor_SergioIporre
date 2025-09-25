package com.example.myapplication;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JobNotification extends JobService {

    private ExecutorService executor;
    private Future<?> runningTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newSingleThreadExecutor();
        }
        runningTask = executor.submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) { }
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(JobNotification.this, "JobNotification ejecutado!", Toast.LENGTH_SHORT).show()
            );
            jobFinished(params, false);
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (runningTask != null && !runningTask.isDone()) {
            runningTask.cancel(true);
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
        return false;
    }
}