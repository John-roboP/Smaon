package com.t_robop.smaon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Ryo on 2016/07/16.
 * Sharedpreferences用のアクティビティ
 */

public class Sharepre extends Activity {
  //  String URLs;
  String readURL;

    //  コントラクター
   public  Sharepre(){

    }
    public void share(String URLs){

           readURL =URLs;
//TODO 　ここが動かない。

        SharedPreferences data = getSharedPreferences("DS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = data.edit();
        editor2.putString("Cid",readURL);

        editor2.apply();

        //SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String Le = data.getString("Cid","0");

        Toast.makeText(this, Le, Toast.LENGTH_LONG).show();

    }


}
