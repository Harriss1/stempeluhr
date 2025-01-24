# Testaufgabe Zeiterfassung

## Allgemeines

- ab 23.01.2025, Abgabe spätestens Donnerstag 30.01.2025
- Übergabe in Form von Git-Repository-Link

## Ziele

1. Abbildung: Wie arbeitet der Projektnehmer? Kommunikationsart, Art der Dokumentation, Art des Vorgehens. Gemäß dem Credo "Setzen Sie Ihre Ideen kreativ und strukturiert um."
2. Aufgabenstellung gemäß PDF bewältigen.

## Funktionale Anforderungen

- REST-API, JSON
- Endpunkte verfügen über die Möglichkeit für einen einzelnen Benutzer die Zeiterfassung zu starten und zu beenden
- CRUD der REST-API vollumfänglich für einzelne spezifische Zeitmessungen eines bestimmten Benutzers

## Qualitätsanforderungen

- Für den Endnutzer "benutzerfreundliche", dh. nachvollziehbare, aufschlussreiche und eindeutige Fehlermeldungen bei nicht gefundenen Benutzer und ungültigen Zeiterfassungseinträgen (Check-In ohne vorherigen Check-Out)
- Dokumentation und Quellcode sind mit Versionverwaltungssystem Git zu hosten
- Dokumentation erfolgt mindestens zu Datenmodell in grafischer Form, Abbildung von Entitäten hat zu erfolgen für:
  - Benutzer: Speichert Informationen über Mitarbeiter, z. B. Name, Benutzer-ID.
  - Zeiterfassungseintrag: Enthält Angaben zu Zeitstempeln wie Check-in, Check-out, Datum und Uhrzeit.
- Eine Methode im Programm muss ermöglichen und beweisen:
  - alle Zeiterfassungseinträge eines bestimmten Benutzers innerhalb eines Datumsbereichs auszugeben
  - Die geleistete Arbeitszeit zu berechnen und diese abzüglich der gesetzlich vorgeschriebenen Pausenzeiten zurück zu geben.
- Eine Repository-Abfrage, dh. Methode, muss ermöglichen und beweisen: 
  - alle Zeiterfassungseinträge eines bestimmten Benutzers innerhalb eines Datumsbereichs auszugeben
  
## Nicht-Anforderungen

- Buildmanangement-Tool muss nicht Gradle sein (Maven ist in Ordnung)
- Datenbank muss nicht PostgreSQL sein (H2 wäre ein empfehlenswerte Option)
- Benutzer müssen vorerst nicht anlegbar sein (ein simples SQL-Script reicht aus)
- CRUD-Funktionalität der REST-API ist nicht für mehrere Zeitmessungen in einem Zeitraum oder für einen Benutzer in einem Zeitraum zu realisieren

## Überlegungen zu optionalen Anforderungen

*eventuell für Entwurf relevant*

- Zeiterfassung pausieren?
- Ausnahmezustand des Starts einer Zeiterfassung bei bereits erfolgtem Start: JSON-Objekt enthält: Nachricht, Fehlertyp, Fehler-ID, Zeitpunkt - und mehr?
- Das ein Benutzer die eine Messung starten und beenden kann, könnte eine Benutzergruppe darstellen. Die Gruppe mit Zusatzrechten kann eventuell Zeitpunkte editieren und löschen.
- Update und Delete könnte an Zeitlimits gebunden sein (erst nach drei Tagen darf ich eine Messung löschen, nur innerhalb von 24 Stunden darf ich eine Messung editieren).
- Benutzer CRUD-Funktionalitäten (niedrigste Priorität)