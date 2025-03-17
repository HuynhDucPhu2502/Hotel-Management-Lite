package iuh.fit.controller;

import com.dlsc.gemsfx.DialogPane;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML private TextField userNameField;
    @FXML private PasswordField hiddenPasswordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Text errorMessage;
    @FXML private Button signInButton;
    @FXML private ImageView showPassButton;
    @FXML private DialogPane dialogPane;
    @FXML private Text forgotPasswordBtn;
    @FXML private Label loginBtn;
    @FXML private Button confirmBtn;
    @FXML private Button resetBtn;

    // Pane
    @FXML private GridPane loginGrid;
    @FXML private GridPane restoreDataGrid;




    @FXML private PasswordField passRestorePasswordField;
    @FXML private TextField passRestoreTextField;
    @FXML private TextField filePathRestoreTextField;
    @FXML private Text passTitleText;
    @FXML private Text filepathTitleText;

    @FXML private Button showPassRestoreButton;
    @FXML private Button filePathRestoreButton;

    @FXML private Button refreshPassRestoreButton;
    @FXML private Button cancelRestoreButton;
    @FXML private Button confirmPassRestoreButton;

    private Stage mainStage;

    @FXML
    public void initialize() {
        dialogPane.toFront();

        hiddenPasswordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());
        passRestorePasswordField.textProperty().bindBidirectional(passRestoreTextField.textProperty());

//        forgotPasswordBtn.setOnMouseClicked(event -> forgotPass());
//        loginBtn.setOnMouseClicked(event -> login());
//        confirmBtn.setOnAction(event -> changePassword());
//        resetBtn.setOnAction(event -> resetAction());
    }

// ======================================================================================================
//
//    Các hàm swith màn hình, switch lên Restore Data Pane hoặc xuống Login Pane.
//    Đã sửa đổi một chút cải thiện từ Project ban đầu.
//
// ======================================================================================================
    private boolean isDefaultIcon = true;

    // Hiển thị màn hình từ Login Pane lên Restore Data Pane
    @FXML
    public void fromLoginGridToRestoreDataGrid() {
        switchGridPane(loginGrid, restoreDataGrid, true);
    }

    // Hiển thị màn hình từ Restore Data Pane về Login Pane
    @FXML
    void fromRestoreDataGridToLoginGrid() {
        switchGridPane(restoreDataGrid, loginGrid, false);
    }

    // Hiệu ứng chuyển lên hoặc xuống
    public void switchGridPane(GridPane gridPaneOut, GridPane gridPaneIn, boolean slideUp) {
        gridPaneIn.setVisible(true);

        double height = gridPaneOut.getHeight();
        double fromYOut = slideUp ? 0 : 0;
        double toYOut = slideUp ? height : -height;
        double fromYIn = slideUp ? -height : height;
        double toYIn = 0;

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), gridPaneOut);
        slideOut.setFromY(fromYOut);
        slideOut.setToY(toYOut);
        slideOut.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), gridPaneIn);
        slideIn.setFromY(fromYIn);
        slideIn.setToY(toYIn);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallelTransition = new ParallelTransition(slideOut, slideIn);
        parallelTransition.setOnFinished(event -> {
            gridPaneOut.setVisible(false);
            gridPaneOut.setTranslateY(0);
        });
        parallelTransition.play();
    }

// ======================================================================================================
//
//    Các hàm liên quan đến ẩn hiện mật khẩu tại Login Pane
//
// ======================================================================================================

    // Bật/tắt tính năng nhìn mật khẩu
    @FXML
    private void changePasswordViewState() {
        if (isDefaultIcon) {
            Image newIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_hidden_password.png")));
            showPassButton.setImage(newIcon);
        } else {
            Image defaultIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_show_password.png")));
            showPassButton.setImage(defaultIcon);
        }

        PasswordVisibility();

        isDefaultIcon = !isDefaultIcon;
    }

    private void PasswordVisibility() {
        if (hiddenPasswordField.isVisible()) {
            hiddenPasswordField.setVisible(false);
            hiddenPasswordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
        } else {
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            hiddenPasswordField.setVisible(true);
            hiddenPasswordField.setManaged(true);
        }
    }

//    public void setupContext(Stage mainStage) {
//        this.mainStage = mainStage;
//
//        signInButton.setOnAction(event -> {
//            try {
//                signIn(this.mainStage);
//            } catch (SQLException ignored) {
//
//            }
//        });
//
//        registerEventEnterKey();
//    }

//    private void registerEventEnterKey() {
//        userNameField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                try {
//                    signIn(mainStage);
//                } catch (SQLException ignored) {
//
//                }
//            }
//        });
//
//        hiddenPasswordField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                try {
//                    signIn(mainStage);
//                } catch (SQLException ignored) {
//
//                }
//            }
//        });
//
//        visiblePasswordField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                try {
//                    signIn(mainStage);
//                } catch (SQLException ignored) {
//
//                }
//            }
//        });
//    }



