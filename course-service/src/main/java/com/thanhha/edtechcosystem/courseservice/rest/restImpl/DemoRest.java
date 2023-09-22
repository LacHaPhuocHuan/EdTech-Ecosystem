package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses/demo")
public class DemoRest {
    @GetMapping("")
    public ResponseEntity<?> goDemo(){
        return ResponseEntity.ok("\"message\""+":"+"\"Demo\"");
    }

}
