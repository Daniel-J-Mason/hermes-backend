package com.allthing.hermesbackend.application.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    
    private final String value;
    
    public Email(String value) {
        if (!isValidEmail(value)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.value = value;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
