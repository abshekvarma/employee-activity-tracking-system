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

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping(value = "/")
public class Controller {

    @SuppressWarnings("unused")
    @Autowired
    private IService service;

    @SuppressWarnings("unused")
    @GetMapping(value = "get")
    public ResponseEntity occurrences() {
        Response response = service.processResponse();
        if (!response.getAll_employees_last_7_days_statistics().isEmpty() && !response.getTodays_activities().isEmpty()) {
            log.info("Response: " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Invalid Response");
            return new ResponseEntity<>("Invalid Response", HttpStatus.NO_CONTENT);
        }
    }
}
