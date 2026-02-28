
# Require4Testing – Überblick & Dokumentation

Require4Testing ist eine **Spring‑Boot‑Webanwendung** zur Organisation **manueller Anwendertests** (Anforderungen, Testfälle, Testläufe, Ergebnisse).  
Das Projekt entstand im Rahmen der **Aufgabenstellung 2 „Require4Testing“** des Studienmoduls  
**IPWA02‑01 – Programmierung industrieller Informationssysteme mit JavaEE** an der IU.

---

## 1. Ziel & Funktionsumfang

- **Anforderungen** anlegen und verwalten
- **Testfälle** je Anforderung erstellen
- **Testläufe** planen und verwalten
- **Testfälle** Testläufen und **Testern** zuordnen
- **Testergebnisse** erfassen (ASSIGNED, PASSED, FAILED)
- **Rollen (UI‑seitig, prototypisch):** Admin, Requirement Engineer, Creator, Manager, Tester

> **Hinweis:** Die REST‑API ist im Prototyp nicht vollständig abgesichert; Rollentrennung erfolgt über die Web‑UI.

---

## 2. Architektur & Technologien

- **Architektur:** klassische **Drei‑Schichten‑Architektur**  
  (Controller/Views – Services – Repositories/Entities)
- **Views / Server‑Side Rendering:** Thymeleaf
- **REST‑API:** z.B. `/api/requirements`, `/api/test-cases`, `/api/test-runs`, …
- **Persistenz:** Spring Data JPA (Hibernate), **MariaDB**/**MySQL**
- **Frameworks / Build:** Spring Boot, Java17, Maven

---

## 3. Voraussetzungen

- Java 17
- Maven
- MariaDB **oder** MySQL

---

## 4. Datenbank & Initialdaten

### 4.1 Variante A (empfohlen): Start über `data.sql`

Spring Boot erzeugt das Schema automatisch und lädt `data.sql` beim Start.

**Minimal‑Konfiguration** (`src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/require4testingdb
spring.datasource.username=root
spring.datasource.password=IhrPasswort
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always
```

---

### 4.2 Variante B: Datenbank‑Dump importieren

Datei: **require4testingdb_dump.sql**

**Import (wenn DB bereits existiert):**
```bash
mysql -u root -p require4testingdb < require4testingdb_dump.sql
```

**Falls die Datenbank noch nicht existiert:**
```sql
CREATE DATABASE require4testingdb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

---

## 5. Projektkonfiguration

Relevante Einstellungen (`application.properties`):

```properties
spring.jpa.hibernate.ddl-auto=create      # Schema wird bei jedem Start neu erzeugt
spring.sql.init.mode=always               # data.sql wird automatisch geladen
```

**MySQL statt MariaDB:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/require4testingdb
```

---

## 6. Anwendung starten

```bash
git clone https://github.com/Ramon2107/Require4Testing.git
cd Require4Testing
mvn spring-boot:run
```

Aufruf im Browser:

```
http://localhost:8080
```

UI‑Login:

```
/ui/login
```

Im Prototyp erfolgt die Anmeldung **ohne Passwort** per Benutzer‑Dropdown.  
Beispielnutzer aus den Seed‑Daten: ADMIN, Nina (MANAGER), Luca (CREATOR),  
Mina (REQUIREMENT_ENGINEER), Timo/Julia (TESTER).

---

## 7. Oberfläche & API‑Endpunkte

### 7.1 Web‑UI

- Anforderungen: `/ui/requirements`
- Testläufe: `/ui/testruns`
- Tester‑Dashboard: `/ui/dashboard`

### 7.2 REST‑API (JSON, prototypisch)

- `/api/requirements`
- `/api/test-cases`
- `/api/test-runs`
- `/api/test-case-testruns`
- `/api/users`

---

## 8. Hinweise für Prüfer:innen (Schnellstart)

1. MariaDB/MySQL starten
2. **Entweder** Variante A: Start mit `data.sql`  
   **oder** Variante B: Dump importieren (`require4testingdb_dump.sql`)
3. Projekt starten: `mvn spring-boot:run`
4. Browser öffnen: `http://localhost:8080 → /ui/login`
5. Über die UI testen (Anforderungen, Testfälle, Testläufe, Ergebnisse)

---

## 9. Lizenz

Dieses Projekt wurde für **Lern‑ und Prüfungszwecke** erstellt.
