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

    /**
     * Retrieves all the scans.
     *
     * @return an iterable collection of Scan objects representing all the scans
     */
    @GetMapping("/")
    public Iterable<Scan> getAllScan() {
        return this.scanRepository.findAll();
    }

    /**
     * Retrieves a student scan by their hashed learning resource network (LRN) value.
     *
     * @param  hash  the hashed LRN value used to search for the student scan
     * @return       the student scan if found, otherwise an empty scan object
     */
    @GetMapping("/get/hash/{hash}")
    public Scan getStudentByLrnHash(@PathVariable String hash) {
        Scan hashed = this.scanRepository.findByHashedLrn(hash);
        return (hashed != null) ? hashed : new Scan();
    }

    /**
     * Retrieves a student scan by their LRN.
     *
     * @param  lrn  the LRN of the student
     * @return      an optional containing the student scan if it exists, otherwise an empty optional
     */
    @GetMapping("/get/lrn/{lrn}")
    public Optional<Scan> getStudentByLrn(@PathVariable Long lrn) {
        return (this.scanRepository.existsById(lrn)) ? this.scanRepository.findById(lrn) : Optional.empty();
    }
}
