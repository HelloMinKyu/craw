package com.controller;

import com.repository.RahodongRepository;
import com.service.RahodongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API")
public class APIController {
    @Autowired
    private RahodongService rahodongService;

    @Autowired
    private RahodongRepository rahodongRepository;

    @GetMapping("/find/{type}/{page}")
    public ResponseEntity<Object> findType(@PathVariable String type, @PathVariable int page) {
        return ResponseEntity.ok(rahodongService.typeFind(page, 10, type));
    }
}
