package com.iu.require4testing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity-Klasse für einen Testfall.
 *
 * Ein Testfall beschreibt die konkreten Schritte und Erwartungswerte zur Verifikation einer Anforderung.
 * Er beinhaltet eine fachliche ID (z. B. TC-001), die über alle Testläufe hinweg fortlaufend ist.
 *
 * @author Require4Testing Team
 * @version 1.6.0
 */
@Entity
@Table(name = "test_cases")
public class TestCase {

    /**
     * Der technische Primärschlüssel der Datenbanktabelle.
     * Wird automatisch generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Eine fachliche, für Menschen lesbare ID (z. B. TC-001).
     * Muss im System eindeutig sein und wird über alle Testläufe hinweg fortlaufend vergeben.
     */
    @Column(name = "readable_id", unique = true, nullable = false)
    private String readableId;

    /**
     * Der Name oder Kurztitel des Testfalls.
     * Darf nicht null sein und hat eine maximale Länge von 500 Zeichen.
     */
    @Column(nullable = false, length = 500)
    private String name;

    /**
     * Eine detaillierte Beschreibung des Test-Szenarios.
     * Wird als Text-Blob in der Datenbank gespeichert.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Die konkreten auszuführenden Testschritte zur Reproduktion.
     * Wird als Text-Blob in der Datenbank gespeichert.
     */
    @Column(name = "test_steps", columnDefinition = "TEXT")
    private String testSteps;

    /**
     * Die ID des Benutzers, der diesen Testfall ursprünglich angelegt hat.
     * Darf nicht null sein.
     */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /**
     * Der Zeitstempel der Erstellung des Datensatzes.
     * Wird automatisch beim ersten Speichern gesetzt und kann nicht geändert werden.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Der Zeitstempel der letzten inhaltlichen Änderung am Testfall.
     * Wird bei jedem Datenbank-Update automatisch aktualisiert.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Die verknüpfte Anforderung (Requirement), zu der dieser Testfall gehört.
     * Es besteht eine Many-to-One Beziehung; eine Anforderung kann viele Testfälle besitzen.
     */
    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    /**
     * Lifecycle-Methode: Wird automatisch vor dem ersten Speichern in der Datenbank aufgerufen.
     * Initialisiert die Zeitstempel und setzt einen Standard-Ersteller, falls keiner angegeben wurde.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (createdBy == null) createdBy = 1L;
    }

    /**
     * Lifecycle-Methode: Wird automatisch vor jedem Update-Vorgang in der Datenbank aufgerufen.
     * Aktualisiert den Zeitstempel für die letzte Änderung.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Liefert die technische Identifikationsnummer (Primärschlüssel) zurück.
     * @return Die technische ID als Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Legt die technische Identifikationsnummer fest.
     * @param id Die neue technische ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Liefert die fachliche, lesbare ID (z. B. TC-001) zurück.
     * @return Die fachliche ID als String.
     */
    public String getReadableId() {
        return readableId;
    }

    /**
     * Setzt die fachliche, lesbare ID für diesen Testfall fest.
     * @param readableId Die neue fachliche ID (z. B. TC-001).
     */
    public void setReadableId(String readableId) {
        this.readableId = readableId;
    }

    /**
     * Liefert den Namen oder Kurztitel des Testfalls zurück.
     * @return Der Name des Testfalls.
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Namen oder Kurztitel des Testfalls fest.
     * @param name Der neue Name des Testfalls.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Liefert die detaillierte Beschreibung des Testfalls zurück.
     * @return Die Beschreibung als String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt eine neue detaillierte Beschreibung für den Testfall fest.
     * @param description Die neue Beschreibung.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Liefert die Liste der Testschritte zurück.
     * @return Die Testschritte als String.
     */
    public String getTestSteps() {
        return testSteps;
    }

    /**
     * Legt die auszuführenden Testschritte fest.
     * @param testSteps Die neuen Testschritte.
     */
    public void setTestSteps(String testSteps) {
        this.testSteps = testSteps;
    }

    /**
     * Liefert die Benutzer-ID des Erstellers zurück.
     * @return Die ID des Erstellers als Long.
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * Legt fest, welcher Benutzer diesen Testfall erstellt hat.
     * @param createdBy Die User-ID des Erstellers.
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Liefert den Zeitpunkt der Erstellung zurück.
     * @return Das Erstellungsdatum als LocalDateTime.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Legt den Zeitpunkt der Erstellung fest.
     * @param createdAt Das neue Erstellungsdatum.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Liefert den Zeitpunkt der letzten inhaltlichen Änderung zurück.
     * @return Das Änderungsdatum als LocalDateTime.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Legt den Zeitpunkt der letzten Änderung fest.
     * @param updatedAt Das neue Änderungsdatum.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Liefert die zugehörige Anforderung (Requirement) zurück.
     * @return Das verknüpfte Requirement-Objekt.
     */
    public Requirement getRequirement() {
        return requirement;
    }

    /**
     * Verknüpft diesen Testfall mit einer spezifischen Anforderung.
     * @param requirement Das neue Requirement-Objekt.
     */
    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    /**
     * Hilfsmethode für das Mapping auf Datentransferobjekte (DTOs).
     * Liefert die technische ID der verknüpften Anforderung.
     * @return Die ID der Anforderung oder null, falls keine verknüpft ist.
     */
    public Long getRequirementId() {
        return requirement != null ? requirement.getId() : null;
    }
}