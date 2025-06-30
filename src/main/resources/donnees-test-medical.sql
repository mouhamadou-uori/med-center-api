-- =====================================================
-- DONNÉES POUR LES NOUVELLES TABLES
-- =====================================================

-- 1. Catégories de pathologies
INSERT IGNORE INTO categorie_pathologie (id, nom, description, icone, ordre, active) VALUES
(1, 'Cardiologie', 'Maladies et troubles du système cardiovasculaire', 'heart', 10, true),
(2, 'Pneumologie', 'Maladies respiratoires et des poumons', 'lungs', 20, true),
(3, 'Diabétologie', 'Diabète et troubles du métabolisme', 'glucometer', 30, true),
(4, 'Neurologie', 'Troubles du système nerveux', 'brain', 40, true),
(5, 'Dermatologie', 'Affections de la peau', 'skin', 50, true),
(6, 'Pédiatrie', 'Maladies infantiles', 'child', 60, true),
(7, 'Gériatrie', 'Pathologies des personnes âgées', 'elder', 70, false);

-- 2. Pathologies
INSERT IGNORE INTO pathologie (id, nom, slug, description, icone, categorie_id, ordre, publiee, created_at, updated_at) VALUES
-- Cardiologie
(1, 'Hypertension Artérielle', 'hypertension-arterielle', 'L''hypertension artérielle est une élévation anormale de la pression exercée par le sang sur la paroi des artères.', 'pressure', 1, 1, true, NOW(), NOW()),
(2, 'Insuffisance Cardiaque', 'insuffisance-cardiaque', 'L''insuffisance cardiaque est l''incapacité du cœur à pomper suffisamment de sang pour répondre aux besoins de l''organisme.', 'heart-failure', 1, 2, true, NOW(), NOW()),
(3, 'Arythmie Cardiaque', 'arythmie-cardiaque', 'L''arythmie cardiaque est un trouble du rythme du cœur.', 'heartbeat', 1, 3, true, NOW(), NOW()),

-- Pneumologie
(4, 'Asthme', 'asthme', 'L''asthme est une maladie inflammatoire chronique des voies respiratoires.', 'inhaler', 2, 1, true, NOW(), NOW()),
(5, 'BPCO', 'bronchopneumopathie-chronique-obstructive', 'La bronchopneumopathie chronique obstructive est une maladie pulmonaire caractérisée par une obstruction permanente des voies aériennes.', 'lungs-disease', 2, 2, true, NOW(), NOW()),
(6, 'Tuberculose', 'tuberculose', 'La tuberculose est une infection bactérienne contagieuse qui atteint principalement les poumons.', 'bacteria', 2, 3, false, NOW(), NOW()),

-- Diabétologie
(7, 'Diabète Type 1', 'diabete-type-1', 'Le diabète de type 1 est une maladie auto-immune où le pancréas ne produit plus d''insuline.', 'insulin', 3, 1, true, NOW(), NOW()),
(8, 'Diabète Type 2', 'diabete-type-2', 'Le diabète de type 2 est caractérisé par une résistance à l''insuline et une insuffisance de production d''insuline.', 'insulin-resistance', 3, 2, true, NOW(), NOW()),

-- Neurologie
(9, 'Maladie d''Alzheimer', 'maladie-alzheimer', 'La maladie d''Alzheimer est une maladie neurodégénérative caractérisée par une perte progressive de la mémoire et des fonctions cognitives.', 'brain-aging', 4, 1, true, NOW(), NOW()),
(10, 'Épilepsie', 'epilepsie', 'L''épilepsie est un trouble neurologique caractérisé par des crises convulsives récurrentes.', 'brain-activity', 4, 2, true, NOW(), NOW()),
(11, 'Migraine', 'migraine', 'La migraine est un type de céphalée caractérisé par des douleurs pulsatiles souvent accompagnées de nausées et d''une sensibilité accrue à la lumière et au bruit.', 'headache', 4, 3, true, NOW(), NOW()),

