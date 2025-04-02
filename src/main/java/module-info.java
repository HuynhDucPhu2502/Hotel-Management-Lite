module iuh.fit {
    // JavaFX
    requires javafx.fxml;
    requires javafx.web;

    // JPA
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Khác
    requires static lombok;
    requires net.datafaker;
    requires com.dlsc.gemsfx;

    // Opens
    opens iuh.fit to javafx.fxml;
    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features to javafx.fxml;
    opens iuh.fit.devtools to javafx.fxml;
    opens iuh.fit.models to jakarta.persistence, org.hibernate.orm.core;

    // Exports
    exports iuh.fit;
    exports iuh.fit.models;
    exports iuh.fit.devtools;

}
