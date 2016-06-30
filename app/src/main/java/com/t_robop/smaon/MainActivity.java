package com.t_robop.smaon;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static TextView txt3;
    static String ondo;
    String Str;
    String Str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt3 = (TextView)findViewById(R.id.textView3);      //温度（現在は名前）

        Intent intent = getIntent();
        Str = intent.getStringExtra("date");
        Str2 = intent.getStringExtra("cels");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private final String uri = "http://192.168.0.31";       //JSONデータのあるURLを設定

        public PlaceholderFragment() {
        }

        @Override
        public void onStart() {
            super.onStart();
            AsyncJsonLoader asyncJsonLoader = new AsyncJsonLoader(new AsyncJsonLoader.AsyncCallback() {
                // 実行前
                public void preExecute() {
                }
                // 実行後
                public void postExecute(JSONObject result) {            //JSONデータがなかったら
                    if (result == null) {
                        showLoadError();                                // エラーメッセージを表示
                        return;
                    }
                    try{
                        JSONArray eventArray = result.getJSONArray("pidatas");    //配列データを取得
                        for (int i = 0; i < 1; i++) {                     //JSONのデータを追加
                            JSONObject eventObj = eventArray.getJSONObject(1);
                            //JSONObject event = eventObj.getJSONObject("temperature").getJSONObject("min");
                            ondo = eventObj.getString("celsius");
                            txt3.setText(ondo);
                        }

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        showLoadError(); // エラーメッセージを表示
                    }
                }
                // 実行中
                public void progressUpdate(int progress) {
                }
                // キャンセル
                public void cancel() {
                }
            });
            // 処理を実行
            asyncJsonLoader.execute(uri);
        }

        // エラーメッセージ表示
        private void showLoadError() {
            Toast toast = Toast.makeText(getActivity(), "データを取得できませんでした。", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

