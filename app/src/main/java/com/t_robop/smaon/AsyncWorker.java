package com.t_robop.smaon;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by Ryo on 2016/06/28.
 */
public class AsyncWorker extends AsyncTaskLoader<JSONObject> {

     String urlText;
    private int i=0;
    String localurl;
    String yestadataurl;
    Context Acontext;




    public AsyncWorker(Context context, String urlText) {
        super(context);
        this.Acontext = context;

        this.urlText = urlText;
    }

    @Override
    public JSONObject loadInBackground() {

    localurl=urlText;
        //sYesterday.();
        for(i=0;i<2;i++){
            if(i==1) {


                HttpClient httpClient = new DefaultHttpClient();

                StringBuilder uri = new StringBuilder(urlText);
                HttpGet request = new HttpGet(uri.toString());
                HttpResponse httpResponse = null;

                try {
                    httpResponse = httpClient.execute(request);
                } catch (Exception e) {
                    Log.d("JsonLoader", "Error Execute");
                    return null;
                }

                int status = httpResponse.getStatusLine().getStatusCode();

                if (HttpStatus.SC_OK == status) {

                    try {

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        httpResponse.getEntity().writeTo(outputStream);
                        String data;
                        data = outputStream.toString(); // JSONデータ

                        JSONObject rootObject = new JSONObject(data);

                        return rootObject;

                    } catch (Exception e) {
                        Log.d("JsonLoader", "Error");
                    }
                } else {
                    Log.d("JsonLoader", "Status" + status);
                    return null;
                }
            }else{  //機能のデータを取得



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

                            SharedPreferences Ydat = Acontext.getSharedPreferences("YSave",Context.MODE_PRIVATE);
                            SharedPreferences.Editor Yed = Ydat.edit();
                            Yed.putString("Yesterdayjson",YjsonArray.toString());
                            Yed.apply();

                     /*       for(i=0;i<24;i++) {

                                Yobject[i] = YjsonArray.getJSONObject(i); //一番初めのデータを参照,0はdate,1はセルシウス
                                Ystr[i]=Yobject[i].getString(Ynum);
                                //    Log.d("JSONObject", str);


                             //   Yshare.Yestersave(Ystr[i],i);



                                Ynum=Ynum+1;
                            }       */
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

            }
        }
        return null;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

}