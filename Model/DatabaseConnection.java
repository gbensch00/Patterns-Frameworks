package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Die Klasse DatabaseConnection stellt eine Verbindung zur Datenbank her und bietet Methoden zum Abrufen 
 * der Verbindung und zum Schließen der Verbindung.

Die Klasse enthält die folgenden Methoden:

    DatabaseConnection(String url, String user, String password): Der Konstruktor der Klasse erhält die 
    Verbindungsparameter url, user und password. Er verwendet die DriverManager-Klasse, um eine Verbindung zur 
    Datenbank herzustellen. Wenn die Verbindung erfolgreich hergestellt wurde, wird das Connection-Objekt initialisiert.

    getConnection(): Diese Methode gibt das Connection-Objekt zurück, das die Verbindung zur Datenbank repräsentiert. 
    Sie ermöglicht anderen Klassen den Zugriff auf die Verbindung, um Datenbankoperationen durchzuführen.

    close(): Diese Methode schließt die Verbindung zur Datenbank. Sie wird verwendet, um die Verbindung ordnungsgemäß 
    zu beenden und Ressourcen freizugeben.

Die Klasse DatabaseConnection vereinfacht den Prozess der Herstellung einer Verbindung zur Datenbank, indem sie eine 
Schnittstelle zum Abrufen der Verbindung bereitstellt und die Verwaltung der Verbindungsressourcen übernimmt.
 */

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

}