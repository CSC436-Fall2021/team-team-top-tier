package csc436.Model;

/**
 * Picture is an object that holds the path, name, and JavaFX image.
 * Picture is now serializable, yay!
 */

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class Picture implements Serializable{

    public static final int IMAGE_CROP_SIZE = 75;

    private String path;
    private String name;
    private double width, height; //actual image information
    private double x1, x2, y1, y2;  //crop parameters (set by user)

    /**
     * Picture(String path, String name) constructor
     * @param path - the file path of the picture
     * @param name - the given name of the picture
     */
    public Picture(String path, String name) {
        this.path = path;
        this.name = name;
        createImage();
    } // Constructor end

    /**
     * Picture(String path, String name) alternative constructor (without name)
     * @param path - the file path of the picture
     */
    public Picture(String path) {
        this.path = path;
        this.name = path;
        createImage(); //sets width and height
    } // Constructor end

    /**
     * Returns the image that is loaded from the file path,
     * this currently also sets the width and height parameters,
     * and prints a debug message
     * @return
     */
    public Image createImage() {
        try {
            Image image = new Image(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
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
    public WritableImage getCroppedImage() {
        Image original = createImage();
        PixelReader reader = original.getPixelReader();
        setSquareCrop(0, 0);
        return new WritableImage(reader, (int) x1, (int)y1, IMAGE_CROP_SIZE, IMAGE_CROP_SIZE);
    }

    public void setSquareCrop(double x1, double y1) {
        //defaults for now, somewhere in the middle
        //TODO: users set this
        this.x1 = width / 2;  //this.x1 = x1;
        this.y1 = height / 2;  //this.y1 = y1;
    }

    /**
     * Getter Methods
     */
    public String getPath() { return path; }
    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String toString() { return this.path; }
}