//    private void signIn(Stage mainStage) throws SQLException {
//        if(!RestoreDatabase.isDatabaseExist(DBHelper.getDatabaseName())) {
//            errorMessage.setText(ErrorMessages.DATABASE_NOT_FOUND);
//            return;
//        }
//
//        String userName = userNameField.getText();
//        String password = hiddenPasswordField.getText();
//
//        if (userName.isEmpty()) {
//            errorMessage.setText(ErrorMessages.LOGIN_INVALID_USERNAME);
//            return;
//        }
//
//        if (password.isEmpty()) {
//            errorMessage.setText(ErrorMessages.LOGIN_INVALID_PASSWORD);
//            return;
//        }
//
//        Account account = AccountDAO.getLogin(userName, password);
//        if (account == null) {
//            errorMessage.setText(ErrorMessages.LOGIN_INVALID_ACCOUNT);
//            return;
//        }
//
//        // Kiểm tra trạng thái tài khoản
//        if (
//                account.getAccountStatus().equals(AccountStatus.INACTIVE)
//                || account.getAccountStatus().equals(AccountStatus.LOCKED)
//        ) {
//            dialogPane.showInformation(
//                    "Thông báo",
//                    "Tài khoản bị khóa hoặc không có hiệu lực.\n" +
//                            "Vui lòng báo người quản lý khách sạn để biết thêm thông tin."
//            );
//            return;
//        }
//
//        // Lấy thông tin cần thiết
//        Position position = account.getEmployee().getPosition();
//        Shift currentShift = ShiftDAO.getCurrentShiftForLogin(account.getEmployee());
//
//
//        if (position.equals(Position.RECEPTIONIST)) {
//            if (currentShift == null)
//                dialogPane.showInformation(
//                        "Thông báo",
//                        "Nhân viên không thuộc ca làm việc hiện tại\n" +
//                                "Không thể đăng nhập"
//                );
//            else {
//                loadMainUI(account, currentShift, mainStage);
//            }
//        } else if (position.equals(Position.MANAGER)) {
//            loadMainUI(account, currentShift, mainStage);
//        }
//    }
//
//    private void loadMainUI(Account account, Shift currentShift, Stage mainStage) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
//            AnchorPane mainPanel = fxmlLoader.load();
//
//            MainController mainController = fxmlLoader.getController();
//            mainController.initialize(account, mainStage, currentShift);
//
//            Scene scene = new Scene(mainPanel);
//
//            mainStage.setWidth(1200);
//            mainStage.setHeight(680);
//            mainStage.setScene(scene);
//            mainStage.setResizable(true);
//            mainStage.setMaximized(true);
//
//            mainStage.centerOnScreen();
//
//            mainStage.show();
//        } catch (Exception e) {
//            errorMessage.setText(e.getMessage());
//        }
//    }
//
//    private void forgotPass(){
//        slideOutGridFromBot(loginGrid, forgotPasswordGrid);
//    }
//
//    private void login(){
//        slideOutGridFromTop(forgotPasswordGrid, loginGrid);
//    }
//
//    public void slideOutGridFromBot(GridPane gridPaneOut, GridPane gridPaneIn) {
//        gridPaneIn.setVisible(true);
//        gridPaneIn.setTranslateY(gridPaneOut.getHeight());
//        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), gridPaneOut);
//        slideOut.setFromY(0);
//        slideOut.setToY(-gridPaneOut.getHeight());
//        slideOut.setInterpolator(Interpolator.EASE_BOTH);
//        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), gridPaneIn);
//        slideIn.setFromY(gridPaneOut.getHeight());
//        slideIn.setToY(0);
//        slideIn.setInterpolator(Interpolator.EASE_BOTH);
//        ParallelTransition parallelTransition = new ParallelTransition(slideOut, slideIn);
//        parallelTransition.setOnFinished(event -> {
//            gridPaneOut.setVisible(false);
//            gridPaneOut.setTranslateY(0);
//        });
//
//        parallelTransition.play();
//    }



    @FXML
    void confirmPassRestore() {
//        final String key = "7523C62ABDB7628C5A9DAD8F97D8D8C5C040EDE36535E531A8A3748B6CAE7E00";
//        final String resourcePath = "/iuh/fit/security/E201065D0554652615C320C00A1D5BC8EDCA469D72C2790E24152D0C1E2B6189.properties";
//
//        String userPassInput = passRestorePasswordField.getText();
//
//        // Kiểm tra mật khẩu người dùng nhập
//        if (userPassInput == null || userPassInput.isBlank()) {
//            showMessage(
//                    Alert.AlertType.INFORMATION,
//                    "Thông báo",
//                    "Vui lòng nhập mật khẩu",
//                    "Nhấn OK để xác nhận"
//            ).show();
//            return;
//        }
//
//        // Kiểm tra file và xác thực mật khẩu
//        try {
//            String storedHash = PropertiesFile.readFileFromResources(resourcePath, key);
//            if (PasswordHashing.hashPassword(userPassInput).equalsIgnoreCase(storedHash)) {
//                switchFromPassPanelToRestorePanel(false, true);
//            } else {
//                showMessage(
//                        Alert.AlertType.INFORMATION,
//                        "Sai mật khẩu",
//                        "Mật khẩu không đúng, vui lòng thử lại.",
//                        "Nhấn OK để xác nhận"
//                ).show();
//            }
//        } catch (Exception e) {
//            showMessage(
//                    Alert.AlertType.ERROR,
//                    "Lỗi",
//                    "Đã xảy ra lỗi khi đọc file từ resources.",
//                    "Vui lòng kiểm tra lại file và thử lại."
//            ).show();
//        }
    }



    @FXML
    void refreshPassRestore() {
        passRestorePasswordField.setText("");
        passRestorePasswordField.requestFocus();
    }

    @FXML
    void showPassRestore() {
        changePasswordViewState();
        if(passRestorePasswordField.isVisible()){
            passRestoreTextField.setVisible(true);
            passRestoreTextField.setManaged(true);
            passRestorePasswordField.setVisible(false);
            passRestorePasswordField.setManaged(false);
        }else{
            passRestoreTextField.setVisible(false);
            passRestoreTextField.setManaged(false);
            passRestorePasswordField.setVisible(true);
            passRestorePasswordField.setManaged(true);
        }
    }

    @FXML
    void confirmRestoreData() throws SQLException {
//        String filePath = filePathRestoreTextField.getText();
//        if (filePath == null || filePath.isBlank()) {
//            showMessage(
//                    Alert.AlertType.INFORMATION,
//                    "Thông báo",
//                    "Hãy chọn tệp dữ liệu để khôi phục.",
//                    "Nhấn OK để xác nhận."
//            ).show();
//            return;
//        }
//
//        String[] parts = filePath.split("\\\\");
//        String fileName = parts[parts.length - 1];
//        if (!fileName.contains(".bak")) {
//            showMessage(
//                    Alert.AlertType.INFORMATION,
//                    "Thông báo",
//                    "Sai loại tệp, tệp phải có đuôi .bak.",
//                    "Nhấn OK để xác nhận."
//            ).show();
//            return;
//        }
//
//        String databaseName = "HotelDatabase";
//        if (RestoreDatabase.isDatabaseExist(databaseName)) {
//            Optional<ButtonType> optional = showMessage(
//                    Alert.AlertType.CONFIRMATION,
//                    "Thông báo",
//                    "Dữ liệu đã tồn tại. Bạn có muốn ghi đè không?",
//                    "Nhấn OK để ghi đè, Cancel để hủy."
//            ).showAndWait();
//
//            if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
//                try {
//                    RestoreDatabase.restoreFullWhenNoDB(filePath);
//                    showMessage(
//                            Alert.AlertType.INFORMATION,
//                            "Phục hồi dữ liệu thành công",
//                            "Dữ liệu đã phục hồi thành công",
//                            "Nhấn OK để xác nhận"
//                    ).show();
//                    backToLoginPanel();
//                } catch (Exception e) {
//                    showMessage(
//                            Alert.AlertType.ERROR,
//                            "Khôi phục thất bại",
//                            "Tệp dữ liệu không hợp lệ. " +
//                                    "Hãy kiểm tra lại đường dẫn, chỉ có thể dụng tệp dữ liệu toàn bộ (FULL) và là dữ liệu của ứng dụng",
//                            "Nhấn OK để xác nhận."
//                    ).show();
//                }
//            }
//        } else {
//            try {
//                RestoreDatabase.restoreFullWhenNoDB(filePath);
//                showMessage(
//                        Alert.AlertType.INFORMATION,
//                        "Phục hồi dữ liệu thành công",
//                        "Dữ liệu đã phục hồi thành công",
//                        "Nhấn OK để xác nhận"
//                ).show();
//                backToLoginPanel();
//            } catch (Exception e) {
//                showMessage(
//                        Alert.AlertType.ERROR,
//                        "Khôi phục thất bại",
//                        "Tệp dữ liệu không hợp lệ. " +
//                                "Hãy kiểm tra lại đường dẫn, chỉ có thể dụng tệp dữ liệu toàn bộ (FULL) và là dữ liệu của ứng dụng",
//                        "Nhấn OK để xác nhận."
//                ).show();
//            }
//        }
    }


    @FXML
    void cancelRestore() {
//        hideComponentsFromRestoreDataGrid();
        filePathRestoreTextField.setText("");
    }

    @FXML
    void getFilePath() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(null);

        if(f == null) return;
        filePathRestoreTextField.setText(f.getAbsolutePath());
    }

    private Alert showMessage(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

}
