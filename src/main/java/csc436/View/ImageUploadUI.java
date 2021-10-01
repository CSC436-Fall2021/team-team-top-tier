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
// broken?


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

        Button importButt =  new Button("Import Images");
        EventHandler<ActionEvent> importEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = doFilePickerUI();
                if (file != null) {
                    //deal with the file...
                    //TODO: add a name this picture popup
                    Picture pic = new Picture(file.getPath());

                    //Show the uploaded image in a pop-up
                    final Stage imageCropUI = new Stage();
                    imageCropUI.initModality(Modality.WINDOW_MODAL); //this makes the rest of the application wait
                    imageCropUI.initOwner(stage);

                    EventHandler<WindowEvent> closeEvent = new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent){


                            //Draw all the Pictures currently in the list!
                            int y = 0;
                            int x = 0;
                            for (Picture p : list) {
                                gc.drawImage(p.getWritableImage(), x * Picture.IMAGE_CROP_SIZE, y * Picture.IMAGE_CROP_SIZE);
                                x++;
                                if (x == 5) {
                                    y ++;
                                    x = 0;
                                }
                            } // for end
                            pane.setCenter(canvas);
                        }
                    };

                    imageCropUI.setOnCloseRequest(closeEvent);

                    Canvas cropCanvas = new Canvas (200, 200);
                    GraphicsContext imageGC = cropCanvas.getGraphicsContext2D();

                    pic.getImageView().setFitHeight(75);
                    pic.getImageView().setPreserveRatio(true);

                    imageGC.drawImage(pic.getImage(), 0, 0, 200, 200);

                    //TODO: can we make a movable, scaleable rectanlge, and a button to crop?

                    double max = Math.max(pic.getWidth(), pic.getHeight());
                    Rectangle2D area = new Rectangle2D(0, 0, Picture.IMAGE_CROP_SIZE, Picture.IMAGE_CROP_SIZE);

                    BorderPane pain = new BorderPane();
                    pain.setCenter(cropCanvas);

                    Scene imageCropScene = new Scene(pain, 200, 200);
                    imageCropUI.setScene(imageCropScene);
                    imageCropUI.show();

                    //crop image to square (note: gc.drawImage will auto resize)
                    pic.createWriteableImage(area);
                    list.add(pic);

//                    imageGC.drawImage(pic.getWritableImage(), Picture.IMAGE_CROP_SIZE, Picture.IMAGE_CROP_SIZE);
                }
            }
        };

        importButt.setOnAction(importEvent);

        pane.setTop(importButt);

        //Show stage and set scene
        Scene scene = new Scene(pane, 500, 500);

        stage.setScene(scene);
        stage.show();
    }

    public File doFilePickerUI() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick an image to upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        //get the file chosen from the file picker
        return fileChooser.showOpenDialog(stage);
    }
}
