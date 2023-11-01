package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.services.RfidService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rfid")
public class RfidCredentialsController {

    private final RfidService rfidService;

    public RfidCredentialsController(RfidService rfidService) {
        this.rfidService = rfidService;
    }

    /**
     * Retrieves all the scans.
     *
     * @return an iterable collection of Scan objects representing all the scans
     */
    @GetMapping("/credentials")
    public Iterable<RfidCredentials> getAllScan() {
        return this.rfidService.getAllRfidCredentials();
    }

    /**
     * Retrieves an RfidCredentials object based on the provided type and data.
     *
     * @param  type  the type of data to search for (hash or studentLrn)
     * @param  data  the data to search for (hashed value or studentLrn)
     * @return the retrieved RfidCredentials object
     */
    @GetMapping("/get")
    public RfidCredentials getStudent(@RequestParam String type, @RequestParam String data) {
        if (type.equals("hash")) {
            return this.rfidService.getRfidCredentialByHashedLrn(data);
        } else {
            return this.rfidService.getRfidCredentialByStudentLrn(Long.parseLong(data));
        }
    }
}
