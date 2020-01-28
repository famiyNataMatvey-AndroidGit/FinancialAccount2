package com.narutomatvey.financialaccount.activity.fragment;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PieGraph {

    private int size = 0;
    private Context context;
    private List<String> names;
    private List<Double> amounts;

    public PieGraph(Context context) {
        this.context = context;
    }

    public GraphicalView getGraphicalView(List<String> categoryNames, List<Double> categoryAmounts) {
        names = categoryNames;
        amounts = categoryAmounts;
        size = names.size();
        return updateGraphicalView();
    }


    private GraphicalView updateGraphicalView() {
        CategorySeries series = new CategorySeries("");
        DefaultRenderer defaultRenderer = generateDefaultRenderer();
        SimpleSeriesRenderer simpleSeriesRenderer;
        List<Integer> colors = getRandomColors();
        for (int i = 0; i < size; i++) {
            series.add(names.get(i), amounts.get(i));
            simpleSeriesRenderer = new SimpleSeriesRenderer();
            simpleSeriesRenderer.setColor(colors.get(i));
            defaultRenderer.addSeriesRenderer(simpleSeriesRenderer);
        }
        return ChartFactory.getPieChartView(context, series, defaultRenderer);
    }

    private DefaultRenderer generateDefaultRenderer() {
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setLabelsTextSize(32);
        defaultRenderer.setLabelsColor(Color.BLACK);
        return defaultRenderer;
    }

    private List<Integer> getRandomColors() {
        List<Integer> colors = new ArrayList<Integer>();
        Integer color;
        int i = 0;
        while (i < size) {
            color = getColor();
            if (colors.contains(color)) {
                continue;
            }
            colors.add(color);
            i++;
        }
        return colors;
    }

    private Integer getColor() {
        Random randomGenerator = new Random();
        int red = randomGenerator.nextInt(256);
        int green = randomGenerator.nextInt(256);
        int blue = randomGenerator.nextInt(256);
        return Color.rgb(red, green, blue);
    }
}