package views.screen.user;

import controller.UserController;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserScreenHandler extends BaseScreenHandler implements Initializable {

    @FXML
    Button newUser;

    @FXML
    HBox hBoxUser;

    private List userCards;
    private UserController userController;

    public UserScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        try {
            var users = userController.getAllUser();
            userCards = new ArrayList();
            for (Object object : users) {
                var user = (User) object;
                if (!user.isAdmin()) {
                    var userCard = new UserCardHandler(stage, Configs.USER_CARD, user);
                    userCards.add(userCard);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        showUserToScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBController(new UserController());
        userController = new UserController();

        newUser.setOnMouseClicked(e -> {
            UserFormScreenHandler userPopupScreenHandler;
            try {
                userPopupScreenHandler = new UserFormScreenHandler(stage, Configs.USER_FORM_PATH);
                userPopupScreenHandler.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void showUserToScreen() {
        var usersClone = (ArrayList) ((ArrayList) userCards).clone();

        hBoxUser.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });

        while (!usersClone.isEmpty()) {
            hBoxUser.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < 3 && !usersClone.isEmpty()) {
                    UserCardHandler user = (UserCardHandler) usersClone.get(0);
                    vBox.getChildren().add(user.getContent());
                    usersClone.remove(user);
                }
            });
            return;
        }
    }
}
