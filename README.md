# F1 Quality Gate — API de réservation de tribunes

Projet réalisé dans le cadre du **Quality Gate Challenge**.

L’objectif est de développer une API REST permettant de gérer la réservation de places en tribune pour un Grand Prix de Formule 1, tout en respectant une démarche qualité basée sur **SonarQube** et le **Quality Gate Sonar Way**.

---

## 1. Stack technique

- Java 21
- Spring Boot 3.5.x
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- MariaDB 11.8.5
- JUnit 5
- Mockito
- AssertJ
- JaCoCo
- SonarQube Community
- Husky + Commitlint
- Bruno pour la recette API

---

## 2. Fonctionnalités couvertes

L’API couvre les 7 fonctionnalités demandées :

| # | Fonctionnalité | Endpoint |
|---|---|---|
| 1 | Créer une tribune | `POST /api/grandstands` |
| 2 | Lister / filtrer les tribunes | `GET /api/grandstands?category=` |
| 3 | Créer une session | `POST /api/sessions` |
| 4 | Inscrire un spectateur | `POST /api/spectators` |
| 5 | Simuler le prix d'une réservation | `POST /api/reservations/quote` |
| 6 | Créer une réservation | `POST /api/reservations` |
| 7 | Annuler une réservation | `POST /api/reservations/{id}/cancel` |

---

## 3. Règles métier implémentées

L’application applique les règles métier suivantes :

| Règle | Statut |
|---|---|
| Calcul du prix de base : `prix_tribune × multiplicateur_session × nb_places` | OK |
| Multiplicateurs par défaut : PRACTICE `0.5`, QUALIFYING `1.0`, SPRINT `1.2`, RACE `1.8` | OK |
| Pass weekend : réduction de `20%` si vendredi + samedi + dimanche | OK |
| Programme fidélité : SILVER `5%`, GOLD `10%` | OK |
| Tarif jeune : moins de 16 ans → réduction de `50%` | OK |
| Limite de 6 places par réservation | OK |
| Disponibilité par tribune et par session | OK |
| Annulation : remboursement `100%` si plus de 7 jours avant la première session, sinon `0%` | OK |
| Email unique pour les spectateurs | OK |
| Validation email | OK |
| Date de naissance dans le passé | OK |
| Multiplicateur de session strictement positif | OK |

---

## 4. Prérequis

Avant de lancer le projet, il faut avoir installé :

```bash
java -version
```

Version attendue :

```txt
Java 21
```

Vérifier aussi Maven :

```bash
mvn -v
```

Docker doit également être installé et lancé :

```bash
docker --version
docker compose version
```

---

## 5. Lancement de l’application

Démarrer la base :

```bash
docker compose up -d
```

Lancer Spring Boot :

```bash
mvn spring-boot:run
```

---

## 6. Lancer les tests

Lancer tous les tests :

```bash
mvn test
```

Les tests couvrent notamment :

- Les enums métier ;
- Les entités ;
- Les DTOs ;
- Les repositories JPA ;
- Les services métier ;
- Les controllers ;
- Les erreurs HTTP ;
- Le moteur de prix ;
- Le calcul de remboursement.

---

## 7. Rapport JaCoCo

Après un :

```bash
mvn clean verify
```

Le rapport JaCoCo est généré ici :

```txt
target/site/jacoco/index.html
```

---

## 8. SonarQube

Le projet utilise SonarQube pour vérifier la qualité du code.


### 8.1 Variables d’environnement

Créer un fichier `.env` à la racine du projet :

```env
SONAR_TOKEN=token_sonar
```

Le fichier `.env` ne doit pas être commit.

Un fichier `.env.example` est fourni.

### 8.3 Script d’analyse

Le script `run_sonar.sh` lance :

1. les tests ;
2. la génération du rapport JaCoCo ;
3. l’analyse Sonar.


Dashboard Sonar :

```txt
http://localhost:9000/dashboard?id=f1-quality-gate
```

---

## 9. Quality Gate

Les métriques attendues par le sujet sont celles du Quality Gate Sonar Way :

