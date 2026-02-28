-- ==========================================================
-- Require4Testing - Nachhaltige Demo-Daten
-- Primäre Seed-Datei: Wird von Spring Boot automatisch beim Start ausgeführt (schema kompatibel).
-- Änderungen an dieser Datei wirken sich direkt auf Demo-Umgebungen aus.
-- ==========================================================

-- 1) Bereinigung (FK-Reihenfolge beachten)
-- Intentional: Clear all demo data for fresh seed
DELETE FROM test_results WHERE 1=1;
DELETE FROM test_case_test_run WHERE 1=1;
DELETE FROM test_runs WHERE 1=1;
DELETE FROM test_cases WHERE 1=1;
DELETE FROM requirements WHERE 1=1;
DELETE FROM users WHERE 1=1;

-- 2) Benutzer (Demo-Accounts) - PROTOTYP
-- WICHTIG: Alle Benutzer haben das Passwort "demo"
-- Hinweis: Für diesen Prototyp verwenden alle Benutzer denselben BCrypt-Hash.
-- BCrypt-Hash für "demo" mit Rounds=10: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (id, username, password, email, role, created_at, updated_at) VALUES
(1, 'Admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@require4testing.local',      'ADMIN',                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Nina',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'nina.mgmt@require4testing.local',  'MANAGER',              CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Luca',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'luca.tc@require4testing.local',    'CREATOR',              CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Mina',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'mina.req@require4testing.local',   'REQUIREMENT_ENGINEER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Timo',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'timo.test@require4testing.local',  'TESTER',               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Julia', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'julia.test@require4testing.local', 'TESTER',               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3) Anforderungen (IDs 1-5)
INSERT INTO requirements (id, readable_id, name, description, status, created_by, created_at, updated_at) VALUES
(1, 'REQ-2026-001', 'Registrierung & Login', 'Kundenkonto-Verwaltung.', 'IN_PROGRESS', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'REQ-2026-002', 'Produktsuche & Filter', 'Produkte finden.', 'SUCCESSFUL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'REQ-2026-003', 'Warenkorb', 'Verwaltung der Artikel.', 'FAILED', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'REQ-2026-004', 'Checkout & Zahlung', 'Bestellabschluss.', 'IN_PROGRESS', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'REQ-2026-005', 'Retouren & Erstattung', 'Rückgabe-Prozess.', 'PLANNING', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- noinspection SqlResolve
INSERT INTO test_cases (id, readable_id, name, description, test_steps, requirement_id, created_by, created_at, updated_at) VALUES
(1, 'TC-001', 'Registrierung validieren', 'Pflichtfelder prüfen.', '1. Seite öffnen\n2. Felder füllen\n3. Senden', 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'TC-002', 'Login mit korrektem PW', 'Erfolgreiche Anmeldung prüfen.', '1. Login öffnen\n2. Daten eingeben', 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'TC-003', 'Login mit falschem PW', 'Fehlermeldung prüfen.', '1. Login öffnen\n2. Falsche Daten eingeben', 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'TC-004', 'Volltextsuche', 'Suche nach Produktnamen.', '1. Suchfeld nutzen\n2. Begriff Sneaker eingeben', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'TC-005', 'Preisfilter', 'Einschränkung der Preisspanne.', '1. Kategorie wählen\n2. Filter 50-100 setzen', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'TC-006', 'Kategoriefilter', 'Filterung nach Marken/Größen.', '1. Marke wählen\n2. Filter anwenden', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'TC-007', 'Add to Cart', 'Artikel in den Warenkorb legen.', '1. Detail öffnen\n2. In den Warenkorb klicken', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'TC-008', 'Menge erhöhen', 'Anpassung der Stückzahl.', '1. Korb öffnen\n2. Menge auf 2 setzen', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'TC-009', 'Remove from Cart', 'Artikel entfernen.', '1. Korb öffnen\n2. Entfernen klicken', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'TC-010', 'Warenkorb-Persistenz', 'Inhalt bleibt nach Login erhalten.', '1. Gastkorb füllen\n2. Einloggen', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'TC-011', 'Adressvalidierung', 'Prüfung der Lieferadresse.', '1. Checkout\n2. Adresse füllen', 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'TC-012', 'PayPal Redirect', 'Weiterleitung zum Zahlungsanbieter.', '1. Checkout bis Zahlung\n2. PayPal wählen', 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'TC-013', 'Kreditkartenzahlung', 'Eingabe von Test-CC-Daten.', '1. Checkout bis Zahlung\n2. CC Daten füllen', 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'TC-014', 'Zahlung abgelehnt', 'Handling von CC Fehlern.', '1. Decline Karte nutzen', 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'TC-015', 'Checkout ohne Adresse', 'Pflichtfeldvalidierung Checkout.', '1. Checkout ohne Adresse starten', 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5) Testläufe
INSERT INTO test_runs (id, readable_id, name, description, status, requirement_id, created_by, created_at, updated_at) VALUES
(1, 'TR-2026-0001', 'Smoke Test Login', 'Basis-Checks.', 'IN_PROGRESS', 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'TR-2026-0002', 'Regression Suche', 'Suche & Filter.', 'SUCCESSFUL', 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'TR-2026-0003', 'Warenkorb Kern', 'Handling & Logik.', 'FAILED', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'TR-2026-0004', 'Checkout E2E', 'Vollständiger Kauf.', 'IN_PROGRESS', 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- noinspection SqlResolve
INSERT INTO test_case_test_run (id, test_case_id, test_run_id, tester_id, status, notes, executed_at) VALUES
(1, 1, 1, 5, 'PASSED', 'Registrierung via E-Mail funktioniert.', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 2, 1, 5, 'PASSED', 'Login-Dauer unter 200ms.', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(3, 3, 1, 6, 'ASSIGNED', NULL, NULL),
(4, 4, 2, 5, 'PASSED', 'Volltextsuche findet alles.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, 10, 3, 5, 'FAILED', 'BUG: Warenkorb leer nach Login!', NOW());

-- 7) Historie (TestResults)
INSERT INTO test_results (id, test_case_id, test_run_id, tester_id, status, notes, executed_at, created_at, updated_at) VALUES
(1, 10, 3, 5, 'FAILED', 'BUG: Warenkorb leer nach Login!', NOW(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
