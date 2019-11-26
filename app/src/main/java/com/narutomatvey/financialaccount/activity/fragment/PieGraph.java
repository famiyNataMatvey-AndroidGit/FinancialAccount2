package com.narutomatvey.financialaccount.activity.fragment;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class PieGraph {

    GraphicalView getGraphicalView(Context context) {
        CategorySeries series = new CategorySeries("");
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setLabelsTextSize(32);
        defaultRenderer.setLabelsColor(Color.BLACK);
        SimpleSeriesRenderer simpleSeriesRenderer = null;
        return ChartFactory.getPieChartView(context, series, defaultRenderer);
    }

    private Integer[] getRandomColors(int colorNumber) {
        Integer[] colors = new Integer[colorNumber];
        Integer color;
        int i = 0;
        while (i < colorNumber) {
            color = getColor();
            if (Arrays.asList(colors).contains(color))
                continue;
            colors[i] = color;
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