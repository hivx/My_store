import controller.UserController;
import org.junit.*;

import java.io.IOException;

public class UserInfoTests {
    private UserController userController;

    @Before
    public void setup() throws IOException {
        userController = new UserController();
    }

    @Test
    public void validateUserInfo_infoValid() {
        var userName = "admin";
        var email = "admin@gmail.com";
        var address = "Ha Noi";
        var phone = "0123456789";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertTrue(errorMsg.isEmpty());
    }

    @Test
    public void validateUserInfo_userNameIsEmpty() {
        var userName = "";
        var email = "admin@gmail.com";
        var address = "Ha Noi";
        var phone = "0123456789";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "User name is required");
    }

    @Test
    public void validateUserInfo_emailIsEmpty() {
        var userName = "admin";
        var email = "";
        var address = "Ha Noi";
        var phone = "0123456789";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Email is required");
    }

    @Test
    public void validateUserInfo_addressNameIsEmpty() {
        var userName = "admin";
        var email = "admin@gmail.com";
        var address = "";
        var phone = "0123456789";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Address is required");
    }

    @Test
    public void validateUserInfo_phoneNameIsEmpty() {
        var userName = "admin";
        var email = "admin@gmail.com";
        var address = "Ha Noi";
        var phone = "";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Phone is required");
    }

    @Test
    public void validateUserInfo_passwordNameIsEmpty() {
        var userName = "admin";
        var email = "admin@gmail.com";
        var address = "Ha Noi";
        var phone = "0123456789";
        var password = "";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Password is required");
    }

    @Test
    public void validateUserInfo_invalidPhone() {
        var userName = "admin";
        var email = "admin@gmail.com";
        var address = "Ha Noi";
        var phone = "012345679";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Invalid phone");
    }

    @Test
    public void validateUserInfo_invalidEmail() {
        var userName = "admin";
        var email = "admingmailcom";
        var address = "Ha Noi";
        var phone = "0123456789";
        var password = "123456";

        var errorMsg = userController.validateUserInfo(userName, email, address, phone, password);
        Assert.assertEquals(errorMsg.get(0), "Invalid email");
    }
}
