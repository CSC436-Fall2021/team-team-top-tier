package csc436.View;

import csc436.Model.Account;
import csc436.Model.AccountCollection;
import csc436.Model.TierList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * Program: AccountUI.java
 * Purpose: AccountUI class that contains a UI for the given user's Account.
 *
 * Created: 10/16/2021
 * @author Victor A. Jimenez Granados
 */
public class AccountUI {
    private static final int loginAppWidth = 660;
    private static final int loginAppHeight = 550;
    private Account account;
    private AccountCollection accounts;
    private LoginUI loginUI;
    private VBox tierLists;
    private int maxItemsPerRow;

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

        //Setting the font of the labels
        usrnameLabel.setFont(Font.font("Regular", FontWeight.BOLD, FontPosture.REGULAR, 70));
        tierListsLabel.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 40));

        //Setting the color of the labels
        usrnameLabel.setStyle("-fx-text-fill: white; -fx-font-family: impact");
        tierListsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        //Setting the font color, background color, and border style of logout button.
        logoutBtn.setStyle("-fx-font-size: 15pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");

        //Adding the components into a vbox.
        HBox hBoxUsername= new HBox(usrnameLabel);
        HBox hBoxTierLists = new HBox(tierLists);
        HBox hBoxTierListText = new HBox(tierListsLabel);
        HBox hBoxLogOutBtn = new HBox(logoutBtn);
        VBox vBox = new VBox(hBoxUsername, hBoxTierListText, hBoxTierLists, hBoxLogOutBtn);

        //Position of the Account components.
        hBoxUsername.setAlignment(Pos.CENTER);
        hBoxTierLists.setAlignment(Pos.CENTER);
        hBoxTierListText.setAlignment(Pos.CENTER_LEFT);
        hBoxLogOutBtn.setAlignment(Pos.CENTER);
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

        //Displays the TierLists of the logged-in user.
        showTierLists();
        return accountScene;
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

        //Iterates through all the TierList objects in the User's TierList List.
        for (int i = 0; i < account.getTierLists().size(); i++) {
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
            Button button = new Button();
            button.setText(account.getTierLists().get(i).getTierListTitle());
            button.setStyle("-fx-font-size: 25pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
            button.setMaxSize(200, 200);

            pane.getChildren().addAll(button);
            tempRow.add(pane, y, x);
            colIndex++;
        }
        int numOfRows = tierLists.getChildren().size();
        //Checks to see if a new row needs to be created with the row constraints.
        if (colIndex % maxItemsPerRow == 0) {
            tierLists.getChildren().add(tempRow);
            tierLists.setMargin(tempRow, new Insets(0, 0, 25, 0));
            tempRow = new GridPane();
            tempRow.setMaxHeight(1);
            tempRow.setMaxWidth(5);
            tempRow.setHgap(150);
            tempRow.getRowConstraints().add(row);
            rowIndex = 0;
            colIndex = 0;
        }//Creates an extra "Add" button to create a new TierList.
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
            button.setOnAction((event) -> {
                final Stage newTierListStage = new Stage();
                BorderPane newTierListPane = new BorderPane();
                newTierListStage.setTitle("Create New TierList");
                newTierListStage.initModality(Modality.APPLICATION_MODAL);

                //Creates the Nodes necessary for a new TierList (Text fields and button).
                GridPane newTierList = new GridPane();
                Label newTierListText = new Label("Create New TierList");
                Label newTierListTitle = new Label("TierList Title:");
                TextField newTierListTitleField = new TextField();
                Button createBtn = new Button("Create");

                //Sets the style of the labels and button.
                newTierListText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));
                createBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");

                //Creates a VBox with all the Nodes.
                HBox hBoxTierListText = new HBox(newTierListText);
                HBox hBoxTierList = new HBox(newTierList);
                HBox hBoxCreateBtn = new HBox(createBtn);
                VBox vBoxNewTierList = new VBox(hBoxTierListText, hBoxTierList, hBoxCreateBtn);

                //Position of Nodes.
                hBoxTierListText.setAlignment(Pos.CENTER);
                hBoxTierList.setAlignment(Pos.CENTER);
                hBoxCreateBtn.setAlignment(Pos.CENTER);
                vBoxNewTierList.setMargin(hBoxTierList, new Insets(10, 0, 10, 0));
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
                    createTierList(newTierListTitleField, newTierListStage);
                });

                newTierListTitleField.setOnKeyPressed((e) -> {
                    if (e.getCode() == KeyCode.ENTER){
                        createTierList(newTierListTitleField, newTierListStage);
                    }
                });


                Scene dialogScene = new Scene(newTierListPane, 300, 200);
                newTierListStage.setScene(dialogScene);
                newTierListStage.show();
            });

            pane.getChildren().addAll(button);
            tempRow.add(pane, colIndex, rowIndex);
            tierLists.getChildren().add(tempRow);
            tierLists.setMargin(tempRow, new Insets(0, 0, 25, 0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose: Creates a new TierList given the info inputted.
     * @param tierListTitle The Title of the TierList.
     * @param tierListStage The Stage which contained the Create button.
     */
    private void createTierList(TextField tierListTitle, Stage tierListStage) {
        String title = tierListTitle.getText();

        //Creates a new TierList and adds it to the TierList List of the user's account.
        TierList newTierList = new TierList(title);
        account.getTierLists().add(newTierList);

        //Creates a new TierListUI and displayed the TierList.
        TierListUI tierListUI = new TierListUI();
        tierListStage.close();
        TierListMaker.changeScenes(tierListUI.getTierListUI());
    }

}
