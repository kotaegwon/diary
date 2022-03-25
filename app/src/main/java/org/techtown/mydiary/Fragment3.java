package org.techtown.mydiary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Fragment3 extends Fragment {

    PieChart pieChart;
    BarChart barChart;
    LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment3,container,false);
        initUI(rootview);
        return rootview;
    }
    private void initUI(ViewGroup rootview) {
        pieChart=rootview.findViewById(R.id.piechart);
        barChart=rootview.findViewById(R.id.barchart);
        lineChart=rootview.findViewById(R.id.linechart);

        //파이차트
        //파이차트 중앙 텍스트
        pieChart.setCenterText("기분별 비율");

        //빈 서클 색, 투명도
        //기본 값 100, 완전 불투명 255, 투명 0
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);


        //파이차트 중앙 텍스트 true
        pieChart.setDrawCenterText(true);

        //차트 탭 강조 처리
        pieChart.setHighlightPerTapEnabled(true);

        //차트 목록
        Legend pielegend= pieChart.getLegend();
        pielegend.setEnabled(false);

        //입력 레이블 색상, 글시 크기
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);

        setPieData();

        //바차트
        barChart=rootview.findViewById(R.id.barchart);
        //막대 위에 그려짐
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        //x축 레이블 설정
        XAxis barxAxis=barChart.getXAxis();
        barxAxis.setEnabled(true);

        //왼쪽 y축 설정
        YAxis barLeftyAxis=barChart.getAxisLeft();
        barLeftyAxis.setLabelCount(6, false);
        barLeftyAxis.setGranularityEnabled(true);
        //최소간격
        barLeftyAxis.setGranularity(1f);

        //오른쪽 y축 설정
        YAxis barRightAxix=barChart.getAxisRight();
        barRightAxix.setEnabled(false);

        Legend barlegend=barChart.getLegend();
        barlegend.setEnabled(false);

        barChart.animateXY(1500, 1500);

        setBarData();

        //라인차트
        lineChart=rootview.findViewById(R.id.linechart);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setViewPortOffsets(0,0,0,0);

        Legend linelegend=lineChart.getLegend();
        linelegend.setEnabled(false);

        XAxis linexAxis=barChart.getXAxis();
        linexAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        linexAxis.setTextSize(10f);
        linexAxis.setTextColor(Color.WHITE);
        linexAxis.setDrawAxisLine(false);
        linexAxis.setDrawGridLines(true);
        linexAxis.setTextColor(Color.rgb(255,192,56));
        linexAxis.setCenterAxisLabels(true);
        linexAxis.setGranularity(1f);

        linexAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat=new SimpleDateFormat("MM-DD", Locale.KOREA);
            @Override
            public String getFormattedValue(float value) {
                long mills= TimeUnit.HOURS.toMillis((long)value);
                return mFormat.format(new Date(mills));
            }
        });

        YAxis lineLeftyAxis=lineChart.getAxisLeft();
        lineLeftyAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        lineLeftyAxis.setTextColor(ColorTemplate.getHoloBlue());
        lineLeftyAxis.setDrawGridLines(true);
        lineLeftyAxis.setGranularityEnabled(true);
        lineLeftyAxis.setAxisMaximum(0f);
        lineLeftyAxis.setAxisMaximum(170f);
        lineLeftyAxis.setYOffset(-9f);
        lineLeftyAxis.setTextColor(Color.rgb(255,192,56));

        YAxis lineRightAxis=lineChart.getAxisRight();
        lineRightAxis.setEnabled(false);

        setLineData();

    }
    private void setPieData(){
        ArrayList<PieEntry> entries=new ArrayList<>();

        entries.add(new PieEntry(20.0f, "", getResources().getDrawable(R.drawable.smile1_24)));
        entries.add(new PieEntry(20.0f, "", getResources().getDrawable(R.drawable.smile2_24)));
        entries.add(new PieEntry(20.0f, "", getResources().getDrawable(R.drawable.smile3_24)));
        entries.add(new PieEntry(20.0f, "", getResources().getDrawable(R.drawable.smile4_24)));
        entries.add(new PieEntry(20.0f, "", getResources().getDrawable(R.drawable.smile5_24)));

        PieDataSet pieDataSetdataSet=new PieDataSet(entries,"기분멸 비율");
        pieDataSetdataSet.setDrawIcons(true);
        pieDataSetdataSet.setSliceSpace(3f);
        pieDataSetdataSet.setIconsOffset(new MPPointF(0, -40));
        pieDataSetdataSet.setSelectionShift(5f);

        ArrayList<Integer>colors=new ArrayList<>();
        for(int color:ColorTemplate.JOYFUL_COLORS){
            colors.add(color);
        }
        pieDataSetdataSet.setColors(colors);

        PieData data=new PieData(pieDataSetdataSet);
        data.setValueTextSize(22.0f);
        data.setValueTextSize(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();

    }
    private void setBarData(){
        ArrayList<BarEntry> entries=new ArrayList<>();

        entries.add(new BarEntry(1.0f, 20.0f, getResources().getDrawable(R.drawable.smile1_24)));
        entries.add(new BarEntry(2.0f, 40.0f, getResources().getDrawable(R.drawable.smile2_24)));
        entries.add(new BarEntry(3.0f, 60.0f, getResources().getDrawable(R.drawable.smile3_24)));
        entries.add(new BarEntry(4.0f, 30.0f, getResources().getDrawable(R.drawable.smile4_24)));
        entries.add(new BarEntry(5.0f, 90.0f, getResources().getDrawable(R.drawable.smile5_24)));

        BarDataSet barDataSet=new BarDataSet(entries, "Sinus Function");
        barDataSet.setColor(Color.rgb(240,120,124));

        ArrayList<Integer> colors=new ArrayList<>();
        for(int color : ColorTemplate.JOYFUL_COLORS){
            colors.add(color);
        }

        barDataSet.setColors(colors);
        barDataSet.setIconsOffset(new MPPointF(0,-10));

        BarData barData=new BarData(barDataSet);
        barData.setValueTextSize(10f);
        barData.setDrawValues(false);
        barData.setBarWidth(0.8f);

        barChart.setData(barData);
        barChart.invalidate();
    }
    private void setLineData(){
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(24f, 20.0f));
        values.add(new Entry(48f, 50.0f));
        values.add(new Entry(72f, 30.0f));
        values.add(new Entry(96f, 70.0f));
        values.add(new Entry(120f, 90.0f));

        LineDataSet lineDataSet = new LineDataSet(values, "DataSet 1");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setValueTextColor(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setDrawCircleHole(false);


        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(Color.WHITE);
        lineData.setValueTextSize(9f);

        // set data
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}
