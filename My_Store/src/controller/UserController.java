package controller;

import common.exception.UserNotExistsException;
import entity.db.AIMSDB;
import entity.media.Media;
import entity.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController extends BaseController {
    public List getAllUser() throws SQLException {
        return new User().getAllUser();
    }

    public ArrayList<String> validateUserInfo(String userName, String email, String address, String phone, String password) {
        var errorMsg = new ArrayList<String>();

        if (userName.trim().isEmpty()) errorMsg.add("User name is required");
        if (email.trim().isEmpty()) errorMsg.add("Email is required");
        if (address.trim().isEmpty()) errorMsg.add("Address is required");
        if (phone.trim().isEmpty()) errorMsg.add("Phone is required");
        if (password.trim().isEmpty()) errorMsg.add("Password is required");
        if (!phone.trim().isEmpty() && (phone.length() != 10 || !phone.matches("\\d+"))) errorMsg.add("Invalid phone");
        if (!email.trim().isEmpty() && !email.matches( "^(.+)@(.+)$")) errorMsg.add("Invalid email");

        return errorMsg;
    }
}
