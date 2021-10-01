package csc436.View;

import csc436.Model.Picture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.io.File;
import java.util.ArrayList;

public class ImageUploadUI extends Application {



    Stage stage;
    ArrayList<Picture> list = new ArrayList<>();

    // GraphicsContext of the main canvas
    GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageIn) throws Exception {
        stage = stageIn;
        stage.setTitle("Image upload");

        Canvas canvas = new Canvas(375, 375);
        gc = canvas.getGraphicsContext2D();
        BorderPane pane = new BorderPane();

        // TODO: create a canvas
        // TODO: add a button that opens a file browser and adds the paths to some collection
        // TODO: draw images on canvas
        /*
         * Eventually have something like
         *
         * for (Picture pic: pictures) {
         *      gc.drawImage(pic.getImage(), x, y);
         * }
         *
         */

        importButt.setOnAction(importEvent);

        pane.setTop(importButt);

        //Show stage and set scene
        Scene scene = new Scene(pane, 500, 500);

        stage.setScene(scene);
        stage.show();
    }
}
