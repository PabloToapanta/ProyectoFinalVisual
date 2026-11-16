package proyectobetavisual.modelo;

import java.time.LocalDateTime;

/**
 * Representa un intento de acceso, autenticado o denegado.
 */
public class Acceso {

    public static final String AUTENTICADO = "AUTENTICADO";
    public static final String DENEGADO = "DENEGADO";

    private int id;
    private Integer idTarjeta;
    private Integer idEstudiante;
    private String codigoLeido;
    private LocalDateTime fechaHora;
    private String resultado;

    public Acceso() {
    }

    public Acceso(int id, Integer idTarjeta, Integer idEstudiante,
            String codigoLeido, LocalDateTime fechaHora, String resultado) {
        this.id = id;
        this.idTarjeta = idTarjeta;
        this.idEstudiante = idEstudiante;
        this.codigoLeido = codigoLeido;
        this.fechaHora = fechaHora;
        this.resultado = resultado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Integer idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCodigoLeido() {
        return codigoLeido;
    }

    public void setCodigoLeido(String codigoLeido) {
        this.codigoLeido = codigoLeido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
