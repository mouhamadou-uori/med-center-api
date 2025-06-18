-- SET FOREIGN_KEY_CHECKS = 0;

-- Tables sans dépendances
CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hopital (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    type ENUM('PUBLIC', 'PRIVE', 'CLINIQUE', 'CHU', 'CENTRE_SANTE') NOT NULL,
    region VARCHAR(50) NOT NULL,
    ville VARCHAR(50) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    site_web VARCHAR(100),
    statut VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tables avec une seule dépendance
CREATE TABLE IF NOT EXISTS utilisateur (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tel VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(50) NOT NULL,
    date_creation DATETIME NOT NULL,
    date_suppression DATETIME,
    actif BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS serveur_dicom (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    url_orthanc VARCHAR(255) NOT NULL,
    port_orthanc VARCHAR(10) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    hopital_id BIGINT NOT NULL,
    CONSTRAINT fk_serveur_hopital FOREIGN KEY (hopital_id) REFERENCES hopital(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS professionnel_sante (
    id BIGINT PRIMARY KEY,
    specialite VARCHAR(100) NOT NULL,
    numero_ordre VARCHAR(50) NOT NULL,
    etablissement VARCHAR(100),
    region VARCHAR(50),
    hopital_id BIGINT,
    CONSTRAINT fk_prof_user FOREIGN KEY (id) REFERENCES utilisateur(id),
    CONSTRAINT fk_prof_hopital FOREIGN KEY (hopital_id) REFERENCES hopital(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS patient (
    id BIGINT PRIMARY KEY,
    numero_secu VARCHAR(15) NOT NULL UNIQUE, -- numéro de sécurité sociale
    adresse VARCHAR(255),
    contact_urgence VARCHAR(20),
    INDEX idx_patient_id (id),
    CONSTRAINT fk_patient_user FOREIGN KEY (id) REFERENCES utilisateur(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS administrateur (
    id BIGINT PRIMARY KEY,
    role VARCHAR(50),
    CONSTRAINT fk_admin_user FOREIGN KEY (id) REFERENCES utilisateur(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tables avec dépendances sur patient et professionnel_sante
CREATE TABLE IF NOT EXISTS consultation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_heure DATETIME NOT NULL,
    type VARCHAR(50) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    notes TEXT,
    professionnel_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    CONSTRAINT fk_consult_prof FOREIGN KEY (professionnel_id) REFERENCES professionnel_sante(id),
    CONSTRAINT fk_consult_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS dossier_medical (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_creation DATETIME NOT NULL,
    date_mise_a_jour DATETIME,
    patient_id BIGINT NOT NULL,
    CONSTRAINT fk_dossier_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS prescription (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_emission DATE NOT NULL,
    date_expiration DATE,
    medicaments TEXT NOT NULL,
    instructions TEXT,
    renouvelable BOOLEAN DEFAULT FALSE,
    consultation_id BIGINT NOT NULL,
    CONSTRAINT fk_prescription_consult FOREIGN KEY (consultation_id) REFERENCES consultation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

---------------------------
-- Tables pour la gestion des conseils médicaux

-- 1. Table des catégories de pathologies
CREATE TABLE IF NOT EXISTS categorie_pathologie (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    icone VARCHAR(50),
    ordre INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table des pathologies
CREATE TABLE IF NOT EXISTS pathologie (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE, -- URL-friendly identifier
    description TEXT,
    icone VARCHAR(50),
    categorie_id BIGINT NOT NULL,
    ordre INT DEFAULT 0,
    publiee BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_pathologie_categorie FOREIGN KEY (categorie_id) REFERENCES categorie_pathologie(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table des conseils médicaux
CREATE TABLE IF NOT EXISTS conseil_medical (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    contenu LONGTEXT NOT NULL,
    resume TEXT,
    mots_cles VARCHAR(255),
    pathologie_id BIGINT NOT NULL,
    auteur_id BIGINT NOT NULL, -- ID du professionnel de santé
    date_creation DATETIME NOT NULL,
    date_modification DATETIME NOT NULL,
    date_publication DATETIME,
    statut ENUM('BROUILLON', 'EN_REVUE', 'PUBLIE', 'ARCHIVE') DEFAULT 'BROUILLON',
    visible_public BOOLEAN DEFAULT FALSE,
    approuve_par_id BIGINT, -- ID du professionnel qui a validé le contenu
    INDEX idx_conseil_pathologie (pathologie_id),
    INDEX idx_conseil_auteur (auteur_id),
    CONSTRAINT fk_conseil_pathologie FOREIGN KEY (pathologie_id) REFERENCES pathologie(id),
    CONSTRAINT fk_conseil_auteur FOREIGN KEY (auteur_id) REFERENCES professionnel_sante(id),
    CONSTRAINT fk_conseil_approbateur FOREIGN KEY (approuve_par_id) REFERENCES professionnel_sante(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Table des sections de conseils (pour structurer le contenu)
CREATE TABLE IF NOT EXISTS section_conseil (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    contenu LONGTEXT NOT NULL,
    ordre INT NOT NULL,
    conseil_id BIGINT NOT NULL,
    CONSTRAINT fk_section_conseil FOREIGN KEY (conseil_id) REFERENCES conseil_medical(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Table pour les ressources associées (liens, PDF, etc.)
CREATE TABLE IF NOT EXISTS ressource_conseil (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    type ENUM('LIEN', 'PDF', 'IMAGE', 'VIDEO') NOT NULL,
    url VARCHAR(255) NOT NULL,
    description TEXT,
    conseil_id BIGINT NOT NULL,
    CONSTRAINT fk_ressource_conseil FOREIGN KEY (conseil_id) REFERENCES conseil_medical(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Table pour les relations entre pathologies (co-morbidités, pathologies associées)
CREATE TABLE IF NOT EXISTS relation_pathologies (
    pathologie_source_id BIGINT NOT NULL,
    pathologie_cible_id BIGINT NOT NULL,
    type_relation VARCHAR(50) NOT NULL, -- ex: 'ASSOCIEE', 'COMORBIDITE', etc.
    PRIMARY KEY (pathologie_source_id, pathologie_cible_id),
    CONSTRAINT fk_relation_source FOREIGN KEY (pathologie_source_id) REFERENCES pathologie(id),
    CONSTRAINT fk_relation_cible FOREIGN KEY (pathologie_cible_id) REFERENCES pathologie(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Table pour les recommandations clés (liste à puces)
CREATE TABLE IF NOT EXISTS recommandation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    texte VARCHAR(255) NOT NULL,
    ordre INT NOT NULL,
    conseil_id BIGINT NOT NULL,
    CONSTRAINT fk_recommandation_conseil FOREIGN KEY (conseil_id) REFERENCES conseil_medical(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- SET FOREIGN_KEY_CHECKS = 1;
