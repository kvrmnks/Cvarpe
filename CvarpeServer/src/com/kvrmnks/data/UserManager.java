package com.kvrmnks.data;

import com.kvrmnks.exception.NoUserException;
import com.kvrmnks.exception.UserExistedException;

import java.io.*;
import java.sql.SQLException;

public class UserManager implements Serializable {


    public UserManager() {
    }

    public static String[] getUserNameAndPassword(String str) {
        return str.split("\\$");
    }

    public static void addUser(String userName, String password) throws UserExistedException, IOException {
        DataBase.addUser(new User(userName, password,MyDate.getRealTimeStamp()));
    }

    public static void modifyPassword(String userName, String newPassword) throws NoUserException {
        DataBase.modifyUserPassword(userName, newPassword);
    }

    public static void deleteUser(String name) throws NoUserException {
        DataBase.deleteUser(name);
    }

    public static boolean checkUser(String s, String s1) {
        try {
            return DataBase.getPassword(s).equals(s1);
        } catch (NoUserException e) {
            return false;
        }
    }
}
