package proyectobetavisual.servicio;

import java.sql.SQLException;
import proyectobetavisual.dao.AdministradorDAO;
import proyectobetavisual.modelo.Administrador;

/** Gestiona el inicio de sesión del administrador. */
public class AdministradorServicio {

    private final AdministradorDAO administradorDAO = new AdministradorDAO();

    /** Devuelve la cuenta si coincide; devuelve null si las credenciales son incorrectas. */
    public Administrador iniciarSesion(String usuario, String clave) throws SQLException {
        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.isEmpty()) {
            return null;
        }
        return administradorDAO.autenticar(usuario.trim(), clave);
    }
}
