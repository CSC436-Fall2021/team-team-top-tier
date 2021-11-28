package csc436.Model;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class SerializableColor implements Serializable {
    private double red;
    private double green;
    private double blue;
    private double alpha;

    public SerializableColor(Color color)
    {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getOpacity();
    }

    public Color getFXColor()
    {
        return new Color(red, green, blue, alpha);
    }
}
