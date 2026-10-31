package data.dfast.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Entrega al navegador la clave pública de Google Maps (se define en application.properties). */
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${google.maps.api.key:}")
    private String googleMapsApiKey;

    @GetMapping("/mapas")
    public ResponseEntity<Map<String, String>> configuracionMapas() {
        return ResponseEntity.ok(Map.of("apiKey", googleMapsApiKey == null ? "" : googleMapsApiKey.trim()));
    }
}
