package csc436.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.*;

/**
 * Program: ExportUI.java
 * Purpose: ExportUI class that contains a button for saving the TierList as a PNG.
 *
 *      Call ExportUI exportUI = ExportUI(appStage.getScene())
 *           exportUI.getExportUI(); // place this inside of the TierList scene
 *
 * Created: 10/12/2021
 * @author David Dung
 */

public class ExportUI {

    BorderPane pane;
    GridPane tierPane;

    /**
     * ExportUI takes the TierList borderpane to crop and save it.
     *
     * @param tierPane This borderpane should be the TierList borderpane
     */
    public ExportUI(GridPane tierPane) {
        this.pane = new BorderPane();
        this.tierPane = tierPane;

        makeButton();
    }

    private void makeButton() {
        Button exportButt = new Button();
        EventHandler<ActionEvent> exportEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO: crop the scene
                WritableImage writableImage = tierPane.snapshot(new SnapshotParameters(), null);
                // TODO: save the scene
                try {
                    // TODO: open a file browser for user to choose location
                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter =
                            new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extFilter);

                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", new File("test.png"));

                    System.out.println("Image Saved");
                } catch (IOException e) {
                    System.out.println("Cannot write image");
                }
            }
        };
        exportButt.setOnAction(exportEvent);
        exportButt.setText("Save TierList");
        pane.setCenter(exportButt);
    }

    public BorderPane getExportUI() {
        return pane;
    }
}