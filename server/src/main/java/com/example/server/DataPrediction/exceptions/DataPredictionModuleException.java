package com.example.server.DataPrediction.exceptions;

public abstract class DataPredictionModuleException extends RuntimeException {
    public DataPredictionModuleException(String message) {
        super(message);
    }

    public DataPredictionModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPredictionModuleException(Throwable cause) {
        super(cause);
    }
}
