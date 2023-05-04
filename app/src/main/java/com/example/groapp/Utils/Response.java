package com.example.groapp.Utils;

public class Response {
    private String id;
    private Boolean result;
    private String message;

    public Response(String id, Boolean result, String message){
        this.id = id;
        this.result = result;
        this.message = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getResult() {
        return result;
    }
}
