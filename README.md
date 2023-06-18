# Semesterprojekt zum Kurs: Patterns and Frameworks SS 2023

### Berliner Hochschule für Technik / Medieninformatik B. online

![alt text](https://github.com/gbensch00/Patterns-Frameworks/blob/Ulf/res/oberflaechen/SpaceTypeLogin.png "Softwareprojekt SS 2023")

## Spiel
R-Type-Space Spiel

### Setup:
Git Clone https://github.com/gbensch00/Patterns-Frameworks.git

### Spiel Startseite
Auf der Startseite kann sich ein Spieler einloggen und registrieren.

### Registrierung und Login

Ein Spieler kann sich auf der Startseite (unten rechts) mittels Button "Hier klicken" einen Account einrichten. Hierzu erscheint ein neues Fenster wo der User einen Namen und Passwort anlegen muss.

Ist der Spieler bereits registriert, kann er sich mit seinem Usernamen und seinem Passwort einloggen (der Name ist einzigartig). Bei Falscheingaben werden Validierungsfehler angezeigt. Nach erfolgreichem Login erscheint das Cockpit Fenster.

### Cockpit Startseite

Nach erfolgreichem Login befindet sich der Spieler im Login-Bereich. Hier kann er mittels Button Einstellungen, einen Highscore, Logout, Singleplayer-Game oder Multiplaer-Game weitere Seiten des Spiels besuchen.

### Settings

Eingeloggte Spieler können hier weitere Einstellungen vornehmen. Zusätzlich kann er dort sein Profilbild ändern.

### Highscore

Hier werden die bisher gesammelten Punkte angezeigt.

### Eject

Möglichkeit des Logouts.

### Single-Game

Der Spieler kann ein Einzelgame starten.

### Multi-Game

Der Spieler kann hier gegen einen anderen Spieler antreten. Nach klick auf dem Button wird er gefragt, ob es einen weiteren Spieler gibt. Sollte dies der Fall sein, kann der zweite User seinen Namen eingeben.

### Spielanleitung

Einzelspiel: Der Spieler bewegt sich mittels den Tasten w, s, a, d. Schießen kann der mit der Space-Taste. Ziel des Spiels ist es, in 60 Sekunden so viele wie möglich Gegner abzuschießen. Dabei gibt es einfache Gegner und spezielle Gegner. Beim Abschuss der einfachen Gegner bekommt der Spieler einen Punkt und beim Abschuss der Spezielgegner 5 Punkte. Der Spieler kann vom Gegner 3x getroffen werden. Nach dem dritten Treffer wird das Spiel beendet. Wird der Spieler nicht getroffen, so wird das Spiel automatisch nach 60 Sekunden beendet.


## Development

### Client starten
Über die Main Klasse kann das Java Spiel gestartet werden.

### Server und Datenbank starten

Für den Server wurde Xampp verwendet.
Die Spieldaten werden in einer MySQL Datenbank gespeichert.

Zur einfacheren Bereitstellung eines Webservers und einer Datenbank wurde auf das Programm XAMP zurückgeriffen. Auf XAMP wurde über phpmyadmin eine Datenbank erstellt, die den klangvollen Namen TestDB erhalten hat. In dieser existieren zwei Tabellen zur Datenspeicherung. Zum einen die Tabelle PLAYER welche Informationen über Nutzernamen, Passwörter und Highscores enthält. Zum anderen die Tabelle UserSettings in der Informationen wie Avatar, FontSize, FontType, Resolution und BackgroundColor gespeichert sind. Diese Tabelle dient dazu, um User Custumizations durchführen zu können, um so z.B. das Interface farblich anzupassen oder Attribute wie Auflösung oder Schriftgröße zu ändern.

Zum Anmelden wird beim Starten des Spieles nach den Login-Informationen verlangt, diese werden in den Feldern Username und Password eingetragen. Mit einem Klick auf Login werden diese Daten in eine XML-Datei umgewandelt und an den Server geschickt. Wenn die Übertragung erfolgreich war, wird die Benutzeroberfläche aktualisiert, um eine neue Szene (die "Lobby") angezeigt und die alte Szene (das Anmeldefenster) wird geschlossen. Sollte die Anmeldung nicht erfolgreich sein und false durch den Server gemeldet werden, erscheint ein Alert, in dem dem Nutzer mitgeteilt wird, dass der Nutzer nicht existiert oder das verwendete Passwort falsch ist.

### Architektur und Verwendung Pattern

MVC, DAO (Data Access Object), Observer Pattern und teilweisew auch Dependency Injection bei DB-abfragen.

MVC - Warum der Einsatz: 
- Trennung der Verantwortlichkeiten, indem die Logik in drei separate Komponenten getrennt wird, wodurch die Anwendung einfacher zu verstehen und zu warten ist. Das macht es auch einfacher für mehrere Entwickler an verschiedenen Komponenten zu arbeiten ohne sich gegenseitig zu behindern.
- MVC ist sehr skalierbar, weil man Komponenten hinzufügen und entfernen kann, ohne die anderen zu beeinträchtigen.
- Beispiel zu konkreter Implementierung: Der GameController nimmt alle User-Inputs der Tastatur entgegen, wenn Inputs den Tasten WASD/Leertaste oder bei 2 Spielern auch den Pfeiltasten/Enter entsprechen, werden die Funktionen Player.setVelocity() oder shoot() aufgerufen, die dann in den jeweiligen Model- oder Viewklassen veränderungen im Spiel wie hier zum Beispiel die Spielerbewegung bzw. die Schüsse erzeugen.

Observer Pattern eignet sich für ein R-Type Spiel sehr gut: In dem Observer Pattern gibt es ein Subjekt an das ein oder mehrere 'Observer' gekoppelt sind, über die das Subjekt selbst gar nichts wissen muss, außer dass sie ein gewisses Interface implementieren. Ein Beispiel: In unserem R-Type Spiel ist der player character das Subjekt. Dieses Subjekt nimmt Schaden von einem Gegner, wodurch sich der health stat verringert. Das Subjekt benachrichtigt den dazugehörigen Observer, also die UI Komponente 'Health Bar', wie viel Health noch übrig bleibt und die Health Bar wird geupdated. Dasselbe könnte man z.B. mit Geschwindigkeits oder Schadenspowerups machen.

Datenobjekte: Diese repräsentieren Entitäten oder Datenbanktabellen und enthalten in der Regel Eigenschaften (Attribute) und Methoden, um auf die Daten zuzugreifen oder diese zu manipulieren.
Datenzugriffsschicht (DAO): Die DAO-Komponente ist für den tatsächlichen Zugriff auf die Datenbank verantwortlich. Sie bietet Methoden zum Erstellen, Lesen, Aktualisieren und Löschen von Datenobjekten. Die DAO-Komponente kapselt die Datenbankdetails und abstrahiert diese vor der restlichen Anwendung.
Geschäftslogik: Dies sind die übrigen Teile der Anwendung, die auf die Datenbank zugreifen möchten. Anstatt direkt auf die Datenbank zuzugreifen, verwenden sie die Methoden der DAO-Komponente, um Daten zu erhalten oder zu ändern.
In unserem Spiel sind diese drei Hauptkomponenten User, UserSettings, UserDAO, UserSettingsDAO, UserDAOImpl und UserSettingsDAOImpl

### Dokumentation

### UI-Frameworks
JavaFX

### Threading-Verwendung

Threading wird in der Main für den Login Server verwendet. Der Thread muss geöffnet werden weil es sich um einen "haltenen" Prozess handelt. Dies haben wir so umgesetzt, da ansonsten unsere Main-Klasse nicht das fxml startet. Hintergrund ist, der Server möchte erst eine Verbindung einrichten bevor weitere Prozesse gestartet werden. Das Threading wurde für die Datenbank verwendet.


