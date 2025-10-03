package com.ftn.sbnz.backward.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.backward.service.services.ActivateTemplateRulesService;

@RestController
@RequestMapping("/template-example")
@CrossOrigin(origins = "http://localhost:4200")
public class ActivateTemplateRulesController {
  private ActivateTemplateRulesService backService;

  @Autowired
  public ActivateTemplateRulesController(ActivateTemplateRulesService backService) {
    this.backService = backService;
  }

  @GetMapping("")
  public String fireAllRules(
          @RequestParam String card1Rank,
          @RequestParam String card1Suit,
          @RequestParam String card2Rank,
          @RequestParam String card2Suit
  ) {
    String ret = backService.fireRules(card1Rank, card1Suit, card2Rank, card2Suit);
    return ret;
  }
}
