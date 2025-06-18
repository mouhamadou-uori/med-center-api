package sn.xyz.medcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sn.xyz.medcenter.model.Etude;
import sn.xyz.medcenter.model.Instance;
import sn.xyz.medcenter.model.PatientOrthanc;
import sn.xyz.medcenter.model.Serie;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrthancService {

    private final WebClient webClient;
    
    // URL du serveur Orthanc
    @Value("${orthanc.server.url}")
    private String orthancServerUrl;

    private final OrthancMapperService mapper;

    public OrthancService(OrthancMapperService mapper, WebClient.Builder webClientBuilder) {
        this.mapper = mapper;
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder());
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder());
                })
                .build();
    }

    /**
     * Récupère la liste des patients du serveur Orthanc
     */
    public Mono<String[]> getPatients() {
        log.info("Récupération de la liste des patients depuis Orthanc");
        log.info("URL Orthanc configurée: {}", orthancServerUrl);
        String fullUrl = orthancServerUrl + "/patients";
        log.info("URL complète: {}", fullUrl);
        
        return webClient.get()
                .uri(fullUrl)
                .exchangeToMono(response -> {
                    log.info("Status code: {}", response.statusCode());
                    log.info("Content-Type: {}", response.headers().contentType());
                    return response.bodyToMono(String[].class)
                            .doOnNext(patients -> {
                                if (patients != null) {
                                    log.info("Patients reçus: {}", Arrays.toString(patients));
                                }
                            });
                })
                .doOnError(e -> {
                    log.error("Erreur lors de la récupération des patients: {}", e.getMessage());
                    log.error("Stack trace:", e);
                })
                .doOnSuccess(patients -> {
                    if (patients != null) {
                        log.info("Patients reçus avec succès. Nombre de patients: {}", patients.length);
                    }
                    log.info("Requête terminée avec succès");
                });
    }

    /**
     * Récupère les détails d'un patient spécifique
     */
    public Mono<PatientOrthanc> getPatient(String patientId) {
        log.info("Récupération des détails du patient: {}", patientId);
        return webClient.get()
                .uri(orthancServerUrl + "/patients/{id}", patientId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(mapper::mapToPatientOrthanc)
                .doOnError(e -> log.error("Erreur lors de la récupération du patient {}: {}", patientId, e.getMessage()));
    }

    /**
     * Récupère la liste des études pour un patient
     */
    public Mono<List<Etude>> getStudies(String patientId) {
        log.info("Récupération des études pour le patient: {}", patientId);
        return webClient.get()
                .uri(orthancServerUrl + "/patients/{id}/studies", patientId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(studies -> studies.stream()
                        .map(mapper::mapToEtude)
                        .toList())
                .doOnError(e -> log.error("Erreur lors de la récupération des études pour le patient {}: {}", patientId, e.getMessage()));
    }

    /**
     * Récupère les détails d'une étude spécifique
     */
    public Mono<Etude> getStudy(String studyId) {
        log.info("Récupération des détails de l'étude: {}", studyId);
        return webClient.get()
                .uri(orthancServerUrl + "/studies/{id}", studyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(mapper::mapToEtude)
                .doOnError(e -> log.error("Erreur lors de la récupération de l'étude {}: {}", studyId, e.getMessage()));
    }

    /**
     * Récupère la liste des séries pour une étude
     */
    public Mono<List<Serie>> getSeries(String studyId) {
        log.info("Récupération des séries pour l'étude: {}", studyId);
        return webClient.get()
                .uri(orthancServerUrl + "/studies/{id}/series", studyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(series -> series.stream()
                        .map(mapper::mapToSerie)
                        .toList())
                .doOnError(e -> log.error("Erreur lors de la récupération des séries pour l'étude {}: {}", studyId, e.getMessage()));
    }

    /**
     * Récupère les statistiques du serveur Orthanc
     */
    public Mono<Map<String, Object>> getStatistics() {
        log.info("Récupération des statistiques du serveur Orthanc");
        return webClient.get()
                .uri(orthancServerUrl + "/statistics")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnError(e -> log.error("Erreur lors de la récupération des statistiques: {}", e.getMessage()));
    }

    /**
     * Récupère le statut du serveur Orthanc
     */
    public Mono<Map<String, Object>> getSystemInfo() {
        log.info("Récupération des informations système d'Orthanc");
        return webClient.get()
                .uri(orthancServerUrl + "/system")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnError(e -> log.error("Erreur lors de la récupération des informations système: {}", e.getMessage()));
    }

    /**
     * Upload d'une nouvelle instance DICOM
     */
    public Mono<Instance> uploadInstance(byte[] dicomData) {
        log.info("Upload d'une nouvelle instance DICOM");
        return webClient.post()
                .uri(orthancServerUrl + "/instances")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(dicomData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(mapper::mapToInstance)
                .doOnError(e -> log.error("Erreur lors de l'upload de l'instance: {}", e.getMessage()));
    }

    /**
     * Export d'une étude vers un autre PACS
     */
    public Mono<Map<String, Object>> exportStudy(String studyId, String targetPacs) {
        log.info("Export de l'étude {} vers {}", studyId, targetPacs);
        return webClient.post()
                .uri(orthancServerUrl + "/studies/{id}/export", studyId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("TargetAet", targetPacs))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnError(e -> log.error("Erreur lors de l'export de l'étude {}: {}", studyId, e.getMessage()));
    }

    /**
     * Recherche DICOM
     */
    public Mono<List<Etude>> findDicom(Map<String, Object> query) {
        log.info("Recherche DICOM avec critères: {}", query);
        return webClient.post()
                .uri(orthancServerUrl + "/tools/find")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(studies -> studies.stream()
                        .map(mapper::mapToEtude)
                        .toList())
                .doOnError(e -> log.error("Erreur lors de la recherche DICOM: {}", e.getMessage()));
    }
}
