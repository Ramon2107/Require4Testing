-- Require4Testing Beispieldaten
-- Diese Datei enthält initiale Testdaten für die Anwendung
-- Hinweis: Die Passwörter sind mit BCrypt gehasht. Die Klartextpasswörter sind:
-- admin: admin123, tester1: test123, tester2: test123, manager: manager123

-- Benutzer einfügen (Passwörter sind BCrypt-gehasht)
INSERT INTO users (username, password, email, role, created_at, updated_at) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@require4testing.de', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester1', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'tester1@require4testing.de', 'TESTER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester2', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'tester2@require4testing.de', 'TESTER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('manager', '$2a$10$rDmFN6YpV.nElLlCW/rg0ezdIz3K5KWVy7H8r8e7/D3qB4Q1mN0PW', 'manager@require4testing.de', 'MANAGER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Anforderungen einfügen
INSERT INTO requirements (name, description, created_by, created_at, updated_at) VALUES
('Login-Funktionalität', 'Benutzer sollen sich mit E-Mail und Passwort anmelden können', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Benutzerregistrierung', 'Neue Benutzer sollen sich selbst registrieren können', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Passwort zurücksetzen', 'Benutzer sollen ihr Passwort per E-Mail zurücksetzen können', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dashboard anzeigen', 'Nach dem Login soll ein Dashboard mit Übersicht angezeigt werden', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Testfälle einfügen
INSERT INTO test_cases (name, description, test_steps, requirement_id, created_by, created_at, updated_at) VALUES
('Erfolgreicher Login', 'Test für erfolgreichen Login mit gültigen Anmeldedaten', '1. Öffne Login-Seite\n2. Gib gültige E-Mail ein\n3. Gib gültiges Passwort ein\n4. Klicke auf Anmelden\n5. Überprüfe, ob Dashboard angezeigt wird', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Login mit falschem Passwort', 'Test für fehlgeschlagenen Login mit falschem Passwort', '1. Öffne Login-Seite\n2. Gib gültige E-Mail ein\n3. Gib falsches Passwort ein\n4. Klicke auf Anmelden\n5. Überprüfe Fehlermeldung', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Registrierung mit validen Daten', 'Test für erfolgreiche Registrierung', '1. Öffne Registrierungsseite\n2. Fülle alle Pflichtfelder aus\n3. Klicke auf Registrieren\n4. Überprüfe Bestätigungsmeldung', 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Passwort-Reset anfordern', 'Test für Passwort-Reset-Anforderung', '1. Klicke auf Passwort vergessen\n2. Gib E-Mail ein\n3. Klicke auf Absenden\n4. Überprüfe E-Mail-Versand', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Testläufe einfügen
INSERT INTO test_runs (name, description, status, created_by, created_at, updated_at) VALUES
('Sprint 1 - Login Tests', 'Testlauf für alle Login-bezogenen Testfälle', 'IN_PROGRESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sprint 2 - Registrierung', 'Testlauf für Registrierungstests', 'PENDING', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Release 1.0 - Smoke Tests', 'Vollständiger Testlauf vor Release', 'COMPLETED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- TestCaseTestRun Zuordnungen einfügen
INSERT INTO test_case_test_run (test_case_id, test_run_id, tester_id) VALUES
(1, 1, 2),
(2, 1, 2),
(3, 2, 3),
(4, 2, 3),
(1, 3, 2),
(3, 3, 3);

-- Testergebnisse einfügen
INSERT INTO test_results (test_case_id, test_run_id, tester_id, status, notes, executed_at, created_at, updated_at) VALUES
(1, 1, 2, 'PASSED', 'Login funktioniert wie erwartet', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2, 'PASSED', 'Fehlermeldung wird korrekt angezeigt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 3, 2, 'PASSED', 'Smoke Test erfolgreich', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, 'FAILED', 'Fehler bei der E-Mail-Validierung', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
