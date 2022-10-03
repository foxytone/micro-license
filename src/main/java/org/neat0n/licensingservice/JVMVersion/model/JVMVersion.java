package org.neat0n.licensingservice.JVMVersion.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JVMVersion {
    private final String name = System.getProperty("java.vm.name");
    private final String home = System.getProperty("java.home");
    private final String vendor = System.getProperty("java.vendor");
    private final String version = System.getProperty("java.version");
    private final String specVendor = System.getProperty("java.specification.vendor");
}
