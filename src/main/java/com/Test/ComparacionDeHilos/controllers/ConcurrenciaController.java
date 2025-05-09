package com.Test.ComparacionDeHilos.controllers;

import com.Test.ComparacionDeHilos.services.ConcurrenciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrenciaController {

    private final ConcurrenciaService service;

    public ConcurrenciaController(ConcurrenciaService service) {
        this.service = service;
    }

    @GetMapping("/test/fixed")
    public String testFixed() throws InterruptedException {
        return service.ejecutarConFixedThreadPool();
    }

    @GetMapping("/test/virtual")
    public String testVirtual() throws InterruptedException {
        return service.ejecutarConVirtualThreads();
    }
}
