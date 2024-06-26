package views.screen.cart;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.ViewCartController;
import entity.cart.CartMedia;
import entity.order.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CartScreenHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());
    @FXML
    VBox vboxCart;
    @FXML
    private ImageView aimsImage;
    @FXML
    private Label pageTitle;
    @FXML
    private Label shippingFees;

    @FXML
    private Label labelAmount;

    @FXML
    private Label labelSubtotal;

    @FXML
    private Label labelVAT;

    @FXML
    private Button btnPlaceOrder;

    private MediaHandler mediaHandler;

    private List<CartMedia> selectedCartMediaList;

    public CartScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);

        mediaHandler = new MediaHandler(Configs.CART_MEDIA_PATH, this);

        // fix relative image path caused by fxml
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        // on mouse clicked, we start processing place order usecase
        btnPlaceOrder.setOnMouseClicked(e -> {

            try {
                requestToPlaceOrder();
            } catch (SQLException | IOException exp) {

                exp.printStackTrace();
                throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
            }

        });
    }


    /**
     * @return Label
     */
    public Label getLabelAmount() {
        return labelAmount;
    }


    /**
     * @return Label
     */
    public Label getLabelSubtotal() {
        return labelSubtotal;
    }


    /**
     * @return ViewCartController
     */
    public ViewCartController getBController() {
        return (ViewCartController) super.getBController();
    }


    /**
     * @param prevScreen
     * @throws SQLException
     */
    public void requestToViewCart(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Cart Screen");
        getBController().checkAvailabilityOfProduct();
        displayCartWithMediaAvailability();
        show();
    }

    /**
     * @throws SQLException
     * @throws IOException
     */

    public void requestToPlaceOrder() throws SQLException, IOException {
        try {
            // create placeOrderController and process the order
            var placeOrderController = new PlaceOrderController();

            boolean hasSelectedProduct = false;

            for (CartMedia cartMedia : (List<CartMedia>) placeOrderController.getListCartMedia()) {
                if (cartMedia.isSelected()) {
                    hasSelectedProduct = true;
                    break;
                }
            }


            if (!hasSelectedProduct) {
                PopupScreen.error("You don't have anything selected to place");
                return;
            }

            placeOrderController.placeOrder();

            // display available media
            // displayCartWithMediaAvailability();

            // create order
            Order order = placeOrderController.createOrder();

            // display shipping form
            ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);

//            ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order,this, mediaHandler.getSelectedCartMediaList());
            ShippingScreenHandler.setPreviousScreen(this);
            ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
            ShippingScreenHandler.setScreenTitle("Shipping Screen");
            ShippingScreenHandler.setBController(placeOrderController);
            ShippingScreenHandler.show();

        } catch (MediaNotAvailableException e) {
            // if some media are not available then display cart and break usecase Place Order
            displayCartWithMediaAvailability();
        }
    }


    /**
     * @throws SQLException
     */
    public void updateCart() throws SQLException {
        getBController().checkAvailabilityOfProduct();
        displayCartWithMediaAvailability();
    }

//    void updateCartAmount() {
//
//        // calculate subtotal and amount
//        int subtotal = getBController().getCartSubtotal();
//        int vat = (int) ((Configs.PERCENT_VAT/100) * subtotal);
//        List lstMedia = getBController().getListCartMedia();
//        for (Object cm : lstMedia) {
//            CartMedia cartMedia = (CartMedia) cm;
//            if (cartMedia.isSelected()) {
//                subtotal += cartMedia.getQuantity() * cartMedia.getPrice();
//            }
//        }
//
//
//        int amount = subtotal + vat;
//
//        labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
//        labelVAT.setText(Utils.getCurrencyFormat(vat));
//        labelAmount.setText(Utils.getCurrencyFormat(amount));
//    }

    void updateCartAmount() {
        int subtotal = 0;
        List lstMedia = getBController().getListCartMedia();
        for (Object cm : lstMedia) {
            CartMedia cartMedia = (CartMedia) cm;
            if (cartMedia.isSelected()) {
                subtotal += cartMedia.getQuantity() * cartMedia.getPrice();
            }
        }

        int vat = (int) ((Configs.PERCENT_VAT / 100) * subtotal);
        int amount = subtotal + vat;

        labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
        labelVAT.setText(Utils.getCurrencyFormat(vat));
        labelAmount.setText(Utils.getCurrencyFormat(amount));
    }

    private void displayCartWithMediaAvailability() {
        // clear all old cartMedia
        vboxCart.getChildren().clear();

        // get list media of cart after check availability
        List lstMedia = getBController().getListCartMedia();

        try {
            for (Object cm : lstMedia) {

                // display the attribute of vboxCart media
                CartMedia cartMedia = (CartMedia) cm;
                MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
                mediaCartScreen.setCartMedia(cartMedia);

                // add spinner
                vboxCart.getChildren().add(mediaCartScreen.getContent());
            }
            // calculate subtotal and amount
            updateCartAmount();
            LOGGER.info("updateCartAmount called"); // Thêm dòng này để kiểm tra
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() throws IOException {
        // Khởi tạo MediaHandler một lần và sử dụng nó ở đây
//        mediaHandler = new MediaHandler(Configs.CART_MEDIA_PATH, this);
    }

    public MediaHandler getMediaHandler() {
        return mediaHandler;
    }

    public List<CartMedia> getSelectedCartMediaList() {
        LOGGER.info("Selected Cart Media List Size (in CartScreenHandler): " + selectedCartMediaList.size());
        return selectedCartMediaList;
    }

}