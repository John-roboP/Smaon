package com.t_robop.smaon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    LineChart lineChart;
    int owmCnt;
    float every3Times[];
    int owmDate;
    float rasTemps[] = new float[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.line_chart);
        //画面遷移時のintent
        Intent oIntent = getIntent();
        owmDate = oIntent.getIntExtra("owmDate", 0);
        String[] jsarray = oIntent.getStringArrayExtra("jsarray");
        String[] owmTemp = oIntent.getStringArrayExtra("owmOndo");
        owmCnt = oIntent.getIntExtra("count", 0);
        every3Times = new float[owmCnt];
        //Raspberry Pi
        for (int i = 0; i < 24; i++) {
            rasTemps[i] = (Float.parseFloat(jsarray[i])) / 1000F;
            //Raspberry Piの温度の小数点第2位を四捨五入
            BigDecimal bd[] = new BigDecimal[24];
            BigDecimal bd1[] = new BigDecimal[24];
            bd[i] = new BigDecimal(rasTemps[i]);
            bd1[i] = bd[i].setScale(1, BigDecimal.ROUND_HALF_UP);
            rasTemps[i] = (bd1[i].floatValue());
        }

        //OWMの温度をセルシウス温度にする
        for (int i = 0; i < owmCnt; i++) {
            every3Times[i] = Float.parseFloat(owmTemp[i]);
            if (every3Times[i] > 0.0) {
                every3Times[i] -= 273.15;
            }
        }
        //OWMの温度の小数点第2位を四捨五入
        for (int i = 0; i < owmCnt; i++) {
            BigDecimal bd[] = new BigDecimal[owmCnt];
            BigDecimal bd1[] = new BigDecimal[owmCnt];
            bd[i] = new BigDecimal(every3Times[i]);
            bd1[i] = bd[i].setScale(1, BigDecimal.ROUND_HALF_UP);
            every3Times[i] = (bd1[i].floatValue());
        }
        View v = findViewById(R.id.time);
        setLineChartDataTime(v);
    }

    //ボタン
    //時系列グラフボタン
    public void setLineChartDataTime(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataTime());
        //拡大設定
        lineChart.setVisibleXRangeMaximum(10F);
        setEnabledGraphButton(1);
    }

    //5日分グラフのボタン
    public void setLineChartDataCurrent(View v) {
        createLineChart();
        lineChart.fitScreen();  //画面の最大化
        //グラフの生成
        lineChart.setData(createLineChartDataCurrent());
        setEnabledGraphButton(2);
    }

    //ボタンの有効化
    public void setEnabledGraphButton(int num) {
        Button button_current = (Button) findViewById(R.id.current);
        Button button_date = (Button) findViewById(R.id.time);
        button_current.setEnabled(true);
        button_date.setEnabled(true);
        switch (num) {
            case 1:
                button_date.setEnabled(false);
                break;
            case 2:
                button_current.setEnabled(false);
                break;
        }
    }

    //グラフ全体の設定
    private void createLineChart() {
        lineChart.getAxisRight().setEnabled(false); //y軸の右ラベルの無効
        lineChart.setDrawGridBackground(true);  //グリッド線
        lineChart.setDoubleTapToZoomEnabled(false); //ダブルタップズームの無効化
        lineChart.getLegend().setEnabled(true); //判例有効化
        lineChart.setBackgroundColor(2);
        lineChart.setDescriptionTextSize(12);   //グラフの説明テキストサイズ
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(false);  //x軸y軸方向のみ拡大有効化
        lineChart.setScaleYEnabled(false);  //y軸方向の拡大無効化
        lineChart.setGridBackgroundColor((int) 4169E1); //背景を青色にする
        //X軸周り
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);  //ラベルの描画
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //ラベルの位置
        xAxis.setDrawGridLines(true);   //グリッド線
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

        //Y軸周り
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setStartAtZero(false);
        yAxis.setDrawGridLines(true);
        yAxis.setEnabled(true);
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setEnabled(false);

        lineChart.invalidate();
    }

    //値の統合設定
    public LineDataSet synthesisPrefer(LineDataSet dataSet){
        dataSet.setValueTextSize(12);   //テキストサイズ
        dataSet.setValueTextColor(Color.WHITE); //色
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));    //フォント
        dataSet.setCircleSize(4);   //点のサイズ
        return dataSet;
    }

    //時系列グラフの作成
    private LineData createLineChartDataTime() {
        lineChart.setDescription("時系列気温");     //グラフの説明
        ArrayList<String> xValues = new ArrayList<>();
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 24; j++) {
                if(j==0){
                xValues.add(getDay(new Date())-1+i+"日");
                }else {
                    xValues.add(j + "時");
                }
            }
        }

        for (int i = 0; i < owmDate + 3; i++) {
            if (i==0) {
                xValues.add(getDay(new Date())+3+"日");
            } else {
                xValues.add(i + "時");
            }
        }

        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();
        //値のセット
        //OWM
        ArrayList<Entry> owmValues = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            owmValues.add(new Entry(every3Times[i], 24 + owmDate + (i * 3)));
            if(i==27){
                for (int j=0;j<owmCnt-i;j++){
                    owmValues.add(new Entry(every3Times[i+j], 24 + owmDate + (i * 3)));
                }
            }
        }

        //個別設定
        LineDataSet owmDataSet = new LineDataSet(owmValues, "予想気温");  //データのセット
        owmDataSet.setColor(ColorTemplate.JOYFUL_COLORS[2]);  //色の設定
        owmDataSet.setCircleColor(ColorTemplate.JOYFUL_COLORS[2]);
        //全体設定
        synthesisPrefer(owmDataSet);
        //  OWMグラフのセット
        LineDataSets.add(owmDataSet);

        //Raspberry Pi
        ArrayList<Entry> rasValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            rasValues.add(new Entry(rasTemps[i], i));
        }
        //個別設定
        LineDataSet rasDataSet = new LineDataSet(rasValues, "ラズパイ");  //データのセット
        rasDataSet.setColor(ColorTemplate.LIBERTY_COLORS[0]);  //色の設定
        //総合設定
        synthesisPrefer(rasDataSet);
        //Raspberry Piグラフのセット
        LineDataSets.add(rasDataSet);

        //グラフのセット
        LineData lineData = new LineData(xValues, LineDataSets);
        return lineData;
    }

    //日平均グラフの作成
    private LineData createLineChartDataCurrent() {
        lineChart.setDescription("日平均気温");     //グラフの説明
        /*
        制限
        アプリ起動時に29日かつ、その月が31日までの時、
        「29,30,31,1,2」日のデータになるように
        ラベル、値をループさせる
         */
        // androidから日を取得
        int day = getDay(new Date());
        //月末までの日数
        int dayLimitLength = getLastDay(new Date()) - getDay(new Date());
        //ラベルの格納
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dayLimitLength--;
            if (dayLimitLength == -2) {
                day = -i + 1;
            }
            xValues.add((i + day - 1) + "日");
        }
        //OWMの1日目の気温の個数
        int lenght1 = (8 - (owmDate / 3) + 1);

        // 値の格納
        ArrayList<Entry> graphValues = new ArrayList<>();
        float averageTemp = 0;
        int flag = 0;
        /*
        「前日」はRaspberry Piの気温
        「1日目～5日目」はOWMの気温
         */
        //前日
        for (int i = 0; i < 24; i++) {
            averageTemp += rasTemps[i];
        }

        graphValues.add(new Entry((getROUND_HALF_UP(averageTemp / 24)), 0));
        averageTemp = 0;
        //1日目
        for (int i = 0; i < lenght1; i++) {
            averageTemp += every3Times[flag];
            flag++;
        }
        graphValues.add(new Entry((getROUND_HALF_UP(averageTemp / lenght1)), 1));
        averageTemp = 0;
        //2,3,4日目
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                averageTemp += every3Times[flag];
                flag++;
            }
            graphValues.add(new Entry(getROUND_HALF_UP(averageTemp / 8), i + 2));
            averageTemp = 0;
        }
        averageTemp = 0;
        //5日目
        for (int i = 0; i < every3Times.length - flag; i++) {
            averageTemp += every3Times[flag];
        }
        graphValues.add(new Entry(getROUND_HALF_UP(averageTemp / (every3Times.length - flag)), 5));

        //個別設定
        LineDataSet daysDataSet = new LineDataSet(graphValues, "平均気温");  //グラフ全体のラベル
        daysDataSet.setColor(ColorTemplate.LIBERTY_COLORS[1]);  //グラフの色
        //総合設定
        synthesisPrefer(daysDataSet);

        //グラフのセット
        LineData lineData = new LineData(xValues, daysDataSet);
        return lineData;
    }

    //ゲッター
    //小数点第二位を四捨五入
    public float getROUND_HALF_UP(float total) {
        BigDecimal bd = new BigDecimal(total);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        float averageTemp = (bd.floatValue());
        return averageTemp;
    }

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
