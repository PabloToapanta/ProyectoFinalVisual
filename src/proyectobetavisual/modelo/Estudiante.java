/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobetavisual.modelo;

/**
 *
 * @author ASUS
 */

public class Estudiante {

    public static final String ACTIVO = "ACTIVO";
    public static final String INACTIVO = "INACTIVO";

    private int id;
    private String cedula;
    private String nombres;
    private String carrera;
    private String estado;

    public Estudiante() {
    }

    public Estudiante(int id, String cedula, String nombres, String carrera, String estado) {
        this.id = id;
        this.cedula = cedula;
        this.nombres = nombres;
        this.carrera = carrera;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

