package com.example.threads;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {
    ProgressBar progressBar;
    TextView textView;

    public MyAsyncTaskLoader(Context context, TextView textView, ProgressBar progressBar){
        super(context);
        this.progressBar = progressBar;
        this.textView = textView;
       // Log.i("MyAsyncTaskLoader", "Im running the " + Thread.currentThread().getId() + " Thread");

    }

    @Override
    public String loadInBackground() {
        long start = System.currentTimeMillis();
        for(int i = 0;i<100;i++){
            for(int j = 0;j<100000;j++){
                UUID.randomUUID();
            }
           // Log.i("MyAsyncTaskLoader","Progress "+i+ "%");
            progressBar.setProgress((i+1));
        }
        long result = System.currentTimeMillis() - start;
        Log.i("MyAsyncTaskLoader","Time "+result);

        return "AsyncTaskLoaderFinished";
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
