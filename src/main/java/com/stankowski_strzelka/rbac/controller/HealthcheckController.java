package com.stankowski_strzelka.rbac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthcheck() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
