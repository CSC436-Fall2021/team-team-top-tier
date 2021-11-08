package csc436.View;

import csc436.Model.Picture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class ImageUploadUI extends Application {

    public static final String TEST_FILE = "fileName.ser";
    public static final int TOOLTIP_XOFFSET = 10;
    public static final int TOOLTIP_YOFFSET = 7;

    public static double cropOriginX;
    public static double cropOriginY;
    public String fileName;

    private Stage stage;
    private ArrayList<Picture> list = new ArrayList<>();
    private double dragStartX, dragStartY;

    BorderPane pane;
    GridPane grid;

    private Button importButt;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stageIn) throws Exception {
        stage = stageIn;
        stage.setTitle("Image upload");

        pane = new BorderPane();
        pane.setStyle("-fx-background-color: crimson");
        grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(15, 15, 15, 15));

        list = loadPictures(TEST_FILE);
        drawPictures(grid);

        createImportButt();

        // draw the importButt onto the scene
        BorderPane secondPane = new BorderPane();
        secondPane.setPadding(new Insets(10, 5, 5, 5));
        secondPane.setCenter(importButt);
        pane.setTop(secondPane);

        //Show stage and set scene
        Scene scene = new Scene(pane, 500, 500);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Constructor used in TierListUI to create the image upload button
     *
     * @param tierListName is used to save the serializable file name
     */
    public ImageUploadUI(String tierListName, GridPane imageGrid) {
        fileName = tierListName + ".ser";
        grid = imageGrid;
        createImportButt();
        list = loadPictures(fileName);
//        drawPictures(imageGrid);
        drawPicturesAsImages(imageGrid);
    }

    /**
     * Create the importButton and set its event handler
     */
    public void createImportButt() {
        importButt =  new Button("Import Images");
//        importButt.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
        EventHandler<ActionEvent> importEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = doFilePickerUI();
                if (file != null) {
                    //deal with the file...
                    //TODO: add a name this picture popup
                    Picture pic_load = new Picture(file.getPath());;

                    double sceneWidth = pic_load.getWidth();
                    double sceneHeight = pic_load.getHeight();
                    if (sceneWidth >= sceneHeight && sceneWidth > Screen.getPrimary().getBounds().getWidth() - 200) {
                        sceneWidth = Screen.getPrimary().getBounds().getWidth() - 500;
                        pic_load = new Picture(file.getPath(), sceneWidth, sceneWidth);
                        sceneHeight = pic_load.getHeight();
                    } else if (sceneHeight >= sceneWidth && sceneHeight > Screen.getPrimary().getBounds().getHeight() - 200) {
                        sceneHeight = Screen.getPrimary().getBounds().getHeight() - 500;
                        pic_load = new Picture(file.getPath(), sceneHeight, sceneHeight);
                        sceneWidth = pic_load.getWidth();
                    }
                    Picture pic = pic_load;

                    doCropUI(pic, false);
                }
            }
        };
        importButt.setOnAction(importEvent);
    }

    public void doCropUI(Picture pic, Boolean edit) {
        //temporary picture copy for editing
        Picture savedPicState = pic;
        pic.setCropDemensions(0);
        //Show the uploaded image in a pop-up
        final Stage imageCropUI = new Stage();
        imageCropUI.setTitle("Crop image");
        imageCropUI.initModality(Modality.WINDOW_MODAL); //this makes the rest of the application wait
        imageCropUI.initOwner(stage);

        BorderPane imagePain = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button save_butt = new Button("Save");
        Button cancel_butt = new Button("Cancel");
        buttons.getChildren().addAll(save_butt, cancel_butt);
        //imagePain.setBottom(buttons);

        Group imageGroup = new Group(); //allows drawing on top of eachother
        imagePain.setCenter(imageGroup);
        ImageView img = new ImageView(pic.createImage());
        imageGroup.getChildren().add(img);
        Rectangle crop = new Rectangle(0,0,0,0);
        //crop.widthProperty().bind(imagePain.widthProperty());

        crop.setStroke(Color.CADETBLUE);
        crop.setFill(null);
        imageGroup.getChildren().add(crop);

        EventHandler<ActionEvent> saveEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                if(crop.getHeight() > 0) {
                    if(!checkCrop(pic, crop)) {
                        pic.setCropDemensions(0);
                    } else {
                        pic.setSquareCrop(crop.getX(), crop.getY());
                        pic.setCropDemensions(crop.getHeight());
                    }
                }
                if (!edit) {
                    list.add(pic);
                }
//                drawPictures(grid);
                drawPicturesAsImages(grid);
                savePictures(list, fileName);
                imageCropUI.close();
            }
        };
        save_butt.setOnAction(saveEvent);

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
        cancel_butt.setOnAction(cancelEvent);

        EventHandler<MouseEvent> begin_drag = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getY() >= pic.getHeight() || click.getX() >= pic.getWidth()) {
                    return;
                }
                imageGroup.getChildren().remove(crop);
                crop.setX(click.getX());
                crop.setY(click.getY());
                cropOriginX = click.getX();
                cropOriginY = click.getY();
                crop.setHeight(0);
                crop.setWidth(0);
                imageGroup.getChildren().add(crop);
            }
        };
        imagePain.setOnMousePressed(begin_drag);

        EventHandler<MouseEvent> do_drag = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent drag) {
                if(drag.getX() >= pic.getWidth() || drag.getY() >= pic.getHeight() || drag.getX() <= 0 || drag.getY() <= 0) {
                    return;
                }
                double drag_width = drag.getX() - cropOriginX;
                double drag_height = drag.getY() - cropOriginY;
                if(drag_width < 0 && drag_height < 0) {
                    System.out.print("NW: ");
                    System.out.println(drag_width);
                    System.out.println(drag_height);
                    drag_width = drag_width * -1;
                    drag_height= drag_height * -1;
                    if (drag_width < drag_height) {
                        crop.setX(drag.getX());
                        crop.setY(cropOriginY - (cropOriginX - drag.getX()));
                    } else {
                        crop.setX(cropOriginX - (cropOriginY - drag.getY()));
                        crop.setY(drag.getY());
                    }
                } else if(drag_width < 0) {
                    System.out.println("SW");
                    crop.setY(cropOriginY);
                    drag_width = drag_width * -1;
                    if (drag_width < drag_height ) {
                        crop.setX(drag.getX());
                    } else {
                        crop.setX(cropOriginX - (drag.getY() - cropOriginY));
                    }
                } else if (drag_height < 0) {
                    System.out.println("NE");
                    crop.setX(cropOriginX);
                    drag_height = drag_height * -1;
                    if (drag_width < drag_height ) {
                        crop.setY(cropOriginY - (drag.getX() - cropOriginX));
                    } else {
                        crop.setY(drag.getY());
                    }
                } else {
                    System.out.println("SE");
                    crop.setX(cropOriginX);
                    crop.setY(cropOriginY);
                }
                if (drag_width < drag_height ) {
                    crop.setWidth(drag_width);
                    crop.setHeight(drag_width);
                } else {
                    crop.setWidth(drag_height);
                    crop.setHeight(drag_height);
                }
            }
        };
        imagePain.setOnMouseDragged(do_drag);

        BorderPane pain = new BorderPane();
        pain.setCenter(imagePain);
        pain.setBottom(buttons);

        imageCropUI.setMaxWidth(pic.getWidth());
        imageCropUI.setMaxHeight(pic.getHeight() + 75);
        imageCropUI.setResizable(false);
        Scene imageCropScene = new Scene(pain, pic.getWidth(), (pic.getHeight() + 75));
        imageCropUI.setScene(imageCropScene);
        imageCropUI.show();
    }

    public void drawPictures(GridPane grid) {
        //Draw all the Pictures currently in the list!
        int y = 1;
        int x = 0;
        for (Picture p : list) {
            EventHandler<ActionEvent> editPicture = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    doCropUI(p, true);
                }
            };
            EventHandler<ActionEvent> renamePicture = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TextInputDialog renamePopup = new TextInputDialog(p.getName());
                    renamePopup.setTitle("Rename image");
                    renamePopup.setHeaderText(null);
                    renamePopup.setGraphic(null);
                    Optional<String> result = renamePopup.showAndWait();
                    if (result.isPresent()) {
                        p.setName(renamePopup.getEditor().getText());
                        drawPictures(grid);  //refesh UI
                        savePictures(list, TEST_FILE);  //save image
                    }
                }
            };

            ContextMenu menu = new ContextMenu();
            MenuItem editContext = new MenuItem("Edit");
            editContext.setOnAction(editPicture);
            MenuItem renameContext = new MenuItem("Rename");
            renameContext.setOnAction(renamePicture);
            menu.getItems().addAll(editContext, renameContext);

            // picture is drawn onto a button
            Button picButt = new Button();
            picButt.setGraphic(p.getCroppedImage());
            picButt.setOnAction(editPicture);
            picButt.setContextMenu(menu);

            Tooltip tip = new Tooltip(p.getName());
            Tooltip.install(picButt, tip);
            tip.setHideDelay(null);
            picButt.setOnMouseMoved( ev -> {
                tip.show(picButt, ev.getScreenX() + TOOLTIP_XOFFSET, ev.getScreenY() + TOOLTIP_YOFFSET);
            });
            picButt.setOnMouseExited( ev -> {
                tip.hide();
            });

            grid.add(picButt, x, y);
            x++;
            if (x == 10) {
                y ++;
                x = 0;
            }
        } // for end
    }

    public void drawPicturesAsImages(GridPane grid) {
        //Draw all the Pictures currently in the list!
        int y = 1;
        int x = 0;
        for (Picture p : list) {
            ImageView pImageView = p.getCroppedImage();
            pImageView.setOnDragDetected((event) -> {
                Dragboard dBoard= pImageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent clipCont= new ClipboardContent();
                clipCont.putImage(pImageView.getImage());
                dBoard.setContent(clipCont);

                event.consume();
            });

            pImageView.setOnDragDone((event) -> {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    grid.getChildren().remove(pImageView);
                }
                event.consume();
                list.remove(p);
            });
            EventHandler<ActionEvent> editPicture = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    doCropUI(p, true);
                }
            };
            grid.add(pImageView, x, y);
            x++;
            if (x == 10) {
                y ++;
                x = 0;
            }
        } // for end
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

    private boolean checkCrop(Picture pic, Rectangle crop) {
        if (crop.getX() > pic.getWidth()) {
            return false;
        }
        if (crop.getY() > pic.getHeight()) {
            return false;
        }
        return true;
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
        } catch (InvalidClassException e2) {
            System.out.println("Class Mismatch... change in Picture.java?");
            File file = new File(filename);
            if(!file.delete()) {
                System.out.println("Failed to delete the bad file... manual delete required");
            }
            return new ArrayList<>();
        } catch ( IOException e3) {
            e3.printStackTrace();
        } catch (ClassNotFoundException e4) {
            e4.printStackTrace();
        }
        return null;
    }

    public Button getImageUploadButt() { return importButt; }
}
