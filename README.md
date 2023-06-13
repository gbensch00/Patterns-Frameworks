# Patterns-Frameworks

#H1 Semesterprojekt zum Kurs: Patterns and Frameworks SS 2023
#H3 Berliner Hochschule für Technik / Medieninformatik B. online

![alt text](https://github.com/gbensch00/Patterns-Frameworks/blob/Ulf/res/oberflaechen/SpaceTypeLogin.png "Softwareprojekt SS 2023")

Setup:
Git Clone https://github.com/gbensch00/Patterns-Frameworks.git

# README-Vorlage

#H3 Spiel Startseite
Auf der Startseite kann sich ein Spieler einloggen und registrieren.

#H3 Registrierung und Login

Ein Spieler kann sich auf der Startseite (unten rechts) mittels Button "Hier klicken" einen Account einrichten. Hierzu erscheint ein neues Fenster wo der User einen Namen und Passwort anlegen muss.

Ist der Spieler bereits registriert, kann er sich mit seinem Usernamen und seinem Passwort einloggen (der Name ist einzigartig). Bei Falscheingaben werden Validierungsfehler angezeigt. Nach erfolgreichem Login erscheint das Cockpit Fenster.

#H3 Cockpit Startseite

Nach erfolgreichem Login befindet sich der Spieler im Login-Bereich. Hier kann er mittels Button Einstellungen, einen Highscore, Logout, Singleplayer-Game oder Multiplaer-Game weitere Seiten des Spiels besuchen.

#H3 Settings

Eingeloggte Spieler können hier weitere Einstellungen vornehmen. Zusätzlich kann er dort sein Profilbild ändern.

#H3 Highscore

Hier werden die bisher gesammelten Punkte angezeigt.

#H3 Eject

Möglichkeit des Logouts.

#H3 Single-Game

Der Spieler kann ein Einzelgame starten.

#H3 Multi-Game

Der Spieler kann hier gegen einen anderen Spieler antreten. Nach klick auf dem Button wird er gefragt, ob es einen weiteren Spieler gibt. Sollte dies der Fall sein, kann der zweite User seinen Namen eingeben.

#H3 Spielanleitung

Einzelspiel: Der Spieler bewegt sich mittels den Tasten w, s, a, d. Schießen kann der mit der Space-Taste. Ziel des Spiels ist es, in 60 Sekunden so viele wie möglich Gegner abzuschießen. Dabei gibt es einfache Gegner und spezielle Gegner. Beim Abschuss der einfachen Gegner bekommt der Spieler einen Punkt und beim Abschuss der Spezielgegner 5 Punkte. Der Spieler kann vom Gegner 3x getroffen werden. Nach dem dritten Treffer wird das Spiel beendet. Wird der Spieler nicht getroffen, so wird das Spiel automatisch nach 60 Sekunden beendet.

#H2 Development

#H3 Client starten
Über die Main Klasse kann das Java Spiel gestartet werden.

#3 Server und Datenbank starten

Für den Server wurde Xampp verwendet.

Die Spieldaten werden in einer MySQL Datenbank gespeichert.

#H3 Dokumentation


#H3 Frameworks und libraries

