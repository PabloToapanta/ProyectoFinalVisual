package proyectobetavisual.servicio;

import java.sql.SQLException;
import proyectobetavisual.dao.AdministradorDAO;
import proyectobetavisual.modelo.Administrador;

/** Gestiona el inicio de sesión del administrador. */
public class AdministradorServicio {

    private final AdministradorDAO administradorDAO = new AdministradorDAO();

    /** Devuelve la cuenta si coincide; devuelve null si las credenciales son incorrectas. */
    public Administrador iniciarSesion(String usuario, String clave) throws SQLException {
        String usuarioLimpio = validarCampo(usuario == null ? null : usuario.trim(), "El usuario", 50);
        validarCampo(clave, "La clave", 255);
        return administradorDAO.autenticar(usuarioLimpio, clave);
    }

    private String validarCampo(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        if (valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " no puede superar " + maximo + " caracteres.");
        }
        return valor;
    }
}
