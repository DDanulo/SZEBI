package com.example.server.DataPrediction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ModelNotTrainedException extends DataPredictionModuleException {
    public ModelNotTrainedException(String message) {
        super(message);
    }

    public ModelNotTrainedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelNotTrainedException(Throwable cause) {
        super(cause);
    }
}
