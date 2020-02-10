package com.controller;

import com.domain.Response;
import com.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class Controller {

    private IService service;

    @Autowired
    public Controller(IService service) {
        this.service = service;
    }

    @GetMapping(value = "get")
    public ResponseEntity occurrences() {
        service.readAllFiles();
        Response response = service.processResponse();
        if (response != null) {
            log.info("Response: "+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Invalid Response");
            return new ResponseEntity<>("Invalid Response", HttpStatus.NO_CONTENT);
        }
    }
}
