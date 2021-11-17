package csc436.Model;

/**
 * Picture is an object that holds the path, name, and JavaFX image.
 * Picture is now serializable, yay!
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DataFormat;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class Picture implements Serializable, Comparable {

    public static final int IMAGE_CROP_SIZE = 75;
    public static PictureSortFlag sortMethod = PictureSortFlag.ByName;

    public static final DataFormat PICTURE_FORMAT = new DataFormat("Picture Object");


    private String path;
    private String name;
    private long timeIn;
    private int bad;
    private double width, height; //actual image information
    private double x1, y1;  //crop parameters (set by user)
    private double cropDemensions;

    /**
     * Picture(String path, String name) alternative constructor (without name)
     * @param path - the file path of the picture
     */
    public Picture(String path) {
        this.path = path;
        this.name = path;
        timeIn = System.currentTimeMillis();
        Image image = null;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
    } // Constructor end

    public Picture(String path, double width, double height) {
        this.path = path;
        this.name = path;
        timeIn = System.currentTimeMillis();
        Image image = null;
        try {
            image = new Image(new FileInputStream(path), width, height, true, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
    } // Constructor end

    /**
     * Returns the image that is loaded from the file path,
     * this currently also sets the width and height parameters,
     * and prints a debug message
     * @return
     */
    public Image createImage() {
        try {
            Image image = new Image(new FileInputStream(path), width, height, true, true);
            System.out.printf("width: %f\nheight: %f\n", width, height);
            return image;
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found.\n", path);
        }
        return null;
    } // createImage end

    /**
     * Returns the image crop as specified by the user parameters
     * @return WriteableImage that has been cropped to the default
     * size
     */
    public ImageView getCroppedImage() {
        Image original = createImage();
        PixelReader reader = original.getPixelReader();
        WritableImage write = new WritableImage(reader, (int) x1, (int)y1, (int)cropDemensions, (int)cropDemensions);
        ImageView view = new ImageView(write);
        view.setPreserveRatio(true);
        view.setFitHeight(IMAGE_CROP_SIZE);
        return view;
    }

    public static void setSortMethod(PictureSortFlag option) {
        sortMethod = option;
    }

    public void setSquareCrop(double x1, double y1) {
        this.x1 = x1;
        this.y1 = y1;
    }

    public void setCropDemensions(double dem) {
        if(dem == 0) {
            setSquareCrop(0, 0);
            if(width < height) {
                this.cropDemensions = width;
            } else {
                this.cropDemensions = height;
            }
            return;
        }
        this.cropDemensions = dem;
    }

    public double getCropDemensions() {
        return cropDemensions;
    }

    /**
     * Getter and Setter Methods
     */
    public String getPath() { return path; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name;}
    public long getUploadTime() { return timeIn; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String toString() { return this.path; }
    public double getCropX() {
        return x1;
    }
    public double getCropY() {
        return y1;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        int retVal = 0;
        switch (sortMethod) {
            case ByDate:
                retVal = ((Long)((Picture)o).getUploadTime()).compareTo(timeIn);
                break;
            case ByName:
                retVal = ((Picture)o).getName().compareTo(name);
                break;
        }
        return retVal;
    }
}