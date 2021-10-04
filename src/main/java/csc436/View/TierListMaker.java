package csc436.View;

import csc436.Model.Account;
import csc436.Model.AccountCollection;
import csc436.Model.TierList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TierListMaker extends Application {
    private static Stage appStage;
    private AccountCollection accounts;
    private LoginUI loginUI;
    private TierListUI tierListUI;
    public static final int appWidth = 660;
    public static final int appHeight = 550;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {

        //Tries to load an AccountCollection. If there isn't an AccountCollection present, creates a new AccountCollection.
        accounts = new AccountCollection();
        try {
            if((AccountCollection) accounts.loadAccountCollection() != null) {
                accounts = (AccountCollection) accounts.loadAccountCollection();
            }
        }catch(Exception e) {
        }
        System.out.println("Number of accounts: " + accounts.getAccCollection().size());
        //Creates a LogInUI object.
        loginUI = new LoginUI(accounts);
        tierListUI= new TierListUI();

        appStage = stage;
        appStage.setTitle("TierList Maker");

        //Initially sets scene to display LogIn Window.
       // appStage.setScene(loginUI.getLogInWindow());

        // TESTING THE TIER LIST UI
        appStage.setScene(tierListUI.getTierListUI());




        appStage.show();

        appStage.setOnCloseRequest((event) -> {
            try {
                accounts.saveAccountCollection();
            }catch(Exception e) {
            }
        });
    }

    /**
     * Purpose: Changes the current scene.
     * @param scene The new Scene to be displayed.
     */
    public static void changeScenes(Scene scene){
        appStage.setScene(scene);
    }

}
