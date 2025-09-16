package com.ftn.sbnz.backward.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sbnz.backward.service.services.ActivateTemplateRulesService;

@RestController
@RequestMapping("/template-example")
public class ActivateTemplateRulesController {
  private ActivateTemplateRulesService backService;

  @Autowired
  public ActivateTemplateRulesController(ActivateTemplateRulesService backService) {
    this.backService = backService;
  }

  @GetMapping("")
  public void fireAllRules() {
    backService.fireRules();
  }
}
