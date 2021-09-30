package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ImageUploadUI extends Application {

    // GraphicsContext of the main canvas
    GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Image upload");

        // TODO: create a canvas
        // TODO: add a button that opens a file browser and adds the paths to some collection
        // TODO: draw images on canvas
        /*
         * Eventually have something like
         *
         * for (Picture pic: pictures) {
         *      gc.drawImage(pic.getImage(), x, y);
         * }
         *
         */

        BorderPane pane = new BorderPane();
        //Show stage and set scene
        Scene scene = new Scene(pane, 1000, 1000);

        stage.setScene(scene);
        stage.show();
    }
}
