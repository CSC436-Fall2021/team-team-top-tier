package csc436.View;

import csc436.Model.Account;
import csc436.Model.AccountCollection;
import csc436.Model.Tier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Program: LoginUI.java
 * Purpose: LoginUI class that contains a UI for a login page.
 *
 * Created: 10/02/2021
 * @author Victor A. Jimenez Granados
 */
public class LoginUI {
    private static final int loginAppWidth = 660;
    private static final int loginAppHeight = 550;
    private AccountCollection accounts;

    public LoginUI(AccountCollection accounts) {
        this.accounts = accounts;
    };
    /**
     * Purpose: Creates the logIn Scene of our app.
     * @return logInWindow, our Scene which displays our UI that allows users to log in.
     */
    public Scene getLogInWindow() {

        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: crimson");

        //Creates the login Nodes necessary for the LogInUI.
        GridPane login = new GridPane();
        Label tierList = new Label("Tier List Maker");
        Label loginText = new Label("Enter your username and password");
        Label usrnameLabel = new Label("Username:");
        Label pwdLabel = new Label("Password:");
        TextField usrnameField = new TextField();
        PasswordField pwdField = new PasswordField();
        Button loginBtn = new Button("Log In");
        Button newAccountBtn = new Button("Create Account");

        //Setting the fond of the labels
        tierList.setFont(Font.font("Regular", FontWeight.BOLD, FontPosture.REGULAR, 70));
        loginText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        //Setting the color of the labels
        tierList.setStyle("-fx-text-fill: white; -fx-font-family: impact");
        loginText.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
        usrnameLabel.setStyle("-fx-text-fill: white; ");
        pwdLabel.setStyle("-fx-text-fill: white; ");

        //Setting the font color, background color, and border style of login button.
        newAccountBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");
        loginBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px; -fx-background-color: white; -fx-text-fill: black;");

        //Adding the components into a vbox.
        HBox hBoxTierList= new HBox(tierList);
        HBox hBoxLoginText = new HBox(loginText);
        HBox hBoxLogin = new HBox(login);
        GridPane gridButtons = new GridPane();
        gridButtons.setHgap(60);
        gridButtons.add(newAccountBtn, 0, 0);
        gridButtons.add(loginBtn, 1, 0);
        VBox vBox = new VBox(hBoxTierList, hBoxLoginText, hBoxLogin, gridButtons);

        //Position of the Login components.
        hBoxTierList.setAlignment(Pos.CENTER);
        hBoxLoginText.setAlignment(Pos.CENTER);
        hBoxLogin.setAlignment(Pos.CENTER);
        gridButtons.setAlignment(Pos.CENTER);
        vBox.setMargin(gridButtons, new Insets(10, 0, 10, 0));
        vBox.setMargin(hBoxTierList, new Insets(0, 0, 25, 0));
        vBox.setMargin(hBoxLoginText, new Insets(0, 0, 10, 0));

        //Size of textFields.
        usrnameField.setPrefWidth(100);
        pwdField.setPrefWidth(100);
        //Adding H and V gaps to components.
        login.setHgap(10);
        login.setVgap(10);

        //Adds the username and password labels/fields to the GridPane.
        login.add(usrnameLabel, 0, 0);
        login.add(usrnameField, 1, 0);
        login.add(pwdLabel, 0, 1);
        login.add(pwdField, 1, 1);

        //Adds event to login button. Attempts to log in the user.
        loginBtn.setOnAction((event)-> {
            attemptLogin(usrnameField, pwdField, loginText);
        });

        //Adds event to username field. Attempts to log in the user.
        usrnameField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER){
                attemptLogin(usrnameField, pwdField, loginText);
            }
        });

        //Adds event to password field. Attempts to log in the user.
        pwdField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER){
                attemptLogin(usrnameField, pwdField, loginText);
            }
        });
        pane.setCenter(vBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        pane.setMargin(vBox, new Insets(100, 0, 0, 0));
        Scene loginScene = new Scene(pane, loginAppWidth, loginAppHeight);

        //Adds event to Create Account button. This allows the user to create a new account.
        newAccountBtn.setOnAction((event) -> {
            usrnameField.setText("");
            pwdField.setText("");
            final Stage newAccountStage = new Stage();
            BorderPane newAccountPane = new BorderPane();
            //newAccountPane.setStyle("-fx-background-color: crimson");
            newAccountStage.setTitle("Create New Account");
            newAccountStage.initModality(Modality.APPLICATION_MODAL);

            //Creates the Nodes necessary for a new account (Text fields and password fields).
            GridPane newAccount = new GridPane();
            Label newAccountText = new Label("Enter your new username and password");
            Label newUsrnameLabel = new Label("New Username:");
            Label newPwdLabel = new Label("New Password:");
            Label confirmPwdLabel = new Label("Re-Enter Password:");
            TextField newUsrnameField = new TextField();
            PasswordField newPwdField = new PasswordField();
            PasswordField confirmPwdField = new PasswordField();
            Button createBtn = new Button("Create");

            //Sets the style of the labels and button.
            newAccountText.setFont(Font.font("Regular", FontWeight.NORMAL, FontPosture.REGULAR, 14));
            createBtn.setStyle("-fx-font-size: 10pt; -fx-background-radius: 20px;");
            /*newAccountText.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
            newUsrnameLabel.setStyle("-fx-text-fill: white; ");
            newPwdLabel.setStyle("-fx-text-fill: white; ");
            confirmPwdLabel.setStyle("-fx-text-fill: white; ");*/

            //Creates a VBox with all the Nodes.
            HBox hBoxAccountText = new HBox(newAccountText);
            HBox hBoxNewAccount = new HBox(newAccount);
            HBox hBoxCreateBtn = new HBox(createBtn);
            VBox vBoxNewAccount = new VBox(hBoxAccountText, hBoxNewAccount, hBoxCreateBtn);

            //Position of Nodes.
            hBoxAccountText.setAlignment(Pos.CENTER);
            hBoxNewAccount.setAlignment(Pos.CENTER);
            hBoxCreateBtn.setAlignment(Pos.CENTER);
            vBoxNewAccount.setMargin(hBoxAccountText, new Insets(0, 0, 10, 0));
            vBoxNewAccount.setMargin(hBoxCreateBtn, new Insets(10, 0, 10, 155));

            //Size of textFields.
            newUsrnameField.setPrefWidth(100);
            newPwdField.setPrefWidth(100);
            confirmPwdField.setPrefWidth(100);
            //Adding H and V gaps to components.
            newAccount.setHgap(10);
            newAccount.setVgap(10);

            //Adds all the Labels and Fields for a new account to the grid.
            newAccount.add(newUsrnameLabel, 0, 0);
            newAccount.add(newUsrnameField, 1, 0);
            newAccount.add(newPwdLabel, 0, 1);
            newAccount.add(newPwdField, 1, 1);
            newAccount.add(confirmPwdLabel, 0, 2);
            newAccount.add(confirmPwdField, 1, 2);

            //Puts the VBox into a GridPane
            newAccountPane.setCenter(vBoxNewAccount);
            vBoxNewAccount.setAlignment(Pos.TOP_CENTER);
            newAccountPane.setMargin(vBoxNewAccount, new Insets(25, 0, 0, 0));

            //Event handlers to create a new Account.
            createBtn.setOnAction((newEvent) -> {
                //Attempts to create an account.
                createAccount(newUsrnameField, newPwdField, confirmPwdField, newAccountText, newAccountStage, loginText);
            });


            Scene dialogScene = new Scene(newAccountPane, 300, 200);
            newAccountStage.setScene(dialogScene);
            newAccountStage.show();
        });

        return loginScene;
    }

    /**
     * Purpose: Checks to see if the current user trying to log in is part of our AccountCollection.
     * @param usrnameField The username text field of our Log in window.
     * @param pwdField The password text field of our Log in window.
     * @param loginText The label text which displays system feedback in our Log in window.
     */
    public void attemptLogin(TextField usrnameField, TextField pwdField, Label loginText){
        //Saves the values of the username and password fields.
        String usrEntered = usrnameField.getText();
        String pwdEntered = pwdField.getText();
        System.out.println("Attempting to login...");
        //Checks if the username and password belongs to an Account of the system.
        if (accounts.logIn(usrEntered, pwdEntered)) {
            //Successfully logged in.
            loginText.setText("You successfully logged in!");
            loginText.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
            //Sets current user to the signed in account.
            Account currentUser = accounts.getAccount(usrEntered, pwdEntered);
            AccountUI user = new AccountUI(currentUser, accounts, this);
            //TierListUI tierListUI = new TierListUI();
            //TierListMaker.changeScenes(tierListUI.getTierListUI());
            TierListMaker.changeScenes(user.getAccountWindow());
            //Updates the scene to our Profile window.
            System.out.println("Username: " + currentUser.getUsrname() + "\nPassword: " + currentUser.getPwd());
        }else {
            //Updates the feedback label to display error.
            loginText.setText("Incorrect username or password.");
            loginText.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold");
            usrnameField.setText("");
            pwdField.setText("");
        }
    }

    /**
     * Purpose: Tries to create a new account given the info inputted.
     * @param newUsrnameField The new username field for a new account.
     * @param newPwdField The new password field for a new account.
     * @param confirmPwdField The confirm-password field for a new account.
     * @param newAccountText The Text displayed in the pop-up UI.
     * @param newAccountStage The Stage of the pop-up UI.
     */
    private void createAccount(TextField newUsrnameField, PasswordField newPwdField, PasswordField confirmPwdField, Label newAccountText, Stage newAccountStage, Label loginText){
        String usrEntered = newUsrnameField.getText();
        String pwdEntered = newPwdField.getText();
        String pwdConfirmEntered = confirmPwdField.getText();

        //Checks to see if the passwords match.
        if (pwdEntered.equals(pwdConfirmEntered)){
            accounts.addAccToCollection(new Account(usrEntered, pwdEntered));
            newAccountStage.close();
            loginText.setText("Account Created Successfully.");
            loginText.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
        }else{
            //Updates the feedback label to display error.
            newAccountText.setText("Passwords do not match.");
            newAccountText.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
            newPwdField.setText("");
            confirmPwdField.setText("");
        }
    }
}

