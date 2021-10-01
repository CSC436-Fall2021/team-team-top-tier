package csc436.Model;

/**
 * Picture is an object that holds the path, name, and JavaFX image.
 */


import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Picture {

    public static final int IMAGE_CROP_SIZE = 75;

    private String path;
    private String name;
    private Image image;
    private ImageView imageView;
    private WritableImage writableImage;
    private SnapshotParameters params;
    private double width, height;

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
        createImage();
    } // Constructor end

    private void createImage() {
        try {
            this.image = new Image(new FileInputStream(path));
            this.imageView = new ImageView(image);

            width = image.getWidth();
            height = image.getHeight();
            System.out.printf("width: %f\nheight: %f\n", width, height);
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found.\n", path);
        }
    } // createImage end

    public void createWriteableImage(Rectangle2D area) {
        //need to use a writeable image that the snapshot can write on to
        writableImage = new WritableImage(IMAGE_CROP_SIZE, IMAGE_CROP_SIZE); //this is right
        params = new SnapshotParameters();
        //params.setViewport(new Rectangle2D(0, 0, Picture.IMAGE_CROP_SIZE, Picture.IMAGE_CROP_SIZE));
        params.setViewport(area);

        writableImage = getImageView().snapshot(params, writableImage);

    }

    /**
     * Getter Methods
     */
    public Image getImage() { return image; }
    public String getPath() { return path; }
    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public WritableImage getWritableImage() { return writableImage; }
    public ImageView getImageView() { return this.imageView; }
    public String toString() { return this.path; }
}