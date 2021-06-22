package com.rest.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = {"Swagger Test"})  // swagger 설정
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {

    @ApiOperation(value = "Test", notes = "테스트용...")
    @GetMapping(value = "/test")
    public ResponseEntity<?> test() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("id", "test_id");
        _map.put("name", "test_name");

        return new ResponseEntity<Map>(_map, HttpStatus.OK);
    }
}
