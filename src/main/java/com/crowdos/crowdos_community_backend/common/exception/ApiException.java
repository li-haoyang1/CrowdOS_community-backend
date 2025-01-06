package com.crowdos.crowdos_community_backend.common.exception;

import com.crowdos.crowdos_community_backend.common.api.IErrorCode;

public class ApiException extends RuntimeException {
    private IErrorCode errorCode;
    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ApiException(String message) {
        super(message);
    }
    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
