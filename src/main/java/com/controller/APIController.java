package com.controller;

import com.repository.GahodongRepository;
import com.service.GahodongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/API")
public class APIController {
    @Autowired
    private GahodongService rahodongService;

    @Autowired
    private GahodongRepository rahodongRepository;

    @GetMapping("/find/{type}/{page}")
    public ResponseEntity<Object> findType(@PathVariable String type, @PathVariable int page, HttpServletRequest request) {
        final int size = Integer.parseInt(request.getParameter("size"));
        return ResponseEntity.ok(rahodongService.typeFind(page, size, type));
    }
}
