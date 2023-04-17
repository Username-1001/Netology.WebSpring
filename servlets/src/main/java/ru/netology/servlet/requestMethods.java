package ru.netology.servlet;

public enum requestMethods {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private String method;
    private requestMethods(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
