# Zeiterfassung - Projektbeschreibung

## Kurzbeschreibung

Mittels REST-Schnittstelle soll ein einzelner Mitarbeiter seine Arbeitszeiten erfassen können.

Hierzu sollen Einträge erstellbar, lesbar, editierbar und löschbar sein.

Außerdem soll über einen Zeitraum hinweg die erfasste Arbeitszeit berechenbar sein. Der Abzug von Pausenzeiten soll in das Ergebnis mit einfließen.

## Ziele

1. REST-Prototyp mittels Spring Boot und Java 21 um grundlegende Erfahrungen aufzufrischen
2. Vorbereitungsprojekt um erweitertes Projekt: Zeiterfassung von Aktivitäten und Auswertungsgrafiken
   - grundlegende Dependencies einbinden für Basisprojekte, ua. auch Style-Autoformatter, Junit und ähnliche regulär zu verwendende Dependencies 
3. optional sind Kenntnisse in Streams, Mapper und Lambda aufzufrischen
4. vorerst optional und von geringster Priorität ist die Einführung von CI-Vorgehen

## Zeitrahmen

- Fertigstellung bis 20. März
- bis zu 10 Personenstunden / Woche
- Meilenstein: Review Grafiken und Entwürfe bis 12. März nach Einarbeitung und Vertiefung von Kenntnissen zu REST

## Funktionale Anforderungen

- REST-API, JSON
- Endpunkte verfügen über die Möglichkeit für einen einzelnen Benutzer die Zeiterfassung zu starten und zu beenden
- CRUD der REST-API vollumfänglich für einzelne spezifische Zeitmessungen eines bestimmten Benutzers

## Qualitätsanforderungen

- Für den Endnutzer "benutzerfreundliche", dh. nachvollziehbare, aufschlussreiche und eindeutige Fehlermeldungen bei nicht gefundenen Benutzer und ungültigen Zeiterfassungseinträgen (Check-In ohne vorherigen Check-Out)
- Dokumentation erfolgt mindestens zu Datenmodell in grafischer Form, Abbildung von Entitäten hat zu erfolgen für:
  - Benutzer: Speichert Informationen über Mitarbeiter, z. B. Name, Benutzer-ID.
  - Zeiterfassungseintrag: Enthält Angaben zu Zeitstempeln wie Check-in, Check-out, Datum und Uhrzeit.
- Eine Methode im Programm muss ermöglichen und beweisen:
  - alle Zeiterfassungseinträge eines bestimmten Benutzers innerhalb eines Datumsbereichs auszugeben
  - Die geleistete Arbeitszeit zu berechnen und diese abzüglich der gesetzlich vorgeschriebenen Pausenzeiten zurück zu geben.
- Eine Repository-Abfrage, dh. Methode, muss ermöglichen und beweisen: 
  - alle Zeiterfassungseinträge eines bestimmten Benutzers innerhalb eines Datumsbereichs auszugeben
  
## Nicht-Anforderungen

- Benutzer müssen vorerst nicht anlegbar sein (ein simples SQL-Script reicht aus)
- CRUD-Funktionalität der REST-API ist nicht für mehrere Zeitmessungen in einem Zeitraum oder für einen Benutzer in einem Zeitraum zu realisieren

## Überlegungen zu optionalen Anforderungen

*eventuell für Entwurf relevant*

- Zeiterfassung in Zusammenhang mit Schichten und Pausenzeiten?
- Ausnahmezustand des Starts einer Zeiterfassung bei bereits erfolgtem Start: JSON-Objekt enthält: Nachricht, Fehlertyp, Fehler-ID, Zeitpunkt - und mehr?
- Das ein Benutzer die eine Messung starten und beenden kann, könnte eine Benutzergruppe darstellen. Die Gruppe mit Zusatzrechten kann eventuell Zeitpunkte editieren und löschen.
- Update und Delete könnte an Zeitlimits gebunden sein (erst nach drei Tagen darf ich eine Messung löschen, nur innerhalb von 24 Stunden darf ich eine Messung editieren).
- Benutzer CRUD-Funktionalitäten (niedrigste Priorität)