package csc436.View;

import csc436.Model.Picture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;

import java.io.*;
import java.util.ArrayList;

public class ImageUploadUI extends Application {

    public static final String TEST_FILE = "fileName.ser";

    private Stage stage;
    private ArrayList<Picture> list = new ArrayList<>();

    BorderPane pane;
    GridPane grid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageIn) throws Exception {
        stage = stageIn;
        stage.setTitle("Image upload");

        grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 50, 25, 50));
        pane = new BorderPane();

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

                    doCropUI(pic, false);
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

    public void doCropUI(Picture pic, Boolean edit) {
        //temporary picture copy for editing
        Picture savedPicState = pic;
        //Show the uploaded image in a pop-up
        final Stage imageCropUI = new Stage();
        imageCropUI.initModality(Modality.WINDOW_MODAL); //this makes the rest of the application wait
        imageCropUI.initOwner(stage);

        EventHandler<ActionEvent> saveEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                if (!edit) {
                    list.add(pic);
                }
                drawPictures(pane,  grid);
                savePictures(list, TEST_FILE);
                imageCropUI.close();
            }
        };

        EventHandler<ActionEvent> cancelEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                if (edit) {
                    int idx = list.indexOf(pic);
                    list.set(idx, savedPicState);
                }
                imageCropUI.close();
            }
        };

        BorderPane imagePain = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER_LEFT);
        Button save_butt = new Button("Save");
        save_butt.setOnAction(saveEvent);
        Button cancel_butt = new Button("Cancel");
        cancel_butt.setOnAction(cancelEvent);
        buttons.getChildren().addAll(save_butt, cancel_butt);
        imagePain.setBottom(buttons);

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

        Scene imageCropScene = new Scene(pain, pic.getWidth(), (pic.getHeight() + 50));
        imageCropUI.setScene(imageCropScene);
        imageCropUI.show();
    }

    public void drawPictures(BorderPane pane, GridPane grid) {
        //Draw all the Pictures currently in the list!
        int y = 0;
        int x = 0;
        for (Picture p : list) {
            EventHandler<ActionEvent> editPicture = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    doCropUI(p, true);
                }
            };
            Button picButt = new Button();
            picButt.setGraphic(p.getCroppedImage());
            picButt.setOnAction(editPicture);
            grid.add(picButt, x, y);
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
