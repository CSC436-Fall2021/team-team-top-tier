package csc436.View;

import csc436.Model.Account;
import csc436.Model.AccountCollection;
import csc436.Model.TierList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Program: AccountUI.java
 * Purpose: AccountUI class that contains a UI for the given user's Account.
 *
 * Created: 10/16/2021
 * @author Victor A. Jimenez Granados and David Dung
 */
public class AccountUI {
    private static final int loginAppWidth = 660;
    private static final int loginAppHeight = 550;
    private Account account;
    private AccountCollection accounts;
    private LoginUI loginUI;
    private VBox tierLists;
    private int maxItemsPerRow;
    private String nameOfTierList;
    private TierList clickedTierList;
    private ArrayList<Button> selectedTagButtons;

    public AccountUI(Account account, AccountCollection accounts, LoginUI login) {
        this.account = account;
        this.accounts = accounts;
        loginUI = login;
    }

    /**
     * Purpose: Creates the Account Scene of our app.
     * @return accountScene, our Scene which displays our UI that displays a user's Account.
     */
    public Scene getAccountWindow() {
        //Number of TierLists allowed to be displayed on a single row.
        maxItemsPerRow = 5;

        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: crimson");

        //Creates the Account Nodes necessary for the AccountUI.
        tierLists = new VBox();
        Label usrnameLabel = new Label("Welcome Back " + account.getUsrname());
        Label tierListsLabel = new Label("My TierLists:");
        Button logoutBtn = new Button("Sign Out");
        logoutBtn.setMaxSize(400, 400);
        // TODO: add EventHandler - on key press for every key
        TextField tagSearchBar = new TextField();
        selectedTagButtons = new ArrayList<Button>();
        Label tagLabel = new Label("Tags:");

        //Setting the font of the labels
        usrnameLabel.setFont(Font.font("Regular", FontWeight.BOLD, FontPosture.REGULAR, 70));
        tierListsLabel.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 40));
        tagLabel.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 25));

        //Setting the tagSearchBar hint text
        tagSearchBar.setPromptText("Enter tag to search");
        tagSearchBar.setFocusTraversable(false);

        //Setting the color of the labels
        usrnameLabel.setStyle("-fx-text-fill: white; -fx-font-family: impact");
        tierListsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
        tagLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        //Setting the font color, background color, and border style of logout button.
        logoutBtn.setStyle("-fx-font-size: 15pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");

        //Adding the components into a vbox.
        HBox hBoxUsername= new HBox(usrnameLabel);
        HBox hBoxTierLists = new HBox(tierLists);
        ScrollPane scroll = new ScrollPane(hBoxTierLists);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: crimson; -fx-border-color: crimson;");
        HBox scrollPane = new HBox(scroll);
        scrollPane.setStyle("-fx-background-color: crimson");
        // TODO: add the tag search bar to hBoxTierListText
        HBox selectedTags = new HBox();
        ScrollPane tagScrollPane = new ScrollPane(selectedTags);
        tagScrollPane.setFitToHeight(true);
        tagScrollPane.setMaxWidth(300);
        tagScrollPane.setStyle("-fx-background: crimson; -fx-border-color: crimson;");
        HBox tagBox = new HBox(tagSearchBar, tagLabel, tagScrollPane);
        HBox hBoxTierListText = new HBox(tierListsLabel, tagBox);
        HBox hBoxLogOutBtn = new HBox(logoutBtn);
        VBox vBox = new VBox(hBoxUsername, hBoxTierListText, scrollPane, hBoxLogOutBtn);

        //Position of the Account components.
        hBoxUsername.setAlignment(Pos.CENTER);
        hBoxTierLists.setAlignment(Pos.CENTER);
        scrollPane.setAlignment(Pos.CENTER);
        hBoxTierListText.setAlignment(Pos.CENTER_LEFT);
        hBoxLogOutBtn.setAlignment(Pos.CENTER);
        tagBox.setAlignment(Pos.CENTER_RIGHT);
        tagBox.setMargin(tagSearchBar, new Insets(25, 0, 25, 150));
        tagBox.setMargin(tagLabel, new Insets(25, 0, 25, 25));
        tagBox.setMargin(selectedTags, new Insets(25, 0, 25, 25));
        tagBox.setMargin(tagScrollPane, new Insets(25, 25, 25, 25));
        hBoxTierListText.setMargin(tagBox, new Insets(25, 0, 25, 0));
        vBox.setMargin(hBoxTierListText, new Insets(25, 0, 25, 20));
        vBox.setMargin(hBoxLogOutBtn, new Insets(25, 0, 25, 0));

        /*//Adding H and V gaps to components.
        tierLists.setHgap(150);
        tierLists.setVgap(25);*/

        //Adds the Account Nodes to the GridPane.
        pane.setCenter(vBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        pane.setMargin(vBox, new Insets(50, 0, 0, 0));
        Scene accountScene = new Scene(pane, loginAppWidth, loginAppHeight);

        //Adds an event listener to logout button. Clicking on the logout button takes user to LogInUI.
        logoutBtn.setOnAction((event) -> {
            try {
                accounts.saveAccountCollection();
            }catch(Exception e) {
                System.out.println("ERROR: UNABLE TO SAVE TIERLIST ACCOUNTS.");
            }
            TierListMaker.changeScenes(loginUI.getLogInWindow());
        });

        //Adds an event listener to tagSearchBar. When the use presses enter, add a text button to the selectedTagButtons.
        tagSearchBar.setOnKeyPressed((tagEnterPressEvent) -> {
            if (tagEnterPressEvent.getCode() == KeyCode.ENTER) {
                Button tempTagButton = new Button(clickedTierList.formatTag(tagSearchBar.getText()));
                tempTagButton.setStyle("-fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
                tempTagButton.setOnMouseClicked((event2) -> {
                    selectedTags.getChildren().remove(tempTagButton);
                    selectedTagButtons.remove(tempTagButton);
                    showTierLists();
                });
                selectedTagButtons.add(tempTagButton);
                tagSearchBar.setText("");
                updateSelectedTags(selectedTags);
                showTierLists();
            }
        });

        //Displays the TierLists of the logged-in user.
        showTierLists();
        //accountScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        return accountScene;
    }

    /**
     * Add the buttons to the HBox selectedTags
     *
     * @param selectedTags - the HBox to hold the selected tags
     *
     * Note: uses global variable selectedTagButtons - the list of button tags
     */
    private void updateSelectedTags(HBox selectedTags) {
        selectedTags.getChildren().clear();
        for (Button butt: selectedTagButtons) {
            selectedTags.getChildren().add(butt);
        }
    }

    /**
     * Draws the GUI that displays the user's TierList's.
     */
    public void showTierLists() {
        tierLists.getChildren().clear();

        //Current row/col of the GridPane
        int rowIndex = 0;
        int colIndex = 0;

        GridPane tempRow = new GridPane();
        tempRow.setMaxHeight(1);
        tempRow.setMaxWidth(5);
        tempRow.setHgap(150);
        tempRow.setVgap(25);

        //Row size constraints of the cells in the GridPane.
        RowConstraints row = new RowConstraints();
        row.setMinHeight(200);
        row.setPrefHeight(200);
        row.setVgrow(Priority.SOMETIMES);
        tempRow.getRowConstraints().add(row);

        //Keep track of the number of tiers that are displayed
        int numOfItems = 0;

        //Iterates through all the TierList objects in the User's TierList List.
        for (int i = 0; i < account.getTierLists().size(); i++) {
            // found is true if the TierList contains all the tags being searched for
            Boolean found = true;
            //check all the tags to see if the tier list has all the tags
            for (Button tagButt: selectedTagButtons) {
                if (!account.getTierLists().get(i).getTagList().contains(tagButt.getText())) {
                        found = false;
                        break;
                    }
            }

            // if the TierList has all the tags, add it to the pane
            if (found) {
                //If the column is greater than the num of items allowed, creates a new row.
                if (colIndex >= maxItemsPerRow){
                    tierLists.getChildren().add(tempRow);
                    tierLists.setMargin(tempRow, new Insets(0, 0, 25, 0));
                    tempRow = new GridPane();
                    tempRow.setMaxHeight(1);
                    tempRow.setMaxWidth(5);
                    tempRow.setHgap(150);
                    tempRow.getRowConstraints().add(row);
                    rowIndex = 0;
                    colIndex = 0;
                }

                int x = rowIndex, y = colIndex;
                //StackPane that holds the button (temp)
                StackPane pane = new StackPane();
                pane.setStyle("-fx-border-color: black; -fx-border-radius: 20px; border-width: thick");
                pane.setPadding(new Insets(0, 0, 0, 0));
                pane.setMaxSize(200, 200);

                //Column size constraints of the cells in the GridPane. Creates one for each item added.
                ColumnConstraints col = new ColumnConstraints();
                col.setMinWidth(200);
                col.setPrefWidth(200);
                col.setHgrow(Priority.SOMETIMES);
                tempRow.getColumnConstraints().add(col);

                //Temporary solution. Possibly add a SnapShot of the actual TierList.
                Button TierButton = new Button();
                clickedTierList = account.getTierLists().get(i);
                nameOfTierList = clickedTierList.getTierListTitle();
                TierButton.setText(nameOfTierList);
                TierButton.setStyle("-fx-font-size: 25pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
                TierButton.setMaxSize(200, 200);

                setButtonsEventHandler(TierButton, i);

                //Add the Tier Button to a StackPane
                pane.getChildren().addAll(TierButton);

                //Add the Tier Button to the GridPane
                tempRow.add(pane, y, x);
                colIndex++;
            }
        }
        //Checks to see if a new row needs to be created with the row constraints.
        if (colIndex % maxItemsPerRow == 0 && numOfItems != 0) {
            tierLists.getChildren().add(tempRow);
            tierLists.setMargin(tempRow, new Insets(0, 0, 25, 0));
            tempRow = new GridPane();
            tempRow.setMaxHeight(1);
            tempRow.setMaxWidth(5);
            tempRow.setHgap(150);
            tempRow.getRowConstraints().add(row);
            rowIndex = 0;
            colIndex = 0;
        }
        createAddTierButton(rowIndex, colIndex, tempRow);
    }

    private void createAddTierButton(int rowIndex, int colIndex, GridPane tempRow) {
        //Creates an extra "Add" button to create a new TierList.
        try {
            //StackPane that will hold the "Add" button.
            StackPane pane = new StackPane();
            pane.setStyle("-fx-border-color: black; -fx-border-radius: 20px; border-width: thick");
            pane.setPadding(new Insets(0, 0, 0, 0));
            pane.setMaxSize(200, 200);

            //Same Column size constraints.
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(200);
            col.setPrefWidth(200);
            col.setHgrow(Priority.SOMETIMES);
            tempRow.getColumnConstraints().add(col);

            //Gets the "Add" button image path.
            InputStream addStream = new FileInputStream("src/main/java/csc436/Images/addImage.png");
            Image addImg = new Image(addStream);
            //Creates ImageViews for addImage.png
            ImageView addView = new ImageView(addImg);
            addView.setFitWidth(150);
            addView.setFitHeight(150);
            addView.setPreserveRatio(true);

            //Button with the addImage.
            Button button = new Button();
            button.setStyle("-fx-font-size: 25pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
            button.setMaxSize(200, 200);
            button.setGraphic(addView);

            //Creates an event handler for the "Add" button. When clicked, a new stage is opened to create a new TierList.
            setCreateButtonEventHandler(button);

            pane.getChildren().addAll(button);
            tempRow.add(pane, colIndex, rowIndex);
            tierLists.getChildren().add(tempRow);
            tierLists.setMargin(tempRow, new Insets(0, 0, 25, 0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the event handler for the "Add" TierList button
     * @param button - the "Add" TierList button
     */
    private void setCreateButtonEventHandler(Button button) {
        button.setOnAction((event) -> {
            final Stage newTierListStage = new Stage();
            BorderPane newTierListPane = new BorderPane();
            newTierListStage.setTitle("Create New TierList");
            newTierListStage.initModality(Modality.APPLICATION_MODAL);

            //Creates the Nodes necessary for a new TierList (Text fields and button).
            GridPane newTierList = new GridPane();
            Label newTierListText = new Label("Create New TierList");
            Label errorMsg = new Label();
            Label newTierListTitle = new Label("TierList Title:");
            TextField newTierListTitleField = new TextField();
            Button createBtn = new Button("Create");

            //Sets the style of the labels and button.
            newTierListText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 24));
            errorMsg.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));
            createBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");

            //Creates a VBox with all the Nodes.
            HBox hBoxTierListText = new HBox(newTierListText);
            HBox hBoxErrorMsg = new HBox(errorMsg);
            HBox hBoxTierList = new HBox(newTierList);
            HBox hBoxCreateBtn = new HBox(createBtn);
            VBox vBoxNewTierList = new VBox(hBoxTierListText, hBoxErrorMsg, hBoxTierList, hBoxCreateBtn);

            //Position of Nodes.
            hBoxTierListText.setAlignment(Pos.CENTER);
            hBoxErrorMsg.setAlignment(Pos.CENTER);
            hBoxTierList.setAlignment(Pos.CENTER);
            hBoxCreateBtn.setAlignment(Pos.CENTER);
            vBoxNewTierList.setMargin(hBoxTierList, new Insets(10, 0, 10, 0));
            vBoxNewTierList.setMargin(hBoxErrorMsg, new Insets(5, 0, 0, 0));
            vBoxNewTierList.setMargin(hBoxCreateBtn, new Insets(10, 0, 10, 155));

            //Size of textFields.
            newTierListTitleField.setPrefWidth(100);
            //Adding H and V gaps to components.
            newTierList.setHgap(10);
            newTierList.setVgap(10);

            //Adds all the Labels and Fields for a new TierList to the grid.
            newTierList.add(newTierListTitle, 0, 0);
            newTierList.add(newTierListTitleField, 1, 0);


            //Puts the VBox into a GridPane
            newTierListPane.setCenter(vBoxNewTierList);
            vBoxNewTierList.setAlignment(Pos.TOP_CENTER);
            newTierListPane.setMargin(vBoxNewTierList, new Insets(25, 0, 0, 0));

            //Event handlers to create a new TierList.
            createBtn.setOnAction((newEvent) -> {
                //Creates and opens the new TierList.
                createTierList(newTierListTitleField, errorMsg, newTierListStage);
            });

            Scene dialogScene = new Scene(newTierListPane, 300, 200);
            newTierListStage.setScene(dialogScene);
            newTierListStage.show();
        });
    }

    /**
     * Set the event handler for the delete button, update button, open button, and tag textfield
     *
     * @param button - a TierList button
     * @param index - the index connected to the TierList button
     */
    private void setButtonsEventHandler(Button button, int index) {
        button.setOnAction((event) -> {
            clickedTierList = account.getTierLists().get(index);
            nameOfTierList = clickedTierList.getTierListTitle();
            final Stage newTierListStage = new Stage();
            BorderPane newTierListPane = new BorderPane();
            newTierListStage.setTitle("Selected TierList");
            newTierListStage.initModality(Modality.APPLICATION_MODAL);

            //Creates the Nodes necessary for a new TierList (Text fields and button).
            GridPane newTierList = new GridPane();
            Label newTierListText = new Label(nameOfTierList + " TierList");
            Label errorMsg = new Label();
            Label newTierListTitle = new Label("Update Title:");
            TextField newTierListTitleField = new TextField();
            Button openBtn = new Button("Open");
            Button deleteBtn = new Button("Delete");
            Button updateBtn = new Button ("Update");
            Text tagText = new Text();
            updateTags(clickedTierList, tagText);
            ScrollPane tagScrollPane = new ScrollPane(tagText);
            TextField tagTextField = new TextField();

            //Sets the style of the labels and button.
            newTierListText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 24));
            errorMsg.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));
            openBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
            deleteBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
            updateBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
            tagScrollPane.setStyle("-fx-font-size: 10pt; -fx-background-color:transparent;");
            tagScrollPane.setPrefViewportWidth(200);

            //Creates a VBox with all the Nodes.
            HBox hBoxTierListText = new HBox(newTierListText);
            HBox hBoxErrorMsg = new HBox(errorMsg);
            HBox hBoxTierList = new HBox(newTierList);
            GridPane gridPaneBtns = new GridPane();
            gridPaneBtns.setHgap(25);
            HBox hBoxBtns = new HBox(gridPaneBtns);
            HBox hBoxTag = new HBox(tagScrollPane, tagTextField);
            VBox vBoxNewTierList = new VBox(hBoxTierListText, hBoxErrorMsg, hBoxTierList, hBoxBtns, hBoxTag);
//                VBox vBoxNewTierList = new VBox(hBoxTierListText, hBoxErrorMsg, hBoxTierList, hBoxBtns);

            //Position of Nodes.
            hBoxTierListText.setAlignment(Pos.CENTER);
            hBoxErrorMsg.setAlignment(Pos.CENTER);
            hBoxTierList.setAlignment(Pos.CENTER);
            hBoxBtns.setAlignment(Pos.CENTER);
            vBoxNewTierList.setMargin(hBoxErrorMsg, new Insets(5, 0, 0, 0));
            vBoxNewTierList.setMargin(hBoxTierList, new Insets(10, 0, 10, 0));
            vBoxNewTierList.setMargin(hBoxBtns, new Insets(10, 0, 10, 0));
            vBoxNewTierList.setMargin(hBoxTag, new Insets(10, 0, 10, 0));

            //Set the margins for tags
            hBoxTag.setMargin(tagTextField, new Insets(10, 10, 10, 10));
            hBoxTag.setMargin(tagScrollPane, new Insets(15, 10, 10, 10));

            //Size of textFields.
            newTierListTitleField.setPrefWidth(100);
            tagTextField.setPrefWidth(100);
            //Adding H and V gaps to components.
            newTierList.setHgap(10);
            newTierList.setVgap(10);

            //Adds all the Labels and Fields for a new TierList to the grid.
            newTierList.add(newTierListTitle, 0, 0);
            newTierList.add(newTierListTitleField, 1, 0);

            gridPaneBtns.add(deleteBtn, 0, 0);
            gridPaneBtns.add(updateBtn, 1, 0);
            gridPaneBtns.add(openBtn, 2, 0);

            //Puts the VBox into a GridPane
            newTierListPane.setCenter(vBoxNewTierList);
            vBoxNewTierList.setAlignment(Pos.TOP_CENTER);
            newTierListPane.setMargin(vBoxNewTierList, new Insets(25, 0, 0, 0));

            //Event handlers to delete the selected TierList.
            deleteBtn.setOnAction((newEvent) -> {
                //Creates and opens the new TierList.
                deleteTierList(clickedTierList, newTierListStage);
            });

            //Event handlers to update the selected TierList.
            updateBtn.setOnAction((event1) -> {
                updateTierList(clickedTierList, newTierListTitleField, errorMsg, newTierListStage);
            });

            //Event handlers to open the selected TierList.
            openBtn.setOnAction((event2) -> {
                openTierList(clickedTierList, newTierListStage);
            });

            //Event handlers to add tags to the selected TierList.
            tagTextField.setOnKeyPressed((event3) -> {
                if (event3.getCode() == KeyCode.ENTER) {
                    addTierListTag(clickedTierList, clickedTierList.formatTag(tagTextField.getText()), tagText);
                    tagTextField.setText("");
                }
            });

            Scene dialogScene = new Scene(newTierListPane, 300, 300);
            newTierListStage.setScene(dialogScene);
            newTierListStage.show();

        });
    }

    /**
     * Purpose: Creates a new TierList given the info inputted.
     * @param tierListTitle The Title of the TierList.
     * @param errorMsg A Label that may display an error message to the user.
     * @param tierListStage The Stage which contained the Create button.
     */
    private void createTierList(TextField tierListTitle, Label errorMsg, Stage tierListStage) {
        String title = tierListTitle.getText();
        boolean exists = false;

        //Iterates through TierList and checks if the title already exists.
        for (TierList t : account.getTierLists()) {
            if (t.getTierListTitle().equals(title)){
                exists = true;
                break;
            }
        }

        if (!exists){
            //Creates a new TierList and adds it to the TierList List of the user's account.
            TierList newTierList = new TierList(title);
            account.getTierLists().add(newTierList);
            //Creates a new TierListUI and displayed the TierList.
            TierListUI tierListUI = new TierListUI(newTierList);
            TierListMaker.changeScenes(tierListUI.getTierListUI());
            tierListStage.close();
        }else{//If Title exists, display error message.
            errorMsg.setText("The Title Already Exists.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            tierListTitle.setText("");
        }
    }

    /**
     * Purpose: Deletes the specified TierList from the user's account.
     * @param tierList The TierList to be deleted.
     * @param newTierListStage The Stage which contained the Delete button.
     */
    private void deleteTierList(TierList tierList, Stage newTierListStage){
        account.getTierLists().remove(tierList);

        //Deletes the serializable list of images for that TierList.
        File tier = null;
        tier = new File(tierList.getTierListTitle() + ".ser");
        if (tier != null){
            tier.delete();
        }
        newTierListStage.close();
        showTierLists();
    }

    /**
     * Purpose: Updates the title of a TierList with the specified String.
     * @param tierList The TierList clicked on the user's Account.
     * @param newTierListTitleField The new title of the TierList.
     * @param errorMsg A Label that may display an error message to the user.
     * @param newTierListStage The Stage which contained the Update button.
     */
    private void updateTierList(TierList tierList, TextField newTierListTitleField, Label errorMsg, Stage newTierListStage) {
        boolean exists = false;
        String newTitle = newTierListTitleField.getText();

        if (!newTitle.isEmpty()) {
            //Iterates through TierList and checks if the title already exists.
            for (TierList t : account.getTierLists()) {
                if (t.getTierListTitle().equals(newTitle)){
                    exists = true;
                    break;
                }
            }

            //If the title does not already exist, update the title of the TierList.
            if (!exists){

                File tier = null;
                tier = new File(tierList.getTierListTitle() + ".ser");
                if (tier != null){
                    tier.renameTo(new File(newTitle + ".ser"));
                }
                tierList.setTierListTitle(newTitle);
                newTierListStage.close();
                showTierLists();
            }else{//If Title exists, display error message.
                errorMsg.setText("The Title Already Exists.");
                errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
                newTierListTitleField.setText("");
            }
        }
        else{//If Title is empty, display error message.
            errorMsg.setText("The Title Can't Be Empty.");
            errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newTierListTitleField.setText("");
        }
    }

    /**
     * Purpose: Opens the TierListUI of the specified TierList.
     * @param tierList The TierList clicked on the user's Account.
     * @param newTierListStage The Stage which contained the Open button.
     */
    private void openTierList(TierList tierList, Stage newTierListStage) {
        //Creates a new TierListUI and displayed the TierList.
        TierListUI tierListUI = new TierListUI(tierList);
        newTierListStage.close();
        TierListMaker.changeScenes(tierListUI.getTierListUI());
    }

    private void addTierListTag(TierList tierList, String tag, Text tagText) {
        tierList.addTag(tag);
        updateTags(tierList, tagText);
    }
    private void updateTags(TierList tierList, Text tagText) {
        String tagContent = "";
        List<String> tagList =  tierList.getTagList();
        for (String tag: tagList) {
            tagContent += "#" + tag + "  ";
        }
        tagText.setText(tagContent);
    }
}