-- Dermatologie
(12, 'Eczéma', 'eczema', 'L''eczéma est une inflammation de la peau qui se manifeste par des rougeurs, des démangeaisons et parfois des vésicules.', 'skin-rash', 5, 1, true, NOW(), NOW()),
(13, 'Psoriasis', 'psoriasis', 'Le psoriasis est une maladie inflammatoire chronique de la peau caractérisée par des plaques rouges et squameuses.', 'skin-patch', 5, 2, true, NOW(), NOW());

-- 3. Conseils médicaux
INSERT IGNORE INTO conseil_medical (id, titre, contenu, resume, mots_cles, pathologie_id, auteur_id, date_creation, date_modification, date_publication, statut, visible_public, approuve_par_id) VALUES
-- Pour l'Hypertension Artérielle
(1, 'Gestion de l''Hypertension au Quotidien', 
   'L''hypertension artérielle est un facteur de risque majeur pour les maladies cardiovasculaires. Une bonne gestion quotidienne est essentielle pour maintenir des niveaux de pression artérielle sains.\n\n
    Le contrôle régulier de votre pression artérielle est primordial. Il est recommandé de la mesurer au moins une fois par semaine, idéalement à la même heure de la journée.\n\n
    L''alimentation joue un rôle crucial dans le contrôle de l''hypertension. Limitez votre consommation de sel à moins de 5g par jour, privilégiez les fruits et légumes frais, et réduisez les aliments transformés riches en sodium.',
   'Conseils pratiques pour gérer l''hypertension artérielle au quotidien, incluant l''alimentation, l''activité physique et la prise de médicaments.',
   'hypertension, pression artérielle, sel, activité physique, médicaments antihypertenseurs',
   1, 3, DATE_SUB(NOW(), INTERVAL 6 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 1 MONTH), 'PUBLIE', true, 6),

-- Pour le Diabète Type 2
(2, 'Alimentation et Diabète Type 2', 
   'Une alimentation équilibrée est fondamentale dans la gestion du diabète de type 2. Cet article explore les principes nutritionnels clés pour maintenir une glycémie stable.\n\n
    L''index glycémique des aliments est un concept important à comprendre. Les aliments à index glycémique bas provoquent une élévation plus lente de la glycémie, ce qui est préférable pour les personnes diabétiques.\n\n
    Il est recommandé de privilégier les fibres alimentaires, présentes dans les légumes, les fruits entiers et les céréales complètes. Elles ralentissent l''absorption des sucres et contribuent à une meilleure régulation de la glycémie.',
   'Guide nutritionnel pour les personnes atteintes de diabète de type 2, avec des recommandations alimentaires pratiques.',
   'diabète type 2, alimentation, index glycémique, fibres, contrôle glycémique',
   8, 5, DATE_SUB(NOW(), INTERVAL 5 MONTH), DATE_SUB(NOW(), INTERVAL 2 MONTH), DATE_SUB(NOW(), INTERVAL 2 MONTH), 'PUBLIE', true, 6),

-- Pour la Migraine
(3, 'Identification et Gestion des Déclencheurs de Migraine', 
   'Les migraines peuvent être déclenchées par divers facteurs environnementaux et personnels. L''identification de ces déclencheurs est une étape cruciale dans la gestion des crises.\n\n
    Tenir un journal des migraines permet de repérer les schémas récurrents et d''identifier les facteurs déclenchants spécifiques à chaque individu. Notez la date, l''heure, l''intensité de la douleur, ainsi que les aliments consommés, l''activité physique et les facteurs de stress dans les 24 heures précédant la crise.\n\n
    Parmi les déclencheurs courants, on trouve le stress, certains aliments (comme le fromage affiné, l''alcool, les aliments contenant des nitrates), l''irrégularité des repas, le manque de sommeil, et les changements hormonaux.',
   'Comment identifier et gérer les facteurs déclenchants des migraines pour réduire la fréquence et l''intensité des crises.',
   'migraine, céphalées, déclencheurs, journal de migraine, stress, alimentation',
   11, 5, DATE_SUB(NOW(), INTERVAL 3 MONTH), DATE_SUB(NOW(), INTERVAL 2 WEEK), DATE_SUB(NOW(), INTERVAL 2 WEEK), 'PUBLIE', true, 6),

