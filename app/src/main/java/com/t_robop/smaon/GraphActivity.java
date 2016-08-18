package com.t_robop.smaon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    LineChart lineChart;
    int screen_transition;
    float monthlyTemp[] = new float[38];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.line_chart);
        //ラズパイの温度を受け取る
        Intent oIntent = getIntent();
        String[] razOndo = oIntent.getStringArrayExtra("owmOndo");

        int i;
        for(i = 0; i < 38; i++){
            monthlyTemp[i] = Float.parseFloat(razOndo[i]);
            if(monthlyTemp[i] > 0.0){
                monthlyTemp[i] -= 273.15;
            }
        }
    }

    //ボタン
    //5日分グラフのボタン
    public void createLineChartDataCurrent(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataCurrent());
        setEnabledGraphButton(1);
    }

    //月間グラフのボタン
    public void createLineChartDataMonth(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataMonth());
        lineChart.fitScreen();
        lineChart.setVisibleXRangeMaximum(6F);    //画面拡大を1周間の気温まで
        //グラフ画面専用変数の初期化
        screen_transition = 0;
        setEnabledGraphButton(2);
    }

    //年間グラフのボタン
    public void createLineChartDataYear(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataYear());
        setEnabledGraphButton(3);
    }

    public void setEnabledGraphButton(int a) {
        Button button_current = (Button) findViewById(R.id.current);
        Button button_month = (Button) findViewById(R.id.month);
        Button button_year = (Button) findViewById(R.id.year);
        button_current.setEnabled(true);
        button_month.setEnabled(true);
        button_year.setEnabled(true);
        switch (a) {
            case 1:
                button_current.setEnabled(false);
                break;
            case 2:
                button_month.setEnabled(false);
                break;
            case 3:
                button_year.setEnabled(false);
                break;
        }
    }

    //グラフの左移動
    public void createLeft_move(View v) {
        if (screen_transition > 0) {
            screen_transition -= 7;
            lineChart.moveViewToX(screen_transition);
        }
    }

    //グラフの右移動
    public void createRight_move(View v) {
        if (screen_transition < 24) {
            screen_transition += 7;
            lineChart.moveViewToX(screen_transition);
        }
    }

    public  void moveMain(View v){
        Intent intent = new Intent(GraphActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //グラフの設定
    private void createLineChart() {
        lineChart.setDescription("さいたま市　平均気温");     //グラフの説明
        lineChart.getAxisRight().setEnabled(false); //y軸の右ラベルの無効
        lineChart.setDrawGridBackground(true);  //グリッド線
        lineChart.setDoubleTapToZoomEnabled(false); //ダブルタップズームの無効化
        lineChart.getLegend().setEnabled(true); //判例有効化
        lineChart.fitScreen();//拡大を初期化

        //凡例の削除
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        //X軸周り
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);  //ラベルの描画
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //ラベルの位置
        xAxis.setDrawGridLines(true);   //グリッド線
        xAxis.setSpaceBetweenLabels(0);

        //Y軸周り
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setShowOnlyMinMax(true);
        yAxis.setStartAtZero(false);
        yAxis.setDrawGridLines(true);
        yAxis.setEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setShowOnlyMinMax(true);
        rightAxis.setStartAtZero(false);
        rightAxis.setDrawGridLines(true);
        rightAxis.setEnabled(false);

        lineChart.invalidate();
    }

    //ラベルの設定
    public LineData setChart(ArrayList graphValues, ArrayList xValues) {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();
        LineDataSet graphValuesDataSet = new LineDataSet(graphValues, "平均気温");  //グラフ全体のラベル
        graphValuesDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);  //グラフの色
        LineDataSets.add(graphValuesDataSet);   //グラフをセット

        LineData lineData = new LineData(xValues, LineDataSets); //グラフを返す
        return lineData;
    }


    float annualTemp[] = {4.5F, 4.8F, 9.4F, 14.1F, 20.8F, 22.3F, 26.6F, 26.6F, 22.5F, 17.8F, 13.2F, 8.1F};

    //週間グラフを作成
    private LineData createLineChartDataCurrent() {
        // androidから日を取得
        int day = getDay(new Date());
        //月末までの日数
        int dayLimitLength = getLastDay(new Date()) - getDay(new Date());

        /*
        週間の制限
        アプリ起動時に29日かつ、その月が31日までの時、
        「29,30,31,1,2」日のデータになるように
        ラベル、値をループさせる
         */
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dayLimitLength--;
            if (dayLimitLength == -2) {
                day = -i + 1;
            }
            xValues.add((i + day) + "日");
        }

        // 週間気温
        ArrayList<Entry> graphValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            graphValues.add(new Entry(monthlyTemp[i], i));
        }

        LineData lineData = setChart(graphValues, xValues);
        return lineData;
    }

    // 月間グラフを作成
    private LineData createLineChartDataMonth() {
        // androidから日付を取得
        int lastDay = getLastDay(new Date());

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < lastDay; i++) {
            xValues.add(String.valueOf(i + 1));
        }

        // 月間気温
        ArrayList<Entry> graphValues = new ArrayList<>();
        for (int i = 0; i < lastDay; i++) {
            graphValues.add(new Entry(monthlyTemp[i], i));
        }

        LineData lineData = setChart(graphValues, xValues);
        return lineData;
    }

    // 年間グラフを作成
    private LineData createLineChartDataYear() {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();
        lineChart.fitScreen();

        // X軸のラベル
        ArrayList<String> xValues = new ArrayList<>();
        String monthNum[] = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        for (int i = 0; i < 12; i++) {
            xValues.add(monthNum[i]);
        }

        // 年間気温
        ArrayList<Entry> graphValues = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            graphValues.add(new Entry(annualTemp[i], i));
        }

        LineData lineData = setChart(graphValues, xValues);
        return lineData;
    }

    //ゲッター
    //月末を取得
    static public int getLastDay(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        c.set(year, month, 1);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    //日を取得
    static public int getDay(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day;
    }
}
