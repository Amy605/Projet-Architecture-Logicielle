package com.esp.newsapp.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserSoapDTO {
    public Long id;
    public String username;
    public String role;

    public UserSoapDTO() {}

    public UserSoapDTO(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
