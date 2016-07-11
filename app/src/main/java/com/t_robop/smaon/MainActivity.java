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
    static TextView txt5;                                   //てーあん
    static TextView txt6;
    static String ondo;                                     //ホリエモンの温度
    static String ondocp;
    static String Str;                                      //ラズパイパイの日付
    static String Str2;                                     //ラズパイパイの温度
    static String Str2cp;
    static double Txt=0.0;                                       //ホリエモンの温度を数値化
    static double Txt2=0.0;                                      //ラズパイパイの温度を数値化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt3 = (TextView)findViewById(R.id.textView3);      //温度（ラズパイ）
        txt = (TextView)findViewById(R.id.textView);        //日付
        txt2 = (TextView)findViewById(R.id.textView2);      //温度（ホリエモン）
        txt5 = (TextView)findViewById(R.id.textView5);      //提案文
        txt6 = (TextView)findViewById(R.id.textView6);

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

                    txt6.setText(Str2);
                    ondocp = ondo.substring(0,ondo.length());
                    Str2cp = Str2.substring(0,Str2.length());
                    Txt = Double.parseDouble(ondocp);
                    Txt2 = Double.parseDouble(Str2cp);

                    if(Txt < Txt2){             //堀江<パイ
                        txt5.setText("現在予想より温度が高くなっております。\n");
                        if(Txt2-Txt > 5){
                            txt5.append("水分補給をこまめに行いましょう。\n");
                        }
                    }
                    else if(Txt2 < Txt){
                        txt5.setText("現在予想より温度が低くなっております。\n");
                        if(Txt-Txt2 > 5){
                            txt5.append("上着を羽織って暖かくするようにしましょう。\n");
                        }
                    }

                    txt5.append("体調管理に気を付けましょう。\n");

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

