package sn.xyz.medcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service pour faire des appels Orthanc avec des URLs dynamiques
 */
@Service
@Slf4j
public class DynamicOrthancService {

    private final WebClient webClient;

    public DynamicOrthancService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Récupère la liste des patients depuis un serveur Orthanc spécifique
     * @param orthancUrl URL du serveur Orthanc
     * @return Liste des IDs des patients
     */
    public Mono<String[]> getPatients(String orthancUrl) {
        log.info("Récupération des patients depuis: {}", orthancUrl);
        String fullUrl = orthancUrl + "/patients";
        
        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(String[].class)
                .doOnNext(patients -> {
                    if (patients != null) {
                        log.info("Patients récupérés depuis {}: {}", orthancUrl, patients.length);
                    }
                })
                .doOnError(e -> log.error("Erreur lors de la récupération des patients depuis {}: {}", orthancUrl, e.getMessage()));
    }

    /**
     * Récupère les détails d'un patient spécifique depuis un serveur Orthanc
     * @param orthancUrl URL du serveur Orthanc
     * @param patientId ID du patient
     * @return Détails du patient
     */
    public Mono<Map<String, Object>> getPatient(String orthancUrl, String patientId) {
        log.info("Récupération des détails du patient {} depuis: {}", patientId, orthancUrl);
        String fullUrl = orthancUrl + "/patients/" + patientId;
        
        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(patient -> log.info("Détails du patient {} récupérés depuis {}", patientId, orthancUrl))
                .doOnError(e -> log.error("Erreur lors de la récupération du patient {} depuis {}: {}", patientId, orthancUrl, e.getMessage()));
    }

    /**
     * Récupère les détails d'une étude spécifique depuis un serveur Orthanc
     * @param orthancUrl URL du serveur Orthanc
     * @param studyId ID de l'étude
     * @return Détails de l'étude
     */
    public Mono<Map<String, Object>> getStudy(String orthancUrl, String studyId) {
        log.info("Récupération des détails de l'étude {} depuis: {}", studyId, orthancUrl);
        String fullUrl = orthancUrl + "/studies/" + studyId;
        
        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(study -> log.info("Détails de l'étude {} récupérés depuis {}", studyId, orthancUrl))
                .doOnError(e -> log.error("Erreur lors de la récupération de l'étude {} depuis {}: {}", studyId, orthancUrl, e.getMessage()));
    }

    /**
     * Récupère les détails d'une série spécifique depuis un serveur Orthanc
     * @param orthancUrl URL du serveur Orthanc
     * @param seriesId ID de la série
     * @return Détails de la série
     */
    public Mono<Map<String, Object>> getSeries(String orthancUrl, String seriesId) {
        log.info("Récupération des détails de la série {} depuis: {}", seriesId, orthancUrl);
        String fullUrl = orthancUrl + "/series/" + seriesId;
        
        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(series -> log.info("Détails de la série {} récupérés depuis {}", seriesId, orthancUrl))
                .doOnError(e -> log.error("Erreur lors de la récupération de la série {} depuis {}: {}", seriesId, orthancUrl, e.getMessage()));
    }
}
