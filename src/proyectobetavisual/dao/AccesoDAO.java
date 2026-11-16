package proyectobetavisual.dao;

import proyectobetavisual.conexion.Conexion;
import proyectobetavisual.modelo.Acceso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Operaciones de base de datos para el historial de accesos. */
public class AccesoDAO {

    private final Conexion conexion = new Conexion();

    /** Registra accesos autenticados y denegados; MySQL completa la fecha si llega null. */
    public boolean registrar(Acceso acceso) throws SQLException {
        String sql;
        if (acceso.getFechaHora() == null) {
            sql = "INSERT INTO acceso (id_tarjeta, id_estudiante, codigo_leido, resultado) "
                    + "VALUES (?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO acceso "
                    + "(id_tarjeta, id_estudiante, codigo_leido, fecha_hora, resultado) "
                    + "VALUES (?, ?, ?, ?, ?)";
        }

        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            if (acceso.getIdTarjeta() == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, acceso.getIdTarjeta());
            }
            if (acceso.getIdEstudiante() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, acceso.getIdEstudiante());
            }
            ps.setString(3, acceso.getCodigoLeido());

            if (acceso.getFechaHora() == null) {
                ps.setString(4, acceso.getResultado());
            } else {
                ps.setTimestamp(4, Timestamp.valueOf(acceso.getFechaHora()));
                ps.setString(5, acceso.getResultado());
            }

            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    /** Las fechas de inicio y fin son inclusivas. Los filtros pueden omitirse con null. */
    public List<Acceso> listarFiltrado(LocalDate desde, LocalDate hasta,
            Integer idEstudiante) throws SQLException {
        String sql = "SELECT id, id_tarjeta, id_estudiante, codigo_leido, fecha_hora, resultado "
                + "FROM acceso";
        String siguienteCondicion = " WHERE ";

        if (desde != null) {
            sql += siguienteCondicion + "fecha_hora >= ?";
            siguienteCondicion = " AND ";
        }
        if (hasta != null) {
            sql += siguienteCondicion + "fecha_hora < ?";
            siguienteCondicion = " AND ";
        }
        if (idEstudiante != null) {
            sql += siguienteCondicion + "id_estudiante = ?";
        }
        sql += " ORDER BY fecha_hora DESC";

        List<Acceso> accesos = new ArrayList<>();
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            int indice = 1;
            if (desde != null) {
                ps.setTimestamp(indice++, Timestamp.valueOf(desde.atStartOfDay()));
            }
            if (hasta != null) {
                ps.setTimestamp(indice++, Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));
            }
            if (idEstudiante != null) {
                ps.setInt(indice, idEstudiante);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                accesos.add(mapearAcceso(rs));
            }
            return accesos;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    private Acceso mapearAcceso(ResultSet rs) throws SQLException {
        Number idTarjetaLeido = (Number) rs.getObject("id_tarjeta");
        Number idEstudianteLeido = (Number) rs.getObject("id_estudiante");
        Timestamp fechaHora = rs.getTimestamp("fecha_hora");

        return new Acceso(
                rs.getInt("id"),
                idTarjetaLeido == null ? null : idTarjetaLeido.intValue(),
                idEstudianteLeido == null ? null : idEstudianteLeido.intValue(),
                rs.getString("codigo_leido"),
                fechaHora == null ? null : fechaHora.toLocalDateTime(),
                rs.getString("resultado")
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
            }
        }
    }

    private void cerrar(PreparedStatement recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (SQLException ex) {
            }
        }
    }

    private void cerrar(Connection recurso) {
        if (recurso != null) {
            try {
                recurso.close();
            } catch (SQLException ex) {
            }
        }
    }
}
