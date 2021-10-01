package csc436.View;

import csc436.Model.TierList;
import javafx.scene.layout.GridPane;

/**
 * Program: TierListUI.java
 * Purpose: TierListUI class that contains a method that constructs and
 *          can return a Scene UI element that displays the properties of a TierList obj.
 *
 * Created: 09/29/2021
 * @author Victor A. Jimenez Granados
 */
public class TierListUI {
    GridPane tierGrid;
    TierList tierList;


    public TierListUI(){
        tierGrid= new GridPane();
    }

    public TierListUI(TierList tierList){
        tierGrid= new GridPane();
    }
}
