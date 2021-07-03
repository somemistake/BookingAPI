package com.foxminded.booking.security;

public class SecurityConstants {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String isAdmin = "hasRole('" + ROLE_ADMIN + "')";
    public static final String isUser = "hasRole('" + ROLE_USER + "')";
    ;
    public static final String isAuthenticated = isAdmin + " or " + isUser;
}
