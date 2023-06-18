package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import Model.User;
import Model.UserDAO;
import Model.UserDAOImpl;
import Model.UserSettings;
import Model.UserSettingsDAO;
import Model.UserSettingsDAOImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Server {
    private static final int PORT = 1234;
    static UserDAO userDAO;
    static UserSettingsDAO userSettingsDAO;
	static String DBURL = "jdbc:mysql://localhost:3307/TestDB";
	static String DBUser = "root";
	static String DBPassword = "";

    public static void startServer() throws SQLException {
    	
    	try {
			Connection con = DriverManager.getConnection(DBURL, DBUser, DBPassword);
			userDAO = new UserDAOImpl(con);
			userSettingsDAO = new UserSettingsDAOImpl(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        try {
            // Server-Socket erstellen
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server gestartet. Warte auf Verbindungen...");

            while (true) {
                // Auf eingehende Verbindungen warten
                Socket clientSocket = serverSocket.accept();
                System.out.println("Neue Verbindung von " + clientSocket.getInetAddress());

                // Verbindung in eigenem Thread verarbeiten
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket clientSocket) {
        try {
            // Empfange das XML vom Client
            String xmlString = receiveXMLFromClient(clientSocket);

            // Parsen des XML-Dokuments
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

                // Prüfe den Typ des XML (Authentifizierung oder Erstellung eines neuen Benutzers)
                String xmlType = doc.getDocumentElement().getNodeName();

                if (xmlType.equals("Auth")) {
                    // Authentifizierung behandeln
                    handleAuthentication(clientSocket, doc);
                } else if (xmlType.equals("NewUser")) {
                    // Neuen Benutzer erstellen behandeln
                    handleNewUser(clientSocket, doc);
                } else {
                    // Ungültiges XML, Fehler an Client senden
                    sendInvalidRequestToClient(clientSocket);
                    System.out.println("Ungültige Anforderung vom Client");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sendInvalidRequestToClient(clientSocket);
                System.out.println("Fehler beim Verarbeiten der Anforderung vom Client");
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAuthentication(Socket clientSocket, Document doc) throws IOException {
        // Extrahiere die Authentifizierungsdaten aus dem XML
        String userID = doc.getElementsByTagName("Username").item(0).getTextContent();
        String password = doc.getElementsByTagName("Password").item(0).getTextContent();

        // Überprüfe die Authentifizierungsdaten
        User user = callUser(userID);
        if (user != null && password.equals(user.getPassword())) {
            // Authentifizierung erfolgreich
            sendAuthenticationConfirmationToClient(clientSocket);
            System.out.println("Authentifizierung erfolgreich");
        } else {
            // Authentifizierung fehlgeschlagen
            sendAuthenticationFailureToClient(clientSocket);
            System.out.println("Authentifizierung fehlgeschlagen");
        }
    }

    private static void handleNewUser(Socket clientSocket, Document doc) throws IOException {
        // Extrahiere die Benutzerdaten aus dem XML
        String userName = doc.getElementsByTagName("Username").item(0).getTextContent();
        String password = doc.getElementsByTagName("Password").item(0).getTextContent();

        User newUser = new User(userName, password);
        boolean success = userDAO.createUser(newUser);

        // Sende eine Bestätigung an den Client
        if (success) {
            sendNewUserConfirmationToClient(clientSocket);
            System.out.println("Neuer Benutzer erstellt");
        } else {
            sendUserCreationFailureToClient(clientSocket);
        }
    }

    private static void sendInvalidRequestToClient(Socket clientSocket) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
        writer.write("Invalid request");
        writer.flush();
    }

  

    private static String receiveXMLFromClient(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder xmlBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            xmlBuilder.append(line);
        }
        return xmlBuilder.toString();
    }

    private static void sendAuthenticationConfirmationToClient(Socket clientSocket) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
        writer.write("Authentication successful");
        writer.flush();
    }

    private static void sendAuthenticationFailureToClient(Socket clientSocket) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
        writer.write("Authentication failed");
        writer.flush();
    }
    
    private static void sendNewUserConfirmationToClient(Socket clientSocket) throws IOException{
    	 OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
         writer.write("Nutzer erfolgreich erstellt");
         writer.flush();
    }
    
    private static void sendUserCreationFailureToClient(Socket clientSocket) throws IOException{
   	 OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
        writer.write("Nutzer existiert bereits");
        writer.flush();
   }
    
    //Helferfunktion da durch das abändern auf DAO Objekte an die Funktionen übergeben werden müssen
    //Wusste mir nicht anders zu helfen... <Tobi>
    public static  User callUser (String user) {
    	User callDBUser = userDAO.getUserByName(user);
    	return callDBUser;
    }
    public static UserSettings callUserSettings (String user) throws SQLException {
    	UserSettings loadedUserSettings = userSettingsDAO.getUserSettingsByUserId(user);
    	return loadedUserSettings;
    }

}
