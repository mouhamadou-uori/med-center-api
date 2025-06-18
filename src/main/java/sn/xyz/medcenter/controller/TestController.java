package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.DtoConverter;
import sn.xyz.medcenter.dto.PatientDTO;
import sn.xyz.medcenter.model.Patient;
import sn.xyz.medcenter.service.MedicalDataService;

import java.util.List;

/**
 * Contrôleur de test pour déboguer les problèmes d'API
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final MedicalDataService medicalDataService;

    /**
     * Endpoint de test pour récupérer tous les patients sans authentification
     */
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        log.info("Requête de test: récupérer tous les patients");
        List<Patient> patients = medicalDataService.getAllPatients();
        List<PatientDTO> patientDTOs = DtoConverter.convertToPatientDTOList(patients);
        return ResponseEntity.ok(patientDTOs);
    }

    /**
     * Endpoint de test pour vérifier que le contrôleur fonctionne
     */
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from TestController!");
    }
}
