package com.t_robop.smaon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SettingActivity extends AppCompatActivity {
    Spinner mSpinner1;
    Spinner Sphokkai;//北海道0
    Spinner Sptohoku;//東北1
    Spinner Spkanto;//関東2
    Spinner Spchubu;//中部3
    Spinner Spkansai;//関西4
    Spinner Spchugoku;//中国5
    Spinner Spshikoku;//四国6
    Spinner Spkyuushu;//九州7

    int spHantei=0;//地方判定↑
    String selected;//地方判定用
    String City;//県判定用

    int CityID=0;//livedoorの県のURL末尾のID→未定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSpinner1 = (Spinner) findViewById(R.id.spinner);
        Sphokkai = (Spinner) findViewById(R.id.spinner2);
        Sptohoku = (Spinner) findViewById(R.id.spinner3);
        Spkanto = (Spinner) findViewById(R.id.spinner4);
        Spchubu = (Spinner) findViewById(R.id.spinner5);
        Spkansai = (Spinner) findViewById(R.id.spinner6);
        Spchugoku = (Spinner) findViewById(R.id.spinner7);
        Spshikoku = (Spinner) findViewById(R.id.spinner8);
        Spkyuushu = (Spinner) findViewById(R.id.spinner9);

        Sphokkai.setVisibility(View.GONE);  //非表示
        Sptohoku.setVisibility(View.GONE);  //非表示
        Spkanto.setVisibility(View.GONE);  //非表示
        Spchubu.setVisibility(View.GONE);  //非表示
        Spkansai.setVisibility(View.GONE);  //非表示
        Spchugoku.setVisibility(View.GONE);  //非表示
        Spshikoku.setVisibility(View.GONE);  //非表示
        Spkyuushu.setVisibility(View.GONE);  //非表示




        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected = (String) mSpinner1.getSelectedItem();

                if (selected.equals("北海道")) {

                    spHantei=0;

                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示

                    Sphokkai.setVisibility(View.VISIBLE);   //北海道表示

                } else if(selected.equals("東北")){
                    Log.d("test", "test");
                    spHantei=1;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Sptohoku.setVisibility(View.VISIBLE);   //東北表示

                }else if(selected.equals("関東")){

                    spHantei=2;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Spkanto.setVisibility(View.VISIBLE);  //関東表示

                }else if(selected.equals("中部")){

                    spHantei=3;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Spchubu.setVisibility(View.VISIBLE);  //中部表示

                }else if(selected.equals("関西")){

                    spHantei=4;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Spkansai.setVisibility(View.VISIBLE);  //関西表示

                }else if(selected.equals("中国")){

                    spHantei=5;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Spchugoku.setVisibility(View.VISIBLE);  //中国表示

                }else if(selected.equals("四国")){

                    spHantei=6;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spkyuushu.setVisibility(View.GONE);  //非表示

                    Spshikoku.setVisibility(View.VISIBLE);  //四国表示

                }else if(selected.equals("九州")){

                    spHantei=7;

                    Sphokkai.setVisibility(View.GONE);  //非表示
                    Sptohoku.setVisibility(View.GONE);  //非表示
                    Spkanto.setVisibility(View.GONE);  //非表示
                    Spchubu.setVisibility(View.GONE);  //非表示
                    Spkansai.setVisibility(View.GONE);  //非表示
                    Spchugoku.setVisibility(View.GONE);  //非表示
                    Spshikoku.setVisibility(View.GONE);  //非表示

                    Spkyuushu.setVisibility(View.VISIBLE);  //九州表示

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    public void OKbutton(View v){

        switch (spHantei){
            case 0:
                City = (String) Sphokkai.getSelectedItem();//取得
                if (City.equals("札幌")){
                    CityID = 2128295;
                }else if(City.equals("函館")){
                    CityID = 2130188;
                }else{
                    CityID = 2129376;//釧路
                }

                break;
            case 1:
                City = (String) Sptohoku.getSelectedItem();
                if (City.equals("青森")){
                    CityID = 2130658;
                }else if(City.equals("岩手")){
                    CityID = 2111834;
                }else if(City.equals("宮城")){
                    CityID = 2111149;
                }else if(City.equals("山形")){
                    CityID = 2110556;
                }else{
                    CityID = 2112141;//福島
                }

                break;
            case 2:
                City = (String) Spkanto.getSelectedItem();
                if (City.equals("茨城")){
                    CityID = 2110683;
                }else if(City.equals("栃木")){
                    CityID = 1849053;
                }else if(City.equals("群馬")){
                    CityID = 1851002;
                }else if(City.equals("埼玉")){
                    CityID = 6940394;
                }else if(City.equals("千葉")){
                    CityID = 2113015;
                }else if(City.equals("東京")){
                    CityID = 1850147;
                }else{
                    CityID = 1848354;//神奈川
                }

                break;
            case 3:
                City = (String) Spchubu.getSelectedItem();
                if (City.equals("山梨")){
                    CityID = 1859100;
                }else if(City.equals("長野")){
                    CityID = 1856215;
                }else if(City.equals("新潟")){
                    CityID = 1855431;
                }else if(City.equals("富山")){
                    CityID = 1849876;
                }else if(City.equals("石川")){
                    CityID = 1857470;
                }else if(City.equals("福井")){
                    CityID = 1863985;
                }else if(City.equals("静岡")){
                    CityID = 1851717;
                }else if(City.equals("愛知")){
                    CityID = 1856057;
                }else if(City.equals("岐阜")){
                    CityID = 1850892;
                }else{
                    CityID = 1849796;//三重
                }

                break;
            case 4:
                City = (String) Spkansai.getSelectedItem();
                if (City.equals("滋賀")){
                    CityID = 1862636;
                }else if(City.equals("京都")){
                    CityID = 1857910;
                }else if(City.equals("大阪")){
                    CityID = 1853909;
                }else if(City.equals("兵庫")){
                    CityID = 1847966;
                }else if(City.equals("奈良")){
                    CityID = 1855612;
                }else{
                    CityID = 1926004;//和歌山
                }

                break;
            case 5:
                City = (String) Spchugoku.getSelectedItem();
                if (City.equals("鳥取")){
                    CityID = 1849892;
                }else if(City.equals("島根")){
                    CityID = 1857550;
                }else if(City.equals("岡山")){
                    CityID = 1854383;
                }else if(City.equals("広島")){
                    CityID = 1862415;
                }else{
                    CityID = 1848689;//山口
                }

                break;
            case 6:
                City = (String) Spshikoku.getSelectedItem();
                if (City.equals("香川")){
                    CityID = 1851100;
                }else if(City.equals("愛媛")){
                    CityID = 1926099;
                }else if(City.equals("徳島")){
                    CityID = 1850158;
                }else {
                    CityID = 1859146;//高知

                }

                break;
            case 7:
                City = (String) Spkyuushu.getSelectedItem();
                if (City.equals("福岡")){
                    CityID = 1863967;
                }else if(City.equals("佐賀")){
                    CityID = 1853303;
                }else if(City.equals("長崎")){
                    CityID = 1856177;
                }else if(City.equals("熊本")){
                    CityID = 1858421;
                }else if(City.equals("大分")){
                    CityID = 1849706;
                }else if(City.equals("宮崎")){
                    CityID = 1856717;
                }else if(City.equals("鹿児島")){
                    CityID = 1860827;
                }else{
                    CityID = 1856035;//沖縄
                }

                break;
        }

        Intent nintent = new Intent();

        nintent.putExtra("Id",CityID);
        nintent.setClass(this,MainActivity.class);

        Intent Sintent = new Intent();
        Sintent.setClassName("com.t_robop.smaon","com.t_robop.smaon.StartActivity");
        startActivity(Sintent);

    }
}