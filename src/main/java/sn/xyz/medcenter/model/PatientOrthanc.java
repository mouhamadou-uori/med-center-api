package sn.xyz.medcenter.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PatientOrthanc {
    private String id; // "ID"
    private boolean isStable; // "IsStable"
    private List<String> labels; // "Labels"
    private String lastUpdate; // "LastUpdate" (format Orthanc: yyyyMMdd'T'HHmmss)
    private Map<String, String> mainDicomTags; // "MainDicomTags"
    private List<String> studies; // "Studies"
    private String type; // "Type"
}
