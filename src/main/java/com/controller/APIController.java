package com.controller;

import com.entity.Rahodong;
import com.repository.RahodongRepository;
import com.service.RahodongService;
import command.SimpleSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
