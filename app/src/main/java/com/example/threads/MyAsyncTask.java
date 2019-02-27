package com.example.threads;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

public class MyAsyncTask extends AsyncTask<Integer,Integer ,String> {
    Context context;
    Button button;
    ProgressBar progressBar;
    TextView textView;
    long result;

    public MyAsyncTask(Context context, Button button, TextView textView, ProgressBar progressBar){
        this.context = context;
        this.button = button;
        this.progressBar = progressBar;
        this.textView = textView;
        Log.i("MyAsynkTask", "Im running the " + Thread.currentThread().getId() + " Thread");

    }

    @Override
    protected String doInBackground(Integer... integers) {
        long start = System.currentTimeMillis();
        for(int i = 0;i<100;i++){
            for(int j = 0;j<100000;j++) {
                UUID.randomUUID();
            }
            Log.i("MyAsynkTask", "I generate " + i + "%");

            publishProgress(i);
        }
        result = System.currentTimeMillis() - start;
        return "Finished";
    }

    @Override
    protected void onPreExecute() {
        progressBar.setMax(100);
        progressBar.setProgress(0);

    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        textView.setText(""+result);
        button.setEnabled(true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        progressBar.setProgress(progress+1);
    }
}
