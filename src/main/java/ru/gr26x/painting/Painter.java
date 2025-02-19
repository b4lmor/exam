package ru.gr26x.painting;

import java.awt.*;

public interface Painter {
    int getWidth();
    int getHeight();
    void setWidth(int width);
    void setHeight(int height);

    void paint(Graphics g);
}
