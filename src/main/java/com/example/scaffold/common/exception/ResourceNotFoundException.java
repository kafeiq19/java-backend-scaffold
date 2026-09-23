package com.example.scaffold.common.exception;

import com.example.scaffold.common.api.ResultCode;

public class ResourceNotFoundException extends BizException {

    public ResourceNotFoundException(String resource, Object id) {
        super(ResultCode.NOT_FOUND, resource + " not found: " + id);
    }
}
