-- Fichier d'initialisation des données (version simplifiée pour débug)

-- SET FOREIGN_KEY_CHECKS = 0;

-- Insertion des utilisateurs s'ils n'existent pas (avec mot de passe 'passer')
INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif)
VALUES (1, 'NDOUR', 'Mouhamadou Wally', 'uori', 'admin@medcenter.xyz', '000000000', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'ADMIN', NOW(), true);

INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif)
VALUES (2, 'MBENGUE', 'Pape Moussa', 'bisko', 'papemoussambengue@esp.sn', '771112233', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'ADMIN', '2023-06-01 09:00:00', true);

INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif)
VALUES (3, 'DIOP', 'Amath', 'akhydocker', 'diopamath@esp.sn', '772223344', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PROFESSIONNEL', '2023-06-15 14:30:00', true);

INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif)
VALUES (4, 'LY', 'Adja Suzanne', 'adjaratou', 'adja.ly@example.com', '773334455', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', '2023-07-01 10:45:00', true);

INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif)
VALUES (5, 'DATH', 'Dioulde Aminata', 'amina', 'amina.dath@example.com', '773334455', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PROFESSIONNEL', '2023-07-01 10:45:00', true);

-- Insertion des hôpitaux s'ils n'existent pas
INSERT IGNORE INTO hopital (id, nom, type, region, ville, adresse, telephone) 
VALUES (1, 'Hôpital Principal de Dakar', 'PUBLIC', 'DAKAR', 'DAKAR', 'Avenue Nelson Mandela', '338839999');

INSERT IGNORE INTO hopital (id, nom, type, region, ville, adresse, telephone) 
VALUES (2, 'Institut Pasteur', 'PUBLIC', 'DAKAR', 'DAKAR', 'Rue Docteur Roux', '338399999');

-- Insertion des professionnels de santé s'ils n'existent pas
INSERT IGNORE INTO professionnel_sante (id, specialite, numero_ordre, hopital_id) 
VALUES (3, 'Radiologie', 'RAD123', 1);

INSERT IGNORE INTO professionnel_sante (id, specialite, numero_ordre, hopital_id) 
VALUES (5, 'Médecine Générale', 'MED456', 2);

-- Insertion des patients s'ils n'existent pas
INSERT IGNORE INTO patient (id, numero_secu, adresse, contact_urgence) 
VALUES (4, 'SN123456789', 'Rue 10 Médina, Dakar', '774445566');

-- Insertion de consultations
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(1, '2024-05-10 10:00:00', 'Suivi', 'TERMINÉE', 3, 4, 'Examen de routine, tout est normal.'),
(2, '2024-05-11 11:30:00', 'Première consultation', 'TERMINÉE', 5, 4, 'Analyse de sang demandée.'),
(3, '2024-05-12 09:00:00', 'Urgence', 'EN_COURS', 3, 4, 'Douleur abdominale aïgue.');

-- Insertion de dossiers médicaux
INSERT IGNORE INTO dossier_medical (id, date_creation, date_mise_a_jour, patient_id) VALUES
(1, '2023-07-01 11:00:00', NOW(), 4);

-- Insertion de prescriptions
INSERT IGNORE INTO prescription (id, date_emission, date_expiration, medicaments, instructions, renouvelable, consultation_id) VALUES
(1, '2024-05-10', '2024-06-10', 'Paracétamol 1000mg', '1 comprimé toutes les 6 heures si douleur.', false, 1),
(2, '2024-05-11', NULL, 'Prise de sang complète', 'À jeun depuis 12 heures.', false, 2);

-- Nouveaux Hôpitaux
INSERT IGNORE INTO hopital (id, nom, type, region, ville, adresse, telephone) VALUES 
(3, 'Hôpital Fann', 'CHU', 'DAKAR', 'DAKAR', 'Avenue Cheikh Anta Diop', '338691818'),
(4, 'Clinique de la Madeleine', 'CLINIQUE', 'DAKAR', 'DAKAR', 'Rue 18, Point E', '338250000');

-- Nouveaux Utilisateurs (Professionnels et Patients)
INSERT IGNORE INTO utilisateur (id, last_name, first_name, username, email, tel, password, roles, date_creation, actif) VALUES 
(6, 'SOW', 'Fatou', 'fsow', 'fatou.sow@medcenter.xyz', '775556677', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PROFESSIONNEL', NOW(), true),
(7, 'FALL', 'Moussa', 'mfall', 'moussa.fall@medcenter.xyz', '776667788', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PROFESSIONNEL', NOW(), true),
(8, 'GUEYE', 'Bineta', 'bgueye', 'bineta.gueye@medcenter.xyz', '777778899', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PROFESSIONNEL', NOW(), true),
(9, 'BA', 'Ousmane', 'oba', 'ousmane.ba@example.com', '761112233', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', NOW(), true),
(10, 'DIALLO', 'Aissatou', 'adiallo', 'aissatou.diallo@example.com', '762223344', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', NOW(), true),
(11, 'SENE', 'Cheikh', 'csene', 'cheikh.sene@example.com', '763334455', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', NOW(), true),
(12, 'KA', 'Mariama', 'mka', 'mariama.ka@example.com', '764445566', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', NOW(), true),
(13, 'THIAM', 'Lamine', 'lthiam', 'lamine.thiam@example.com', '765556677', '$2a$10$751H/JM5XzwLJ2VV5p3vteje/wrIfkckqcUSgaQex4NwNqMKb31nW', 'PATIENT', NOW(), true);

-- Nouveaux Professionnels de Santé
INSERT IGNORE INTO professionnel_sante (id, specialite, numero_ordre, hopital_id) VALUES 
(6, 'Cardiologie', 'CARD789', 3),
(7, 'Pédiatrie', 'PED101', 4),
(8, 'Dermatologie', 'DERM112', 1);

-- Nouveaux Patients
INSERT IGNORE INTO patient (id, numero_secu, adresse, contact_urgence) VALUES 
(9, 'SN987654321', 'Cité Keur Gorgui, Dakar', '761112233'),
(10, 'SN112233445', 'Sacré Coeur 3, Dakar', '762223344'),
(11, 'SN556677889', 'Yoff, Dakar', '763334455'),
(12, 'SN998877665', 'Almadies, Dakar', '764445566'),
(13, 'SN123123123', 'Mermoz, Dakar', '765556677');

-- Données de consultation variées
-- Consultations pour Patient 4 (Adja Suzanne LY)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(4, DATE_SUB(NOW(), INTERVAL 2 MONTH), 'Suivi Cardiologique', 'TERMINÉE', 6, 4, 'Contrôle de tension. RAS.'),
(5, DATE_SUB(NOW(), INTERVAL 1 MONTH), 'Consultation Dermatologique', 'TERMINÉE', 8, 4, 'Eruption cutanée mineure. Crème prescrite.');

-- Consultations pour Patient 9 (Ousmane BA)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(6, DATE_SUB(NOW(), INTERVAL 10 DAY), 'Consultation Générale', 'TERMINÉE', 5, 9, 'Symptômes grippaux.'),
(7, DATE_SUB(NOW(), INTERVAL 5 DAY), 'Contrôle Pédiatrique', 'TERMINÉE', 7, 9, 'Vaccination de rappel.');

-- Consultations pour Patient 10 (Aissatou DIALLO)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(8, DATE_SUB(NOW(), INTERVAL 45 DAY), 'Examen Radiologique', 'TERMINÉE', 3, 10, 'Radio du poignet. Aucune fracture.'),
(9, DATE_SUB(NOW(), INTERVAL 3 WEEK), 'Suivi Général', 'TERMINÉE', 5, 10, 'Bilan de santé annuel.');

-- Consultations pour Patient 11 (Cheikh SENE)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(10, DATE_SUB(NOW(), INTERVAL 2 WEEK), 'Consultation Cardiologique', 'TERMINÉE', 6, 11, 'ECG de routine.'),
(11, DATE_SUB(NOW(), INTERVAL 1 WEEK), 'Consultation Cardiologique', 'EN_COURS', 6, 11, 'Pose d''un Holter.');

-- Consultations pour Patient 12 (Mariama KA)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(12, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Urgence Pédiatrique', 'TERMINÉE', 7, 12, 'Fièvre élevée.'),
(13, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Consultation Dermatologique', 'ANNULÉE', 8, 12, 'Patient ne s''est pas présenté.');

-- Consultations pour Patient 13 (Lamine THIAM)
INSERT IGNORE INTO consultation (id, date_heure, type, statut, professionnel_id, patient_id, notes) VALUES
(14, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Première consultation', 'TERMINÉE', 3, 13, 'Bilan radiologique initial.'),
(15, DATE_SUB(NOW(), INTERVAL 50 DAY), 'Consultation Générale', 'TERMINÉE', 5, 13, 'Discussion des résultats.');

-- Nouveaux Dossiers Médicaux
INSERT IGNORE INTO dossier_medical (id, date_creation, date_mise_a_jour, patient_id) VALUES
(2, NOW(), NOW(), 9),
(3, NOW(), NOW(), 10),
(4, NOW(), NOW(), 11),
(5, NOW(), NOW(), 12),
(6, NOW(), NOW(), 13);

-- Nouvelles Prescriptions
INSERT IGNORE INTO prescription (id, date_emission, date_expiration, medicaments, instructions, renouvelable, consultation_id) VALUES
(3, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 MONTH), INTERVAL 1 MONTH), 'Crème hydratante', 'Appliquer matin et soir.', false, 5),
(4, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 10 DAY), INTERVAL 7 DAY), 'Sirop pour la toux', '1 cuillère 3 fois par jour.', false, 6),
(5, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 5 DAY), 'Doliprane enfant', 'Selon le poids, toutes les 6h.', true, 12);

-- Charger les données de test pour les conseils médicaux et pathologies
SOURCE donnees-test-medical.sql;

-- Insertion de données de test pour les emails
INSERT IGNORE INTO email_log (id, resend_id, to_email, from_email, subject, status, error_message, sent_at, created_at) VALUES
-- Emails entre professionnels de santé
(1, 'resend_001', 'diopamath@esp.sn', 'admin@medcenter.xyz', 'Rapport de consultation - Patient Adja SUZANNE LY', 'SUCCESS', NULL, '2024-05-10 14:30:00', '2024-05-10 14:30:00'),
(2, 'resend_002', 'admin@medcenter.xyz', 'diopamath@esp.sn', 'Demande urgente de matériel radiologique', 'SUCCESS', NULL, '2024-05-11 09:15:00', '2024-05-11 09:15:00'),
(3, 'resend_003', 'amina.dath@example.com', 'diopamath@esp.sn', 'Résultats d\'imagerie - Patient Ousmane BA', 'SUCCESS', NULL, '2024-05-12 16:45:00', '2024-05-12 16:45:00'),
(4, 'resend_004', 'papemoussambengue@esp.sn', 'admin@medcenter.xyz', 'Planning des consultations - Semaine 20', 'SUCCESS', NULL, '2024-05-13 08:00:00', '2024-05-13 08:00:00'),
(5, 'resend_005', 'diopamath@esp.sn', 'papemoussambengue@esp.sn', 'Re: Planning des consultations - Modification demandée', 'SUCCESS', NULL, '2024-05-13 08:30:00', '2024-05-13 08:30:00'),

-- Emails de notification aux patients
(6, 'resend_006', 'adja.ly@example.com', 'diopamath@esp.sn', 'Rappel de rendez-vous - Consultation radiologie', 'SUCCESS', NULL, '2024-05-14 10:00:00', '2024-05-14 10:00:00'),
(7, 'resend_007', 'ousmane.ba@example.com', 'amina.dath@example.com', 'Résultats d\'analyses disponibles', 'SUCCESS', NULL, '2024-05-15 11:30:00', '2024-05-15 11:30:00'),
(8, 'resend_008', 'aissatou.diallo@example.com', 'diopamath@esp.sn', 'Instructions pré-examen IRM', 'SUCCESS', NULL, '2024-05-16 14:20:00', '2024-05-16 14:20:00'),

-- Emails de rapports et communications administratives
(9, 'resend_009', 'admin@medcenter.xyz', 'diopamath@esp.sn', 'Rapport mensuel d\'activité - Radiologie', 'SUCCESS', NULL, '2024-05-17 17:20:00', '2024-05-17 17:20:00'),
(10, 'resend_010', 'admin@medcenter.xyz', 'amina.dath@example.com', 'Nouveau protocole d\'examens médicaux', 'SUCCESS', NULL, '2024-05-18 09:45:00', '2024-05-18 09:45:00'),
(11, 'resend_011', 'papemoussambengue@esp.sn', 'diopamath@esp.sn', 'Formation continue - Nouvelles techniques d\'imagerie', 'SUCCESS', NULL, '2024-05-19 13:15:00', '2024-05-19 13:15:00'),

-- Emails d'urgence et communications critiques
(12, 'resend_012', 'diopamath@esp.sn', 'amina.dath@example.com', 'URGENT: Anomalie détectée - Patient Cheikh SENE', 'SUCCESS', NULL, '2024-05-20 16:00:00', '2024-05-20 16:00:00'),
(13, 'resend_013', 'amina.dath@example.com', 'admin@medcenter.xyz', 'Re: URGENT - Prise en charge immédiate nécessaire', 'SUCCESS', NULL, '2024-05-20 16:15:00', '2024-05-20 16:15:00'),

-- Emails de coordination entre services
(14, 'resend_014', 'admin@medcenter.xyz', 'papemoussambengue@esp.sn', 'Coordination inter-services - Cas complexe Patient KA', 'SUCCESS', NULL, '2024-05-21 10:30:00', '2024-05-21 10:30:00'),
(15, 'resend_015', 'diopamath@esp.sn', 'admin@medcenter.xyz', 'Demande de consultation spécialisée - Cardiologie', 'SUCCESS', NULL, '2024-05-22 08:45:00', '2024-05-22 08:45:00'),

-- Emails de suivi et relances
(16, 'resend_016', 'cheikh.sene@example.com', 'diopamath@esp.sn', 'Suivi post-examen - Résultats et recommandations', 'SUCCESS', NULL, '2024-05-23 14:10:00', '2024-05-23 14:10:00'),
(17, 'resend_017', 'mariama.ka@example.com', 'amina.dath@example.com', 'Rappel: Prise de médicaments et contrôle', 'SUCCESS', NULL, '2024-05-24 09:20:00', '2024-05-24 09:20:00'),

-- Emails techniques et maintenance
(18, 'resend_018', 'admin@medcenter.xyz', 'diopamath@esp.sn', 'Maintenance préventive équipements - Planning', 'SUCCESS', NULL, '2024-05-25 15:00:00', '2024-05-25 15:00:00'),
(19, 'resend_019', 'diopamath@esp.sn', 'admin@medcenter.xyz', 'Rapport de dysfonctionnement - Scanner salle 2', 'SUCCESS', NULL, '2024-05-26 11:40:00', '2024-05-26 11:40:00'),

-- Emails de formation et mise à jour
(20, 'resend_020', 'papemoussambengue@esp.sn', 'diopamath@esp.sn', 'Mise à jour protocoles sécurité radiologique', 'SUCCESS', NULL, '2024-05-27 13:30:00', '2024-05-27 13:30:00'),
(21, 'resend_021', 'admin@medcenter.xyz', 'amina.dath@example.com', 'Formation: Nouvelles directives qualité', 'SUCCESS', NULL, '2024-05-28 10:15:00', '2024-05-28 10:15:00'),

-- Emails avec erreurs pour tester les cas d'échec
(22, 'resend_022', 'email.invalide@domaine.inexistant', 'diopamath@esp.sn', 'Test envoi - Adresse invalide', 'ERROR', 'Adresse email destinataire invalide', NULL, '2024-05-29 14:00:00'),
(23, NULL, 'lamine.thiam@example.com', 'admin@medcenter.xyz', 'Convocation consultation de suivi', 'PENDING', NULL, NULL, '2024-05-30 09:00:00'),
(24, 'resend_024', 'diopamath@esp.sn', 'serveur.indisponible@test.com', 'Rapport technique hebdomadaire', 'ERROR', 'Serveur de messagerie temporairement indisponible', NULL, '2024-05-30 16:30:00'),

-- Emails récents (derniers jours)
(25, 'resend_025', 'admin@medcenter.xyz', 'diopamath@esp.sn', 'Statistiques d\'activité - Mai 2024', 'SUCCESS', NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(26, 'resend_026', 'amina.dath@example.com', 'papemoussambengue@esp.sn', 'Collaboration inter-disciplinaire - Cas Patient DIALLO', 'SUCCESS', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(27, 'resend_027', 'diopamath@esp.sn', 'admin@medcenter.xyz', 'Demande congés - Période estivale', 'SUCCESS', NULL, DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(28, NULL, 'papemoussambengue@esp.sn', 'diopamath@esp.sn', 'Réunion équipe médicale - Ordre du jour', 'PENDING', NULL, NULL, DATE_SUB(NOW(), INTERVAL 1 HOUR)),

-- Emails de notifications système
(29, 'resend_029', 'diopamath@esp.sn', 'system@medcenter.xyz', 'Sauvegarde données patient effectuée', 'SUCCESS', NULL, DATE_SUB(NOW(), INTERVAL 12 HOUR), DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(30, 'resend_030', 'admin@medcenter.xyz', 'system@medcenter.xyz', 'Rapport sécurité mensuel - Accès système', 'SUCCESS', NULL, DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR));

-- SET FOREIGN_KEY_CHECKS = 1;
