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
    requires com.dlsc.unitfx;
    requires com.calendarfx.view;


    // xuat exel
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.rmi;
    requires java.naming;
    requires org.checkerframework.checker.qual;
    requires itextpdf;
    requires org.apache.pdfbox;

    // Opens
    opens iuh.fit to javafx.fxml;

    // open excel
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.controller.features.customer to javafx.fxml;
    opens iuh.fit.controller.features.employee to javafx.fxml;
    opens iuh.fit.controller.features.room to javafx.fxml;
    opens iuh.fit.controller.features.room.creating_reservation_form_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.checking_in_reservation_list_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.service_ordering_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.room_changing_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.checking_out_controllers to javafx.fxml;
    opens iuh.fit.controller.features.invoice to javafx.fxml;
    opens iuh.fit.controller.features.statistics to javafx.fxml;

    opens iuh.fit.devtools to javafx.fxml;


    opens iuh.fit.models to jakarta.persistence, org.hibernate.orm.core;
    opens iuh.fit.models.wrapper to javafx.fxml;
    opens iuh.fit.models.enums to javafx.fxml;

    opens iuh.fit.security to javafx.fxml;

    opens iuh.fit.utils to javafx.fxml;

    opens iuh.fit.dao.daoimpl to javafx.fxml;


    // Exports
    exports iuh.fit;
    exports iuh.fit.models;
    exports iuh.fit.devtools;

    exports iuh.fit.dto;
    exports iuh.fit.models.wrapper;
    exports iuh.fit.models.enums;
    exports iuh.fit.utils;
    exports iuh.fit.security;
    exports iuh.fit.controller.features.statistics;
    exports iuh.fit.dao.daoimpl;
    exports iuh.fit.dao.daointerface;
    opens iuh.fit.dao.daointerface to javafx.fxml;
}
