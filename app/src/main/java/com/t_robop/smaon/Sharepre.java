package com.t_robop.smaon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Ryo on 2016/07/16.
 * Sharedpreferences用のアクティビティ
 */

public  class Sharepre extends Activity {
  //  String URLs;
  String readURL;
    private Context mContext;

    //  コントラクター
   public  Sharepre(Context context){
       // this.context = context;
        readURL="xxx";
       mContext = context;

   }
    public  void share(String URLs){

           readURL =URLs;



        SharedPreferences datum = mContext.getSharedPreferences("DataSave",Context.MODE_PRIVATE);
//TODO 　ここが動かない。

        //SharedPreferences datum = getSharedPreferences("DataSave", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = datum.edit();
        editor2.putString("Cid",readURL);

        editor2.apply();

        //SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String Le = datum.getString("Cid","Nothing");

        Toast.makeText(this, Le, Toast.LENGTH_LONG).show();

    }


}
