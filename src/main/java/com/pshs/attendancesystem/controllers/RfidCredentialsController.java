package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rfid")
public class RfidCredentialsController {

    private final RfidCredentialsRepository rfidCredentialsRepository;

    public RfidCredentialsController(RfidCredentialsRepository rfidCredentialsRepository) {
        this.rfidCredentialsRepository = rfidCredentialsRepository;
    }

    /**
     * Retrieves all the scans.
     *
     * @return an iterable collection of Scan objects representing all the scans
     */
    @GetMapping("/credentials")
    public Iterable<RfidCredentials> getAllScan() {
        return this.rfidCredentialsRepository.findAll();
    }

    /**
     * Retrieves a student scan by their hashed learning resource network (LRN) value.
     *
     * @param hash the hashed LRN value used to search for the student scan
     * @return the student scan if found, otherwise an empty scan object
     */
    @GetMapping("/get/hash/{hash}")
    public RfidCredentials getStudentByLrnHash(@PathVariable String hash) {
        RfidCredentials hashed = this.rfidCredentialsRepository.findByHashedLrn(hash);
        return (hashed != null) ? hashed : new RfidCredentials();
    }

    /**
     * Retrieves a student scan by their LRN.
     *
     * @param lrn the LRN of the student
     * @return an optional containing the student scan if it exists, otherwise an empty optional
     */
    @GetMapping("/get/lrn/{lrn}")
    public Optional<RfidCredentials> getStudentByLrn(@PathVariable Long lrn) {
        return (this.rfidCredentialsRepository.existsById(lrn)) ? this.rfidCredentialsRepository.findById(lrn) : Optional.empty();
    }
}
