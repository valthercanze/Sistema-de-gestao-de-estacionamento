package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL =
        "jdbc:mysql://localhost:3306/parque_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String UTILIZADOR = "root";
    private static final String PASSWORD = "root"; 

    private ConexaoBD() {}

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(URL, UTILIZADOR, PASSWORD);
    }
}