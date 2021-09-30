package csc436.Model;

/**
 * Picture is an object that holds the path, name, and JavaFX image.
 */


import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Picture {
    private String path;
    private String name;
    private Image image;

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
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found.\n", path);
        }
    } // createImage end

    /**
     * Getter Methods
     */
    public Image getImage() { return image; }
    public String getPath() { return path; }
    public String getName() { return name; }
}