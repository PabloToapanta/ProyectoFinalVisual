/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobetavisual.servicio;

/**
 *
 * @author ASUS
 */

import java.sql.SQLException;
import java.util.List;
import proyectobetavisual.dao.EstudianteDAO;
import proyectobetavisual.modelo.Estudiante;

/** Reglas básicas para gestionar estudiantes. */
public class EstudianteServicio {

    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    public boolean registrar(Estudiante estudiante) throws SQLException {
        validarDatos(estudiante);
        estudiante.setEstado(Estudiante.ACTIVO);
        return estudianteDAO.insertar(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {
        validarDatos(estudiante);
        if (estudiante.getId() <= 0) {
            throw new IllegalArgumentException("Selecciona un estudiante para editar.");
        }
        validarEstado(estudiante.getEstado());
        return estudianteDAO.actualizar(estudiante);
    }

    public List<Estudiante> listarTodos() throws SQLException {
        return estudianteDAO.listarTodos();
    }

    public Estudiante buscarPorId(int id) throws SQLException {
        return estudianteDAO.buscarPorId(id);
    }

    public Estudiante buscarPorCedula(String cedula) throws SQLException {
        return estudianteDAO.buscarPorCedula(validarCedula(cedula));
    }

    public boolean desactivar(int id) throws SQLException {
        return estudianteDAO.desactivar(id);
    }

    private void validarDatos(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("Ingresa los datos del estudiante.");
        }
        estudiante.setCedula(validarCedula(estudiante.getCedula()));
        estudiante.setNombres(validarCampo(estudiante.getNombres(), "Los nombres", 100));
        estudiante.setCarrera(validarCampo(estudiante.getCarrera(), "La carrera", 100));
    }

    private String validarCedula(String cedula) {
        String valor = validarCampo(cedula, "La cédula", 10);
        if (!valor.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("La cédula debe tener 10 dígitos.");
        }
        return valor;
    }

    private String validarCampo(String valor, String campo, int maximo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw new IllegalArgumentException(campo + " no puede superar " + maximo + " caracteres.");
        }
        return limpio;
    }

    private void validarEstado(String estado) {
        if (!Estudiante.ACTIVO.equals(estado) && !Estudiante.INACTIVO.equals(estado)) {
            throw new IllegalArgumentException("El estado del estudiante no es válido.");
        }
    }
}

