package org.neat0n.licensingservice.exceptions;

import lombok.Getter;

@Getter
public enum Errors {
    NO_LICENSE_FOUND(101),
    EMPTY_LICENSE_UUID(102),
    EMPTY_ORGANIZATION_ID(103),
    INVALID_LICENSE_TYPE_LENGTH(104),
    INVALID_DESCRIPTION_LENGTH(105),
    INVALID_PRODUCT_NAME_LENGTH(106),
    INVALID_COMMENT_LENGTH(107), LICENSE_ALREADY_EXIST(108);
    
    @Getter
    private final int id;
    
    Errors(int code) {
        this.id = code;
    }
}
