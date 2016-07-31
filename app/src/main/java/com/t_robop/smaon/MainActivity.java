package com.t_robop.smaon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
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
    static TextView txt4;
    static ImageView img;
    static String ondo;                                     //ホリエモンの温度
    static String ondocp;
    static String tenki;                                    //その日の天気
    static String Str;                                      //ラズパイパイの日付
    static String Str2;                                     //ラズパイパイの温度
    static String Str2cp;
    static double Txt=0.0;                                       //ホリエモンの温度を数値化
    static double Txt2=0.0;                                      //ラズパイパイの温度を数値化
    static String cityId;
    static String time;
    static int humid=0;
    static int Intime=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt3 = (TextView)findViewById(R.id.textView3);      //温度（ラズパイ）
        txt = (TextView)findViewById(R.id.textView);
        txt2 = (TextView)findViewById(R.id.textView2);      //温度（ホリエモン）
        txt5 = (TextView)findViewById(R.id.textView5);      //提案文
        txt6 = (TextView)findViewById(R.id.textView6);
        txt4 = (TextView)findViewById(R.id.textView4);      //日付
        img = (ImageView)findViewById(R.id.imageView2);

        Intent intent = getIntent();                            //ラズパイのデータ取得
        Str = intent.getStringExtra("date");
        Str2 = intent.getStringExtra("temper");

        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);        //openweathermapのデータ取得
        cityId = data.getString("Cid", "0");


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private final String uri = "http://api.openweathermap.org/data/2.5/forecast/city?id="+cityId+"&APPID=d943f13cb21447ca156076c493e2f236";             //JSONデータのあるURLを設定

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
                        JSONArray eventArray = result.getJSONArray("list");//配列データを取得
                        JSONObject eventObj = eventArray.getJSONObject(0);
                        JSONArray tenkiArray = eventObj.getJSONArray("weather");
                        JSONObject tenkiObj = tenkiArray.getJSONObject(0);
                        tenki = tenkiObj.getString("main");
                        time = eventObj.getString("dt_txt");
                        JSONObject event = eventObj.getJSONObject("main");
                        ondo = event.getString("temp");
                        humid = event.getInt("humidity");
                        txt3.setText(ondo);
                        txt4.setText(time);
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        showLoadError(); // エラーメッセージを表示
                    }

                    txt6.setText(Str2);
                    ondocp = ondo.substring(0,ondo.length());
                    Str2cp = Str2.substring(0,Str2.length());
                    try {
                        Txt = Math.round(Double.parseDouble(ondocp) - 273.15);
                        Txt2 = Double.parseDouble(Str2cp);
                        Intime = Integer.parseInt(time.substring(12,13));
                    }catch(NumberFormatException e){

                    }

                    txt3.setText(String.valueOf(Txt));

                    if(Txt < Txt2){             //堀江<パイ
                        txt5.setText("現在予想より温度が高くなっております。");
                        if(tenki.equals("Rain")){
                            img.setImageResource(R.drawable.ame);
                            txt5.append("また、雨も降っておりジメジメするでしょう。\n");
                            if(humid >= 80){
                                txt5.append("湿度も高く蒸し暑い日になります。\n");
                            }
                        }
                        if(tenki.equals("Clear") && Intime >=6 && Intime <= 15){
                            img.setImageResource(R.drawable.hare);
                            txt5.append("洗濯物を干すのに良い天気です。\n");
                            if(Txt2 > 30){
                                txt5.append("日差しも強く、熱中症になる恐れがあります。\n");
                            }
                            if(Txt2 < 20){
                                txt5.append("ポカポカして暖かい一日になるでしょう。\n");
                            }
                        }
                        else if(tenki.equals("Clear") && (Intime >=18 || Intime <= 3)){
                            img.setImageResource(R.drawable.luna);
                            txt5.append("今夜はきれいな星空が見れるでしょう。\n");
                            if(Txt2 > 30){
                                txt5.append("しかし、温度が高いので熱帯夜になりそうです。\n");
                                txt5.append("熱中症に気を付けましょう。\n");
                            }
                            if(Txt2 < 20){
                                txt5.append("今夜は涼しくなりますので、体を冷やしすぎないようにしましょう。\n");
                            }
                        }
                        if(tenki.equals("Clouds")){
                            img.setImageResource(R.drawable.kumo);
                            txt5.append("日は照っておらず比較的涼しくなるでしょう。\n");
                        }
                        if(Txt2-Txt > 5){
                            txt5.append("水分補給をこまめに行いましょう。\n");
                        }
                    }
                    else if(Txt2 < Txt){
                        txt5.setText("現在予想より温度が低くなっております。\n");
                        if(tenki.equals("Rain")){
                            img.setImageResource(R.drawable.ame);
                            txt5.append("雨も降っており寒い1日になるでしょう。\n");
                            if(humid >= 80){
                                txt5.append("除湿を心掛け、カビやダニの繁殖に気を付けましょう。\n");
                            }
                        }
                        if(tenki.equals("Clear") && (Intime >=6 && Intime <= 15)){
                            img.setImageResource(R.drawable.hare);
                            txt5.append("しかし日が照っているので、暖かくなるでしょう。\n");
                        }
                        else if(tenki.equals("Clear") && (Intime >=18 || Intime <= 3)){
                            img.setImageResource(R.drawable.luna);
                            txt5.append("今夜は寒くなりそうですので、暖かくしたほうがいいでしょう。\n");
                        }
                        if(tenki.equals("Clouds")){
                            img.setImageResource(R.drawable.kumo);
                            txt5.append("日が雲で隠れており涼しくなるでしょう\n");
                        }
                        if(Txt-Txt2 > 5){
                            txt5.append("上着を羽織って暖かくするようにしましょう。\n");
                        }
                    }

                    txt5.append("\n体調管理に気を付けましょう。\n");

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode != KeyEvent.KEYCODE_BACK){
                    finish();
                    return true;
                }else{
                    return false;
                }
    }

}

