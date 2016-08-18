package com.t_robop.smaon;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


/**
 * Created by Ryo on 2016/08/18.
 */
public  class Yesterday extends AsyncTaskLoader<JSONObject> {
    String localurl;
    String yestadataurl;
    String[] Ystr;
    String   Ynum="0";//getString用
    JSONObject[] Yobject;//宣言
    static Sharepre Yshare;



    int i;

    public Yesterday(Context context, String urlText) {
        super(context);
        localurl=urlText;


    }


    public  JSONObject loadInBackground(){
        Yobject= new JSONObject[23];    //24回回します。
        Ystr   = new String[23];


        yestadataurl=localurl+"index2.html";

        HttpClient httpClientY = new DefaultHttpClient();

        StringBuilder Yurl = new StringBuilder(yestadataurl);
        HttpGet request = new HttpGet(Yurl.toString());
        HttpResponse httpResponseY = null;

        try {
            httpResponseY = httpClientY.execute(request);
        } catch (Exception e) {
            Log.d("YJsonLoader", "YError Execute");

        }

        int Ystatus = httpResponseY.getStatusLine().getStatusCode();

        if (HttpStatus.SC_OK == Ystatus) {

            try {

                ByteArrayOutputStream YoutputStream = new ByteArrayOutputStream();
                httpResponseY.getEntity().writeTo(YoutputStream);
                String Ydata;
                Ydata = YoutputStream.toString(); // JSONデータ

                JSONObject yesterdayObject = new JSONObject(Ydata);
                try {

                    JSONArray YjsonArray = yesterdayObject.getJSONArray("yesterdata"); //pidatasの配列参照

                    for(i=0;i<24;i++) {

                        Yobject[i] = YjsonArray.getJSONObject(i); //一番初めのデータを参照,0はdate,1はセルシウス
                        Ystr[i]=Yobject[i].getString(Ynum);
                        //    Log.d("JSONObject", str);

                        Yshare.Yestersave(Ystr[i],i);

                        Ynum=Ynum+1;
                    }
                    //保存
                } catch (JSONException e) {
                    Log.d("onLoadFinished", "JSONのパースに失敗しました。 YJSONException=" + e);
                }



            } catch (Exception e) {
                Log.d("YJsonLoader", "YError");
            }
        } else {
            Log.d("YJsonLoader", "Status" + Ystatus);

        }

    return null;}

}



