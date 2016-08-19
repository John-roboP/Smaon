package com.t_robop.smaon;

import android.content.Intent;
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
    float every3Times[] = new float[36];
    int owmDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.line_chart);

        Intent oIntent = getIntent();
        String[] owmTemp = oIntent.getStringArrayExtra("owmOndo");
        owmDate = oIntent.getIntExtra("owmDate", 0);

        for (int i = 0; i < 35; i++) {
            every3Times[i] = Float.parseFloat(owmTemp[i]);
            if (every3Times[i] > 0.0) {
                every3Times[i] -= 273.15;
            }
        }
    }

    //ボタン
    //時刻ごとのグラフボタン
    public void setLineChartDataTime(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataTime());
        lineChart.setGridBackgroundColor((int)4169E1);

        setEnabledGraphButton(1);
    }

    //5日分グラフのボタン
    public void setLineChartDataCurrent(View v) {
        createLineChart();
        //グラフの生成
        lineChart.setData(createLineChartDataCurrent());
        setEnabledGraphButton(2);
    }

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

    //グラフの左移動
    public void leftMove(View v) {
        if (screen_transition > 0) {
            screen_transition -= 7;
            lineChart.moveViewToX(screen_transition);
        }
    }

    //グラフの右移動
    public void rightMove(View v) {
        if (screen_transition < 24) {
            screen_transition += 7;
            lineChart.moveViewToX(screen_transition);
        }
    }

    public void moveMain(View v) {
        Intent intent = new Intent(GraphActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //グラフ全体の設定
    private void createLineChart() {
        lineChart.setDescription("さいたま市　平均気温");     //グラフの説明
        lineChart.getAxisRight().setEnabled(false); //y軸の右ラベルの無効
        lineChart.setDrawGridBackground(true);  //グリッド線
        lineChart.setDoubleTapToZoomEnabled(false); //ダブルタップズームの無効化
        lineChart.getLegend().setEnabled(true); //判例有効化
        lineChart.fitScreen();//拡大を初期化
        lineChart.setBackgroundColor(2);

//        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setScaleEnabled(true);

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
        yAxis.setStartAtZero(false);
        yAxis.setDrawGridLines(true);
        yAxis.setEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setEnabled(false);

        lineChart.invalidate();
    }

    //ラベルの設定
    public LineData setChart(ArrayList graphValues, ArrayList xValues) {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();
        LineDataSet graphValuesDataSet = new LineDataSet(graphValues, "平均気温");  //グラフ全体のラベル
        graphValuesDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);  //グラフの色
        graphValuesDataSet.setValueTextSize(12);    //値のテキストサイズ
        LineDataSets.add(graphValuesDataSet);   //グラフをセット

        LineData lineData = new LineData(xValues, LineDataSets); //グラフを返す
        return lineData;
    }

    //時刻グラフを作成
    private LineData createLineChartDataTime() {
        /*
        制限
        アプリ起動時に29日かつ、その月が31日までの時、
        「29,30,31,1,2」日のデータになるように
        ラベル、値をループさせる
         */

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 8 - (owmDate / 3); i++) {
            xValues.add(owmDate + (i * 3) + "時");
        }
        for (int j = 0; j < (owmDate / 3) + 1; j++) {
            xValues.add((j * 3) + "時");
        }

        // 週間気温
        ArrayList<Entry> graphValues = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            graphValues.add(new Entry(every3Times[i], i));
        }

        LineData lineData = setChart(graphValues, xValues);
        return lineData;
    }

    //5日間グラフを作成
    private LineData createLineChartDataCurrent() {
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
        for (int i = 0; i < 5; i++) {
            dayLimitLength--;
            if (dayLimitLength == -2) {
                day = -i + 1;
            }
            xValues.add((i + day) + "日");
        }

        int lenght1 =  (8- (owmDate / 3)+1);

        // 値の格納
        ArrayList<Entry> graphValues = new ArrayList<>();

        float averageTemp=0;
        int flag=0;
        //1日目
        for (int i = 0; i < lenght1; i++) {
            averageTemp += every3Times[i];
            flag++;
        }
        graphValues.add(new Entry((averageTemp/lenght1), 0));
        averageTemp=0;
        //2,3,4日目
        for (int i = 0; i < 3; i++) {
            for (int j=0;j<8;j++){
                averageTemp+=every3Times[(lenght1)+i*8+j];
            }
            graphValues.add(new Entry(averageTemp/8,i+1));
            flag++;
            averageTemp=0;
        }
        averageTemp=0;
        //5日目
        for (int i = 0; i <= 8-lenght1; i++) {
            averageTemp += every3Times[flag+i];
        }
        graphValues.add(new Entry((averageTemp/lenght1+1), 4));




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
