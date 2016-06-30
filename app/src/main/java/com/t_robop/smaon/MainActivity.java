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
    static TextView txt;
    static TextView txt2;
    static String ondo;
    static String Str;
    static String Str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt3 = (TextView)findViewById(R.id.textView3);      //温度（ラズパイ）
        txt = (TextView)findViewById(R.id.textView);        //日付
        txt2 = (TextView)findViewById(R.id.textView2);      //温度（ホリエモン）

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
        private final String uri = "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010";       //JSONデータのあるURLを設定

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
                        JSONArray eventArray = result.getJSONArray("forecasts");    //配列データを取得
                        for (int i = 0; i < 1; i++) {                     //JSONのデータを追加
                            JSONObject eventObj = eventArray.getJSONObject(1);
                            JSONObject event = eventObj.getJSONObject("temperature").getJSONObject("min");
                            ondo = event.getString("celsius");
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
            txt.setText(Str);
            txt2.setText(Str2);
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

