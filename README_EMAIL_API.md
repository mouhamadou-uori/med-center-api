# API d'envoi d'emails - MedCenter

## Configuration

L'API d'envoi d'emails utilise le service Resend. La configuration se trouve dans `application.properties` :

```properties
# Configuration Resend pour l'envoi d'emails
resend.api.key=re_7e8cmLUb_GzHB5NnNJes7wfbfQ5JrVEbs
resend.from.email=MedCenter <no-reply@medcenter.sn>
```

## Endpoints disponibles

### 1. Envoyer un email complet
```
POST /api/emails/send
```

**Corps de la requête :**
```json
{
  "to": ["destinataire@example.com"],
  "subject": "Sujet de l'email",
  "html": "<h1>Contenu HTML</h1><p>Message de l'email</p>",
  "text": "Version texte (optionnel)",
  "cc": ["copie@example.com"],
  "bcc": ["copie_cachee@example.com"]
}
```

**Réponse :**
```json
{
  "id": "01234567-89ab-cdef-0123-456789abcdef",
  "status": "SUCCESS",
  "message": "Email envoyé avec succès",
  "sentAt": "2025-06-29T10:30:00"
}
```

### 2. Envoyer un email de manière asynchrone
```
POST /api/emails/send-async
```
Même format que l'endpoint précédent, mais l'email est traité en arrière-plan.

### 3. Envoyer un email simple
```
POST /api/emails/send-simple?to=destinataire@example.com&subject=Sujet&htmlContent=<h1>Contenu</h1>
```

### 4. Envoyer un email de bienvenue
```
POST /api/emails/send-welcome?to=utilisateur@example.com&firstName=Jean
```

### 5. Envoyer un email de réinitialisation de mot de passe
```
POST /api/emails/send-password-reset?to=utilisateur@example.com&resetToken=abc123def456
```

### 6. Test rapide (endpoint de test)
```
POST /api/test/send-test-email?to=test@example.com
```

## Sécurité

- Les endpoints d'envoi d'emails requièrent les rôles `ADMIN` ou `PROFESSIONNEL_SANTE`
- L'endpoint de bienvenue nécessite le rôle `ADMIN` uniquement
- L'endpoint de réinitialisation de mot de passe est accessible sans authentification

## Logging

Tous les emails envoyés sont enregistrés dans la table `email_log` avec :
- L'ID retourné par Resend
- L'adresse de destination
- Le sujet
- Le statut (SUCCESS, ERROR, PENDING)
- Les dates de création et d'envoi
- Les messages d'erreur éventuels

## Utilisation

### Exemple avec curl :

```bash
# Envoyer un email simple
curl -X POST "http://localhost:9000/api/emails/send-simple" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "to=test@example.com&subject=Test&htmlContent=<h1>Hello World</h1>"

# Envoyer un email complet
curl -X POST "http://localhost:9000/api/emails/send" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "to": ["test@example.com"],
    "subject": "Test depuis MedCenter",
    "html": "<h1>Bonjour</h1><p>Ceci est un test</p>"
  }'
```

### Exemple en JavaScript :

```javascript
const emailData = {
  to: ["destinataire@example.com"],
  subject: "Notification MedCenter",
  html: "<h1>Nouvelle notification</h1><p>Vous avez un nouveau message.</p>"
};

fetch('http://localhost:9000/api/emails/send', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
  },
  body: JSON.stringify(emailData)
})
.then(response => response.json())
.then(data => console.log(data));
```

## Modèles d'emails inclus

- **Email de bienvenue** : Template personnalisé avec le nom de l'utilisateur
- **Email de réinitialisation** : Avec lien sécurisé et instructions
- **Email simple** : Pour des notifications générales

## Gestion des erreurs

L'API gère automatiquement les erreurs et retourne des codes de statut appropriés :
- `200` : Email envoyé avec succès
- `202` : Email mis en file d'attente (pour l'envoi asynchrone)
- `400` : Données d'entrée invalides
- `500` : Erreur lors de l'envoi