-- Pour l'Asthme (brouillon)
(4, 'L''Asthme et l''Exercice Physique', 
   'L''exercice physique peut déclencher des symptômes d''asthme, mais avec une bonne gestion, les personnes asthmatiques peuvent pratiquer une activité physique régulière et bénéfique.\n\n
    L''asthme induit par l''exercice (AIE) est une réaction des voies respiratoires à l''effort physique, caractérisée par une respiration sifflante, un essoufflement et une toux. Il est important de consulter un médecin avant de débuter un programme d''exercice.\n\n
    Certains sports sont plus adaptés aux personnes asthmatiques, comme la natation (en raison de l''air humide), la marche, le vélo à faible intensité, ou les exercices d''étirement comme le yoga.',
   'Guide pour pratiquer une activité physique sécuritaire et bénéfique malgré l''asthme.',
   'asthme, exercice physique, activité sportive, asthme induit par l''exercice, bronchodilatateurs',
   4, 8, DATE_SUB(NOW(), INTERVAL 1 MONTH), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 'BROUILLON', false, NULL),

-- Pour la BPCO (en revue)
(5, 'Techniques de Respiration pour les Patients BPCO', 
   'La BPCO rend la respiration difficile, mais certaines techniques peuvent aider à améliorer l''efficacité respiratoire et réduire l''essoufflement.\n\n
    La respiration à lèvres pincées est une technique simple mais efficace. Inspirez lentement par le nez, puis expirez doucement par la bouche en formant un "O" avec les lèvres, comme si vous souffliez sur une bougie sans l''éteindre. Cette technique aide à ralentir la respiration et à évacuer plus d''air des poumons.\n\n
    La respiration diaphragmatique, ou respiration abdominale, renforce le diaphragme et réduit l''effort respiratoire. Placez une main sur votre poitrine et l''autre sur votre abdomen. Inspirez par le nez en gonflant votre abdomen (la main sur votre abdomen doit se soulever), puis expirez lentement par la bouche en contractant légèrement les muscles abdominaux.',
   'Techniques respiratoires pour améliorer la capacité respiratoire et réduire l''essoufflement chez les patients atteints de BPCO.',
   'BPCO, techniques de respiration, respiration à lèvres pincées, respiration diaphragmatique, réhabilitation pulmonaire',
   5, 5, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'EN_REVUE', false, NULL);

-- 4. Sections de conseil
INSERT IGNORE INTO section_conseil (id, titre, contenu, ordre, conseil_id) VALUES
-- Pour le conseil sur l'Hypertension
(1, 'Mesure de la pression artérielle', 
   'Pour mesurer correctement votre pression artérielle à domicile :\n\n
    1. Utilisez un tensiomètre électronique validé cliniquement.\n
    2. Mesurez votre tension à la même heure chaque jour, idéalement le matin avant la prise de médicaments et le soir.\n
    3. Évitez le café, l''alcool et le tabac 30 minutes avant la mesure.\n
    4. Asseyez-vous confortablement, dos soutenu, pieds à plat sur le sol et bras reposant sur une table.\n
    5. Restez immobile et en silence pendant la mesure.\n
    6. Notez les valeurs dans un carnet de suivi.',
   1, 1),
   
(2, 'Recommandations alimentaires', 
   'Une alimentation adaptée pour contrôler l''hypertension :\n\n
    - Réduisez votre consommation de sel à moins de 5g par jour (environ une cuillère à café).\n
    - Privilégiez les fruits et légumes frais (au moins 5 portions par jour).\n
    - Consommez des produits laitiers à faible teneur en matières grasses.\n
    - Limitez la consommation d''alcool (maximum 2 verres par jour pour les hommes et 1 pour les femmes).\n
    - Réduisez les aliments transformés, souvent riches en sodium.\n
    - Intégrez des aliments riches en potassium comme les bananes, les pommes de terre et les légumes à feuilles vertes.',
   2, 1),
   
