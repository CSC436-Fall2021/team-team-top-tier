package csc436.View;

import csc436.Model.Picture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import java.io.*;
import java.util.ArrayList;

public class ImageUploadUI extends Application {

    public static final String TEST_FILE = "fileName.ser";

    private Stage stage;
    private ArrayList<Picture> list = new ArrayList<>();

    // GraphicsContext of the main canvas
    private GraphicsContext gc;

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

        list = loadPictures(TEST_FILE);
        drawPictures(pane, canvas);

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
                            drawPictures(pane,  canvas);
                        }
                    };

                    imageCropUI.setOnCloseRequest(closeEvent);

                    Image img = pic.createImage();
                    ImageView imgView = new ImageView(pic.createImage());

                    Canvas cropCanvas = new Canvas (pic.getWidth(), pic.getHeight());
                    GraphicsContext imageGC = cropCanvas.getGraphicsContext2D();

                    imageGC.drawImage(pic.createImage(), 0, 0, pic.getWidth(), pic.getHeight());

                    //TODO: can we make a movable, scaleable rectanlge, and a button to crop?

                    double max = Math.max(pic.getWidth(), pic.getHeight());

                    BorderPane pain = new BorderPane();
                    pain.setCenter(cropCanvas);

                    Scene imageCropScene = new Scene(pain, pic.getWidth(), pic.getHeight());
                    imageCropUI.setScene(imageCropScene);
                    imageCropUI.show();

                    //crop image to square (note: gc.drawImage will auto resize)
                    list.add(pic);
                    savePictures(list, TEST_FILE);
//                  imageGC.drawImage(pic.getWritableImage(), Picture.IMAGE_CROP_SIZE, Picture.IMAGE_CROP_SIZE);
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

    public void drawPictures(BorderPane pane, Canvas canvas) {
        //Draw all the Pictures currently in the list!
        int y = 0;
        int x = 0;
        for (Picture p : list) {
            gc.drawImage(p.getCroppedImage(), x * Picture.IMAGE_CROP_SIZE, y * Picture.IMAGE_CROP_SIZE);
            x++;
            if (x == 5) {
                y ++;
                x = 0;
            }
        } // for end
        pane.setCenter(canvas);
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

    public void savePictures(ArrayList<Picture> pictures, String filename) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(pictures);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public ArrayList<Picture> loadPictures(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Picture> out = (ArrayList<Picture>) in.readObject();
            in.close();
            fileIn.close();
            return out;
        } catch (FileNotFoundException e) {
            System.out.println("No picture file found");
            return new ArrayList<>();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
