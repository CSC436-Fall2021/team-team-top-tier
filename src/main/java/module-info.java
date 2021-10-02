module csc436.tierlistmaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires junit;

    opens csc436.View to javafx.fxml;
    exports csc436.View;

    opens csc436 to junit;
    exports csc436;
}