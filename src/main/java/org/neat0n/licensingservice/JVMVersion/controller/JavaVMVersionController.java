package org.neat0n.licensingservice.JVMVersion.controller;

import org.neat0n.licensingservice.JVMVersion.model.JVMVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jvm")
public class JavaVMVersionController {
    @GetMapping
    ResponseEntity<JVMVersion> jvmversion() {
        return ResponseEntity.ok(new JVMVersion());
    }
}
