package com.onlinebank.common.exception;

import org.springframework.http.HttpStatus;
import com.onlinebank.common.config.exception.BusinessException;

/**
 * Exception thrown when trying to create a resource that already exists.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public class DuplicateResourceException extends BusinessException {

    private static final String ERROR_CODE = "DUPLICATE_RESOURCE";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private final String resourceType;
    private final String fieldName;
    private final String fieldValue;

    public DuplicateResourceException(String resourceType, String fieldName, String fieldValue) {
        super(
                String.format("%s already exists with %s: %s", resourceType, fieldName, fieldValue),
                ERROR_CODE,
                STATUS
        );
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}