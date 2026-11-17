/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobetavisual.dao;

/**
 *
 * @author ASUS
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import proyectobetavisual.conexion.Conexion;
import proyectobetavisual.modelo.Estudiante;

/** Operaciones de base de datos para estudiantes. */
public class EstudianteDAO {

    private final Conexion conexion = new Conexion();

    public boolean insertar(Estudiante estudiante) throws SQLException {
        String sql = "INSERT INTO estudiante (cedula, nombres, carrera, estado) VALUES (?, ?, ?, ?)";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, estudiante.getCedula());
            ps.setString(2, estudiante.getNombres());
            ps.setString(3, estudiante.getCarrera());
            ps.setString(4, estudiante.getEstado());
            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {
        String sql = "UPDATE estudiante SET cedula = ?, nombres = ?, carrera = ?, estado = ? WHERE id = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, estudiante.getCedula());
            ps.setString(2, estudiante.getNombres());
            ps.setString(3, estudiante.getCarrera());
            ps.setString(4, estudiante.getEstado());
            ps.setInt(5, estudiante.getId());
            return ps.executeUpdate() > 0;
        } finally {
            cerrar(ps);
            cerrar(conn);
        }
    }

    public List<Estudiante> listarTodos() throws SQLException {
        String sql = "SELECT id, cedula, nombres, carrera, estado FROM estudiante ORDER BY nombres";
        List<Estudiante> estudiantes = new ArrayList<>();
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                estudiantes.add(mapearEstudiante(rs));
            }
            return estudiantes;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public Estudiante buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, cedula, nombres, carrera, estado FROM estudiante WHERE id = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next() ? mapearEstudiante(rs) : null;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    public Estudiante buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT id, cedula, nombres, carrera, estado FROM estudiante WHERE cedula = ?";
        Connection conn = obtenerConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();
            return rs.next() ? mapearEstudiante(rs) : null;
        } finally {
            cerrar(rs);
            cerrar(ps);
            cerrar(conn);
        }
    }

    /** Desactiva al estudiante sin borrar sus datos ni su historial. */
    public boolean desactivar(int id) throws SQLException {
        return cambiarEstado(id, Estudiante.INACTIVO);
    }

    public boolean cambiarEstado(int id, String estado) throws SQLException {
        String sql = "UPDATE estudiante SET estado = ? WHERE id = ?";
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

    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        return new Estudiante(
                rs.getInt("id"),
                rs.getString("cedula"),
                rs.getString("nombres"),
                rs.getString("carrera"),
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
