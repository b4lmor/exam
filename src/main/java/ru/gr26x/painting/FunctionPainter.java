package ru.gr26x.painting;

import ru.gr26x.math.Function;
import ru.gr26x.math.StringFunction;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FunctionPainter extends CartesianPainter implements Painter{

    private Function function = null;

    public FunctionPainter(double xMin, double xMax,
                           double yMin, double yMax){
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

    }

    public void setHeight(int height){

    }

    public void setFunction(StringFunction function){
        this.function = function;
    }

    public void paintFunction(Graphics g) {
        g.setColor(Color.GREEN);
        double step = 0.01;
        double xPrev = converter.getXMin();
        double yPrev = function.evaluate(xPrev);

        for (double x = converter.getXMin(); x <= converter.getXMax(); x += step) {
            double y = function.evaluate(x);
            int xScreen = converter.xCrt2Scr(x);
            int yScreen = converter.yCrt2Scr(y);

            g.drawLine(converter.xCrt2Scr(xPrev), converter.yCrt2Scr(yPrev), xScreen, yScreen);

            xPrev = x;
            yPrev = y;
        }
    }
}
