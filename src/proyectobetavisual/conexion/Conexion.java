package proyectobetavisual.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Abre conexiones con la base de datos local SistemaEstudiantil.
 */
public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/SistemaEstudiantil?serverTimezone=America/Guayaquil";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    /**
     * Intenta conectar con MySQL. Devuelve null si ocurre un error.
     */
    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar con la base de datos:\n" + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

}
