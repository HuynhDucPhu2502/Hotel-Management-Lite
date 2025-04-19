package iuh.fit.controller.features.invoice;

import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.controller.MainController;
import iuh.fit.dao.daointerface.InvoiceDAO;
import iuh.fit.dao.daoimpl.InvoiceDAOImpl;
import iuh.fit.models.Employee;
import iuh.fit.models.Invoice;
import iuh.fit.utils.EditDateRangePicker;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class InvoiceManagerController {
    private InvoiceDAO invoiceDAO = new InvoiceDAOImpl();

    @FXML
    private DateRangePicker invoiceDateRangeSearchField;
    @FXML
    private TextField invoiceIDSearchField;
    @FXML
    private GridPane invoiceGridPane;
    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox invoiceListContainer;

    // Context
    private MainController mainController;
    private Employee employee;
    private List<Invoice> invoiceList;

    public InvoiceManagerController() throws RemoteException {
    }


    public void initialize() {
        setupSearchListeners();
    }

    public void setupContext(MainController mainController, Employee employee) {
        this.mainController = mainController;
        this.employee = employee;


        loadData();
    }

    private void loadData() {
        Task<List<Invoice>> loadDataTask = new Task<>() {
            @Override
            protected List<Invoice> call() throws RemoteException {
//                RoomManagementService.autoCheckoutOverdueRooms(notificationButtonController, mainController);
                return invoiceDAO.getAllInvoices();
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            invoiceList = loadDataTask.getValue();
            displayInvoices(invoiceList);
            EditDateRangePicker.editDateRangePicker(invoiceDateRangeSearchField);
            invoiceDateRangeSearchField
                    .getDateRangeView()
                    .presetTitleProperty()
                    .set("Thời điểm tạo hóa đơn");
        });

        loadDataTask.setOnFailed(event -> {
            Throwable exception = loadDataTask.getException();
            if (exception != null) {
                exception.printStackTrace();
            }
        });


        new Thread(loadDataTask).start();
    }

    private void displayInvoices(List<Invoice> invoices) {
        if (!invoices.isEmpty()) {
            invoiceGridPane.getChildren().clear();

            int row = 0;
            int col = 0;

            try {
                for (Invoice invoice : invoices) {
                    Pane invoiceItem = loadInvoiceItem(invoice);

                    invoiceGridPane.add(invoiceItem, col, row);

                    col++;
                    if (col == 3) {
                        col = 0;
                        row++;
                    }
                }

                invoiceGridPane.setVisible(true);
                invoiceGridPane.setManaged(true);
                emptyLabelContainer.setVisible(false);
                emptyLabelContainer.setManaged(false);
                invoiceListContainer.setAlignment(Pos.TOP_CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            invoiceGridPane.setVisible(false);
            invoiceGridPane.setManaged(false);

            if (emptyLabelContainer.getChildren().isEmpty()) {
                Label emptyLabel = new Label("Không có phiếu đặt phòng nào.");
                emptyLabel.setStyle("-fx-font-size: 42px; -fx-text-fill: gray;");
                emptyLabelContainer.getChildren().add(emptyLabel);
            }

            emptyLabelContainer.setVisible(true);
            emptyLabelContainer.setManaged(true);

            invoiceListContainer.setAlignment(Pos.CENTER);
        }
    }

    private Pane loadInvoiceItem(Invoice invoice) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/invoice/InvoiceItem.fxml"));
        Pane invoiceItem = loader.load();

        InvoiceItemController controller = loader.getController();
        controller.setupContext(mainController, employee, invoice);

        return invoiceItem;
    }

    private void setupSearchListeners() {
        invoiceIDSearchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        invoiceDateRangeSearchField.valueProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());
    }

    private void handleSearchAction() {
        if (invoiceList == null)
            return;

        String invoiceID = invoiceIDSearchField.getText().trim();
        LocalDate startDate = invoiceDateRangeSearchField.getValue().getStartDate();
        LocalDate endDate = invoiceDateRangeSearchField.getValue().getEndDate();

        Task<List<Invoice>> searchTask = new Task<>() {
            @Override
            protected List<Invoice> call() {
                return invoiceList.stream()
                        .filter(invoice -> (invoiceID.isEmpty() || invoice.getInvoiceID().contains(invoiceID)) &&
                                (startDate == null || endDate == null ||
                                        (!invoice.getInvoiceDate().toLocalDate().isBefore(startDate) &&
                                                !invoice.getInvoiceDate().toLocalDate().isAfter(endDate))))
                        .collect(Collectors.toList());
            }
        };

        searchTask.setOnSucceeded(event -> displayInvoices(searchTask.getValue()));

        new Thread(searchTask).start();
    }

}
