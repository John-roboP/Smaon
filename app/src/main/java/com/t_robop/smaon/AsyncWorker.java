package com.t_robop.smaon;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ryo on 2016/06/28.
 */
public class AsyncWorker extends AsyncTaskLoader<JSONObject> {

    private String urlText;

    public AsyncWorker(Context context, String urlText) {
        super(context);
        // TODO 自動生成されたコンストラクター・スタブ
        this.urlText = urlText;
    }

    @Override
    public JSONObject loadInBackground() {
        // TODO 自動生成されたメソッド・スタブ

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
        return null;
    }

    @Override
    protected void onStartLoading() {
        // TODO 自動生成されたメソッド・スタブ
        forceLoad();
    }

}