package com.ftn.sbnz.backward.service.controllers;

import com.ftn.sbnz.backward.service.services.BluffCepService;
import com.ftn.sbnz.backward.service.services.TiltCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CepController {
    private final BluffCepService bluffCepService;
    private final TiltCepService tiltCepService;

    @Autowired
    public CepController(BluffCepService bluffCepService, TiltCepService tiltCepService) {
        this.bluffCepService = bluffCepService;
        this.tiltCepService = tiltCepService;
    }


    @GetMapping("/bluff-detect")
    public void detectBluffs(){
        try{
            bluffCepService.detectBluffs();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @GetMapping("/tilt-detect")
    public void detectTilt() {
        try {
            tiltCepService.detectTilt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
