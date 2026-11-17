package proyectobetavisual.dao;

import proyectobetavisual.conexion.Conexion;
import proyectobetavisual.modelo.Tarjeta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Operaciones de base de datos para tarjetas RFID. */
public class TarjetaDAO {

    private final Conexion conexion = new Conexion();

    public boolean insertar(Tarjeta tarjeta) throws SQLException {
        String sql = "INSERT INTO tarjeta (codigo_rfid, id_estudiante, estado) VALUES (?, ?, ?)";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tarjeta.getCodigoRfid());
            ps.setInt(2, tarjeta.getIdEstudiante());
            ps.setString(3, tarjeta.getEstado());
            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    public boolean actualizar(Tarjeta tarjeta) throws SQLException {
        String sql = "UPDATE tarjeta SET codigo_rfid = ?, id_estudiante = ?, estado = ? WHERE id = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tarjeta.getCodigoRfid());
            ps.setInt(2, tarjeta.getIdEstudiante());
            ps.setString(3, tarjeta.getEstado());
            ps.setInt(4, tarjeta.getId());
            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    public List<Tarjeta> listarTodas() throws SQLException {
        String sql = "SELECT id, codigo_rfid, id_estudiante, estado FROM tarjeta ORDER BY codigo_rfid";
        List<Tarjeta> tarjetas = new ArrayList<>();
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tarjetas.add(mapearTarjeta(rs));
            }
            return tarjetas;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public List<Tarjeta> listarPorEstudiante(int idEstudiante) throws SQLException {
        String sql = "SELECT id, codigo_rfid, id_estudiante, estado FROM tarjeta "
                + "WHERE id_estudiante = ? ORDER BY codigo_rfid";
        List<Tarjeta> tarjetas = new ArrayList<>();
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            rs = ps.executeQuery();
            while (rs.next()) {
                tarjetas.add(mapearTarjeta(rs));
            }
            return tarjetas;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public Tarjeta buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, codigo_rfid, id_estudiante, estado FROM tarjeta WHERE id = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next() ? mapearTarjeta(rs) : null;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public Tarjeta buscarPorCodigoRfid(String codigoRfid) throws SQLException {
        String sql = "SELECT id, codigo_rfid, id_estudiante, estado FROM tarjeta WHERE codigo_rfid = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigoRfid);
            rs = ps.executeQuery();
            return rs.next() ? mapearTarjeta(rs) : null;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public boolean cambiarEstado(int id, String estado) throws SQLException {
        String sql = "UPDATE tarjeta SET estado = ? WHERE id = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    private Tarjeta mapearTarjeta(ResultSet rs) throws SQLException {
        return new Tarjeta(
                rs.getInt("id"),
                rs.getString("codigo_rfid"),
                rs.getInt("id_estudiante"),
                rs.getString("estado")
        );
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
