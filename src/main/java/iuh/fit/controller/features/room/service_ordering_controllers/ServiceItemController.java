package iuh.fit.controller.features.room.service_ordering_controllers;

import iuh.fit.models.HotelService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ServiceItemController {
    @FXML
    private ImageView serviceCategoryImg;

    @FXML
    private Label serviceName, servicePrice, totalPrice;

    @FXML
    private Button addServiceBtn;

    @FXML
    private Spinner<Integer> amountField;

    private HotelService hotelService;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(HotelService hotelService) {
        this.hotelService = hotelService;

        loadData();
    }

    private void loadData() {
        if (hotelService != null) {
            serviceName.setText(hotelService.getServiceName());
            servicePrice.setText("Thành tiền: " + hotelService.getServicePrice() + " VND");
            String iconPath = "/iuh/fit/icons/service_icons/ic_" + hotelService.getServiceCategory().getIcon() + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
            serviceCategoryImg.setImage(image);
        }

        amountField.setValueFactory(new SpinnerValueFactory
                .IntegerSpinnerValueFactory(1, 100, 1));

        totalPrice.textProperty().bind(Bindings.createStringBinding(() ->
                        "Thành tiền: " + (amountField.getValue() * hotelService.getServicePrice()) + " VND",
                amountField.valueProperty()));
    }

    // ==================================================================================================================
    // 3. Hàm getter truyền ra ngoài
    // ==================================================================================================================
    public Spinner<Integer> getAmountField() {
        return amountField;
    }

    public Button getAddServiceBtn() {
        return addServiceBtn;
    }


}
