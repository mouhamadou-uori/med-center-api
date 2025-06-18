package sn.xyz.medcenter.dto;

import lombok.Builder;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatPeriodeDTO {
    private String date;
    private long patients;
    private long consultations;
}
