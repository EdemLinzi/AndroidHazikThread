package com.example.threads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    ProgressBar progressBarIntentService;
    ProgressBar progressBarAsyncTask;
    ProgressBar progressBarAsyncTaskLoader;
    ProgressBar progressBarThread;

    MyReciver myReciver ;

    Button btnAsyncTask;
    Button btnAsyncTaskLoader;
    Button btnThread;
    Button btnIntentService;

    TextView textViewIntentService;
    TextView textViewAsyncTask;
    TextView textViewAsyncTaskLoader;
    TextView textViewThread;

    long start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","I am the " + Thread.currentThread().getId() + " Thread");

        myReciver = new MyReciver();

        btnIntentService = findViewById(R.id.startService);
        btnAsyncTask = findViewById(R.id.startAsyncTask);
        btnAsyncTaskLoader = findViewById(R.id.startAsyncTaskLoader);
        btnThread = findViewById(R.id.startThread);

        progressBarIntentService =(ProgressBar) findViewById(R.id.progressBarIntentService);
        progressBarAsyncTask = (ProgressBar) findViewById(R.id.progressBarAsynkTask);
        progressBarAsyncTaskLoader = (ProgressBar) findViewById(R.id.progressBarAsyncTaskLoader);
        progressBarThread = (ProgressBar) findViewById(R.id.progressBarThread);

        textViewIntentService = findViewById(R.id.tvIntentService);
        textViewAsyncTask = findViewById(R.id.tvAsyncTask);
        textViewAsyncTaskLoader = findViewById(R.id.tvAsyncTaskLoader);
        textViewThread = findViewById(R.id.tvThread);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.threads.MainActivity");
        registerReceiver(myReciver,intentFilter);
        Log.i("MainActivity","RegisterReciver is done");

        if (getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

    }

   public void startIntentService(View v){
       Log.i("MainActivity","startIntentService");
       Intent intent = new Intent(this,MyIntentService.class);
       intent.putExtra("value",10000);
       startService(intent);
       btnIntentService.setEnabled(false);
   }

   public void startAsyncTask(View v){
       Log.i("MainActivity","startAsyncTask");
       MyAsyncTask myAsyncTask = new MyAsyncTask(this,btnAsyncTask,textViewAsyncTask,progressBarAsyncTask);
       myAsyncTask.execute();
       btnAsyncTask.setEnabled(false);
   }

    public void startAsyncTaskLoader(View v){
        Log.i("MainActivity","startAsyncTaskLoader");
        getSupportLoaderManager().restartLoader(0,new Bundle(),this);
        btnAsyncTaskLoader.setEnabled(false);
        start = System.currentTimeMillis();
    }

    public void startThread(View v){
        UUIDThread();
    }

    private void UUIDThread(){
        Log.i("MainActivity","startUUIDThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                btnThread.post(new Runnable() {
                    @Override
                    public void run() {
                        btnThread.setEnabled(false);
                    }
                });

                long start = System.currentTimeMillis();
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 100000; j++) {
                        UUID.randomUUID();
                    }

                    Intent progress = new Intent();
                    progress.putExtra("progress", (i + 1));
                    progress.putExtra("Type", "Thread");
                    long end = System.currentTimeMillis() - start;
                    progress.putExtra("time", end);
                    progress.setAction("com.example.threads.MainActivity");
                    sendBroadcast(progress);

                }

                btnThread.post(new Runnable() {
                    @Override
                    public void run() {
                        btnThread.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.i("MainActivity","onCreateLoader");
        return new MyAsyncTaskLoader(this,textViewAsyncTaskLoader,progressBarAsyncTaskLoader);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        Log.i("MainActivity","onLoadFinished");
        long result =  System.currentTimeMillis() - start ;
        textViewAsyncTaskLoader.setText(""+result);
        btnAsyncTaskLoader.setEnabled(true);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    class MyReciver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {


           String type = intent.getStringExtra("Type");
           if(type.equals("Thread")){
               int progressThread = intent.getIntExtra("progress",0);
               long timeThread = intent.getLongExtra("time",-1);

               if(progressThread == 100){
                   textViewThread.setText(""+timeThread);
               }
               progressBarThread.setProgress(progressThread);
           }
           else{
               int progress = intent.getIntExtra("progress",0);
               long time = intent.getLongExtra("time",-1);

               if(progress==100) {
                   textViewIntentService.setText(""+time);
                   btnIntentService.setEnabled(true);
               }
               progressBarIntentService.setProgress(progress);

           }


       }
   }

}
