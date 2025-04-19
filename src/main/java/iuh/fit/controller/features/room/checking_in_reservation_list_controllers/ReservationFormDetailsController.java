package iuh.fit.controller.features.room.checking_in_reservation_list_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.dao.ReservationRoomDetailDAO;
import iuh.fit.dao.RoomWithReservationDAO;
import iuh.fit.models.*;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.RoomChargesCalculate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReservationFormDetailsController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, reservationFormListNavigate, bookingRoomNavigate,
            reservationFormBtn;

    @FXML
    private Button deleteReservationFormBtn, checkInBtn,
            earlyCheckInBtn;

    @FXML
    private Label roomNumberLabel, roomCategoryLabel, checkInDateLabel,
            checkOutDateLabel, stayLengthLabel, bookingDepositLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel, cusomerPhoneNumberLabel,
            customerIDCardNumberLabel;

    @FXML
    private Label employeeFullNameLabel, employeePositionLabel, employeeIDLabel,
            employeePhoneNumberLabel;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TitledPane titledPane;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));


    private MainController mainController;
    private ReservationForm reservationForm;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==========================================s========================================================================
    public void initialize() {
        dialogPane.toFront();
    }

    public void setupContext(
            MainController mainController, ReservationForm reservationForm,
            Employee employee, RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.roomWithReservation = roomWithReservation;
        this.employee = employee;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());

        setupReservationForm();
        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToReservationListPanel());
        reservationFormListNavigate.setOnAction(e -> navigateToReservationListPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Current Panel Button
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInTime = reservationForm.getApproxcheckInDate();
        LocalDateTime checkInTimePlus2Hours = checkInTime.plusHours(2);
        LocalDateTime earlyCheckInStart = checkInTime.minusMinutes(30);
        LocalDateTime earlyCheckInEnd = checkInTime.minusSeconds(10);

        checkInBtn.setDisable(!now.isAfter(checkInTime) || !now.isBefore(checkInTimePlus2Hours));
        earlyCheckInBtn.setDisable(!(now.isAfter(earlyCheckInStart) && now.isBefore(earlyCheckInEnd)));
        checkInBtn.setOnAction(e -> handleCheckIn());
        earlyCheckInBtn.setOnAction(e -> handleEarlyCheckin());
        deleteReservationFormBtn.setOnAction(e -> handleDeleteAction());

        reservationFormBtn.setText("Phiếu đặt phòng " + reservationForm.getReservationID());
    }

    // ==================================================================================================================
    // 3.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        Room reservationFormRoom = reservationForm.getRoom();
        Customer reservationFormCustomer = reservationForm.getCustomer();
        Employee reservationFormEmployee = reservationForm.getEmployee();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomCategory().getRoomCategoryName());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getApproxcheckInDate()));
        checkOutDateLabel.setText(dateTimeFormatter.format(reservationForm.getApproxcheckOutTime()));
        stayLengthLabel.setText(RoomChargesCalculate.calculateStayLengthToString(
                reservationForm.getApproxcheckInDate(),
                reservationForm.getApproxcheckOutTime()
        ));
        bookingDepositLabel.setText(RoomChargesCalculate.calculateRoomCharges(
                reservationForm.getApproxcheckInDate(),
                reservationForm.getApproxcheckOutTime(),
                reservationFormRoom
        ) * 0.1 + " VND");

        customerIDLabel.setText(reservationFormCustomer.getCustomerCode());
        customerFullnameLabel.setText(reservationFormCustomer.getFullName());
        cusomerPhoneNumberLabel.setText(reservationFormCustomer.getPhoneNumber());
        customerIDCardNumberLabel.setText(reservationFormCustomer.getIdCardNumber());

        employeeFullNameLabel.setText(reservationFormEmployee.getFullName());
        employeePositionLabel.setText(reservationFormEmployee.getPosition().toString());
        employeeIDLabel.setText(reservationFormEmployee.getEmployeeCode());
        employeePhoneNumberLabel.setText(reservationFormEmployee.getPhoneNumber());
    }

    // ==================================================================================================================
    // 4. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Có tin nhắn
    private void navigateToReservationListPanel(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.getDialogPane().showInformation("Thông Báo", message);
            reservationListController.setupContext(
                    mainController, employee, roomWithReservation
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Không tin nhắn
    private void navigateToReservationListPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.setupContext(
                    mainController, employee, roomWithReservation
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 5. Xử lý chức năng xóa phiếu đặt phòng
    // ==================================================================================================================
    private void handleDeleteAction() {
        try{

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn xóa phiếu đặt phòng này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ReservationFormDAO.deleteData(reservationForm.getReservationID());
                    navigateToReservationListPanel();

                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // ==================================================================================================================
    // 6. Xử lý chức năng CheckIn
    // ==================================================================================================================
    private void handleCheckIn() {
        try {
            ReservationRoomDetailDAO.roomCheckingIn(
                    reservationForm.getReservationID()
            );

            roomWithReservation = RoomWithReservationDAO
                    .getRoomWithReservationByID(reservationForm.getReservationID(), roomWithReservation.getRoom().getRoomID());

            navigateToReservationListPanel("Check-in thành công tại phòng đã đặt.");
        } catch (Exception e) {
            navigateToReservationListPanel(e.getMessage());
        }
    }

    private void handleEarlyCheckin() {
        try {
            String result = ReservationRoomDetailDAO.roomEarlyCheckingIn(
                    reservationForm.getReservationID()
            );

            switch (result) {
                case "ROOM_CHECKING_IN_SUCCESS" -> {
                    roomWithReservation = RoomWithReservationDAO
                            .getRoomWithReservationByID(reservationForm.getReservationID(), roomWithReservation.getRoom().getRoomID());
                    navigateToReservationListPanel("Check-in thành công tại phòng đã đặt.");
                }
                case "ROOM_CHECKING_IN_TIME_INVALID" ->
                        navigateToReservationListPanel("Không nằm trong khoảng thời gian nhận phòng sớm.");
                case "ROOM_CHECKING_IN_INVALID_RESERVATION" ->
                        navigateToReservationListPanel("Phiếu đặt phòng không hợp lệ hoặc đã nhận phòng.");
                default -> navigateToReservationListPanel("Đã xảy ra lỗi không xác định.");
            }
        } catch (Exception e) {
            navigateToReservationListPanel(e.getMessage());
        }
    }


}
