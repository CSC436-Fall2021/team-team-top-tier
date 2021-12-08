package csc436.View;

import csc436.Model.Account;
import csc436.Model.AccountCollection;
import csc436.Model.TierList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TierListMaker extends Application {
    private static Stage appStage;
    private AccountCollection accounts;
    private LoginUI loginUI;
    public static final int appWidth = 660;
    public static final int appHeight = 550;
    private static Screen screen;
    private static Rectangle2D bounds;

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
        //Creates a LogInUI object.
        loginUI = new LoginUI(accounts);

        appStage = stage;
        screen = Screen.getPrimary();
        bounds = screen.getVisualBounds();

        appStage.setX(bounds.getMinX());
        appStage.setY(bounds.getMinY());
        appStage.setWidth(bounds.getWidth());
        appStage.setHeight(bounds.getHeight());
        appStage.setTitle("TierList Maker");

        //Initially sets scene to display LogIn Window.
        appStage.setScene(loginUI.getLogInWindow());
        appStage.show();

        appStage.setOnCloseRequest((event) -> {
            try {
                accounts.saveAccountCollection();
            }catch(Exception e) {
                System.out.println("ERROR: UNABLE TO SAVE TIERLIST ACCOUNTS.");
            }
        });
    }

    /**
     * Purpose: Changes the current scene.
     * @param scene The new Scene to be displayed.
     */
    public static void changeScenes(Scene scene) {
        appStage.setScene(scene);
    }

}
