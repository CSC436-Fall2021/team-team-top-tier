package csc436.View;

import csc436.Model.Tier;
import csc436.Model.TierList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.controlsfx.control.spreadsheet.Grid;

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

    public Scene  getTierListUI(){
        List<Tier> tiers= tierList.getTiers();
        BorderPane pane= new BorderPane();
        GridPane tierGrid= new GridPane();
        GridPane imageGrid= new GridPane();

        Label title= new Label(tierList.getTierListTitle());
        HBox titleBox= new HBox(title);

        for (int i=0;i<tiers.size();i++){
            tierGrid.add(new HBox(new Label(tiers.get(i).getTierTitle())),0,i);
            tierGrid.add(new HBox(),1,i);
            tierGrid.add(new VBox(new Button("Add Tier"),new Button("Delete Tier")),2, i);


            for (javafx.scene.Node child: tierGrid.getChildren()){
                child.setStyle("-fx-border-color: black;" + "-fx-border-insets: 1;" + "-fx-border-width: 2;");
            }
        }

        // TOD DO: CHANGE CONSTRAINTS TO USER'S MONITOR DIMENSIONS
        tierGrid.getColumnConstraints().add(new ColumnConstraints(200));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(680));
        tierGrid.getColumnConstraints().add(new ColumnConstraints(200));

        pane.setTop(titleBox);
        pane.setCenter(tierGrid);
        pane.setBottom(imageGrid);

        titleBox.setAlignment(Pos.TOP_CENTER);
        tierGrid.setAlignment(Pos.CENTER);



        Scene scene= new Scene(pane, windowWidth, windowHeight);

        return scene;
    }
}
