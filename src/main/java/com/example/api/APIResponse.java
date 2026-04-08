package com.example.api;

public class APIResponse<T> {

    private String status;
    private String message;
    private T data;

    public APIResponse() {}

    public APIResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse<>("success", message, data);
    }

    public static <T> APIResponse<T> success(String message) {
        return new APIResponse<>("success", message, null);
    }

    public static <T> APIResponse<T> error(String message) {
        return new APIResponse<>("error", message, null);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}