-- ==========================================================
-- Require4Testing - Demo-/Testdaten (E-Commerce Szenario)
-- ==========================================================
-- Ziel:
-- - Korrekte Rollen: Admin, Tester, Testfallersteller, Testmanager, Requirements Engineer
-- - Mehrere Anforderungen mit unterschiedlichen Zuständen
-- - Testfälle pro Anforderung (realistische Schritte)
-- - Testläufe inkl. Zuweisungen mit Status: ASSIGNED, PASSED, FAILED
-- - Ein Testlauf bewusst ohne Zuweisung (bleibt PLANNING)
-- - Eine Anforderung bewusst ohne Testfälle (bleibt PLANNING)
--
-- ==========================================================

-- ----------------------------------------------------------
-- 1) Reset / Bereinigung (Reihenfolge wegen FK-Beziehungen)
-- ----------------------------------------------------------
DELETE FROM test_results;
DELETE FROM test_case_test_run;
DELETE FROM test_runs;
DELETE FROM test_cases;
DELETE FROM requirements;
DELETE FROM users;

-- ----------------------------------------------------------
-- 2) Benutzer (Demo-Accounts)
-- Rollen-Codes im System: ADMIN, MANAGER, CREATOR, TESTER, REQUIREMENT_ENGINEER
-- UI-Bezeichnungen: Admin, Testmanager, Testfallersteller, Tester, Requirements Engineer
-- ----------------------------------------------------------
INSERT INTO users (id, username, password, email, role, created_at, updated_at) VALUES
(1,  'Admin', 'demo', 'admin@require4testing.local',      'ADMIN',                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2,  'Nina',  'demo', 'nina.mgmt@require4testing.local',  'MANAGER',              CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3,  'Luca',  'demo', 'luca.tc@require4testing.local',    'CREATOR',              CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4,  'Mina',  'demo', 'mina.req@require4testing.local',   'REQUIREMENT_ENGINEER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5,  'Timo',  'demo', 'timo.test@require4testing.local',  'TESTER',               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6,  'Julia', 'demo', 'julia.test@require4testing.local', 'TESTER',               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
-- ----------------------------------------------------------
-- 3) Anforderungen
-- ----------------------------------------------------------
INSERT INTO requirements (id, readable_id, name, description, status, created_by, created_at, updated_at) VALUES
(10, 'REQ-2025-0001', 'Registrierung & Login',
 'Als Kunde möchte ich ein Konto erstellen und mich anmelden können, damit ich Bestellungen einsehen und schneller einkaufen kann.',
 'IN_PROGRESS', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(11, 'REQ-2025-0002', 'Produktsuche & Filter',
 'Als Kunde möchte ich Produkte suchen und filtern können, damit ich passende Artikel schnell finde.',
 'SUCCESSFUL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(12, 'REQ-2025-0003', 'Warenkorb (Add/Remove/Update)',
 'Als Kunde möchte ich Produkte in den Warenkorb legen, Mengen ändern und Artikel entfernen können, damit ich meinen Einkauf vorbereite.',
 'FAILED', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(13, 'REQ-2025-0004', 'Checkout & Zahlung',
 'Als Kunde möchte ich meine Bestellung abschließen und bezahlen können, damit der Kauf erfolgreich durchgeführt wird.',
 'IN_PROGRESS', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(14, 'REQ-2025-0005', 'Retouren & Erstattung',
 'Als Kunde möchte ich eine Retoure anmelden können, damit ich fehlerhafte oder unpassende Artikel zurückgeben kann.',
 'PLANNING', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ----------------------------------------------------------
-- 4) Testfälle
-- ----------------------------------------------------------
INSERT INTO test_cases (id, name, description, test_steps, requirement_id, created_by, created_at, updated_at) VALUES
-- REQ-2025-0001 Registrierung & Login
(100, 'Registrierung mit gültigen Pflichtfeldern',
 'Neuer Kunde kann sich mit gültigen Daten registrieren.',
 '1. Registrierungsseite öffnen
2. Pflichtfelder (E-Mail, Passwort, Name) ausfüllen
3. AGB akzeptieren
4. Registrierung absenden
5. Bestätigung/Weiterleitung zur Login-Seite oder direktes Login',
 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(101, 'Registrierung mit bereits verwendeter E-Mail',
 'Registrierung muss abgewiesen werden, wenn E-Mail bereits existiert.',
 '1. Registrierungsseite öffnen
2. Bereits registrierte E-Mail eingeben
3. Registrierung absenden
4. Fehlermeldung „E-Mail bereits vorhanden“ erscheint',
 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(102, 'Login mit gültigen Zugangsdaten',
 'Kunde kann sich anmelden und landet im Konto-Bereich.',
 '1. Login-Seite öffnen
2. Gültige E-Mail + Passwort eingeben
3. Anmelden klicken
4. Kontoübersicht wird angezeigt',
 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(103, 'Login mit falschem Passwort',
 'Login muss abgewiesen werden; keine Session wird erstellt.',
 '1. Login-Seite öffnen
2. E-Mail korrekt, Passwort falsch
3. Anmelden klicken
4. Fehlermeldung erscheint, Benutzer bleibt ausgeloggt',
 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- REQ-2025-0002 Produktsuche & Filter
(110, 'Produktsuche liefert relevante Treffer',
 'Suche nach Produktname zeigt relevante Treffer an.',
 '1. Suchfeld öffnen
2. Suchbegriff „Sneaker“ eingeben
3. Suche starten
4. Trefferliste enthält Produkte mit „Sneaker“ im Namen/Keywords',
 11, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(111, 'Filter nach Preisbereich funktioniert',
 'Filter auf Preisbereich reduziert Treffer korrekt.',
 '1. Kategorie „Schuhe“ öffnen
2. Preisfilter 50–100 setzen
3. Filter anwenden
4. Trefferliste enthält nur Produkte im Preisbereich',
 11, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(112, 'Filterkombination: Marke + Größe',
 'Kombinierte Filter liefern erwartete Ergebnisse.',
 '1. Kategorie „Schuhe“ öffnen
2. Marke „Acme“ wählen
3. Größe 42 wählen
4. Filter anwenden
5. Treffer entsprechen Marke und Größe',
 11, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- REQ-2025-0003 Warenkorb
(120, 'Artikel in Warenkorb legen (Detailseite)',
 'Artikel wird aus Detailseite in den Warenkorb übernommen.',
 '1. Produktdetail öffnen
2. Größe/Farbe auswählen (falls vorhanden)
3. „In den Warenkorb“ klicken
4. Warenkorb zeigt Artikel mit Menge 1',
 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(121, 'Menge im Warenkorb erhöhen',
 'Mengenänderung aktualisiert Position und Summen.',
 '1. Warenkorb öffnen
2. Menge eines Artikels von 1 auf 2 erhöhen
3. Aktualisieren
4. Zwischensumme/Gesamtsumme passen sich an',
 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(122, 'Artikel aus Warenkorb entfernen',
 'Artikel kann entfernt werden, Warenkorb wird aktualisiert.',
 '1. Warenkorb öffnen
2. Bei einem Artikel „Entfernen“ klicken
3. Warenkorb aktualisiert sich
4. Artikel ist nicht mehr vorhanden',
 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(123, 'Warenkorb bleibt nach Login erhalten',
 'Warenkorb-Inhalt bleibt nach Login erhalten (Guest→User).',
 '1. Ohne Login Artikel in Warenkorb legen
2. Zum Login wechseln
3. Einloggen
4. Warenkorb enthält weiterhin die Artikel',
 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- REQ-2025-0004 Checkout & Zahlung
(130, 'Checkout mit gültiger Lieferadresse',
 'Checkout kann mit gültiger Adresse abgeschlossen werden.',
 '1. Warenkorb mit Artikeln öffnen
2. Checkout starten
3. Lieferadresse vollständig ausfüllen
4. Versandart wählen
5. Weiter zur Zahlung
6. Bestellübersicht erscheint',
 13, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(131, 'Checkout ohne Lieferadresse',
 'Pflichtfeldvalidierung verhindert Bestellung.',
 '1. Warenkorb öffnen
2. Checkout starten
3. Lieferadresse leer lassen
4. Weiter klicken
5. Validierungsfehler erscheint, kein Fortschritt',
 13, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(132, 'Zahlung per Kreditkarte erfolgreich',
 'Zahlung wird akzeptiert, Bestellung wird erstellt.',
 '1. Checkout bis Zahlung
2. Kreditkarte auswählen
3. Gültige Testkartendaten eingeben
4. Zahlung bestätigen
5. Bestellbestätigung wird angezeigt, Bestellnummer vorhanden',
 13, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(133, 'Zahlung abgelehnt (Kreditkarte)',
 'Abgelehnte Zahlung erzeugt keine Bestellung.',
 '1. Checkout bis Zahlung
2. Kreditkarte auswählen
3. Testkartendaten eingeben, die „decline“ simulieren
4. Zahlung bestätigen
5. Fehlermeldung, keine Bestellung erzeugt',
 13, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- REQ-2025-0005 Retouren & Erstattung

-- ----------------------------------------------------------
-- 5) Testläufe
-- ----------------------------------------------------------
INSERT INTO test_runs (id, readable_id, name, description, status, created_by, created_at, updated_at, requirement_id) VALUES
(200, 'TR-2025-0001', 'Release 1 – Login Smoke',
 'Smoke-Test für Registrierung und Login.',
 'IN_PROGRESS', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10),

(201, 'TR-2025-0002', 'Release 1 – Suche/Filter Regression',
 'Regression rund um Produktsuche und Filter.',
 'SUCCESSFUL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 11),

(202, 'TR-2025-0003', 'Release 1 – Warenkorb Kernfunktionen',
 'Add/Remove/Update und Persistenz des Warenkorbs.',
 'FAILED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 12),

(203, 'TR-2025-0004', 'Release 1 – Checkout & Payment',
 'E2E-Check vom Warenkorb bis Zahlung.',
 'IN_PROGRESS', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13),

(204, 'TR-2025-0005', 'Release 2 – Retouren Planung',
 'Geplanter Lauf, wird später befüllt.',
 'PLANNING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 14);

-- ----------------------------------------------------------
-- 6) Zuordnungen Testfall ↔ Testlauf (TestCaseTestRun)
-- Statusmodell: ASSIGNED / PASSED / FAILED
-- ----------------------------------------------------------
INSERT INTO test_case_test_run (id, test_case_id, test_run_id, tester_id, status) VALUES
-- TR-2025-0001 Login Smoke
(300, 100, 200, 5, 'PASSED'),
(301, 101, 200, 5, 'PASSED'),
(302, 102, 200, 6, 'ASSIGNED'),
(303, 103, 200, 6, 'FAILED'),

-- TR-2025-0002 Suche/Filter Regression (alles bestanden)
(310, 110, 201, 5, 'PASSED'),
(311, 111, 201, 5, 'PASSED'),
(312, 112, 201, 6, 'PASSED'),

-- TR-2025-0003 Warenkorb Kernfunktionen (mind. 1 Fail)
(320, 120, 202, 6, 'PASSED'),
(321, 121, 202, 6, 'PASSED'),
(322, 122, 202, 5, 'PASSED'),
(323, 123, 202, 5, 'FAILED'),

-- TR-2025-0004 Checkout & Payment (gemischt)
(330, 130, 203, 5, 'ASSIGNED'),
(331, 131, 203, 5, 'PASSED'),
(332, 132, 203, 6, 'ASSIGNED'),
(333, 133, 203, 6, 'FAILED');

-- TR-2025-0005 Retouren Planung: keine Zuordnungen -> bleibt PLANNING

-- ----------------------------------------------------------
-- 7) Optional: Testergebnis-Historie (TestResult)
-- ----------------------------------------------------------
INSERT INTO test_results (id, test_case_id, test_run_id, tester_id, status, notes, executed_at, created_at, updated_at) VALUES
(400, 100, 200, 5, 'PASSED', 'Registrierung ok.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(401, 103, 200, 6, 'FAILED', 'Fehlermeldung unklar/fehlender Hinweis.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(402, 112, 201, 6, 'PASSED', 'Kombifilter korrekt.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(403, 123, 202, 5, 'FAILED', 'Warenkorb nach Login leer – Bug.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(404, 133, 203, 6, 'FAILED', 'Decline erzeugt trotzdem Bestellung – Bug.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
