package views.screen.user;

import common.exception.UserNotExistsException;
import controller.UserController;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class UserFormScreenHandler extends BaseScreenHandler implements Initializable {
    @FXML
    Button backToUserScreen;

    @FXML
    Button addNewUser;

    @FXML
    TextField userName;

    @FXML
    TextField email;

    @FXML
    TextField address;

    @FXML
    TextField phone;

    @FXML
    TextField password;

//    @FXML
//    RadioButton isAdmin;

    private boolean isEditUser;
    private User currentUser;
    private UserController userController;

    public UserFormScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        this.isEditUser = false;
        userController = new UserController();
        addNewUser.setText("Add");
    }

    public UserFormScreenHandler(Stage stage, String screenPath, User user) throws IOException {
        super(stage, screenPath);
        this.isEditUser = true;
        userController = new UserController();
        addNewUser.setText("Update");
        this.currentUser = user;

        userName.setText(user.getName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
        password.setText(user.getPassword());
//        isAdmin.setSelected(user.isAdmin());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backToUserScreen.setOnMouseClicked(e -> {
           backToUserScreen();
        });

        addNewUser.setOnMouseClicked(e -> {
            try {
                var errorMsg = validateUserInfo();
                if (errorMsg.isEmpty()) {
                    var user = new User(0, userName.getText(), email.getText(), address.getText(), phone.getText(), password.getText(), false);
                    if (isEditUser) {
                        user.setId(currentUser.getId());
                        user.editUser();
                    } else {
                        user.addUser();
                    }
                    backToUserScreen();
                } else {
                    StringJoiner stringJoiner = new StringJoiner(", ");
                    for (String str : errorMsg) {
                        stringJoiner.add(str);
                    }
                    String joinedString = stringJoiner.toString();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, joinedString);
                    ButtonType yesButton = new ButtonType("Close", ButtonBar.ButtonData.YES);
                    alert.getButtonTypes().setAll(yesButton);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        alert.close();
                    } else {
                        alert.close();
                    }
                }
            } catch (SQLException | UserNotExistsException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void backToUserScreen() {
        UserScreenHandler userScreenHandler;
        try {
            userScreenHandler = new UserScreenHandler(stage, Configs.USER_SCREEN_PATH);
            userScreenHandler.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ArrayList<String> validateUserInfo() {
        var errorMsg = new ArrayList<String>();
        errorMsg = userController.validateUserInfo(userName.getText(), email.getText(), address.getText(), phone.getText(), password.getText());
        return errorMsg;
    }
}
