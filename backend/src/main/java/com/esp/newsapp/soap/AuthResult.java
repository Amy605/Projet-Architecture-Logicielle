package com.esp.newsapp.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class AuthResult {
    public boolean success;
    public String role;
    public String message;

    public AuthResult() {}

    public AuthResult(boolean success, String role, String message) {
        this.success = success;
        this.role = role;
        this.message = message;
    }
}
