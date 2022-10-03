package org.neat0n.licensingservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;

@RestController
@RequestMapping("jvm/version")
public class JVMVersionController{
    @GetMapping
    ResponseEntity<String> JVMVersion(){
        return ResponseEntity.ok(System.getProperty("java.version"));
    }
}
