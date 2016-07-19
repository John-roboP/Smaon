package com.t_robop.smaon;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends Activity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final int ADDRESSLOADER_ID = 0;
    String str=null;
    String str2=null;


    int Level = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startService(new Intent(getBaseContext(),TempService.class));   //サービスの開始


        SharedPreferences data = getSharedPreferences("DataSave",MODE_PRIVATE);    //sharedPreference
        Level = data.getInt("sStart",0);




        if(Level==0){   //初回起動判定→設定画面に飛ばす。


            Intent setIntent =new Intent (this,SettingActivity.class);

            startActivity(setIntent); //settingActivity変遷

        }else {

            getLoaderManager().restartLoader(ADDRESSLOADER_ID, null, this); //スレッド処理開始



        }


    }
  /*  private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
            startService(new Intent(NotificationActivity.this, NotificationChangeService.class));
        }
    };

    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
            stopService(new Intent(NotificationActivity.this, NotificationChangeService.class));
        }
    };

            */
    @Override
        public Loader<JSONObject> onCreateLoader ( int id, Bundle args){


            String url = "http://192.168.0.31/index.html";

            return new AsyncWorker(this, url);
        }

        @Override
        public void onLoadFinished (Loader < JSONObject > loader, JSONObject data){

            if (data != null) {

                try {

                    JSONArray jsonArray = data.getJSONArray("pidatas"); //pidatasの配列参照

                    JSONObject object = jsonArray.getJSONObject(0); //一番初めのデータを参照,0はdate,1はセルシウス
                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    //   String str = (String) object.get("name");
                    str = object.getString("date");   //strにdate or celsius を代入
                    str2 = obj1.getString("celsius");

                    Log.d("JSONObject", str);

                 //   str1 = str2;
                    //String date = jsonObject.getString("name");
                    Log.d("JSONObject", "JSONObject");

                } catch (JSONException e) {
                    Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
                }


            } else {
                Log.d("onLoadFinished", "onLoadFinished error!");
            }

            Intent sIntent = new Intent();      //インテント生成

            sIntent.putExtra("date",str);       //日付を送っている
            sIntent.putExtra("temper",str2);        //温度データを送っている
            sIntent.setClass(this,MainActivity.class);


            // MainActivity の起動
            startActivity(sIntent);

        }

        @Override
        public void onLoaderReset (Loader < JSONObject > loader) {


        }

    }
