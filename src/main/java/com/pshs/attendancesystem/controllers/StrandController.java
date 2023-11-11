package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.services.StrandService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/strand")
public class StrandController {

    private final StrandService strandService;

    public StrandController(StrandService strandService) {
        this.strandService = strandService;
    }

    @GetMapping("/all")
    public Iterable<Strand> getAllStrand() {
        return this.strandService.getAllStrand();
    }
}
