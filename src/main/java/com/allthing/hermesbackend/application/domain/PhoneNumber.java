package com.allthing.hermesbackend.application.domain;

import java.util.Objects;

public class PhoneNumber {
    
    private final String value;
    
    public PhoneNumber(String value) {
        if (!isValidPhoneNumber(value)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.value = value;
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            return false;
        return phoneNumber.matches("\\d{10}");
    }
    
    public String toFormattedString() {
        return "(" + value.substring(0, 3) + ") " + value.substring(3, 6) + "-" + value.substring(6);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
