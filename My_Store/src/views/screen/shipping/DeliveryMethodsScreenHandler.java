package views.screen.shipping;

import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.shipping.Shipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

import java.io.IOException;
import java.util.HashMap;

public class DeliveryMethodsScreenHandler extends BaseScreenHandler {

    private Order order;

    @FXML
    private RadioButton placeRushOrderValue;

    @FXML
    private RadioButton placeOrderValue;

    @FXML
    private TextField deliveryInstruction;

    @FXML
    private TextField shipmentDetail;

    @FXML
    private DatePicker deliveryTime;

    @FXML
    private Label errorProvince;

    @FXML
    private Button updateDeliveryMethodInfoButton;

    @FXML
    private TextField weightTextField;

    public DeliveryMethodsScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
        super(stage, screenPath);
        this.order = order;
    }


    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void updateDeliveryMethodInfo(MouseEvent event) throws IOException {
        String deliveryInstructionString = new String(deliveryInstruction.getText());
        String shipmentDetailString = new String(shipmentDetail.getText());
        String deliveryDateString = new String();
        if (deliveryTime.getValue() != null) {
            deliveryDateString = new String(deliveryTime.getValue().toString());
        }
        int typeDelivery;
        if (placeRushOrderValue.isSelected()) {
            typeDelivery = utils.Configs.PLACE_RUSH_ORDER;
        } else {
            typeDelivery = utils.Configs.PALCE_ORDER;
        }
        var shipment = new Shipment(typeDelivery);
        shipment.setShipmentDetail(shipmentDetailString);
        shipment.setDeliveryTime(deliveryDateString);
        shipment.setDeliveryInstruction(deliveryInstructionString);

//        // Lấy giá trị weight từ TextField
//        int weight = Integer.parseInt(weightTextField.getText());
//
//        var placeOrderController = new PlaceOrderController();
//        int cartSize = placeOrderController.getListCartMedia().size();
//        String ship = new String(order.getProvince());

//         // Tính phí vận chuyển
//        int shippingFees = getBController().calculateShippingFee(order.getAmount(), weight);
//        if (order.getAmount() >= 100) {
//            shippingFees = 0;
//        } else if (ship.equals("Hà Nội") || ship.equals("Hồ Chí Minh")) {
//            if (weight <= 3) {
//                shippingFees = 22;
//            } else {
//                // Số lượng 0.5 kg tiếp theo sau 3 kg đầu
//                int additionalWeight = (weight * 2) - 6; // Trừ đi 6 vì đã tính 3 kg đầu
//                // Phí cho trọng lượng thêm theo quy tắc 2500 cho mỗi 0.5 kg
//                int additionalFee = additionalWeight * 2;
//
//                // Tổng phí
//                shippingFees = 22 + additionalFee;
//            }
//
//            if (ship.equals("Hà Nội")) {
//                shippingFees += cartSize * 10;
//            }
//        }
//        order.setShippingFees(shippingFees);


        PlaceRushOrderController.validatePlaceRushOrderData(shipment);
        order.setShipment(shipment);

        // // create invoice screen
        Invoice invoice = getBController().createInvoice(order);
        BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
        InvoiceScreenHandler.setPreviousScreen(this);
        InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
        InvoiceScreenHandler.setScreenTitle("Invoice Screen");
        InvoiceScreenHandler.setBController(getBController());
        InvoiceScreenHandler.show();
    }


    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        // Back to previous screen
        BaseScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH,
                this.order);
        ShippingScreenHandler.setPreviousScreen(this);
        ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
        ShippingScreenHandler.setScreenTitle("Shipping Screen");
        ShippingScreenHandler.setBController(getBController());
        ShippingScreenHandler.show();
    }


    /**
     * @param event
     */
    @FXML
    private void handleDeliveryType(ActionEvent event) {
        if (placeOrderValue.isSelected()) {
            deliveryInstruction.setDisable(true);
            shipmentDetail.setDisable(true);
            deliveryTime.setDisable(true);
        } else if (placeRushOrderValue.isSelected()) {
            deliveryInstruction.setDisable(false);
            shipmentDetail.setDisable(false);
            deliveryTime.setDisable(false);
        }
        handleProvinceError(event);
    }

    /**
     * @param event
     */
    @FXML
    private void handleProvinceError(ActionEvent event) {
        String province = new String(order.getProvince());

        errorProvince.setVisible(false);
        deliveryInstruction.setDisable(true);
        shipmentDetail.setDisable(true);
        deliveryTime.setDisable(true);
        updateDeliveryMethodInfoButton.setDisable(false);

        if (!province.equals("Hà Nội")) {
            if (placeRushOrderValue.isSelected()) {
                errorProvince.setVisible(true);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
                updateDeliveryMethodInfoButton.setDisable(true);
            } else {
                updateDeliveryMethodInfoButton.setDisable(false);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
            }
        } else {
            if (placeRushOrderValue.isSelected()) {
                errorProvince.setVisible(false);
                deliveryInstruction.setDisable(false);
                shipmentDetail.setDisable(false);
                deliveryTime.setDisable(false);
                updateDeliveryMethodInfoButton.setDisable(false);
            } else {
                updateDeliveryMethodInfoButton.setDisable(false);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
                errorProvince.setVisible(false);
            }
        }
    }

    /**
     * @return PlaceOrderController
     */
    public PlaceOrderController getBController() {
        return (PlaceOrderController) super.getBController();
    }
}