| Métrique | Condition |
|---|---|
| New Issues | doit être à 0 |
| Security Hotspots Reviewed | doit être à 100% |
| Coverage on New Code | minimum 4% |
| Duplicated Lines on New Code | maximum 3% |

Objectif final :

```txt
Quality Gate: Passed
```

---

## 10. Convention de commits

Le projet utilise Husky + Commitlint pour imposer des messages de commit propres.

Format attendu :

```txt
type(scope): message
```

Exemples :

```txt
chore(quality): add sonar and commitlint setup
feat(model): add business enums
feat(grandstands): add create and list endpoints
test(api): cover controller error responses
docs(readme): add technical documentation
```

Types autorisés :

```txt
feat, fix, docs, style, refactor, test, chore, build, ci, perf, revert
```

---

## 11. Endpoints API

### 11.1 Tribunes

Créer une tribune :

```http
POST /api/grandstands
```

Lister les tribunes :

```http
GET /api/grandstands
```

Filtrer par catégorie :

```http
GET /api/grandstands?category=GOLD
```

Catégories possibles :

```txt
BRONZE, SILVER, GOLD, PLATINUM
```

---

### 11.2 Sessions

Créer une session :

```http
POST /api/sessions
```

Lister les sessions :

```http
GET /api/sessions
```

Jours possibles :

```txt
FRIDAY, SATURDAY, SUNDAY
```

Types possibles :

```txt
PRACTICE, QUALIFYING, SPRINT, RACE
```

---

### 11.3 Spectateurs

Créer un spectateur :

```http
POST /api/spectators
```

Lister les spectateurs :

```http
GET /api/spectators
```

Programmes fidélité possibles :

```txt
NONE, SILVER, GOLD
```

---

### 11.4 Réservations

Simuler un prix :

```http
POST /api/reservations/quote
```

Créer une réservation :

```http
POST /api/reservations
```

Lister les réservations :

```http
GET /api/reservations
```

Lister les réservations d’un spectateur :

```http
GET /api/reservations?spectatorId=1
```

Annuler une réservation :

```http
POST /api/reservations/{id}/cancel
```

---

## 12. Gestion des erreurs

L’API retourne les erreurs au format JSON suivant :

```json
{
  "error": "Message d'erreur"
}
```

Codes HTTP utilisés :

| Code | Cas |
|---|---|
| 400 | Données invalides |
| 404 | Ressource introuvable |
| 409 | Conflit métier |

Exemples :

```json
{
  "error": "Données d'entrée invalides"
}
```

```json
{
  "error": "Tribune introuvable"
}
```

```json
{
  "error": "Email déjà utilisé"
}
```

```json
{
  "error": "Pas assez de places sur la session SUNDAY — RACE"
}
```

---

## 13. Recette manuelle

La recette manuelle est réalisée avec Bruno (`./bruno`).

Scénario testé :

1. Créer une tribune GOLD ;
2. Créer une tribune BRONZE à faible capacité ;
3. Créer les sessions du vendredi, samedi et dimanche ;
4. Créer des spectateurs adulte, jeune et sans fidélité ;
5. Simuler un prix simple ;
6. Simuler un pass weekend ;
7. Vérifier les remises fidélité et jeune ;
8. Créer une réservation ;
9. Lister les réservations ;
10. Annuler une réservation ;
11. Vérifier les erreurs 400, 404 et 409 ;
12. Vérifier le manque de places.

---

## 14. Structure du projet

Structure principale :

```txt
├── bruno
├── commitlint.config.cjs
├── docker-compose.yml
├── docs
├── HELP.md
├── mvnw
├── mvnw.cmd
├── package.json
├── package-lock.json
├── pom.xml
├── README.md
├── run_sonar.sh
├── sonar-project.properties
└── src/
    ├── main/
    │   ├── java/com/f1gp/f1_quality_gate/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── handler/
    │   │   ├── model/
    │   │   │   ├── entity/
    │   │   │   └── enums/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/f1gp/f1_quality_gate/
            ├── controller/
            ├── dto/
            ├── exception/
            ├── handler/
            ├── model/
            ├── repository/
            └── service/
```
