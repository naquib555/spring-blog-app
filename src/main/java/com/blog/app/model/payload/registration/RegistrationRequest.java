package com.blog.app.model.payload.registration;

import com.blog.app.model.UserInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class RegistrationRequest extends UserInfo {

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
