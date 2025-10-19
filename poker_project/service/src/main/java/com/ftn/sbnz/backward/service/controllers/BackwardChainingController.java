
package com.ftn.sbnz.backward.service.controllers;

import com.ftn.sbnz.backward.service.services.BackwardAggressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backward")
@CrossOrigin(origins = "http://localhost:4200")
public class BackwardChainingController {

    private final BackwardAggressionService backwardService;

    @Autowired
    public BackwardChainingController(BackwardAggressionService backwardService) {
        this.backwardService = backwardService;
    }

    @GetMapping("/test")
    public String testBackwardAggression() {
        return backwardService.testBackwardAggression();
    }

    @GetMapping("/simulate-round")
    public String simulateRound() {
        // poziv servisa koji simulira jednu rundu i vraća string
        return backwardService.simulateRound();
    }
}
