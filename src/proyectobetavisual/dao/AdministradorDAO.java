package proyectobetavisual.dao;

import proyectobetavisual.conexion.Conexion;
import proyectobetavisual.modelo.Administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Operaciones de acceso para cuentas administradoras. */
public class AdministradorDAO {

    private final Conexion conexion = new Conexion();

    /** Devuelve la cuenta si las credenciales coinciden; de lo contrario devuelve null. */
    public Administrador autenticar(String usuario, String clave) throws SQLException {
        String sql = "SELECT id, usuario, clave FROM administrador WHERE usuario = ? AND clave = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, clave);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return new Administrador(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("clave")
            );
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        Connection conn = conexion.conectar();
        if (conn == null) {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }
        return conn;
    }

    private void cerrar(ResultSet recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (SQLException ex) {
                // No se reemplaza el error original por uno ocurrido al cerrar.
            }
        }
    }

    private void cerrar(PreparedStatement recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (SQLException ex) {
                // No se reemplaza el error original por uno ocurrido al cerrar.
            }
        }
    }

    private void cerrar(Connection recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (SQLException ex) {
                // No se reemplaza el error original por uno ocurrido al cerrar.
            }
        }
    }
}
