package proyectobetavisual.servicio;

import java.sql.SQLException;
import java.util.List;
import proyectobetavisual.dao.EstudianteDAO;
import proyectobetavisual.dao.TarjetaDAO;
import proyectobetavisual.modelo.Tarjeta;

/** Reglas básicas para asignar y gestionar tarjetas RFID. */
public class TarjetaServicio {

    private final TarjetaDAO tarjetaDAO = new TarjetaDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    public boolean asignar(Tarjeta tarjeta) throws SQLException {
        validarDatos(tarjeta);
        validarEstado(tarjeta.getEstado());
        return tarjetaDAO.insertar(tarjeta);
    }

    public boolean actualizar(Tarjeta tarjeta) throws SQLException {
        validarDatos(tarjeta);
        if (tarjeta.getId() <= 0) {
            throw new IllegalArgumentException("Selecciona una tarjeta para editar.");
        }
        validarEstado(tarjeta.getEstado());
        return tarjetaDAO.actualizar(tarjeta);
    }

    public List<Tarjeta> listarTodas() throws SQLException {
        return tarjetaDAO.listarTodas();
    }

    public List<Tarjeta> listarPorEstudiante(int idEstudiante) throws SQLException {
        return tarjetaDAO.listarPorEstudiante(idEstudiante);
    }

    public Tarjeta buscarPorCodigoRfid(String codigoRfid) throws SQLException {
        return tarjetaDAO.buscarPorCodigoRfid(validarCampo(codigoRfid, "El código RFID", 32));
    }

    public boolean cambiarEstado(int idTarjeta, String estado) throws SQLException {
        validarEstado(estado);
        return tarjetaDAO.cambiarEstado(idTarjeta, estado);
    }

    private void validarDatos(Tarjeta tarjeta) throws SQLException {
        if (tarjeta == null) {
            throw new IllegalArgumentException("Ingresa los datos de la tarjeta.");
        }
        tarjeta.setCodigoRfid(validarCampo(tarjeta.getCodigoRfid(), "El código RFID", 32));
        if (tarjeta.getIdEstudiante() <= 0
                || estudianteDAO.buscarPorId(tarjeta.getIdEstudiante()) == null) {
            throw new IllegalArgumentException("Selecciona un estudiante registrado.");
        }
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
        if (!Tarjeta.ACTIVO.equals(estado) && !Tarjeta.INACTIVO.equals(estado)) {
            throw new IllegalArgumentException("El estado de la tarjeta no es válido.");
        }
    }
}
