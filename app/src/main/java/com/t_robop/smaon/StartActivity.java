package com.t_robop.smaon;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getLoaderManager().restartLoader(ADDRESSLOADER_ID, null, this);

    }

        @Override
        public Loader<JSONObject> onCreateLoader ( int id, Bundle args){
            // TODO 自動生成されたメソッド・スタブ

            String url = "http://192.168.0.31/index.html";

            return new AsyncWorker(this, url);
        }

        @Override
        public void onLoadFinished (Loader < JSONObject > loader, JSONObject data){
            // TODO 自動生成されたメソッド・スタブ
            if (data != null) {

                try {

                    JSONArray jsonArray = data.getJSONArray("pidatas"); //pidatasの配列参照

                    JSONObject object = jsonArray.getJSONObject(0); //一番初めのデータを参照,0はdate,1はセルシウス
                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    //   String str = (String) object.get("name");
                    String str = object.getString("date");   //strにdate or celsius を代入
                    String str2 = obj1.getString("celsius");

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
        }

        @Override
        public void onLoaderReset (Loader < JSONObject > loader) {
            // TODO 自動生成されたメソッド・スタブ

        }

    }
