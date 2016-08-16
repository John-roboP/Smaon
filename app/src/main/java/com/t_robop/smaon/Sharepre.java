package com.t_robop.smaon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Ryo on 2016/07/16.
 * Sharedpreferences用のアクティビティ
 * CityIDを保存するクラス
 */

public  class Sharepre extends Activity {

  String readURL;
    String rTemp;
    private Context mContext;

    //  コンストラクター
   public  Sharepre(Context context){

        readURL="xxx";
       mContext = context;

   }
    public  void share(String URLs){

           readURL =URLs;

        SharedPreferences datum = mContext.getSharedPreferences("DataSave",Context.MODE_PRIVATE);
//07/19完了


        SharedPreferences.Editor editor2 = datum.edit();
        editor2.putString("Cid",readURL);

        editor2.apply();    //保存



    }
    public void graTemp(String Temp){

        rTemp = Temp;

        SharedPreferences Ondo =mContext.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor4 = Ondo.edit();
        editor4.putString("rOndo",rTemp);     //初回起動判定を１にする
        editor4.apply();    //保存
    }


}
