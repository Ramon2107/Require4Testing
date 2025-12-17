-- Require4Testing Beispieldaten
-- 1. BENUTZER
INSERT INTO users (id, username, password, email, role, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@require4testing.de', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'tester1', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'tester1@require4testing.de', 'TESTER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'tester2', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'tester2@require4testing.de', 'TESTER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'manager', '$2a$10$rDmFN6YpV.nElLlCW/rg0ezdIz3K5KWVy7H8r8e7/D3qB4Q1mN0PW', 'manager@require4testing.de', 'MANAGER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'creator', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'creator@require4testing.de', 'CREATOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'req_eng', '$2a$10$7PtcjEnWb/ZkgyXyxY1/Jev5OlK4sJ0mJdBw1iqFKz7yJn8N7OeKe', 'req@require4testing.de', 'REQUIREMENT_ENGINEER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. ANFORDERUNGEN
INSERT INTO requirements (id, name, description, status, readable_id, created_by, created_at, updated_at) VALUES
(1, 'Login-Funktionalität', 'User Login mit E-Mail/Passwort', 'SUCCESSFUL', 'REQ-2025-0001', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Registrierung', 'Neuregistrierung von Benutzern', 'IN_PROGRESS', 'REQ-2025-0002', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Profilverwaltung', 'Profilbild und Daten ändern', 'FAILED', 'REQ-2025-0003', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Suchfunktion', 'Globale Suche', 'PLANNED', 'REQ-2025-0004', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'API Schnittstelle', 'REST API für externe Partner', 'PLANNED', 'REQ-2025-0005', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. TESTFÄLLE
INSERT INTO test_cases (id, name, description, test_steps, requirement_id, created_by, created_at, updated_at) VALUES
-- Zu REQ 1 (Login)
(10, 'Login Valid', 'Login mit korrekten Daten', '1. Daten eingeben\n2. Klick Login', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Login Invalid', 'Login mit falschem PW', '1. Falsche Daten\n2. Klick Login', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Zu REQ 2 (Registrierung)
(20, 'Reg Valid', 'Registrierung erfolgreich', '...', 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'Reg Duplicate', 'E-Mail existiert schon', '...', 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Zu REQ 3 (Profil)
(30, 'Bild Upload JPG', 'Upload JPG Bild', '...', 3, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(31, 'Bild Upload PNG', 'Upload PNG Bild', '...', 3, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Zu REQ 4 (Suche)
(40, 'Suche leer', 'Leere Suche absenden', '...', 4, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Zu REQ 5 (API)
(50, 'API Auth', 'Test Auth Header', '...', 5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. TESTLÄUFE
-- TR-IDs ab 100.
INSERT INTO test_runs (id, name, description, status, readable_id, requirement_id, created_by, created_at, updated_at) VALUES
-- REQ 1 (Login) -> SUCCESSFUL (Alle PASSED)
(100, 'Sprint 1: Login', 'Login Tests', 'SUCCESSFUL', 'TR-2025-0001', 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- REQ 2 (Reg) -> IN_PROGRESS (Einer PASSED, Einer OPEN)
(200, 'Sprint 2: Reg Tests', 'Reg Tests', 'IN_PROGRESS', 'TR-2025-0002', 2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- REQ 3 (Profil) -> FAILED (Einer FAILED)
(300, 'Sprint 3: Profil', 'Profil Tests', 'FAILED', 'TR-2025-0003', 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- REQ 4 (Suche) -> PLANNING (Keine Zuordnungen oder nur leerer Run)
(400, 'Sprint 4: Suche', 'Noch nicht gestartet', 'PLANNING', 'TR-2025-0004', 4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- REQ 2 (Reg) -> PLANNING (Zweiter Run, noch leer)
(500, 'Sprint 2: Reg Retest', 'Retest', 'PLANNING', 'TR-2025-0005', 2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. ZUORDNUNGEN
INSERT INTO test_case_test_run (test_case_id, test_run_id, tester_id, status) VALUES
-- TR 100 (Login) -> SUCCESSFUL
(10, 100, 2, 'PASSED'),
(11, 100, 2, 'PASSED'),

-- TR 200 (Reg) -> IN_PROGRESS
(20, 200, 3, 'PASSED'),
(21, 200, 3, 'OPEN'),

-- TR 300 (Profil) -> FAILED
(30, 300, 2, 'PASSED'),
(31, 300, 2, 'FAILED');


