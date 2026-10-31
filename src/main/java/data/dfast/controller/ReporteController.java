package data.dfast.controller;

import data.dfast.service.ReporteService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> resumen() {
        return ResponseEntity.ok(reporteService.resumenGeneral());
    }
}
