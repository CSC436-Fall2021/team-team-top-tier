package csc436.View;

import csc436.Model.Picture;
import csc436.Model.Tier;
import csc436.Model.TierList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Program: TierListUI.java
 * Purpose: TierListUI class that contains a method that constructs and
 *          can return a Scene UI element that displays the properties of a TierList obj.
 *          This is the core user interface for the Tier Maker application.
 *
 * Created: 09/29/2021
 * @author AMir Ameri, Victor Jimenez, David Dung
 */
public class TierListUI {

    public static final int MAX_PICTURE_NUMBER = 6;
    public static final int PICTURE_DRAW_SIZE = 120;

    private static final int COLUMN_WIDTH = 200;

    private TierList tierList;
    private float windowWidth;
    private int  windowHeight;
    private String selectedTierTitle;
    private Tier selectedTier;
    private BorderPane pane;
    private int indexOfSelectedTier;
    private Scene tierListScene;

    Color tierLevelCurrent;

    private HashMap<FlowPane, ImageView> tierBoxes;
    private GridPane tierGrid;
    private GridPane imageGrid;

    private ImageUploadUI imageUploadUI;

    public TierListUI(TierList tierList){
        this.tierList= tierList;
        windowWidth= 1080;
        windowHeight= 720;
    }

    public Scene getTierListUI() {

        pane= new BorderPane();
        tierGrid= new GridPane();
        imageGrid = new GridPane();
        tierBoxes = new HashMap<FlowPane, ImageView>();
        //ScrollPane added for tiers section
        ScrollPane tierGridScroll = new ScrollPane(tierGrid);
        tierGridScroll.setPrefViewportHeight(500);
        tierGridScroll.setPrefViewportWidth(1120);
        tierGridScroll.setFitToHeight(true);
        tierGridScroll.setFitToWidth(true);
        tierGridScroll.setStyle("-fx-background: black; -fx-border-color: black;");
        HBox scrollPane = new HBox(tierGridScroll);

        //ScrollPane added for images section
        ScrollPane imageGridScroll = new ScrollPane(imageGrid);
        imageGridScroll.setFitToHeight(true);
        imageGridScroll.setFitToWidth(true);
        imageGridScroll.setStyle("-fx-background: black; -fx-border-color: black;");
        HBox imageScrollPane = new HBox(imageGridScroll);

        VBox gridBox= new VBox(scrollPane,imageScrollPane);

        //menu to save picture
        MenuBar menu = new MenuBar();
        Menu saveMenu = new Menu("Save");
        saveMenu.getItems().add(makeExportMenu());
        menu.getMenus().add(saveMenu);
        Menu styleMenu = new Menu("Style");
        styleMenu.getItems().add(makeColorMenu());
        menu.getMenus().add(styleMenu);

        // Add Back Button
        //Gets the "back" button image path.

        InputStream backStream = null;
        try {
            backStream = new FileInputStream("src/main/java/csc436/Images/BackArrow.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //ImageView for the back button (Cog button)
        Image backImage = new Image(backStream);
        //Creates ImageViews for BackArrow.png
        ImageView backView = new ImageView(backImage);
        backView.setFitWidth(150);
        backView.setFitHeight(150);
        backView.setPreserveRatio(true);

        // Options Button code section
        Button backButton = new Button();
        backButton.setStyle("-fx-font-size: 25pt; -fx-background-radius: 20px; -fx-background-color: black; -fx-text-fill: black;");
        backButton.setGraphic(backView);

        backButton.setOnAction((event) -> {
            System.out.println("GO BACK");
            });

        Label title= new Label(tierList.getTierListTitle());
        VBox titleBox= new VBox();
        titleBox.getChildren().addAll(menu, title,backButton);

        pane.setTop(titleBox);
        pane.setCenter(gridBox);

        // Position title and grids
        pane.setMargin(titleBox, new Insets(0,0,40,0));
        gridBox.setMargin(scrollPane, new Insets(0,0,40,0));
        gridBox.setMargin(imageScrollPane, new Insets(0,0,40,0));
        imageGrid.setPadding(new Insets(0, 10, 0, 10));

        titleBox.setAlignment(Pos.TOP_CENTER);
        scrollPane.setAlignment(Pos.TOP_CENTER);
        imageScrollPane.setAlignment(Pos.BOTTOM_CENTER);

        pane.setStyle("-fx-background-color: black");
        tierGrid.setBackground(new Background(new BackgroundFill(tierList.getBackgroundColor(), null, null)));

        title.setStyle("-fx-text-fill: white; -fx-font-family: impact");
        title.setFont(Font.font("Regular", FontWeight.BOLD, FontPosture.REGULAR, 70));

        Scene scene = new Scene(pane, windowWidth, windowHeight);
        // TODO: CHANGE CONSTRAINTS TO USER'S MONITOR DIMENSIONS
        tierGrid.getColumnConstraints().add(new ColumnConstraints(COLUMN_WIDTH));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(MAX_PICTURE_NUMBER * PICTURE_DRAW_SIZE + 6));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(COLUMN_WIDTH + 8));

        getImageUI();
        makeTierListUI();
        return scene;
    }

