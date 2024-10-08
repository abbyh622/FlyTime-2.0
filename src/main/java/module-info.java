module com.abby {
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires json.simple;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;

    opens com.abby.main to javafx.base, javafx.graphics, javafx.fxml;
    opens com.abby.ui to javafx.base, javafx.graphics, javafx.fxml;
}
