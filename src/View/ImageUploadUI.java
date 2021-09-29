package View;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
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

    }
}