    private void refreshTier(FlowPane pictureList, Tier current, HBox addIcon) {
        pictureList.getChildren().clear();

        for(Picture pic : current.getPictures()) {
            ImageView picture = pic.getCroppedImage();
            picture.setOnMouseClicked((event) -> {
                current.removePicture(pic);
                refreshTier(pictureList, current, addIcon);
                imageUploadUI.getList().add(pic);
                imageUploadUI.drawPicturesAsImages();
//                imageUploadUI.savePictures(imageUploadUI.getList(), imageUploadUI.fileName);
            });
            picture.setFitHeight(PICTURE_DRAW_SIZE);
            picture.setFitWidth(PICTURE_DRAW_SIZE);
            pictureList.getChildren().add(picture);
        }

        pictureList.getChildren().add(addIcon);
    }

    // Create and return the Tier List's base user interface
    public void  makeTierListUI() {
        tierGrid.getChildren().clear();
        tierGrid.getRowConstraints().clear();
        imageGrid.getChildren().clear();
        List<Tier> tiers= tierList.getTiers();

        // The loop adds nodes to the tier list GridPane and stylizes them as well
        tierLevelCurrent = tierList.getTierRowColor();
        for (int i=0;i<tiers.size();i++) {
            int index = i;
            Label tierTitleLabel = new Label(tiers.get(index).getTierTitle());
            HBox hBoxTierTitle = new HBox(tierTitleLabel);

            // Modifies tier name boxes
            hBoxTierTitle.setAlignment(Pos.CENTER);
            String r = Integer.toString((int) (tierLevelCurrent.getRed() * 255));
            String g = Integer.toString((int) (tierLevelCurrent.getGreen() * 255));
            String b = Integer.toString((int) (tierLevelCurrent.getBlue() * 255));
            tierTitleLabel.setStyle(
                    "-fx-text-fill: white;"+"-fx-font-size: 50px;"+"-fx-font-weight: bold;");
            hBoxTierTitle.setStyle("-fx-background-color: rgb("+r+","+g+","+b+");" +
                    "-fx-border-color: white;" + "-fx-border-width: 3;");
            tierLevelCurrent = tierLevelCurrent.darker();


            //Gets the "Add" button image path.
            InputStream addStream = null;
            try {
                addStream = new FileInputStream("src/main/java/csc436/Images/addImage.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image addImg = new Image(addStream, PICTURE_DRAW_SIZE,PICTURE_DRAW_SIZE, false,true);
            ImageView addPicture = new ImageView(addImg);
            HBox addIcon = new HBox(addPicture);
            addIcon.setOpacity(0.6);
            FlowPane fpImageView = new FlowPane();
            refreshTier(fpImageView, tiers.get(index), addIcon);

            tierGrid.getRowConstraints().add(new RowConstraints(PICTURE_DRAW_SIZE *2 + 6));
            //tierGrid.getRowConstraints().add(new RowConstraints(PICTURE_DRAW_SIZE *((fpImageView.getChildren().size()/6)+1) + 6));
            tierGrid.add(hBoxTierTitle,0, index);

            // Modifies the tiers themselves
            fpImageView.setAlignment(Pos.CENTER_LEFT);
            fpImageView.setStyle("-fx-border-color: white;" + "-fx-border-width: 3;");
            tierGrid.add(fpImageView,1, index);
            tierBoxes.put(fpImageView, addPicture);

            addIcon.setOnDragOver((newEvent) -> {;
                if (newEvent.getDragboard().hasImage()){
                    newEvent.acceptTransferModes(TransferMode.MOVE);
                } else if (newEvent.getDragboard().getContentTypes().contains(Picture.PICTURE_FORMAT)){
                    newEvent.acceptTransferModes(TransferMode.MOVE);
                }
            });

            addIcon.setOnDragDropped((newEvent)  -> {
                Picture newPic = (Picture) newEvent.getDragboard().getContent(Picture.PICTURE_FORMAT);
                tiers.get(index).addPicture(newPic);
                refreshTier(fpImageView, tiers.get(index), addIcon);
                newEvent.setDropCompleted(true);
            });

            //Gets the "Cog" button image path.
            InputStream cogStream = null;
            try {
                cogStream = new FileInputStream("src/main/java/csc436/Images/white-cog.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //ImageView for the options button (Cog button)
            Image cogImg = new Image(cogStream);
            //Creates ImageViews for white-cog.png
            ImageView cogView = new ImageView(cogImg);
            cogView.setFitWidth(50);
            cogView.setFitHeight(50);
            cogView.setPreserveRatio(true);

            // Options Button code section
            Button options = new Button();
            options.setStyle("-fx-font-size: 25pt; -fx-background-radius: 20px; -fx-background-color: black; -fx-text-fill: black;");
            options.setGraphic(cogView);

            options.setOnAction((event) -> {
                int thisTierIndex = index;
                selectedTier = tiers.get(index);
                selectedTierTitle = selectedTier.getTierTitle();
                final Stage newTierListStage = new Stage();
                BorderPane newTierListPane = new BorderPane();
                newTierListStage.setTitle("Selected Tier");
                newTierListStage.initModality(Modality.APPLICATION_MODAL);

                //Creates the Nodes necessary for a new TierList (Text fields and button).
                GridPane newTier = new GridPane();
                GridPane updateTier = new GridPane();
                Label newTierText = new Label(selectedTierTitle + " Tier");
                Label errorMsg = new Label();
                Label newTierTitle = new Label("New Tier:");
                Label updateTierTitle = new Label("Update Tier:");
                TextField newTierTitleField = new TextField();
                TextField updateTierTitleField = new TextField();
                Button addUpBtn = new Button("Add On Top");
                Button addDownBtn = new Button("Add Below");
                Button deleteBtn = new Button("Delete");
                Button updateBtn = new Button ("Update");

                //Sets the style of the labels and button.
                newTierText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 24));
                errorMsg.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));
                addUpBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
                addDownBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
                deleteBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
                updateBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");

                //Creates a VBox with all the Nodes.
                HBox hBoxTierListText = new HBox(newTierText);
                HBox hBoxErrorMsg = new HBox(errorMsg);
                HBox hBoxTier = new HBox(newTier);
                HBox hBoxUpdateTier = new HBox(updateTier);
                VBox vBoxAddBtns = new VBox(addUpBtn, addDownBtn);
                VBox vBoxBtns = new VBox(updateBtn, deleteBtn);
                //HBox hBoxBtns = new HBox(gridPaneBtns);
                VBox vBoxNewTier = new VBox(hBoxTierListText, hBoxErrorMsg, hBoxTier, hBoxUpdateTier);

                //Position of Nodes.
                hBoxTierListText.setAlignment(Pos.CENTER);
                hBoxErrorMsg.setAlignment(Pos.CENTER);
                hBoxTier.setAlignment(Pos.CENTER);
                hBoxUpdateTier.setAlignment(Pos.CENTER);
                vBoxAddBtns.setAlignment(Pos.CENTER);
                vBoxBtns.setAlignment(Pos.CENTER);
                //hBoxBtns.setAlignment(Pos.CENTER);
                vBoxNewTier.setMargin(hBoxErrorMsg, new Insets(5, 0, 0, 0));
                vBoxAddBtns.setMargin(addUpBtn, new Insets(0, 0, 5, 0));
                vBoxAddBtns.setMargin(updateBtn, new Insets(0, 0, 5, 0));
                vBoxNewTier.setMargin(hBoxTier, new Insets(10, 0, 20, 0));
                vBoxNewTier.setMargin(hBoxUpdateTier, new Insets(0, 10, 0, 0));
                //vBoxNewTier.setMargin(hBoxBtns, new Insets(10, 0, 10, 0));

                //Size of textFields.
                newTierTitleField.setPrefWidth(100);
                updateTierTitleField.setPrefWidth(100);
                //Adding H and V gaps to components.
                newTier.setHgap(10);
                newTier.setVgap(10);
                updateTier.setHgap(10);
                updateTier.setVgap(10);

                //Adds all the Labels and Fields for a new TierList to the grid.
                newTier.add(newTierTitle, 0, 0);
                newTier.add(newTierTitleField, 1, 0);
                newTier.add(vBoxAddBtns, 2, 0);
                updateTier.add(updateTierTitle, 0, 0);
                updateTier.add(updateTierTitleField, 1, 0);
                updateTier.add(vBoxBtns, 2, 0);

                //Puts the VBox into a GridPane
                newTierListPane.setCenter(vBoxNewTier);
                vBoxNewTier.setAlignment(Pos.TOP_CENTER);
                newTierListPane.setMargin(vBoxNewTier, new Insets(25, 0, 0, 0));

                //Event handlers to delete the selected TierList.
                deleteBtn.setOnAction((newEvent) -> {
                    //Creates and opens the new TierList.
                    deleteTier(selectedTier, newTierListStage);
                });

                //Event handlers to update the selected TierList.
                updateBtn.setOnAction((event1) -> {
                    updateTier(selectedTier, updateTierTitleField, errorMsg, newTierListStage);
                });

                //Event handlers to open the selected TierList.
                addUpBtn.setOnAction((event2) -> {
                    addTopTier(selectedTier, newTierTitleField, errorMsg, newTierListStage, thisTierIndex);
                });

                //Event handlers to open the selected TierList.
                addDownBtn.setOnAction((event2) -> {
                    addBottonTier(selectedTier, newTierTitleField, errorMsg, newTierListStage, thisTierIndex);
                });

                Scene dialogScene = new Scene(newTierListPane, 300, 300);
                newTierListStage.setScene(dialogScene);
                newTierListStage.show();
            });
            VBox vBoxOptionsBtn = new VBox(options);
            // Modifies the option boxes on the right
            vBoxOptionsBtn.setAlignment(Pos.CENTER);
            vBoxOptionsBtn.setStyle("-fx-border-color: white;" + "-fx-border-width: 3;");
            tierGrid.add(vBoxOptionsBtn,2, index);
        }

        imageUploadUI.drawPicturesAsImages();
    }

