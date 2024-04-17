module com.scottlandyard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.jgrapht.core;


    opens com.scottlandyard to javafx.fxml;
    exports com.scottlandyard;
}