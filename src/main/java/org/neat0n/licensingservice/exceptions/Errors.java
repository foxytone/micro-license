package org.neat0n.licensingservice.exceptions;

import lombok.Getter;

@Getter
public enum Errors {
    NO_LICENSE_FOUND(101);
    
    Errors(int code) {
    
    }
}
