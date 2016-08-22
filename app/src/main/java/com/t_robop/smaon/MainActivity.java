package com.t_robop.smaon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static TextView txt;
    static TextView txt2;
    static TextView txt3;
    static TextView txt4;
    static TextView txt5;                                   //てーあん
    static TextView txt6;
    static TextView txt7;
    static TextView txt8;
    static TextView txt9;
    static ImageView img;
    static String ondo;                                     //OWMの温度
    static String ondocp;                                   //OWMの温度（計算用）
    static String tenki;                                    //その日の天気
    static String Str;                                      //ラズパイの日付
    static String Str2;                                     //ラズパイの温度
    static String Str2cp;                                   //ラズパイの温度（計算用）
    static String gOndo;
    static double Txt=0.0;                                       //ホリエモンの温度を数値化
    static double Txt2=0.0;                                      //ラズパイパイの温度を数値化
    static String cityId;
    static String time;                                     //OWMの時刻
    static int gTime;                                       //timeをGraphActivityに送るために格納
    static String nTime;                                    //現在時刻
    static int humid=0;
    static int Intime=0;
    static int sum=0;
    static Sharepre Sharepre;
    static String[] gaOndo = new String[40];
    static String[] yesOndo = new String[24];               //昨日の温度
    static double nexOndo=0.0;                              //1h後の予想温度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.textView);
        txt2 = (TextView) findViewById(R.id.textView2);
        txt3 = (TextView) findViewById(R.id.textView3);      //温度（OWM）
        txt4 = (TextView) findViewById(R.id.textView4);      //日付
        txt5 = (TextView) findViewById(R.id.textView5);      //提案文
        txt6 = (TextView) findViewById(R.id.textView6);      //温度（ラズパイ）
        txt7 = (TextView) findViewById(R.id.textView7);      //天気（文）
        txt8 = (TextView) findViewById(R.id.textView8);      //1h予想温度
        txt9 = (TextView) findViewById(R.id.textView9);      //昨日の同時刻
        img = (ImageView) findViewById(R.id.imageView2);     //天気（図）

        txt6.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));  //ラズパイ温度のフォントを変更
        txt7.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));  //天気概要のフォントを変更

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        //MainActivity→GraphActivityに遷移
                        Intent gIntent = new Intent(getApplicationContext(), GraphActivity.class);
                        gIntent.putExtra("owmOndo",gaOndo);
                        gIntent.putExtra("owmDate",gTime);
                        gIntent.putExtra("count",sum);
                        gIntent.putExtra("jsarray",yesOndo);
                        startActivity(gIntent);
                        break;
                    case R.id.action_settings2:
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, new PlaceholderFragment())
                                .commit();
                        break;
                    case R.id.action_settings3:
                        Intent setIntent = new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(setIntent);
                        break;
                        default:
                            break;
                }
                return true;
            }
        });


        //ラズパイのデータ取得
        Intent intent = getIntent();
        Str = intent.getStringExtra("date");                                                    //現在時刻
        Str2 = intent.getStringExtra("temper");                                                 //現在時刻の温度
        nexOndo = intent.getDoubleExtra("estima", 0.0);                                         //1h後の予想温度
        yesOndo = intent.getStringArrayExtra("jsarray");                                        //昨日の同時刻の温度


        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);        //openweathermapのデータ取得
        cityId = data.getString("Cid", "0");

        Sharepre = new Sharepre(this.getApplicationContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                public void postExecute(JSONObject result) {                            //JSONデータがなかったら
                    if (result == null) {
                        showLoadError();                                                // エラーメッセージを表示
                        return;
                    }

                    //現在の日付を取得
                    Date nDate = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    int now = Integer.parseInt((sdf1.format(nDate)).substring(11,13));  //時間を抽出(NOW)
                    int day = Integer.parseInt((sdf1.format(nDate)).substring(8,10));   //日にちを抽出(NOW)
                    int mon = Integer.parseInt((sdf1.format(nDate)).substring(5,7));   //月を抽出(NOW)

                    try{
                        JSONArray eventArray = result.getJSONArray("list");             //配列データを取得
                        for(int i=0;i<eventArray.length();i++){
                            JSONObject eventObj = eventArray.getJSONObject(i);            //一番初めのデータを取得
                            JSONArray tenkiArray = eventObj.getJSONArray("weather");    //"main"配列の中身を取得
                            JSONObject tenkiObj = tenkiArray.getJSONObject(0);           //一番初めのデータを取得
                            time = eventObj.getString("dt_txt");                        //日付を取得
                            Intime = Integer.parseInt(time.substring(11,13));           //時間を抽出(OWM)
                            int intDay = Integer.parseInt(time.substring(8,10));        //日にちを抽出(OWM)
                            JSONObject event = eventObj.getJSONObject("main");          //天気データを参照
                            gOndo = event.getString("temp");                            //GraphActivityに送る温度を格納
                            gaOndo[i] = gOndo;                                          //gOndoを配列化してGraphActivityに送る
                            if(i==0){
                                gTime = Intime;
                            }
                            if(i==now){
                                StringBuilder sb = new StringBuilder();
                                sb.append("昨日の同時刻\n"+yesOndo[i]);
                                sb.insert(9,".");
                                String Tem = sb.toString();
                                txt9.setText(Tem);                //昨日の同時刻の温度を出力
                            }
                            if((Math.abs(now - Intime)) <= 3 && (day == intDay)){
                                nTime = sdf1.format(nDate);
                                tenki = tenkiObj.getString("main");                         //その時の天気を取得
                                ondo = event.getString("temp");                              //温度を取得
                                humid = event.getInt("humidity");                           //湿度を取得
                            }
                            sum++;
                        }
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        showLoadError(); // エラーメッセージを表示
                    }

                    //ラズパイとオープンウェザーの温度をそれぞれコピー
                    ondocp = ondo.substring(0,ondo.length());
                    Str2cp = Str2.substring(0,Str2.length());

                    //String→intに直す
                    try {
                        Txt = Math.round(Double.parseDouble(ondocp) - 273.15);                      //絶対温度からセルシウスに変換
                        Txt2 = Double.parseDouble(Str2cp);
                    }catch(NumberFormatException e){

                    }
                    txt3.setText(String.valueOf(Txt));                                              //OWMの温度をテキストビューに格納
                    txt6.setText(Str2);                                                             //ラズパイの温度をテキストビューに格納
                    txt4.setText(nTime);                                                            //現在時刻をテキストビューに格納
                    txt8.setText("1時間後の予想\n"+String.valueOf(nexOndo));                      //昨日の同時刻の温度を格納

                    if(Txt < Txt2){             //オープン<ラズパイ
                        txt5.setText("現在予想より温度が高くなっております。");
                        if(tenki.equals("Rain")){
                            txt7.setText("雨");
                            img.setImageResource(R.drawable.ame);
                            txt5.append("また、雨も降っておりジメジメするでしょう。\n");
                            if(humid >= 80){
                                txt5.append("湿度も高く蒸し暑い日になります。\n");
                            }
                        }
                        if(tenki.equals("Clear") && Intime >=6 && Intime <= 15){
                            txt7.setText("晴れ");
                            img.setImageResource(R.drawable.hare);
                            txt5.append("洗濯物を干すのに良い天気です。\n");
                            if(Txt2 > 30){
                                txt5.append("日差しも強く、熱中症になる恐れがあります。\n");
                            }
                            if(Txt2 < 20){
                                txt5.append("ポカポカして暖かい一日になるでしょう。\n");
                            }
                            if(humid >= 80){
                                txt5.append("湿度も高く蒸し暑い日になります。\n");
                            }
                        }
                        else if(tenki.equals("Clear") && (Intime >=18 || Intime <= 3)){
                            txt7.setText("晴れ");
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
                            txt7.setText("くもり");
                            img.setImageResource(R.drawable.kumo);
                            if(humid < 80){
                                txt5.append("日は照っておらず比較的涼しくなるでしょう。\n");
                            }else{
                                txt5.append("太陽は雲に隠れていますが、湿度が高くとても蒸し暑くなるでしょう。\n");
                            }
                        }
                        if(Txt2-Txt > 5){
                            txt5.append("水分補給をこまめに行いましょう。\n");
                        }
                    }
                    else if(Txt2 < Txt){            //オープン>ラズパイ
                        txt5.setText("現在予想より温度が低くなっております。\n");
                        if(tenki.equals("Rain")){
                            txt7.setText("雨");
                            img.setImageResource(R.drawable.ame);
                            txt5.append("雨も降っており寒い1日になるでしょう。\n");
                            if(humid >= 80){
                                txt5.append("除湿を心掛け、カビやダニの繁殖に気を付けましょう。\n");
                            }
                        }
                        if(tenki.equals("Clear") && (Intime >=6 && Intime <= 15)){
                            txt7.setText("晴れ");
                            img.setImageResource(R.drawable.hare);
                            if(Txt2 < 26){
                                txt5.append("しかし日が照っているので、暖かくなるでしょう。\n");
                            }
                            if(Txt2 >= 26){
                                txt5.append("しかし日が照っており暑くなります。場合によっては熱中症になる恐れがあります。\n");
                            }
                        }
                        else if(tenki.equals("Clear") && (Intime >=18 || Intime <= 3)){
                            txt7.setText("晴れ");
                            img.setImageResource(R.drawable.luna);
                            txt5.append("今夜は寒くなりそうですので、暖かくしたほうがいいでしょう。\n");
                        }
                        if(tenki.equals("Clouds")){
                            txt7.setText("くもり");
                            img.setImageResource(R.drawable.kumo);
                            txt5.append("日が雲で隠れており涼しくなるでしょう\n");
                        }
                        if(Txt-Txt2 > 5){
                            txt5.append("上着を羽織って暖かくするようにしましょう。\n");
                        }
                    }

                    if(mon > 6 && mon < 10){
                        if(Txt > 30){
                            txt5.append("とても暑いです。ですのでエアコンの温度を25℃にすれば快適に過ごせるでしょう。\n");
                        }
                        else if(Txt2 <= 30 && Txt2 > 27){
                            txt5.append("エアコンの温度を26～27℃にすれば快適に過ごせるでしょう。\n");
                        }
                        else if(Txt2 < 27){
                            txt5.append("冷たいものを摂るなどして、エアコンはつけずに涼しく過ごしましょう。\n");
                        }
                    }
                    else if(mon > 10 || mon < 3){
                        if(Txt < 15 ){
                            txt5.append("とても寒いのでエアコンの暖房を21℃にすると快適です。\n");
                        }
                        else if(Txt2 >= 15 && Txt2 < 18){
                            txt5.append("エアコンの温度を20℃にすれば快適に過ごせるでしょう。\n");
                        }
                        else if(Txt2 > 18){
                            txt5.append("気温が高めなので、厚着をしてエアコンはつけないようにしましょう。\n");
                        }
                    }
                    txt5.append("エアコンを使用する際は節電を心掛けて使いましょう。\n");

                    if(Math.abs(nexOndo-Txt) > 5){
                        txt5.append("気温が急に変化するので、体温管理をしっかりしましょう。\n");
                    }
                    if(humid<30){
                        txt5.append("空気が乾燥しているので、保湿を怠らないようにしましょう。\n");
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

        @Override
        public void onResume(){
            super.onResume();
            sum=0;
        }
    }

    //戻るボタンを押された時の処理
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
            }
        return true;
    }

}

