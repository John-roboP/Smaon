package com.t_robop.smaon;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Calendar;

public class StartActivity extends Activity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final int ADDRESSLOADER_ID = 0;
    private  String  str = "aaa";   //取得時間
    public String str2 = "bbb"; //現在の気温
    String[] Ytime;
    JSONObject[] Yobject;   //宣言
    private String Ynum="0";
    private String Yjson;
    int Level = 0;
    int penint = 0;
    double estimatemp; //予則温度
    double estimatempY;
    int estimatempI;
    double yestertempPD;
    double yestertempND;
    int yestertempP;//+1時間
    int yestertempM; //−1時間
    int yestertempN; //今の時間
    int yesterR;
    private double nowtemp;    //リアルタイム温度
    int zP;//引き算後
    int zPa;//絶対値



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Yobject =new JSONObject[24];
        Ytime =new String[24];

        SharedPreferences datasy = getSharedPreferences("DataSave", MODE_PRIVATE);    //sharedPreference
        Level = datasy.getInt("sStart", 0); //初期値0
        penint = datasy.getInt("tdata",0);



        if (Level == 0) {   //初回起動判定→設定画面に飛ばす。


            Intent setIntent = new Intent(this, SettingActivity.class);

            startActivity(setIntent); //settingActivity変遷

        } else {

            getLoaderManager().restartLoader(ADDRESSLOADER_ID, null, this); //スレッド処理開始


        }


    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {


        String url = "http://japantelecom.sakura.ne.jp/";

        return new AsyncWorker(this, url);

    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

        SharedPreferences Ydata = getSharedPreferences("YSave", MODE_PRIVATE);    //sharedPreference
        Yjson = Ydata.getString("Yesterdayjson","abc" ); //初期値abc

        /**
        ！昨日の温度データを配列に代入！
         **/

        try {
            JSONArray Yarray = new JSONArray(Yjson);
            for(int i = 0, length = Yarray.length(); i < length; i++){
                Ynum= String.valueOf(i);
                Yobject[i] = Yarray.getJSONObject(i);
                Ytime[i]=Yobject[i].getString(Ynum);        //jsonobjectからstringに変換

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }




        if (data != null) {

            try {

                JSONArray jsonArray = data.getJSONArray("pidatas"); //pidatasの配列参照

                JSONObject object = jsonArray.getJSONObject(0); //一番初めのデータを参照,0はdate,1はセルシウス
                JSONObject obj1 = jsonArray.getJSONObject(1);

                str = object.getString("date");   //strにdate or celsius を代入
                str2 = obj1.getString("celsius");

                Log.d("JSONObject", str);


                Log.d("JSONObject", "JSONObject");

            } catch (JSONException e) {
                Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
            }


        } else {
            Log.d("onLoadFinished", "onLoadFinished error!");
        }

        //nullデータを受け取った時の処理
        if (str.equals("aaa") || str2.equals("bbb")) {
            str = "xxx";
            str2 = "0.0";
        }
        /**
         *
         * 一時間後の温度を予想するアルゴリズム
         *
         */

        Calendar calendar = Calendar.getInstance();//インスタンス化
        int hour = calendar.get(Calendar.HOUR_OF_DAY);  //時間を取得
        int minute = calendar.get(Calendar.MINUTE);      //分を取得
        nowtemp    = Double.parseDouble(str2);   //現在の温度をint型に変換

        if(hour==23){
            estimatemp=99.9;    //error温度を送信
        }
        else if(hour==22){   //ない配列にアクセスしないようにしている
            yestertempM = Integer.parseInt(Ytime[hour-1]);    //-1
            yestertempN = Integer.parseInt(Ytime[hour]);    //今の温度をStringからint型に変換
            yestertempP = Integer.parseInt(Ytime[hour + 1]);
        }
        else if(minute>=40 || hour == 0 ){ //40より大きかったら次の1時間を比較
            yestertempM = Integer.parseInt(Ytime[hour]);
            yestertempN = Integer.parseInt(Ytime[hour+1]);    //今の温度をStringからint型に変換
            yestertempP = Integer.parseInt(Ytime[hour+2]);


        }else {
            yestertempM = Integer.parseInt(Ytime[hour-1]);    //-1
            yestertempN = Integer.parseInt(Ytime[hour]);    //今の温度をStringからint型に変換
            yestertempP = Integer.parseInt(Ytime[hour + 1]);
        }

       zP=yestertempN-yestertempP;

        zPa=Math.abs(zP);   //絶対値


        if(zPa<200) {
                estimatemp=nowtemp;     //第2.5.8パターン
        } else  if (yestertempN - yestertempM > 0 && yestertempP - yestertempN > 0) {
                //第1パターン
            estimatemp =yestertempP/yestertempN * nowtemp;
        }else if(yestertempN-yestertempM>0 && yestertempP-yestertempN>0){
                //第3パターン
            estimatemp=(yestertempN+(nowtemp*1000-yestertempN)) / 1000;
        }else if(yestertempM-yestertempN>0 && yestertempP-yestertempN>0){
                //第7パターン
            estimatemp=(yestertempN+(nowtemp*1000-yestertempN)) / 1000;
        }else  if(yestertempM-yestertempN>0 && yestertempP-yestertempN>0){
            //第9パターン
            estimatemp =yestertempP/yestertempN * nowtemp;
        }else if(yestertempP-yestertempN>0){
            //第4パターン
            yestertempND=yestertempN;
            yestertempPD=yestertempP;
            estimatempY = (yestertempPD/yestertempND*nowtemp) *1.05;
            estimatemp=Math.round(estimatempY);
        }else if(yestertempN-yestertempP>0){
            //第6パターン
            yestertempND=yestertempN;
            yestertempPD=yestertempP;
            estimatempY =(yestertempPD/yestertempND*nowtemp) *0.95 ;
            estimatemp=Math.round(estimatempY);

        }




        Intent sIntent = new Intent();      //インテント生成

        sIntent.putExtra("estima", estimatemp);  //1時間後の予測温度を送っている
        sIntent.putExtra("jsarray",Ytime);
        sIntent.putExtra("date", str);       //日付を送っている
        sIntent.putExtra("temper", str2);        //温度データを送っている
        sIntent.setClass(this, MainActivity.class);


        // MainActivity の起動
        startActivity(sIntent);

        StartActivity.this.finish();

    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {


    }

}