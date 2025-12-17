package com.jhaadarsh.digital_library.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourceNotFoundException
 *
 * Role:
 * -----
 * Domain-level exception indicating a missing resource.
 *
 * Why custom exception:
 * - Meaningful error semantics
 * - Centralized logging
 * - Easy to map to HTTP later using @ControllerAdvice
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> cla,
                                     String fieldName,
                                     String fieldValue) {
        super("Resource of type " + cla.getSimpleName()
                + " with " + fieldName + " = " + fieldValue + " not found");

        log.error("Resource not found: {} {}={}",
                cla.getSimpleName(), fieldName, fieldValue);
    }
}
