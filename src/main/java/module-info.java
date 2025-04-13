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

    // xuat exel
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    // Opens
    opens iuh.fit to javafx.fxml;
    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.controller.features.customer to javafx.fxml;
    opens iuh.fit.controller.features.employee to javafx.fxml;
    opens iuh.fit.controller.features.room to javafx.fxml;

    opens iuh.fit.devtools to javafx.fxml;
    opens iuh.fit.models to jakarta.persistence, org.hibernate.orm.core;

    opens iuh.fit.controller.features.statistics to javafx.fxml;
    opens iuh.fit.security to javafx.fxml;
    opens iuh.fit.models.wrapper to javafx.fxml;
    opens iuh.fit.models.enums to javafx.fxml;
    opens iuh.fit.utils to javafx.fxml;
    opens iuh.fit.dao to javafx.fxml;


    // Exports
    exports iuh.fit;
    exports iuh.fit.models;
    exports iuh.fit.devtools;

    exports iuh.fit.dao;
    exports iuh.fit.models.wrapper;
    exports iuh.fit.models.enums;
    exports iuh.fit.utils;
    exports iuh.fit.security;
    exports iuh.fit.controller.features.statistics;
}
