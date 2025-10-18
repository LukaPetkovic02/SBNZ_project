
package com.ftn.sbnz.backward.service.controllers;

import com.ftn.sbnz.backward.service.services.BackwardAggressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backward")
public class BackwardChainingController {

    private final BackwardAggressionService backwardService;

    @Autowired
    public BackwardChainingController(BackwardAggressionService backwardService) {
        this.backwardService = backwardService;
    }

    @GetMapping("/test")
    public String testBackwardAggression() {
        backwardService.testBackwardAggression();
        return "Backward reasoning test executed — check console output for results.";
    }
}
