package com.thanhha.edtechcosystem.userservice.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidatedUtils {
    private static final String REGEX_PASSWORD="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String REGEX_EMAIL="^[A-Za-z0-9+_.-]+@(.+)$";

    public boolean validPassword(String password){
        Pattern pattern= Pattern.compile(REGEX_PASSWORD);
        Matcher matcher= pattern.matcher(password);
        return matcher.matches();
    }
    public boolean validEmail(String email){
        Pattern pattern= Pattern.compile(REGEX_EMAIL);
        Matcher matcher= pattern.matcher(email);
        return matcher.matches();
    }
}
