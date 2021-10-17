package csc436.View;

import csc436.Model.Tier;
import csc436.Model.TierList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

/**
 * Program: TierListUI.java
 * Purpose: TierListUI class that contains a method that constructs and
 *          can return a Scene UI element that displays the properties of a TierList obj.
 *          This is the core user interface for the Tier Maker application.
 *
 * Created: 09/29/2021
 * @author AMir Ameri
 */
public class TierListUI {
    private TierList tierList;
    private float windowWidth;
    private int  windowHeight;
    private String selectedTierTitle;
    private Tier selectedTier;
    private int indexOfSelectedTier;

    public TierListUI(TierList tierList){
        this.tierList= tierList;
        windowWidth= 1080;
        windowHeight= 720;
    }

    // Create and return the Tier List's user interface
    public Scene  getTierListUI(){
        List<Tier> tiers= tierList.getTiers();
        BorderPane pane= new BorderPane();
        GridPane tierGrid= new GridPane();
        GridPane imageGrid= new GridPane();
        VBox gridBox= new VBox(tierGrid,imageGrid);

        Label title= new Label(tierList.getTierListTitle());
        HBox titleBox= new HBox(title);

        // Following arrays are usd to track the nodes which comprise the tier list itself
        LinkedList<HBox> nameBoxes= new LinkedList<HBox>();
        LinkedList<HBox> tierBoxes= new LinkedList<HBox>();
        LinkedList<VBox> buttonBoxes= new LinkedList<VBox>();

        // The loop adds nodes to the tier list GridPane and stylizes them as well
        for (int i=0;i<tiers.size();i++) {
            indexOfSelectedTier = i;
            selectedTier = tiers.get(indexOfSelectedTier);
            selectedTierTitle = selectedTier.getTierTitle();
            nameBoxes.add(new HBox(new Label(selectedTierTitle)));
            tierGrid.add(nameBoxes.get(i),0,i);

            tierBoxes.add(new HBox());
            tierGrid.add(tierBoxes.get(i),1,i);

            Button options = new Button("Options");
            //HBox button = new HBox(options);
            //button.setAlignment(Pos.CENTER);
            //button.setStyle("-fx-border-color: white;" + "-fx-border-width: 3;");

            options.setOnAction((event) -> {
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
                    addTopTier(selectedTier, newTierTitleField, errorMsg, newTierListStage, indexOfSelectedTier);
                });

                //Event handlers to open the selected TierList.
                addDownBtn.setOnAction((event2) -> {
                    addBottonTier(selectedTier, newTierTitleField, errorMsg, newTierListStage, indexOfSelectedTier);
                });

                Scene dialogScene = new Scene(newTierListPane, 300, 300);
                newTierListStage.setScene(dialogScene);
                newTierListStage.show();
            });
            buttonBoxes.add(new VBox(options));
            //buttonBoxes.add(new VBox(new Button("Add Tier"),new Button("Delete Tier")));
            tierGrid.add(buttonBoxes.get(i),2, i);
        }

        // TO DO: Modify this to be user-selected colors
        int redLevel= 255;
        for (int i=0;i<nameBoxes.size();i++){
            // Modifies tier name boxes
            nameBoxes.get(i).setAlignment(Pos.CENTER);
            ((Label) nameBoxes.get(i).getChildren().get(0)).setStyle(
                    "-fx-text-fill: white;"+"-fx-font-size: 50px;"+"-fx-font-weight: bold;");
            nameBoxes.get(i).setStyle("-fx-background-color: rgb("+redLevel+",0,0);" +
                    "-fx-border-color: white;" + "-fx-border-width: 3;");
            redLevel-=40;

            // Modifies the tiers themselves
            tierBoxes.get(i).setAlignment(Pos.CENTER_LEFT);
            tierBoxes.get(i).setPadding(new Insets(35,0,35,0));
            tierBoxes.get(i).setStyle("-fx-border-color: white;" + "-fx-border-width: 3;");

            // Modifies the option boxes on the right
            buttonBoxes.get(i).setAlignment(Pos.CENTER);
            buttonBoxes.get(i).setStyle("-fx-border-color: white;" + "-fx-border-width: 3;");
        }

        for (int i=0; i<3;i++){
            for (int j=0; j<10;j++){
                imageGrid.add(new HBox(new Label("Image")),j,i);
            }
        }

        // TOD DO: CHANGE CONSTRAINTS TO USER'S MONITOR DIMENSIONS
        tierGrid.getColumnConstraints().add(new ColumnConstraints(200));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(680));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(200));

        imageGrid.setHgap(75);
        imageGrid.setVgap(75);

        pane.setTop(titleBox);
        pane.setCenter(gridBox);

        // set the Export button pane
        ExportUI exportButt = new ExportUI(tierGrid);
        pane.setBottom(exportButt.getExportUI());

        // Position title and grids
        pane.setMargin(titleBox, new Insets(10,0,40,0));
        gridBox.setMargin(tierGrid, new Insets(00,0,40,0));

        titleBox.setAlignment(Pos.TOP_CENTER);
        tierGrid.setAlignment(Pos.TOP_CENTER);
        imageGrid.setAlignment(Pos.BOTTOM_CENTER);

        pane.setStyle("-fx-background-color: black");

        title.setStyle("-fx-text-fill: white; -fx-font-family: impact");
        title.setFont(Font.font("Regular", FontWeight.BOLD, FontPosture.REGULAR, 70));

        Scene scene= new Scene(pane, windowWidth, windowHeight);

        return scene;
    }

    /**
     * Purpose: Deletes the specified TierList from the user's account.
     * @param tier The Tier to be deleted.
     * @param newTierStage The Stage which contained the Delete button.
     */
    private void deleteTier(Tier tier, Stage newTierStage){
        tierList.getTiers().remove(tier);
        newTierStage.close();
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
            tierList.getTiers().add(index, newTier);
            //Creates a new TierListUI and displayed the TierList.
            newTierListStage.close();
        }else{//If Title exists, display error message.
            errorMsg.setText("The Title Already Exists.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierTitleField.setText("");
        }
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
        }
    }
}
