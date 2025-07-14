package com.otterdev.domain.valueObject.helper.response;

public class OtterResponse<T> {
    private boolean success;
    private String message;
    private T data;        // success data
    private String error;  // error code or message

    // Constructors for success
    public OtterResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    // Constructor for error
    public OtterResponse(String error, String message) {
        this.success = false;
        this.error = error;
        this.message = message;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