    private MenuItem makeColorMenu() {
        MenuItem item = new MenuItem("Tier Colors");
        EventHandler<ActionEvent> colorEvent = actionEvent -> {
            final Stage colorStage = new Stage();
            BorderPane colorPane = new BorderPane();

            Label backgroundLabel = new Label("Background color");
            ColorPicker backgroundCp = new ColorPicker(tierList.getBackgroundColor());
            HBox bgPicker = new HBox(backgroundLabel, backgroundCp);
            bgPicker.setPadding(new Insets(10, 10, 10, 10));
            bgPicker.setSpacing(10);

            Label tierLabel = new Label("Tier color");
            ColorPicker tierCp = new ColorPicker(tierList.getTierRowColor());
            HBox tierPicker = new HBox(tierLabel, tierCp);
            tierPicker.setPadding(new Insets(10, 10, 10, 10));
            tierPicker.setSpacing(10);

            VBox pickers = new VBox(bgPicker, tierPicker);
            pickers.setAlignment(Pos.CENTER);
            colorPane.setCenter(pickers);

            Button save = new Button("Save");
            save.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tierList.setTierRowColor(tierCp.getValue());
                    makeTierListUI();
                    tierList.setBackgroundColor(backgroundCp.getValue());
                    tierGrid.setBackground(new Background(new BackgroundFill(tierList.getBackgroundColor(), null, null)));
                    colorStage.close();
                }
            });

            Button cancel = new Button("Cancel");
            cancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    colorStage.close();
                }
            });

            HBox buttons = new HBox(save, cancel);
            colorPane.setBottom(buttons);

            Scene dialogScene = new Scene(colorPane, 300, 300);
            colorStage.setScene(dialogScene);
            colorStage.show();
        };
        item.setOnAction(colorEvent);
        return item;
    }

    private MenuItem makeExportMenu() {
        MenuItem exportButt = new MenuItem("Save");
        EventHandler<ActionEvent> exportEvent = actionEvent -> {
            // TODO: crop the scene
            // TODO: save the scene
            // TODO: open a file browser for user to choose location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                try {

                    //remove the plus icons
                    Image plusIcon = null;
                    for (FlowPane tierBox : tierBoxes.keySet()) {
                        if (plusIcon == null) {
                            plusIcon = tierBoxes.get(tierBox).getImage();
                        }
                        tierBoxes.get(tierBox).setImage(null);
                    }

                    SnapshotParameters params = new SnapshotParameters();
                    params.setViewport(new Rectangle2D(0, 0, tierGrid.getWidth() - (COLUMN_WIDTH + 6), tierGrid.getHeight() - 6));
                    WritableImage writableImage = tierGrid.snapshot(params, null);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                    System.out.println("Image Saved");

                    //replace the plus icons
                    for (FlowPane tierBox : tierBoxes.keySet()) {
                        tierBoxes.get(tierBox).setImage(plusIcon);
                    }

                } catch (IOException e) {
                }
            }
        };
        exportButt.setOnAction(exportEvent);
        return exportButt;
    }

    private void getImageUI() {
        imageGrid.setAlignment(Pos.BOTTOM_CENTER);
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(PICTURE_DRAW_SIZE);
            imageGrid.getColumnConstraints().add(column);
        }

        imageUploadUI = new ImageUploadUI(tierList, tierGrid, imageGrid, tierList.getPictureArrayList(), new Stage());
        imageUploadUI.drawPicturesAsImages();
    }

    /**
     * Purpose: Deletes the specified TierList from the user's account.
     * @param tier The Tier to be deleted.
     * @param newTierStage The Stage which contained the Delete button.
     */
    private void deleteTier(Tier tier, Stage newTierStage){
        tierList.getTiers().remove(tier);
        newTierStage.close();
        //TierListMaker.changeScenes(getTierListUI());
        makeTierListUI();
    }

    /**
     * Purpose: Updates the title of a TierList with the specified String.
     * @param tier The Tier clicked on the user's Account.
     * @param newTierTitleField The new title of the Tier.
     * @param errorMsg A Label that may display an error message to the user.
     * @param newTierStage The Stage which contained the Update button.
     */
    private void updateTier(Tier tier, TextField newTierTitleField, Label errorMsg, Stage newTierStage) {
        boolean exists = false;
        String newTitle = newTierTitleField.getText();

        if (!newTitle.isEmpty()){
            //Iterates through Tier and checks if the title already exists.
            for (Tier t : tierList.getTiers()) {
                if (t.getTierTitle().equals(newTitle)){
                    exists = true;
                    break;
                }
            }

            //If the title does not already exist, update the title of the Tier.
            if (!exists){
                tier.setTierTitle(newTitle);
                newTierStage.close();
                //showTierLists();
            }else{//If Title exists, display error message.
                errorMsg.setText("The Title Already Exists.");
                errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
                newTierTitleField.setText("");
            }
        }else{
            errorMsg.setText("The Title Can't Be Empty.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
        }
        //TierListMaker.changeScenes(getTierListUI());
        makeTierListUI();
    }

    /**
     * Purpose: Adds a new Tier at the bottom of the clicked Tier.
     * @param selectedTier The tier that was selected.
     * @param newTierTitleField The entered title for the new Tier.
     * @param errorMsg A Label that may display an error message to the user.
     * @param newTierListStage The Stage which contained the Update button.
     * @param index The index of the Tier.
     */
    private void addBottonTier(Tier selectedTier, TextField newTierTitleField, Label errorMsg, Stage newTierListStage, int index) {
        String title = newTierTitleField.getText();
        if (title.isEmpty()) {
            errorMsg.setText("The Title Can't Be Empty.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
            return;
        }

        boolean exists = false;
        //Iterates through Tier and checks if the title already exists.
        for (Tier t : tierList.getTiers()) {
            if (t.getTierTitle().equals(title)){
                exists = true;
                break;
            }
        }

        if (!exists){
            //Creates a new TierList and adds it to the TierList List of the user's account.
            Tier newTier = new Tier(title, index + 1);
            tierList.getTiers().add(index + 1, newTier);
            //Creates a new TierListUI and displayed the TierList.
            newTierListStage.close();
        }else{//If Title exists, display error message.
            errorMsg.setText("The Title Already Exists.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
            return;
        }
        //TierListMaker.changeScenes(getTierListUI());
        makeTierListUI();
    }

    /**
     * Purpose: Adds a new Tier at the top of the clicked Tier.
     * @param selectedTier The tier that was selected.
     * @param newTierTitleField The entered title for the new Tier.
     * @param errorMsg A Label that may display an error message to the user.
     * @param newTierStage The Stage which contained the Update button.
     * @param index The index of the Tier.
     */
    private void addTopTier(Tier selectedTier, TextField newTierTitleField, Label errorMsg, Stage newTierStage, int index) {
        String title = newTierTitleField.getText();
        if (title.isEmpty()) {
            errorMsg.setText("The Title Can't Be Empty.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
            return;
        }

        boolean exists = false;
        //Iterates through Tier and checks if the title already exists.
        for (Tier t : tierList.getTiers()) {
            if (t.getTierTitle().equals(title)){
                exists = true;
                break;
            }
        }

        if (!exists){
            //Creates a new Tier and adds it to the Tier List of the user's account.
            Tier newTierList = new Tier(title, index - 1);
            tierList.getTiers().add(index, newTierList);

            newTierStage.close();
        }else{//If Title exists, display error message.
            errorMsg.setText("The Title Already Exists.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
            return;
        }
        makeTierListUI();
    }
}
