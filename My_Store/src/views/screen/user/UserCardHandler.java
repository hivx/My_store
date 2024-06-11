package views.screen.user;

import common.exception.UserNotExistsException;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.FXMLScreenHandler;

import java.io.IOException;
import java.sql.SQLException;

public class UserCardHandler extends FXMLScreenHandler {
    @FXML
    Text role;
    @FXML
    Text userName;
    @FXML
    Text email;
    @FXML
    Text address;
    @FXML
    Text phone;
    @FXML
    Button editUserBtn;
    @FXML
    Button deleteUserBtn;

    private User user;

    public UserCardHandler(Stage stage, String screenPath, User user) throws IOException {
        super(screenPath);
        this.user = user;

        editUserBtn.setOnMouseClicked(e -> {
            UserFormScreenHandler userPopupScreenHandler;
            try {
                userPopupScreenHandler = new UserFormScreenHandler(stage, Configs.USER_FORM_PATH, user);
                userPopupScreenHandler.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteUserBtn.setOnMouseClicked(e -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete user: " + this.user.getName() + "?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Delete");
                alert.setHeaderText(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                   this.user.deleteUser();
                    var userScreenHandler = new UserScreenHandler(stage, Configs.USER_SCREEN_PATH);
                    userScreenHandler.show();
                    alert.close();
                } else {
                    alert.close();
                }
            } catch (SQLException | UserNotExistsException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setUserInfo();
    }

    private void setUserInfo() {
        role.setText("Role: " + (this.user.isAdmin() ? "Admin" : "User"));
        userName.setText("Name: " + this.user.getName());
        email.setText("Email: " + this.user.getEmail());
        address.setText("Address: " + this.user.getAddress());
        phone.setText("Phone: " + this.user.getPhone());
    }
}
