package ru.gr26x.painting;

import ru.gr26x.math.Function;
import ru.gr26x.math.StringFunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionPainter extends CartesianPainter implements Painter {

    private Function function = null;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public FunctionPainter(double xMin, double xMax, double yMin, double yMax) {
        super(xMin, xMax, yMin, yMax);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    public void setWidth(int width) {
        converter.setWidth(width);
    }

    public void setHeight(int height) {
        converter.setHeight(height);
    }

    public void setFunction(StringFunction function) {
        this.function = function;
    }

    public void paintFunction(Graphics g) {
        if (function == null) return;

        g.setColor(Color.GREEN);

        int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);

        double step = 0.01;
        double range = converter.getXMax() - converter.getXMin();
        double chunkSize = range / numThreads;

        for (int i = 0; i < numThreads; i++) {
            double startX = converter.getXMin() + i * chunkSize;
            double endX = startX + chunkSize;

            executorService.submit(() -> {
                try {
                    double xPrev = startX;
                    double yPrev = function.evaluate(xPrev);

                    for (double x = startX; x <= endX; x += step) {
                        double y = function.evaluate(x);
                        int xScreen = converter.xCrt2Scr(x);
                        int yScreen = converter.yCrt2Scr(y);

                        synchronized (g) {
                            g.drawLine(converter.xCrt2Scr(xPrev), converter.yCrt2Scr(yPrev), xScreen, yScreen);
                        }

                        xPrev = x;
                        yPrev = y;
                    }

                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка при ожидании завершения потоков", e);
        }
    }
}