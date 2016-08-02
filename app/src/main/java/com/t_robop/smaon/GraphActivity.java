package com.t_robop.smaon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {

    LineChart lineChart;
    int screen_transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.line_chart);

    }


    //ボタン
    //週間グラフのボタン
    public void createLineChartData_week(View v) {
        createLineChart();
        lineChart.fitScreen();
        //グラフの生成
        lineChart.setData(createLineChartDataWeek());

        Button button_week = (Button) findViewById(R.id.week);
        Button button_month = (Button) findViewById(R.id.month);
        Button button_year = (Button) findViewById(R.id.year);
        button_week.setEnabled(false);
        button_month.setEnabled(true);
        button_year.setEnabled(true);
    }

    //月間グラフのボタン
    public void createLineChartData_month(View v) {
        createLineChart();

        //グラフの生成
        lineChart.setData(createLineChartDataMonth());
        lineChart.fitScreen();
        lineChart.setVisibleXRangeMaximum(6F);    //画面拡大を1周間の気温まで
        //グラフ画面専用変数の初期化
        screen_transition = 0;

        Button button_week = (Button) findViewById(R.id.week);
        Button button_month = (Button) findViewById(R.id.month);
        Button button_year = (Button) findViewById(R.id.year);
        button_week.setEnabled(true);
        button_month.setEnabled(false);
        button_year.setEnabled(true);
    }

    //年間グラフのボタン
    public void createLineChartData_year(View v) {
        createLineChart();
        lineChart.fitScreen();
        //グラフの生成
        lineChart.setData(createLineChartDataYear());

        Button button_week = (Button) findViewById(R.id.week);
        Button button_month = (Button) findViewById(R.id.month);
        Button button_year = (Button) findViewById(R.id.year);
        button_week.setEnabled(true);
        button_month.setEnabled(true);
        button_year.setEnabled(false);
    }

    //グラフの左移動
    public void createLeft_move(View v) {
        screen_transition -= 7;
        lineChart.moveViewToX(screen_transition);
    }

    //グラフの右移動
    public void createRight_move(View v) {
        screen_transition += 7;
        lineChart.moveViewToX(screen_transition);
    }


    //グラフの設定
    private void createLineChart() {
        lineChart.setDescription("さいたま市　平均気温");     //グラフの説明
        lineChart.getAxisRight().setEnabled(false); //y軸の右ラベルの無効
        lineChart.setDrawGridBackground(true);  //グリッド線
        lineChart.setDoubleTapToZoomEnabled(false); //ダブルタップズームの無効化
        lineChart.setDragEnabled(false);    //スクロール無効化
        lineChart.setScaleEnabled(false);   //ピンチズーム無効化

        lineChart.getLegend().setEnabled(true); //判例有効化


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


    //  月間気温
    float monthly_temp[] = {29F, 29F, 30F, 32F, 33F, 33F, 33F, 32F, 34F, 32F, 31F, 32F, 32F, 33F, 31F, 32F, 32F, 33F, 33F, 32F, 32F, 32F, 32F, 32F, 32F, 32F, 33F, 29F, 29F, 30F, 31F};
    //年間気温
    float annual_temp[] = {4.5F, 4.8F, 9.4F, 14.1F, 20.8F, 22.3F, 26.6F, 26.6F, 22.5F, 17.8F, 13.2F, 8.1F};

    //週間グラフを作成
    private LineData createLineChartDataWeek() {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();


        Date now = new Date();
        // androidから日を取得
        int day = getDay(now);
        //月末までの日数
        int dayLimitLength = getLastDay(now) - getDay(now);

        //週間の制限
        /*データは5つ以上でなければならない
            もし条件を満たさないのであれば、
            else文で処理する
         */

        ArrayList<String> xValues = new ArrayList<>();
        dayLimitLength--;
        for (int i = 0; i < 5; i++) {
            if (dayLimitLength == -2) {
                day = -i + 1;
            }
            xValues.add((i + day) + "日");
        }

        // 週間気温
        ArrayList<Entry> values_graph = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            values_graph.add(new Entry(monthly_temp[i], i));
        }


        LineDataSet values_graphDataSet = new LineDataSet(values_graph, "平均気温");  //グラフ全体のラベル
        values_graphDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);  //グラフの色
        LineDataSets.add(values_graphDataSet);   //グラフをセット


        LineData lineData = new LineData(xValues, LineDataSets); //グラフを返す
        return lineData;
    }

    // 月間グラフを作成
    private LineData createLineChartDataMonth() {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();


        Date now = new Date();
        // androidから日付を取得
        int lastDay = getLastDay(now);


        ArrayList<String> xValues = new ArrayList<>();

        for (int i = 0; i < lastDay; i++) {
            xValues.add(String.valueOf(i + 1));
        }


        // 月間気温
        ArrayList<Entry> values_graph = new ArrayList<>();

        for (int i = 0; i < lastDay; i++) {
            values_graph.add(new Entry(monthly_temp[i], i));
        }

        LineDataSet values_graphDataSet = new LineDataSet(values_graph, "平均気温");  //グラフ全体のラベル
        values_graphDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);  //グラフの色
        LineDataSets.add(values_graphDataSet);   //グラフをセット

        LineData lineData = new LineData(xValues, LineDataSets); //グラフを返す
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


    // 年間グラフを作成
    private LineData createLineChartDataYear() {
        ArrayList<LineDataSet> LineDataSets = new ArrayList<>();
        // X軸のラベル
        ArrayList<String> xValues = new ArrayList<>();
        String monthNum[] = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        for (int i = 0; i < 12; i++) {
            xValues.add(monthNum[i]);
        }
        // 年間気温
        ArrayList<Entry> values_graph = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            values_graph.add(new Entry(annual_temp[i], i));
        }

        LineDataSet values_graphDataSet = new LineDataSet(values_graph, "平均気温");  //グラフ全体のラベル
        values_graphDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);  //グラフの色
        LineDataSets.add(values_graphDataSet);   //グラフをセット


        LineData lineData = new LineData(xValues, LineDataSets); //グラフを返す
        return lineData;
    }
}



