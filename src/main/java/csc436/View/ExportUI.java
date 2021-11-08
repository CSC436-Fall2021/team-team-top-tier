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
import javafx.stage.Stage;

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

    private BorderPane pane;
    private GridPane tierPane;
    private Button exportButt;

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
        exportButt = new Button();
        EventHandler<ActionEvent> exportEvent = actionEvent -> {
            // TODO: crop the scene
            // TODO: save the scene
            // TODO: open a file browser for user to choose location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                try {
                    WritableImage writableImage = tierPane.snapshot(new SnapshotParameters(), null);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                    System.out.println("Image Saved");
                } catch (IOException e) {
                }
            }
        };
        exportButt.setOnAction(exportEvent);
        exportButt.setText("Save");
        pane.setCenter(exportButt);
    }

    public BorderPane getExportUI() {
        return pane;
    }
    public Button getExportButton() {
        return exportButt;
    }
}