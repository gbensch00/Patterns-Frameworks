# Semesterprojekt zum Kurs: Patterns and Frameworks SS 2023

### Berliner Hochschule für Technik / Medieninformatik B. online

![Alternativer Text](https://github.com/gbensch00/Patterns-Frameworks/blob/Tobi/res/oberflaechen/Login.png)


## Spiel
Space-Type Shoot'em Up 

## Idee
Bei Space-Type handelt es sich um ein Softwareprojekt von drei Studenten der Berliner Hochschule für Technik.
Die Idee und inspiration von Space-Type kam vom Spiel Super R-Type aus dem Jahre 1991 welches für das Super Nintendo erschien.

## Startbedingungen
Um das Spiel spielen und starten zu können ist es wichtig einen Webserver inklusive Datenbank zu haben. Anbieten würde sich hierfür XAMPP (diesen nutzten wir selbst fürs Testen). Die MySQL DB sollte auf Port: 3307 betrieben werden, da dies der Port ist, auf dem die Server Class die Verbindung zur DB aufbaut.
Sobald XAMPP läuft, sollte sich die TestDB-3.sql heruntergeladen werden und über PHPMyAdmin importiert werden. Die DB befindet Sicht im Root-Verzeichnis dieses Repos. Sobald diese Schritte abgeschlossen sind, kann das Projekt in eine beliebige Entwicklungsumgebung importiert werden (Eclipse, Visual Studio Code oder IntelliJ).

### Eclipse:
Hinzufügen der Startbedingungen für die Main.java
--module-path /Users/username/Downloads/javafx-sdk-17.0.6/lib --add-modules javafx.controls,javafx.fxml,javafx.media
und den Haken entfernen beim Punkt ... when launching with SWT
Haken setzen bei: Use the -XX...

### Buildpath
Modulpath: JavaFx20, JavaFXSDK, OpenJDK20
Classpath: mysql-connector-java-8.0.2.5 (zu finden unter Extra Libs im Repo)

### Setup:
Git Clone https://github.com/gbensch00/Patterns-Frameworks.git

# Spiel Startseite
Sobald das Spiel gestartet wurde landet man auf dem Login-Screen, hier kann sich ein Spieler einloggen und registrieren.

### Registrierung und Login

Ein Spieler kann sich auf der Startseite (unten rechts) mittels Button "Click here" einen Account einrichten. Hierzu erscheint ein neues Fenster wo der User einen Namen und Passwort anlegen muss. Das Spiel überprüft in der DB ob ein Account mit dem selben Namen bereits existiert und gibt dem Nutzer entsprechend feedback.

Ist der Spieler registriert, kann er sich mit seinem Usernamen und seinem Passwort einloggen (der Name ist einzigartig). Bei Falscheingaben werden Validierungsfehler angezeigt. Nach erfolgreichem Login erscheint das Cockpit Fenster.

### Cockpit Startseite

Nach erfolgreichem Login befindet sich der Spieler im Login-Bereich (das Cockpit). Hier kann er mittels Buttons wie "Settings", "Highscore", "Eject", "Singleplayer" oder "Multiplaer" weitere Seiten des Spiels besuchen.

### Settings

Eingeloggte Spieler können hier weitere Einstellungen vornehmen:
- Anpassung der Font
- Anpassung der Font-Size
- Anpassung der Window-Size (Auflösung)
- Anpassung der Cockpit-Farbe im Cockpit-Menü
Zusätzlich ist es dort möglich sein Profilbild ändern.
Das Profilbild sollte ein PNG sein und die größe von 1MB nicht überschreiten.

### Highscore

Hier werden die bisher gesammelten Punkte der Top 10 Spieler angezeigt.

### Eject

Möglichkeit des Logouts.

### Singleplayer-Game

Der Spieler kann ein Einzelgame lokal starten.

### Multiplayer-Game

Der Spieler kann hier lokal mit einen anderen Spieler gemeinsam antreten. Nach klick auf dem Button wird er gefragt, ob es einen weiteren Spieler gibt. Sollte dies der Fall sein, kann der zweite User seinen Namen eingeben.

### Spielanleitung

Einzelspiel: Der Spieler bewegt sich mittels den Tasten w, s, a, d. Geschossen wird mit der Space-Taste. Ziel des Spiels ist es, in 60 Sekunden so viele wie möglich Gegner abzuschießen. Dabei gibt es einfache Gegner und spezielle Gegner. Beim Abschuss der einfachen Gegner bekommt der Spieler einen Punkt und beim Abschuss der Spezielgegner 5 Punkte. Der Spieler kann vom Gegner 3x getroffen werden. Nach dem dritten Treffer wird das Spiel beendet. Wird der Spieler nicht getroffen, so wird das Spiel automatisch nach 60 Sekunden beendet. Zusätzlich hat der Spieler die möglichkeit PowerUps einzusammeln die die Zeit um 15 Sekunden verlängern um mehr Punkte zu erhalten.


## Development

### Client starten
Über die Main Klasse kann das Java Spiel gestartet werden.

### Server und Datenbank starten

Für den DB-Server wurde Xampp verwendet.
Die Spieldaten werden in einer MySQL Datenbank gespeichert.

Zur einfacheren Bereitstellung eines Webservers und einer Datenbank wurde auf das Programm XAMPP zurückgeriffen. Auf XAMPP wurde über phpmyadmin eine Datenbank erstellt, die den klangvollen Namen TestDB erhalten hat. In dieser existieren zwei Tabellen zur Datenspeicherung. Zum einen die Tabelle PLAYER welche Informationen über Nutzernamen, Passwörter und Highscores enthält. Zum anderen die Tabelle UserSettings in der Informationen wie Avatar, FontSize, FontType, Resolution und BackgroundColor gespeichert sind. Diese Tabelle dient dazu, um User Custumizations durchführen zu können, um so z.B. das Interface farblich anzupassen oder Attribute wie Auflösung oder Schriftgröße zu ändern.

Zum Anmelden wird beim Starten des Spieles nach den Login-Informationen verlangt, diese werden in den Feldern Username und Password eingetragen. Mit einem Klick auf Login werden diese Daten in eine XML-Datei umgewandelt und an den Server geschickt. Wenn die Übertragung erfolgreich war, wird die Benutzeroberfläche aktualisiert, um eine neue Szene (die "Lobby") angezeigt und die alte Szene (das Anmeldefenster) wird geschlossen. Sollte die Anmeldung nicht erfolgreich sein und false durch den Server gemeldet werden, erscheint ein Alert, in dem dem Nutzer mitgeteilt wird, dass der Nutzer nicht existiert oder das verwendete Passwort falsch ist.

### Architektur und Verwendung Pattern

MVC, DAO (Data Access Object), Observer Pattern und teilweise auch Dependency Injection bei DB-abfragen.

MVC:
- Trennung der Verantwortlichkeiten, indem die Logik in drei separate Komponenten getrennt wird, wodurch die Anwendung einfacher zu verstehen und zu warten ist. Das macht es auch einfacher für mehrere Entwickler an verschiedenen Komponenten zu arbeiten ohne sich gegenseitig zu behindern.

- MVC ist sehr skalierbar, weil man Komponenten hinzufügen und entfernen kann, ohne die anderen zu beeinträchtigen.
- Beispiel zu konkreter Implementierung: Der GameController nimmt alle User-Inputs der Tastatur entgegen, wenn Inputs den WASD/Leertaste oder bei 2 Spielern auch den Pfeiltasten/Enter entsprechen, werden die Funktionen Player.setVelocity() oder shoot() aufgerufen, die dann in den jeweiligen Model- oder Viewklassen veränderungen im Spiel wie hier zum Beispiel die Spielerbewegung bzw. die Schüsse erzeugen.


Observer: 
- In dem Observer Pattern gibt es ein Subjekt an das ein oder mehrere 'Observer' gekoppelt sind, über die das Subjekt selbst gar nichts wissen muss, außer dass sie ein gewisses Interface implementieren. Dadurch wird der Code skalierbarer und flexibler.
-  Ein Beispiel: In unserem R-Type Spiel ist der player character das Subjekt. Dieses Subjekt nimmt Schaden von einem Gegner, wodurch sich der health stat verringert. Das Subjekt benachrichtigt den dazugehörigen Observer, also die UI Komponente 'Health Bar', wie viel Health noch übrig bleibt und die Health Bar wird geupdated. Dasselbe könnte man z.B. mit Geschwindigkeits oder Schadenspowerups machen.

Datenobjekte: Diese repräsentieren Entitäten oder Datenbanktabellen und enthalten in der Regel Eigenschaften (Attribute) und Methoden, um auf die Daten zuzugreifen oder diese zu manipulieren.
Datenzugriffsschicht (DAO): Die DAO-Komponente ist für den tatsächlichen Zugriff auf die Datenbank verantwortlich. Sie bietet Methoden zum Erstellen, Lesen, Aktualisieren und Löschen von Datenobjekten. Die DAO-Komponente kapselt die Datenbankdetails und abstrahiert diese vor der restlichen Anwendung.
Geschäftslogik: Dies sind die übrigen Teile der Anwendung, die auf die Datenbank zugreifen möchten. Anstatt direkt auf die Datenbank zuzugreifen, verwenden sie die Methoden der DAO-Komponente, um Daten zu erhalten oder zu ändern.
In unserem Spiel sind diese drei Hauptkomponenten User, UserSettings, UserDAO, UserSettingsDAO, UserDAOImpl und UserSettingsDAOImpl

### Dokumentation

### UI-Frameworks
JavaFX

### Threading-Verwendung
Threading wurde in der Hinsicht verwendet, dass Login, DB-Abfragen und Aktualisierung über die Server Klasse durchgeführt wird, welche in einem parallelen Thread läuft.
Login: Da das Programm nur einen Einstiegspunkt hat (Main.java) und es nicht separaten Startbereich nur für den Server existiert, wurde hier auf Threading zurückgegriffen, da der Server auf eine eingehende Verbindung wartet und den Quellcode sonst nicht weiter ausgeführt werden kann (was ein Starten des Login-Screens verhindert).
Der Server lauscht dann auf den Port: 1234, ob sich eine Verbindung aufbaut und ob es sich dabei um einen Login (Auth) oder eine Nutzererstellung handelt (NewUser).

