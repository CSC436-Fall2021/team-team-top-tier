package csc436.View;

import csc436.Model.Picture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;

import java.io.*;
import java.util.ArrayList;

public class ImageUploadUI extends Application {

    public static final String TEST_FILE = "fileName.ser";

    private Stage stage;
    private ArrayList<Picture> list = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageIn) throws Exception {
        stage = stageIn;
        stage.setTitle("Image upload");

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 50, 25, 50));
        BorderPane pane = new BorderPane();

        list = loadPictures(TEST_FILE);
        drawPictures(pane, grid);

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
                            drawPictures(pane,  grid);
                            savePictures(list, TEST_FILE);
                        }
                    };

                    imageCropUI.setOnCloseRequest(closeEvent);

                    BorderPane imagePain = new BorderPane();
                    Group imageGroup = new Group(); //allows drawing on top of eachother
                    imagePain.setCenter(imageGroup);
                    imageGroup.getChildren().add(new ImageView(pic.createImage()));
                    Rectangle crop = new Rectangle(0,0,0,0);
                    crop.setStroke(Color.CADETBLUE);
                    crop.setFill(null);
                    imageGroup.getChildren().add(crop);

                    EventHandler<MouseEvent> begin_drag = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent click) {
                            imageGroup.getChildren().remove(crop);
                            pic.setSquareCrop(click.getX(), click.getY());
                            crop.setX(click.getX());
                            crop.setY(click.getY());
                            crop.setHeight(0);
                            crop.setWidth(0);
                            imageGroup.getChildren().add(crop);
                        }
                    };

                    EventHandler<MouseEvent> do_drag = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent drag) {
                            double x_drag = drag.getX() - pic.getCropX();
                            double y_drag = drag.getY() - pic.getCropY();
                            if (x_drag > y_drag) {
                                pic.setCropDemensions(x_drag);
                            } else {
                                pic.setCropDemensions(y_drag);
                            }
                            crop.setWidth(pic.getCropDemensions());
                            crop.setHeight(pic.getCropDemensions());
                        }
                    };

                    imagePain.setOnMousePressed(begin_drag);
                    imagePain.setOnMouseDragged(do_drag);

                    BorderPane pain = new BorderPane();
                    pain.setCenter(imagePain);

                    Scene imageCropScene = new Scene(pain, pic.getWidth(), pic.getHeight());
                    imageCropUI.setScene(imageCropScene);
                    imageCropUI.show();

                    //crop image to square (note: gc.drawImage will auto resize)
                    list.add(pic);
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

    public void drawPictures(BorderPane pane, GridPane grid) {
        //Draw all the Pictures currently in the list!
        int y = 0;
        int x = 0;
        for (Picture p : list) {
            grid.add(p.getCroppedImage(), x, y);
            x++;
            if (x == 5) {
                y ++;
                x = 0;
            }
        } // for end
        pane.setCenter(grid);
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
