package com.ftn.sbnz.backward.service.controllers;

import com.ftn.sbnz.backward.service.services.BluffCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CepController {
    private final BluffCepService bluffCepService;

    @Autowired
    public CepController(BluffCepService bluffCepService) {
        this.bluffCepService = bluffCepService;
    }

    @GetMapping("/bluff-detect")
    public void detectBluffs(){
        try{
            bluffCepService.detectBluffs();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
