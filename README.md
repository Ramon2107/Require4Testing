# Require4Testing

Eine Spring Boot Web-Anwendung zur Organisation manueller Anwendertests.

## Inhaltsverzeichnis

- [Übersicht](#übersicht)
- [Technologien](#technologien)
- [Installation](#installation)
- [API-Dokumentation](#api-dokumentation)
- [Datenmodell](#datenmodell)

## Übersicht

Require4Testing ist eine Web-Anwendung, die Teams dabei unterstützt, manuelle Anwendertests zu organisieren und zu verwalten. Die Anwendung ermöglicht:

- Verwaltung von Anforderungen (Requirements)
- Erstellung und Verwaltung von Testfällen (Test Cases)
- Durchführung von Testläufen (Test Runs)
- Dokumentation von Testergebnissen (Test Results)
- Benutzerverwaltung mit Rollen

## Technologien

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **Thymeleaf**
- **Maven**

## Installation

### Voraussetzungen

- Java 17 oder höher
- Maven 3.6+
- MySQL 8.0+ (oder MariaDB)

### MySQL Datenbank einrichten

1. Starten Sie MySQL Server
2. Die Datenbank `require4testingdb` wird automatisch erstellt beim ersten Start

Optional können Sie die Datenbank manuell erstellen:
```sql
CREATE DATABASE require4testingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Anwendung starten

```bash
# Repository klonen
git clone https://github.com/Ramon2107/Require4Testing.git
cd Require4Testing

# Anwendung bauen und starten
mvn spring-boot:run
```

Die Anwendung ist dann unter `http://localhost:8080` erreichbar.

### Datenbank-Konfiguration anpassen

Die MySQL-Verbindungsdaten können in `src/main/resources/application.properties` angepasst werden:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/require4testingdb
spring.datasource.username=root
spring.datasource.password=IhrPasswort
```

## API-Dokumentation

Die REST-API bietet folgende Endpunkte:

### Benutzer (Users)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/users` | Alle Benutzer abrufen |
| GET | `/api/users/{id}` | Benutzer nach ID abrufen |
| GET | `/api/users/username/{username}` | Benutzer nach Benutzername abrufen |
| POST | `/api/users` | Neuen Benutzer erstellen |
| PUT | `/api/users/{id}` | Benutzer aktualisieren |
| DELETE | `/api/users/{id}` | Benutzer löschen |

**Beispiel - Benutzer erstellen:**
```json
POST /api/users
{
    "username": "neuerbenutzer",
    "password": "passwort123",
    "email": "neuer@example.de",
    "role": "TESTER"
}
```

### Anforderungen (Requirements)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/requirements` | Alle Anforderungen abrufen |
| GET | `/api/requirements/{id}` | Anforderung nach ID abrufen |
| GET | `/api/requirements/creator/{createdBy}` | Anforderungen nach Ersteller |
| GET | `/api/requirements/search?name={name}` | Anforderungen suchen |
| POST | `/api/requirements` | Neue Anforderung erstellen |
| PUT | `/api/requirements/{id}` | Anforderung aktualisieren |
| DELETE | `/api/requirements/{id}` | Anforderung löschen |

**Beispiel - Anforderung erstellen:**
```json
POST /api/requirements
{
    "name": "Neue Anforderung",
    "description": "Beschreibung der Anforderung",
    "createdBy": 1
}
```

### Testfälle (Test Cases)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/test-cases` | Alle Testfälle abrufen |
| GET | `/api/test-cases/{id}` | Testfall nach ID abrufen |
| GET | `/api/test-cases/requirement/{requirementId}` | Testfälle nach Anforderung |
| GET | `/api/test-cases/creator/{createdBy}` | Testfälle nach Ersteller |
| GET | `/api/test-cases/search?name={name}` | Testfälle suchen |
| POST | `/api/test-cases` | Neuen Testfall erstellen |
| PUT | `/api/test-cases/{id}` | Testfall aktualisieren |
| DELETE | `/api/test-cases/{id}` | Testfall löschen |

**Beispiel - Testfall erstellen:**
```json
POST /api/test-cases
{
    "name": "Login Test",
    "description": "Test der Login-Funktionalität",
    "testSteps": "1. Seite öffnen\n2. Anmelden\n3. Prüfen",
    "requirementId": 1,
    "createdBy": 1
}
```

### Testläufe (Test Runs)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/test-runs` | Alle Testläufe abrufen |
| GET | `/api/test-runs/{id}` | Testlauf nach ID abrufen |
| GET | `/api/test-runs/creator/{createdBy}` | Testläufe nach Ersteller |
| GET | `/api/test-runs/status/{status}` | Testläufe nach Status |
| POST | `/api/test-runs` | Neuen Testlauf erstellen |
| PUT | `/api/test-runs/{id}` | Testlauf aktualisieren |
| DELETE | `/api/test-runs/{id}` | Testlauf löschen |

**Beispiel - Testlauf erstellen:**
```json
POST /api/test-runs
{
    "name": "Sprint 3 Tests",
    "description": "Testlauf für Sprint 3",
    "status": "PENDING",
    "createdBy": 1
}
```

### Testergebnisse (Test Results)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/test-results` | Alle Testergebnisse abrufen |
| GET | `/api/test-results/{id}` | Testergebnis nach ID abrufen |
| GET | `/api/test-results/test-case/{testCaseId}` | Ergebnisse nach Testfall |
| GET | `/api/test-results/test-run/{testRunId}` | Ergebnisse nach Testlauf |
| GET | `/api/test-results/tester/{testerId}` | Ergebnisse nach Tester |
| POST | `/api/test-results` | Neues Testergebnis erstellen |
| PUT | `/api/test-results/{id}` | Testergebnis aktualisieren |
| DELETE | `/api/test-results/{id}` | Testergebnis löschen |

**Beispiel - Testergebnis erstellen:**
```json
POST /api/test-results
{
    "testCaseId": 1,
    "testRunId": 1,
    "testerId": 2,
    "status": "PASSED",
    "notes": "Test erfolgreich durchgeführt"
}
```

### Testfall-Testlauf-Zuordnungen (Test Case Test Run Assignments)

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| GET | `/api/test-case-test-runs` | Alle Zuordnungen abrufen |
| GET | `/api/test-case-test-runs/{id}` | Zuordnung nach ID abrufen |
| GET | `/api/test-case-test-runs/test-case/{testCaseId}` | Zuordnungen nach Testfall |
| GET | `/api/test-case-test-runs/test-run/{testRunId}` | Zuordnungen nach Testlauf |
| GET | `/api/test-case-test-runs/tester/{testerId}` | Zuordnungen nach Tester |
| POST | `/api/test-case-test-runs` | Neue Zuordnung erstellen |
| PUT | `/api/test-case-test-runs/{id}` | Zuordnung aktualisieren |
| DELETE | `/api/test-case-test-runs/{id}` | Zuordnung löschen |

**Beispiel - Testfall einem Testlauf mit Tester zuordnen:**
```json
POST /api/test-case-test-runs
{
    "testCaseId": 1,
    "testRunId": 1,
    "testerId": 2
}
```

## Datenmodell

### User (Benutzer)
- `id` - Eindeutige ID
- `username` - Benutzername
- `password` - Passwort
- `email` - E-Mail-Adresse
- `role` - Rolle (ADMIN, MANAGER, TESTER, USER)
- `createdAt` - Erstellungszeitpunkt
- `updatedAt` - Aktualisierungszeitpunkt

### Requirement (Anforderung)
- `id` - Eindeutige ID
- `name` - Name der Anforderung
- `description` - Beschreibung
- `createdBy` - Ersteller-ID
- `createdAt` - Erstellungszeitpunkt
- `updatedAt` - Aktualisierungszeitpunkt

### TestCase (Testfall)
- `id` - Eindeutige ID
- `name` - Name des Testfalls
- `description` - Beschreibung
- `testSteps` - Testschritte
- `requirementId` - Zugehörige Anforderungs-ID
- `createdBy` - Ersteller-ID
- `createdAt` - Erstellungszeitpunkt
- `updatedAt` - Aktualisierungszeitpunkt

### TestRun (Testlauf)
- `id` - Eindeutige ID
- `name` - Name des Testlaufs
- `description` - Beschreibung
- `status` - Status (PENDING, IN_PROGRESS, COMPLETED)
- `createdBy` - Ersteller-ID
- `createdAt` - Erstellungszeitpunkt
- `updatedAt` - Aktualisierungszeitpunkt

### TestResult (Testergebnis)
- `id` - Eindeutige ID
- `testCaseId` - Testfall-ID
- `testRunId` - Testlauf-ID
- `testerId` - Tester-ID
- `status` - Status (PASSED, FAILED, BLOCKED, SKIPPED)
- `notes` - Notizen
- `executedAt` - Ausführungszeitpunkt
- `createdAt` - Erstellungszeitpunkt
- `updatedAt` - Aktualisierungszeitpunkt

### TestCaseTestRun (Testfall-Testlauf-Zuordnung)
- `id` - Eindeutige ID
- `testCaseId` - Testfall-ID
- `testRunId` - Testlauf-ID
- `testerId` - Zugewiesener Tester-ID

## Lizenz

Dieses Projekt ist für Lernzwecke erstellt.
