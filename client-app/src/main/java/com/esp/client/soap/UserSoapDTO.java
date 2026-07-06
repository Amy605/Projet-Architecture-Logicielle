package com.esp.client.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserSoapDTO {
    public Long id;
    public String username;
    public String role;

    public UserSoapDTO() {}
}
