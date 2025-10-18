package com.ftn.sbnz.backward.service.controllers;

import com.ftn.sbnz.backward.service.services.ForwardChainingDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forward")
public class ForwardChainingController {

    private final ForwardChainingDecisionService forwardService;

    @Autowired
    public ForwardChainingController(ForwardChainingDecisionService forwardService) {
        this.forwardService = forwardService;
    }

    @GetMapping("/test")
    public String testForwardChaining() {
        forwardService.testForwardDecision();
        return "Forward chaining test executed — check console output for results.";
    }
}
