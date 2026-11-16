package proyectobetavisual.modelo;

/**
 * Representa una tarjeta RFID asignada a un estudiante.
 */
public class Tarjeta {

    public static final String ACTIVO = "ACTIVO";
    public static final String INACTIVO = "INACTIVO";

    private int id;
    private String codigoRfid;
    private int idEstudiante;
    private String estado;

    public Tarjeta() {
    }

    public Tarjeta(int id, String codigoRfid, int idEstudiante, String estado) {
        this.id = id;
        this.codigoRfid = codigoRfid;
        this.idEstudiante = idEstudiante;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoRfid() {
        return codigoRfid;
    }

    public void setCodigoRfid(String codigoRfid) {
        this.codigoRfid = codigoRfid;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
