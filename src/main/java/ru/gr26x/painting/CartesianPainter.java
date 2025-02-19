package ru.gr26x.painting;

import java.awt.*;

public class CartesianPainter implements Painter {

    enum Size{

        Small(2),
        Medium(4),
        Large(7);

        public final int val;
        Size(int v){
            val = v;
        }
    }

    protected final Converter converter;
    public CartesianPainter(double xMin, double xMax, 
                            double yMin, double yMax){
        converter = new Converter(xMin, xMax, yMin, yMax, 0, 0);
    }

    @Override
    public int getWidth() {
        return converter.getWidth();
    }

    @Override
    public int getHeight() {
        return converter.getHeight();
    }

    @Override
    public void setWidth(int width) {
        converter.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        converter.setHeight(height);
    }

    @Override
    public void paint(Graphics g) {
        drawAxes(g);
        drawTicks(g);
    }

    private void drawTicks(Graphics g) {
        drawXTicks(g);
        drawYTicks(g);
    }

    private void drawXTicks(Graphics g) {
        var w = converter.getWidth();
        for (double i = converter.getXMin(); i<=converter.getXMax(); i+=0.1){
            i = Math.round(i * 10) / 10.0;
            Size size = switch (Math.abs((int) Math.round(i * 10)) % 10) {
                case 0 -> Size.Large;
                case 5 -> Size.Medium;
                default -> Size.Small;
            };
            drawXTick(g, converter.xCrt2Scr(i), size);
        }
    }

    private void drawYTicks(Graphics g) {
        var w = converter.getHeight();
        for (double i = converter.getYMin(); i<=converter.getYMax(); i+=0.1){
            i = Math.round(i * 10) / 10.0;
            Size size = switch (Math.abs((int)Math.round(i * 10)) % 10) {
                case 0 -> Size.Large;
                case 5 -> Size.Medium;
                default -> Size.Small;
            };
            drawYTick(g, converter.yCrt2Scr(i), size);
        }
    }

    private void drawXTick(Graphics g, int pos, Size sz){
        var c = (sz == Size.Large)?Color.RED:Color.BLUE;
        var y0 = converter.yCrt2Scr(0);
        g.setColor(c);
        int p1, p2;
        if (y0<0 || y0>converter.getHeight()){
            p1 = 0;
            p2 = converter.getHeight();
        } else {
            p1 = p2 = y0;
        }
        g.drawLine(pos, p1, pos, p1+sz.val);
        g.drawLine(pos, p2, pos, p2-sz.val);
        if (sz == Size.Large) {
            drawXLabel(g, pos, p1 + sz.val + 2, Math.round(converter.xScr2Crt(pos) * 10) / 10., true);
            if (p1 != p2) drawXLabel(g, pos, p2 - sz.val - 2, Math.round(converter.xScr2Crt(pos) * 10) / 10., false);
        }
    }

    private void drawYTick(Graphics g, int pos, Size sz){
        var c = (sz == Size.Large)?Color.RED:Color.BLUE;
        var x0 = converter.xCrt2Scr(0);
        g.setColor(c);
        g.drawLine(x0-sz.val, pos, x0+sz.val, pos);
        int p1, p2;
        if (x0<0 || x0>converter.getWidth()){
            p1 = 0;
            p2 = converter.getWidth();
        } else {
            p1 = p2 = x0;
        }
        g.drawLine(p1, pos, p1+sz.val, pos);
        g.drawLine(p2, pos, p2-sz.val, pos);
        if (sz == Size.Large) {
            drawYLabel(g, p1 + sz.val + 2, pos, Math.round(converter.yScr2Crt(pos) * 10) / 10., true);
            if (p1 != p2)
                drawYLabel(g, p2 - sz.val - 2, pos, Math.round(converter.yScr2Crt(pos) * 10) / 10., false);
        }
    }

    private void drawXLabel(Graphics g, int posX, int posY, double value, boolean down){
        if (Double.compare(value, 0)==0) return;
        Font f = new Font("Cambria", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(f);
        var b = metrics.getStringBounds(
                ""+value,
                g
        );
        var w = b.getWidth();
        posX -= (int)(w/2);
        g.setFont(f);
        g.setColor(Color.RED);
        g.drawString(""+value,
                posX,
                posY + (int)((down)?b.getHeight():-2)
        );
    }

    private void drawYLabel(Graphics g, int posX, int posY, double value, boolean right){
        if (Double.compare(value, 0)==0) return;
        Font f = new Font("Cambria", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(f);
        var b = metrics.getStringBounds(""+value, g);
        var h = b.getHeight();
        posY += (int)(h/3);
        g.setFont(f);
        g.setColor(Color.RED);
        g.drawString(""+value,
                posX - (int)((right)?0:b.getWidth()),
                posY
        );
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.BLACK);
        var x0 = converter.xCrt2Scr(0);
        var y0 = converter.yCrt2Scr(0);
        g.drawLine(0, y0, converter.getWidth(), y0);
        g.drawLine(x0, 0, x0, converter.getHeight());
    }
}