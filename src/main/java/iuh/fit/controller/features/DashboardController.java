package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.dao.daointerface.RoomDAO;
import iuh.fit.dao.daoimpl.RoomDAOImpl;
import iuh.fit.models.Account;
import iuh.fit.models.enums.Position;

import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.TimelineManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import lombok.SneakyThrows;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {

    private final RoomDAO roomDAO = new RoomDAOImpl();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roomAvailabelCountLabel, roomOnUseCountLabel,
            roomOverdueCountLabel;

    @FXML
    private GridPane featureGridPane;

    @FXML
    private TextField inputTextField;

    private Account account;
    private MainController mainController;

    // 2 phần:
    // Phần 1: tên chức năng, danh sách từ khoá
    // Phần 2: đường dẫn FXML
    private HashMap<HashMap<String, String>, String> featureKeywordFXMLMapping;

    public DashboardController() throws RemoteException {
    }

    public void initialize() {
    }

    public void setupContext(Account account, MainController mainController) throws Exception {
        this.account = account;
        this.mainController = mainController;

        loadData();
    }


    @SneakyThrows
    private void loadData(){
        String empName = account.getEmployee().getFullName();
        welcomeLabel.setText("Xin chào, " + empName);

        loadDataIntoKeywords();
        loadFeaturesIntoGridPane();
        bindSearchFunctionality();
        Platform.runLater(() -> {
            try {
                loadNumberOfRoomInformation();
            } catch (RemoteException e) {
                e.printStackTrace(); // hoặc xử lý phù hợp
            }
        });
    }

    private void loadDataIntoKeywords() {
        featureKeywordFXMLMapping = new HashMap<>();
        Position position = account.getEmployee().getPosition();

        // Chung cho tất cả vị trí
        featureKeywordFXMLMapping.put(createKeyword("Trang chủ", "Trang chủ, trang chu, tc, dashboard"), "/iuh/fit/view/features/DashboardPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm phòng", "Tìm kiếm phòng, tim kiem phong, tkp, room_search"), "/iuh/fit/view/features/room/RoomSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Đặt phòng", "Đặt phòng, dat phong, dp, room_booking"), "/iuh/fit/view/features/room/RoomBookingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Quản lý hóa đơn", "Quản lý hóa đơn, quan ly hoa don, qlhd, invoice_manager"), "/iuh/fit/view/features/invoice/InvoiceManagerPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm dịch vụ", "Tìm kiếm dịch vụ, tim kiem dich vu, tkdv, hotel_service_search"), "/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm khách hàng", "Tìm kiếm khách hàng, tim kiem khach hang, tkkh, customer_search"), "/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Quản lý khách hàng", "Quản lý khách hàng, quan ly khach hang, qlkh, customer_manager"), "/iuh/fit/view/features/customer/CustomerManagerPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Thống kê doanh thu", "Thống kê doanh thu, thong ke doanh thu, tkdt, revenue_statistics"), "/iuh/fit/view/features/statistics/RevenueStatisticalPanel.fxml");

        // Thêm chức năng riêng cho MANAGER
        if (position.equals(Position.MANAGER)) {
            featureKeywordFXMLMapping.put(createKeyword("Quản lý nhân viên", "Quản lý nhân viên, quan ly nhan vien, qlnv, employee_manager"), "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Thêm nhân viên", "Thêm nhân viên, them nhan vien, tnv"), "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập nhật thông tin nhân viên", "Cập nhật thông tin nhân viên, cap nhat thong tin nhan vien, cnttnv"), "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");

            featureKeywordFXMLMapping.put(createKeyword("Quản lý tài khoản", "Quản lý tài khoản, quan ly tai khoan, qltk, account_manager"), "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập nhật thông tin tài khoản", "Cập nhật thông tin tài khoản, cập nhật tài khoản, cap nhat thong tin tai khoan, cap nhat tai khoan, cntk"), "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");

            featureKeywordFXMLMapping.put(createKeyword("Quản lý loại phòng", "Quản lý loại phòng, quan ly loai phong, qllp, room_category_manager"), "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Thêm loại phòng", "Thêm loại phòng, them loai phong, tlp"), "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Xóa loại phòng", "Xóa loại phòng, xoa loai phong, xlp"), "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập nhật loại phòng", "Cập nhật loại phòng, cap nhat loai phong, cnlp"), "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");

            featureKeywordFXMLMapping.put(createKeyword("Quản lý phòng", "Quản lý phòng, quan ly phong, qlp, room_manager"), "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Thêm phòng", "Thêm phòng, them phong, tp"), "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Xóa phòng", "Xóa phòng, xoa phong, xp"), "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập nhật phòng", "Cập nhật phòng, cap nhat phong, cnp"), "/iuh/fit/view/features/room/RoomManagerPanel.fxml");

            featureKeywordFXMLMapping.put(createKeyword("Quản lý loại dịch vụ", "Quản lý loại dịch vụ, quan ly loai dich vu, qlldv, service_category_manager"), "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Thêm loại dịch vụ", "Thêm loại dịch vụ, them loai dich vu, tldv"), "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Xóa loại dịch vụ", "Xóa loại dịch vụ, xoa loai dich vu, xldv"), "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập loại nhật dịch vụ", "Cập nhật loại dịch vụ, cap nhat loai dich vu, cnldv"), "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");

            featureKeywordFXMLMapping.put(createKeyword("Quản lý dịch vụ", "Quản lý dịch vụ, quan ly dich vu, qldv, hotel_service_manager"), "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Thêm dịch vụ", "Thêm dịch vụ, them dich vu, tdv"), "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Xóa dịch vụ", "Xóa dịch vụ, xoa dich vu, xdv"), "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Cập nhật dịch vụ", "Cập nhật dịch vụ, cap nhat dich vu, cndv"), "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
        }
    }

    @SneakyThrows
    private void loadNumberOfRoomInformation() throws RemoteException {
        TimelineManager.getInstance().removeTimeline("REALTIME_DASHBOARD");
        getNumbersOfRoomInformation();
        Timeline timeline =  new Timeline(new KeyFrame(Duration.seconds(60), event -> {
            try {
                getNumbersOfRoomInformation();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE); // Lặp vô hạn
        timeline.play(); // Bắt đầu chạy

        TimelineManager.getInstance().addTimeline("REALTIME_DASHBOARD", timeline);
    }


    private void getNumbersOfRoomInformation() throws RemoteException {
        Map<RoomStatus, Long> roomStatusCount = roomDAO.getRoomStatusCount();

        roomAvailabelCountLabel.setText(String.valueOf(roomStatusCount.getOrDefault(RoomStatus.AVAILABLE, 0L)));
        roomOnUseCountLabel.setText(String.valueOf(roomStatusCount.getOrDefault(RoomStatus.IN_USE, 0L)));
        roomOverdueCountLabel.setText(String.valueOf(roomStatusCount.getOrDefault(RoomStatus.OVER_DUE, 0L)));
    }


    private HashMap<String, String> createKeyword(String functionName, String keyword) {
        HashMap<String, String> map = new HashMap<>();
        map.put(functionName, keyword);
        return map;
    }

    private HBox createFeatureBox(String functionName, String fxmlPath) {
        HBox featureBox = new HBox();
        featureBox.setAlignment(Pos.CENTER);
        featureBox.setStyle("-fx-border-color: #ccc; -fx-padding: 20; -fx-background-color: #f0f0f0;");
        featureBox.setPrefSize(200, 100);

        Label label = new Label(functionName);
        label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        label.setWrapText(true);
        featureBox.getChildren().add(label);

        featureBox.setOnMouseEntered(event -> featureBox.setStyle(
                "-fx-border-color: #0078d7; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #e6f7ff; -fx-cursor: hand;"));
        featureBox.setOnMouseExited(event -> featureBox.setStyle(
                "-fx-border-color: #ccc; -fx-padding: 20; -fx-background-color: #f0f0f0;"));

        featureBox.setOnMouseClicked(event -> mainController.loadPanel(fxmlPath, mainController, account));

        return featureBox;
    }

    private void loadFeaturesIntoGridPane() {
        featureGridPane.getChildren().clear();
        int row = 0;
        int col = 0;

        for (HashMap<String, String> featureMap : featureKeywordFXMLMapping.keySet()) {
            String functionName = featureMap.keySet().iterator().next();
            String fxmlPath = featureKeywordFXMLMapping.get(featureMap);

            HBox featureBox = createFeatureBox(functionName, fxmlPath);

            featureGridPane.add(featureBox, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    private void bindSearchFunctionality() {
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            featureGridPane.getChildren().clear();

            if (newValue == null || newValue.trim().isEmpty()) {
                loadFeaturesIntoGridPane();
            } else {
                int row = 0;
                int col = 0;

                for (HashMap<String, String> featureMap : featureKeywordFXMLMapping.keySet()) {
                    String functionName = featureMap.keySet().iterator().next();
                    String keywords = featureMap.values().iterator().next().toLowerCase();
                    newValue = newValue.toLowerCase();

                    if (functionName.toLowerCase().contains(newValue) || keywords.contains(newValue)) {
                        String fxmlPath = featureKeywordFXMLMapping.get(featureMap);
                        HBox featureBox = createFeatureBox(functionName, fxmlPath);

                        featureGridPane.add(featureBox, col, row);

                        col++;
                        if (col == 4) {
                            col = 0;
                            row++;
                        }
                    }
                }
            }
        });
    }

}