(3, 'Activité physique', 
   'L''exercice régulier aide à réduire la pression artérielle et à maintenir un poids santé :\n\n
    - Visez au moins 150 minutes d''activité modérée par semaine (marche rapide, natation, vélo).\n
    - Répartissez l''activité sur plusieurs jours de la semaine.\n
    - Commencez progressivement si vous n''êtes pas habitué à l''exercice.\n
    - Incluez des exercices de renforcement musculaire deux fois par semaine.\n
    - Consultez votre médecin avant de commencer un nouveau programme d''exercice, surtout si votre hypertension est sévère.',
   3, 1),
   
-- Pour le conseil sur le Diabète Type 2
(4, 'Comprendre l''index glycémique', 
   'L''index glycémique (IG) est un système qui classe les aliments contenant des glucides selon leur impact sur la glycémie :\n\n
    - Aliments à IG bas (moins de 55) : La plupart des fruits et légumes, légumineuses, produits laitiers, pain de seigle.\n
    - Aliments à IG moyen (56-69) : Pain complet, riz brun, couscous.\n
    - Aliments à IG élevé (70 et plus) : Pain blanc, riz blanc, pommes de terre, sucreries.\n\n
    Privilégiez les aliments à IG bas et modéré pour éviter les pics de glycémie.',
   1, 2),
   
(5, 'Importance des repas réguliers', 
   'La régularité des repas est essentielle pour maintenir une glycémie stable :\n\n
    - Prenez trois repas par jour à heures fixes.\n
    - Évitez de sauter des repas, notamment le petit-déjeuner.\n
    - Si nécessaire, prévoyez des collations saines entre les repas principaux.\n
    - Gardez une collation d''urgence (comme des fruits secs ou une barre de céréales complètes) en cas d''hypoglycémie.\n\n
    Cette régularité aide votre corps à mieux gérer la production d''insuline et l''utilisation du glucose.',
   2, 2),
   
-- Pour le conseil sur la Migraine
(6, 'Tenir un journal des migraines', 
   'Un journal des migraines est un outil précieux pour identifier les déclencheurs personnels :\n\n
    - Notez la date et l''heure du début de chaque crise.\n
    - Évaluez l''intensité de la douleur sur une échelle de 1 à 10.\n
    - Décrivez la localisation et le type de douleur (pulsatile, constante, etc.).\n
    - Enregistrez les symptômes associés (nausées, sensibilité à la lumière, etc.).\n
    - Documentez les médicaments pris et leur efficacité.\n
    - Notez les aliments, boissons, activités et facteurs environnementaux dans les 24h précédant la crise.\n\n
    Après quelques semaines, analysez votre journal pour identifier des schémas récurrents.',
   1, 3),
   
(7, 'Déclencheurs alimentaires courants', 
   'Certains aliments et boissons sont connus pour déclencher des migraines chez les personnes sensibles :\n\n
    - Aliments contenant des amines biogènes : fromages affinés, vin rouge, bière, aliments fermentés.\n
    - Aliments contenant des nitrates et nitrites : charcuterie, certaines viandes transformées.\n
    - Caféine et boissons contenant de la théine (effet paradoxal : le sevrage peut aussi déclencher des migraines).\n
    - Alcool, particulièrement le vin rouge et les spiritueux.\n
    - Glutamate monosodique, présent dans certains plats préparés et cuisines asiatiques.\n
    - Édulcorants artificiels comme l''aspartame.\n\n
    Essayez d''éliminer ces aliments un par un pour identifier vos déclencheurs spécifiques.',
   2, 3),
   
(8, 'Gestion du stress', 
   'Le stress est l''un des déclencheurs les plus courants des crises de migraine :\n\n
    - Pratiquez des techniques de relaxation comme la méditation, la respiration profonde ou le yoga.\n
    - Maintenez un horaire de sommeil régulier, avec 7-8 heures de sommeil par nuit.\n
    - Intégrez l''activité physique régulière dans votre routine pour réduire le stress.\n
    - Considérez des thérapies comme la thérapie cognitive-comportementale pour gérer le stress chronique.\n
    - Apprenez à reconnaître les signes avant-coureurs de stress et agissez avant qu''ils ne s''intensifient.\n\n
    La gestion proactive du stress peut significativement réduire la fréquence des migraines.',
   3, 3);

