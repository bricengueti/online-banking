package com.onlinebank.common.exception;

import org.springframework.http.HttpStatus;

import com.onlinebank.common.config.exception.BusinessException;
/**
 * Exception thrown when a requested resource is not found.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public class ResourceNotFoundException extends BusinessException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(
                String.format("%s not found with id: %s", resourceType, resourceId),
                ERROR_CODE,
                STATUS
        );
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String resourceType, String fieldName, String fieldValue) {
        super(
                String.format("%s not found with %s: %s", resourceType, fieldName, fieldValue),
                ERROR_CODE,
                STATUS
        );
        this.resourceType = resourceType;
        this.resourceId = fieldValue;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}