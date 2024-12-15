package com.LMS.LMS.Controllers.ControllerParams;

import org.springframework.lang.NonNull;

public class RegisterParams {
    @NonNull
    public String name;
    @NonNull
    public String email;
    @NonNull
    public String password;
    @NonNull
    public String role;
}
