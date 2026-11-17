/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyectobetavisual.vista.ventanas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import proyectobetavisual.modelo.Acceso;
import proyectobetavisual.modelo.Estudiante;
import proyectobetavisual.servicio.AccesoServicio;
import proyectobetavisual.servicio.EstudianteServicio;

/**
 *
 * @author pablo
 */
public class Historial
        extends javax.swing.JDialog {

    private final AccesoServicio accesoServicio = new AccesoServicio();
    private final EstudianteServicio estudianteServicio = new EstudianteServicio();
    private final List<Estudiante> estudiantesCombo = new ArrayList<>();
    private final DateTimeFormatter formatoFechaHora =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    

    /**
     * Creates new form Historial
     */
    public Historial(java.awt.Frame parent,
            boolean modal) {
        super(parent, modal);
        initComponents();
        cargarModeloEstudiantesComboBox();
        cargarModeloAccesosTabla();
        cargarAccesosEnTabla(null, null, null);
    }

    private void cargarModeloEstudiantesComboBox() {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Todos los estudiantes");
        estudiantesCombo.clear();

        try {
            for (Estudiante estudiante : estudianteServicio.listarTodos()) {
                estudiantesCombo.add(estudiante);
                modelo.addElement(estudiante.getCedula() + " - " + estudiante.getNombres());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los estudiantes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        estudiantesComboBox.setModel(modelo);
    }

    private Integer obtenerIdEstudianteSeleccionado() {
        int indice = estudiantesComboBox.getSelectedIndex();
        if (indice <= 0) {
            return null;
        }
        return estudiantesCombo.get(indice - 1).getId();
    }

    private String obtenerNombreEstudiante(Integer idEstudiante) {
        if (idEstudiante == null) {
            return "No registrado";
        }
        for (Estudiante estudiante : estudiantesCombo) {
            if (estudiante.getId() == idEstudiante) {
                return estudiante.getNombres();
            }
        }
        return "Desconocido";
    }

    private void cargarModeloAccesosTabla() {
        String[] cabecera = {"Fecha y hora", "Código leído", "Estudiante", "Resultado"};
        DefaultTableModel modelo = new DefaultTableModel(null, cabecera) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        AccesosTable.setModel(modelo);
    }

    private DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) AccesosTable.getModel();
    }

    private void cargarAccesosEnTabla(LocalDate desde, LocalDate hasta,
            Integer idEstudiante) {
        DefaultTableModel modelo = getModeloTabla();
        modelo.setRowCount(0);

        try {
            for (Acceso acceso : accesoServicio.listarFiltrado(desde, hasta, idEstudiante)) {
                modelo.addRow(new Object[]{
                    acceso.getFechaHora().format(formatoFechaHora),
                    acceso.getCodigoLeido(),
                    obtenerNombreEstudiante(acceso.getIdEstudiante()),
                    acceso.getResultado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el historial: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate leerFecha(String texto) {
        if (texto.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(texto.trim());
    }

    private void buscarAccesos() {
        try {
            LocalDate desde = leerFecha(jTextField1.getText());
            LocalDate hasta = leerFecha(jTextField2.getText());
            Integer idEstudiante = obtenerIdEstudianteSeleccionado();
            cargarAccesosEnTabla(desde, hasta, idEstudiante);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Escribe las fechas con el formato AAAA-MM-DD.");
        }
    }

    private void limpiarFiltros() {
        jTextField1.setText("");
        jTextField2.setText("");
        estudiantesComboBox.setSelectedIndex(0);
        cargarAccesosEnTabla(null, null, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        estudiantesComboBox = new javax.swing.JComboBox<>();
        buttonBUscar = new javax.swing.JButton();
        buttonLimpiar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AccesosTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Filtros");

        jLabel2.setText("Desde");

        jLabel3.setText("Hasta");

        jLabel4.setText("Estudiante");

        estudiantesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        buttonBUscar.setText("Buscar");
        buttonBUscar.addActionListener(this::buttonBUscarActionPerformed);

        buttonLimpiar.setText("Limpiar");
        buttonLimpiar.addActionListener(this::buttonLimpiarActionPerformed);

        jLabel5.setText("Usa el formato AAAA-MM-DD");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(49, 49, 49))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonBUscar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(estudiantesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addComponent(buttonLimpiar)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(buttonBUscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estudiantesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonLimpiar)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setFocusCycleRoot(true);

        AccesosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(AccesosTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonBUscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBUscarActionPerformed
        buscarAccesos();
    }//GEN-LAST:event_buttonBUscarActionPerformed

    private void buttonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLimpiarActionPerformed
        limpiarFiltros();
    }//GEN-LAST:event_buttonLimpiarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AccesosTable;
    private javax.swing.JButton buttonBUscar;
    private javax.swing.JButton buttonLimpiar;
    private javax.swing.JComboBox<String> estudiantesComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
