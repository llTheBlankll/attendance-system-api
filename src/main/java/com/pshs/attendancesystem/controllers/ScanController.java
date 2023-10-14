package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Scan;
import com.pshs.attendancesystem.repositories.ScanRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/scan")
public class ScanController {

    private final ScanRepository scanRepository;

    public ScanController(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    @GetMapping("/")
    public Iterable<Scan> getAllScan() {
        return this.scanRepository.findAll();
    }

    @GetMapping("/get/hash/{hash}")
    public Scan getStudentByLrnHash(@PathVariable String hash) {
        Scan hashed = this.scanRepository.findByHashedLrn(hash);
        return (hashed != null) ? hashed : new Scan();
    }

    @GetMapping("/get/lrn/{lrn}")
    public Optional<Scan> getStudentByLrn(@PathVariable Long lrn) {
        return (this.scanRepository.existsById(lrn)) ? this.scanRepository.findById(lrn) : Optional.empty();
    }
}
