package com.example.Army.controller;
import com.example.Army.model.Cadet;
import com.example.Army.repository.CadetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CadetController {

    @Autowired
    private CadetRepository repository;

    @GetMapping("/")
    public String getAllCadet(Model model){
        List<Cadet> cadets = repository.findAll();
        model.addAttribute("cadets", cadets);
        return "index";
    }

    @PostMapping(value = "/add")
    public String saveProduct(@ModelAttribute("cadet") Cadet cadet) {
        repository.save(cadet);
        return "redirect:/";
    }
}
