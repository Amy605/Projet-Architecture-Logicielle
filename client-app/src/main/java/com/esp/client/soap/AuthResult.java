package com.esp.client.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class AuthResult {
    public boolean success;
    public String role;
    public String message;

    public AuthResult() {}
}
