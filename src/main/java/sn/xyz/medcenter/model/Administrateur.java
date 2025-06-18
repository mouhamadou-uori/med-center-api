package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "administrateur")
@EqualsAndHashCode(callSuper = true)
public class Administrateur extends Utilisateur {
    private String role;
}