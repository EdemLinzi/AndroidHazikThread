package com.example.threads;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.util.UUID;

public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
        Log.i("MyIntentService", "Im running the " + Thread.currentThread().getId() + " Thread");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyIntentService","onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("MyIntentService","onStart");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyIntentService","onHadndleIntent");
        int value = intent.getIntExtra("value",0);
        long start = System.currentTimeMillis();

        for(int k = 0;k<100;k++){
            for (int i = 0; i < 100000; i++) {
                UUID.randomUUID();
                //Log.i("MyIntentService", "I am the " + Thread.currentThread().getId() + " Thread");
                //Log.i("MyIntentService", "I create " + i + " UUID");
            }
            long result =System.currentTimeMillis() - start;
           // Log.i("MyIntentService","Time "+result);

            Intent progress =  new Intent();
            progress.putExtra("progress",(k+1));
            progress.putExtra("Type","IntentService");
            progress.putExtra("time",result);
            progress.setAction("com.example.threads.MainActivity");
            sendBroadcast(progress);
           // Log.i("MyIntentService", "Progress was sended");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyIntentService","onDestroy");
    }
}