-- 5. Ressources de conseil
INSERT IGNORE INTO ressource_conseil (id, titre, type, url, description, conseil_id) VALUES
(1, 'Guide de la Fondation Cœur et Artères sur l''hypertension', 'PDF', 'https://www.fondation-cœur-arteres.org/guide-hypertension.pdf', 'Guide complet sur la compréhension et la gestion de l''hypertension artérielle.', 1),
(2, 'Application de suivi de la tension artérielle', 'LIEN', 'https://play.google.com/store/apps/details?id=org.medcenter.bp.tracker', 'Application mobile pour enregistrer et suivre vos mesures de tension artérielle au quotidien.', 1),
(3, 'Vidéo: Comment mesurer sa tension à domicile', 'VIDEO', 'https://www.youtube.com/watch?v=correctmeasurementbp', 'Tutoriel vidéo montrant la technique correcte pour mesurer sa pression artérielle à domicile.', 1),
(4, 'Calculateur d''Index Glycémique', 'LIEN', 'https://www.glycemicindex.org/calculator', 'Outil en ligne pour calculer l''index glycémique des repas.', 2),
(5, 'Guide nutritionnel pour diabétiques', 'PDF', 'https://www.diabetes-association.sn/nutrition-guide-fr.pdf', 'Guide nutritionnel adapté au contexte sénégalais pour les personnes atteintes de diabète.', 2),
(6, 'Application de journal de migraine', 'LIEN', 'https://play.google.com/store/apps/details?id=org.medcenter.migraine.diary', 'Application pour suivre vos migraines et identifier les déclencheurs.', 3),
(7, 'Illustration des points de pression pour soulager les migraines', 'IMAGE', 'https://www.migrainehelp.org/pressure-points-image.jpg', 'Image montrant les points d''acupression qui peuvent aider à soulager les symptômes de migraine.', 3);

-- 6. Recommandations de conseil
INSERT IGNORE INTO recommandation (id, texte, ordre, conseil_id) VALUES
-- Pour le conseil sur l'Hypertension
(1, 'Mesurez votre tension à la même heure chaque jour, idéalement le matin et le soir.', 1, 1),
(2, 'Limitez votre consommation de sel à moins de 5g par jour.', 2, 1),
(3, 'Pratiquez une activité physique modérée pendant au moins 150 minutes par semaine.', 3, 1),
(4, 'Prenez vos médicaments antihypertenseurs régulièrement, même si vous vous sentez bien.', 4, 1),
(5, 'Évitez le tabac et limitez votre consommation d''alcool.', 5, 1),

-- Pour le conseil sur le Diabète Type 2
(6, 'Privilégiez les aliments à index glycémique bas ou modéré.', 1, 2),
(7, 'Maintenez un horaire régulier pour vos repas.', 2, 2),
(8, 'Augmentez votre consommation de fibres (légumes, fruits entiers, céréales complètes).', 3, 2),
(9, 'Surveillez votre glycémie selon les recommandations de votre médecin.', 4, 2),
(10, 'Restez physiquement actif pour améliorer la sensibilité à l''insuline.', 5, 2),

-- Pour le conseil sur la Migraine
(11, 'Tenez un journal détaillé de vos migraines pour identifier vos déclencheurs personnels.', 1, 3),
(12, 'Maintenez un horaire de sommeil régulier, même les week-ends.', 2, 3),
(13, 'Restez bien hydraté tout au long de la journée.', 3, 3),
(14, 'Pratiquez des techniques de gestion du stress comme la méditation ou la respiration profonde.', 4, 3),
(15, 'Prenez vos médicaments dès l''apparition des premiers symptômes pour une meilleure efficacité.', 5, 3);
