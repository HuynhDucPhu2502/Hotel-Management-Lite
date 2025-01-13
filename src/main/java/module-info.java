module iuh.fit {
    // Các module Java SE tiêu chuẩn
    requires java.desktop;
    requires java.sql;

    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Jakarta Persistence (JPA) và Hibernate ORM
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Các thư viện bên thứ ba
    requires static lombok;
    requires net.datafaker;

    // Mở các gói cụ thể để sử dụng reflection
    opens iuh.fit to javafx.fxml;
    opens iuh.fit.models to jakarta.persistence, org.hibernate.orm.core;


    // Xuất (exports) các gói công khai
    exports iuh.fit;
    exports iuh.fit.models;
}
