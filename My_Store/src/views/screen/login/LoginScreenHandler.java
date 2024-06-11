package views.screen.login;

import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.user.UserScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginScreenHandler extends BaseScreenHandler implements Initializable {
    @FXML
    TextField emailLogin;

    @FXML
    TextField passwordLogin;

    @FXML
    RadioButton isAdminLogin;

    @FXML
    Button signInBtn;

    public LoginScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signInBtn.setOnMouseClicked(e -> {
            if (emailLogin.getText().isEmpty() || passwordLogin.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Email and password are required!");
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
            } else {
                User user = null;
                try {
                    user = new User().login(emailLogin.getText(), passwordLogin.getText(), isAdminLogin.isSelected());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (user != null) {
                    if (user.isAdmin()) {
                        UserScreenHandler userHandler = null;
                        try {
                            userHandler = new UserScreenHandler(stage, Configs.USER_SCREEN_PATH);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        userHandler.setScreenTitle("Admin Screen");
                        userHandler.show();
                    } else {
                        HomeScreenHandler homeHandler = null;
                        try {
                            homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        homeHandler.setScreenTitle("Home Screen");
                        homeHandler.setImage();
                        homeHandler.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Invalid email or password!");
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
            }
        });
    }
}
