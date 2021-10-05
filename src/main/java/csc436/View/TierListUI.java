package csc436.View;

import csc436.Model.Tier;
import csc436.Model.TierList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.Console;
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


    public TierListUI(){
        tierList= new TierList("Example Tier List");
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
        for (int i=0;i<tiers.size();i++){
            nameBoxes.add(new HBox(new Label(tiers.get(i).getTierTitle())));
            tierGrid.add(nameBoxes.get(i),0,i);

            tierBoxes.add(new HBox());
            tierGrid.add(tierBoxes.get(i),1,i);

            buttonBoxes.add(new VBox(new Button("Add Tier"),new Button("Delete Tier")));
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
}
