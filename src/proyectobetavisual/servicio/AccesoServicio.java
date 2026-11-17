package proyectobetavisual.servicio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import proyectobetavisual.dao.AccesoDAO;
import proyectobetavisual.dao.EstudianteDAO;
import proyectobetavisual.dao.TarjetaDAO;
import proyectobetavisual.modelo.Acceso;
import proyectobetavisual.modelo.Estudiante;
import proyectobetavisual.modelo.Tarjeta;

/** Identifica la tarjeta, decide el resultado y registra cada lectura. */
public class AccesoServicio {

    private final TarjetaDAO tarjetaDAO = new TarjetaDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final AccesoDAO accesoDAO = new AccesoDAO();

    /** Devuelve el estudiante si se autentica; devuelve null si se deniega. */
    public Estudiante procesarCodigo(String codigoRfid) throws SQLException {
        if (codigoRfid == null || codigoRfid.trim().isEmpty()) {
            throw new IllegalArgumentException("No se recibió un código RFID.");
        }

        String codigoLeido = codigoRfid.trim();
        Tarjeta tarjeta = tarjetaDAO.buscarPorCodigoRfid(codigoLeido);
        Estudiante estudiante = null;
        if (tarjeta != null) {
            estudiante = estudianteDAO.buscarPorId(tarjeta.getIdEstudiante());
        }

        boolean autenticado = 
                tarjeta != null
                && Tarjeta.ACTIVO.equals(tarjeta.getEstado())
                && estudiante != null
                && Estudiante.ACTIVO.equals(estudiante.getEstado());

        Acceso acceso = new Acceso(
                0,
                tarjeta == null ? null : tarjeta.getId(),
                estudiante == null ? null : estudiante.getId(),
                codigoLeido,
                null,
                autenticado ? Acceso.AUTENTICADO : Acceso.DENEGADO
        );

        if (!accesoDAO.registrar(acceso)) {
            throw new SQLException("No se pudo guardar el intento de acceso.");
        }
        return autenticado ? estudiante : null;
    }

    public List<Acceso> listarFiltrado(LocalDate desde, LocalDate hasta,
            Integer idEstudiante) throws SQLException {
        return accesoDAO.listarFiltrado(desde, hasta, idEstudiante);
    }
}
