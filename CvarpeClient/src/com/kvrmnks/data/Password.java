package com.kvrmnks.data;

import com.kvrmnks.exception.PasswordException;
import com.kvrmnks.exception.PasswordTooFewException;
import com.kvrmnks.exception.PasswordTooWeakException;

public class Password {
    public static boolean isProper(String password) throws PasswordTooFewException, PasswordTooWeakException {
        if (password == null) throw new PasswordTooFewException();
        if (password.length() < 6) throw new PasswordTooFewException();
        boolean hasNumber = false, hasAlpha = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= '0' && c <= '9') hasNumber = true;
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) hasAlpha = true;
        }
        if ((!hasNumber) || (!hasAlpha)) throw new PasswordTooWeakException();
        return true;
    }
}
