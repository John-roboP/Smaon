package com.t_robop.smaon;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Ryo on 2016/07/19.
 *
 */


public class TempService extends Service {

    final static String TAG = "TempService";


  @Override
    public IBinder onBind(Intent intent) {


      return null;
  }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

    /**    try{
            Thread.sleep(60000);     //ミリ秒sleep2分
        }catch (InterruptedException e){        //sleep処理.遅延
        }

     **/



//TODO サービスの起動と時間での分岐


       Context context = getBaseContext();

        Intent Vintent = new Intent(context, TempService.class);

        PendingIntent pendingIntent = PendingIntent.getService(context, -1, Vintent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),AlarmManager.INTERVAL_HOUR, pendingIntent);

       // Log.d(TAG, "Alarm");

        Calendar cal = Calendar.getInstance();

        int minute = cal.get(Calendar.MINUTE);




      if(minute==0) {//毎時0時に起動

          SharedPreferences tempg =getSharedPreferences("DataSave", Context.MODE_PRIVATE);
          SharedPreferences.Editor tor = tempg.edit();
          tor.putInt("tdata",1);     //初回起動判定を１にする
          tor.apply();    //保存

          Intent servintent = new Intent();
          servintent.setClass(this, AsyncWorker.class);
          startActivity(servintent);


      }else{

      }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }


}