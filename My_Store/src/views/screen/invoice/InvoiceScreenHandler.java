package views.screen.invoice;

import common.exception.ProcessInvoiceException;
import controller.PaymentController;
import entity.invoice.Invoice;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import subsystem.VnPaySubsystem;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.payment.PaymentScreenHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class InvoiceScreenHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());

    @FXML
    private Label pageTitle;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label province;

    @FXML
    private Label address;

    @FXML
    private Label instructions;

    @FXML
    private Label subtotal;

    @FXML
    private Label shippingFees;

    @FXML
    private Label total;

    @FXML
    private VBox vboxItems;

    private Invoice invoice;

    public InvoiceScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        setInvoiceInfo();
    }

    private void setInvoiceInfo() {

        name.setText(invoice.getOrder().getName());
        phone.setText(invoice.getOrder().getPhone());
        province.setText(invoice.getOrder().getProvince());
        instructions.setText(invoice.getOrder().getInstruction());
        address.setText(invoice.getOrder().getAddress());
        subtotal.setText(Utils.getCurrencyFormat(invoice.getOrder().getAmount()));
        shippingFees.setText(Utils.getCurrencyFormat(invoice.getOrder().getShippingFees()));
        int amount = invoice.getOrder().getAmount() + invoice.getOrder().getShippingFees();
        total.setText(Utils.getCurrencyFormat(amount));
        invoice.setAmount(amount);
        invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                vboxItems.getChildren().add(mis.getContent());
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }

        });

    }


    /**
     * @param event
     * @throws IOException
     */
    @FXML
    void confirmInvoice(MouseEvent event) throws IOException {
        BaseScreenHandler paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice);
        paymentScreen.setBController(new PaymentController());
        paymentScreen.setPreviousScreen(this);
        paymentScreen.setHomeScreenHandler(homeScreenHandler);
        paymentScreen.setScreenTitle("Payment Screen");
        paymentScreen.show();

    }

}
