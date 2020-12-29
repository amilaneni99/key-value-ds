package com.abhinav.keyvalueds.shared.utils;

public enum ErrorMessages {
    KEY_ALREADY_EXISTS("Key already exists"),
    RECORD_NOT_FOUND("Record Not Found"),
    KEY_EXPIRED("The Key has expired."),
    INVALID_KEY("INVALID KEY. Key must be a String of max. 32 chars."),
    VALUE_SIZE_EXCEEDED("MAX VALUE SIZE EXCEEDED. The max size of value is 16kB"),
    INVALID_PATH("INVALID URI"),
    FILE_SIZE_EXCEEDED("MAX FILE SIZE EXCEEDED. The max size of data file is 1GB");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
