/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyectobetavisual.vista.ventanas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import proyectobetavisual.modelo.Estudiante;
import proyectobetavisual.modelo.Tarjeta;
import proyectobetavisual.servicio.EstudianteServicio;
import proyectobetavisual.servicio.TarjetaServicio;

/**
 *
 * @author pablo
 */
public class RFID
        extends javax.swing.JDialog {

    private final TarjetaServicio tarjetaServicio = new TarjetaServicio();
    private final EstudianteServicio estudianteServicio = new EstudianteServicio();
    private final List<Estudiante> estudiantesCombo = new ArrayList<>();
    private int idTarjetaSeleccionada = 0;
    

    /**
     * Creates new form RFID
     */
    public RFID(java.awt.Frame parent,
            boolean modal) {
        super(parent, modal);
        initComponents();
        cargarModeloEstudiantesComboBox();
        cargarModeloTarjetasTabla();
        cargarTarjetasEnTabla();
        configurarSeleccionDeTabla();
        nuevaTarjeta();
    }

    private void cargarModeloEstudiantesComboBox() {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Seleccione un estudiante");
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
        jcomboboxEstudiantes.setModel(modelo);
    }

    private int obtenerIdEstudianteSeleccionado() {
        int indice = jcomboboxEstudiantes.getSelectedIndex();
        if (indice <= 0) {
            return 0;
        }
        return estudiantesCombo.get(indice - 1).getId();
    }

    private void seleccionarEstudianteEnComboBox(int idEstudiante) {
        for (int indice = 0; indice < estudiantesCombo.size(); indice++) {
            if (estudiantesCombo.get(indice).getId() == idEstudiante) {
                jcomboboxEstudiantes.setSelectedIndex(indice + 1);
                return;
            }
        }
        jcomboboxEstudiantes.setSelectedIndex(0);
    }

    private String obtenerNombreEstudiante(int idEstudiante) {
        for (Estudiante estudiante : estudiantesCombo) {
            if (estudiante.getId() == idEstudiante) {
                return estudiante.getNombres();
            }
        }
        return "";
    }

    private void cargarModeloTarjetasTabla() {
        String[] cabecera = {"ID", "Código RFID", "ID estudiante", "Estudiante", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(null, cabecera) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        RFIDtable.setModel(modelo);
    }

    private DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) RFIDtable.getModel();
    }

    private void cargarTarjetasEnTabla() {
        DefaultTableModel modelo = getModeloTabla();
        modelo.setRowCount(0);

        try {
            for (Tarjeta tarjeta : tarjetaServicio.listarTodas()) {
                modelo.addRow(new Object[]{
                    tarjeta.getId(),
                    tarjeta.getCodigoRfid(),
                    tarjeta.getIdEstudiante(),
                    obtenerNombreEstudiante(tarjeta.getIdEstudiante()),
                    tarjeta.getEstado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las tarjetas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarSeleccionDeTabla() {
        RFIDtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        RFIDtable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evento) {
                if (!evento.getValueIsAdjusting()) {
                    sincronizarTarjetaDeTablaAFormulario();
                }
            }
        });
    }

    private void sincronizarTarjetaDeTablaAFormulario() {
        int fila = RFIDtable.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idTarjetaSeleccionada = (Integer) RFIDtable.getValueAt(fila, 0);
        jtxtRFID.setText(RFIDtable.getValueAt(fila, 1).toString());
        seleccionarEstudianteEnComboBox((Integer) RFIDtable.getValueAt(fila, 2));
        String estado = RFIDtable.getValueAt(fila, 4).toString();
        activoRadioButton.setSelected(Tarjeta.ACTIVO.equals(estado));
        inactivoRadioButton.setSelected(Tarjeta.INACTIVO.equals(estado));
    }

    private void nuevaTarjeta() {
        RFIDtable.clearSelection();
        idTarjetaSeleccionada = 0;
        jtxtRFID.setText("");
        jcomboboxEstudiantes.setSelectedIndex(0);
        activoRadioButton.setSelected(true);
        jtxtRFID.requestFocusInWindow();
    }

    private Tarjeta crearTarjetaDesdeFormulario(int id) {
        String estado = activoRadioButton.isSelected() ? Tarjeta.ACTIVO : Tarjeta.INACTIVO;
        return new Tarjeta(id, jtxtRFID.getText(),
                obtenerIdEstudianteSeleccionado(), estado);
    }

    private void asignarTarjeta() {
        try {
            if (tarjetaServicio.asignar(crearTarjetaDesdeFormulario(0))) {
                cargarTarjetasEnTabla();
                nuevaTarjeta();
                JOptionPane.showMessageDialog(this, "Tarjeta asignada.");
            } else {
                JOptionPane.showMessageDialog(this, "No se asignó la tarjeta.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo asignar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTarjeta() {
        if (idTarjetaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarjeta para actualizar.");
            return;
        }

        try {
            if (tarjetaServicio.actualizar(crearTarjetaDesdeFormulario(idTarjetaSeleccionada))) {
                cargarTarjetasEnTabla();
                nuevaTarjeta();
                JOptionPane.showMessageDialog(this, "Tarjeta actualizada.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la tarjeta.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo actualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtxtRFID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jcomboboxEstudiantes = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        inactivoRadioButton = new javax.swing.JRadioButton();
        activoRadioButton = new javax.swing.JRadioButton();
        nuevaButton = new javax.swing.JButton();
        asignarButton = new javax.swing.JButton();
        actualizarButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RFIDTable = new javax.swing.JScrollPane();
        RFIDtable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Datos de la tarjeta");

        jLabel2.setText("Codigo RFID");

        jLabel3.setText("Estudiante Asignado");

        jcomboboxEstudiantes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Estado de la tarjeta");

        buttonGroup1.add(inactivoRadioButton);
        inactivoRadioButton.setText("INACTIVO");

        buttonGroup1.add(activoRadioButton);
        activoRadioButton.setText("ACTIVO");

        nuevaButton.setText("Nueva");
        nuevaButton.addActionListener(this::nuevaButtonActionPerformed);

        asignarButton.setText("Asignar");
        asignarButton.addActionListener(this::asignarButtonActionPerformed);

        actualizarButton.setText("Actualizar");
        actualizarButton.addActionListener(this::actualizarButtonActionPerformed);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        RFIDtable.setModel(new javax.swing.table.DefaultTableModel(
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
        RFIDTable.setViewportView(RFIDtable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RFIDTable, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RFIDTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(106, 106, 106))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtxtRFID, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jcomboboxEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inactivoRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(activoRadioButton))
                    .addComponent(nuevaButton)
                    .addComponent(asignarButton)
                    .addComponent(actualizarButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {actualizarButton, asignarButton, nuevaButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtRFID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(jcomboboxEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inactivoRadioButton)
                            .addComponent(activoRadioButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nuevaButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(asignarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actualizarButton)
                        .addGap(0, 106, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaButtonActionPerformed
        nuevaTarjeta();
    }//GEN-LAST:event_nuevaButtonActionPerformed

    private void asignarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asignarButtonActionPerformed
        asignarTarjeta();
    }//GEN-LAST:event_asignarButtonActionPerformed

    private void actualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarButtonActionPerformed
        actualizarTarjeta();
    }//GEN-LAST:event_actualizarButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane RFIDTable;
    private javax.swing.JTable RFIDtable;
    private javax.swing.JRadioButton activoRadioButton;
    private javax.swing.JButton actualizarButton;
    private javax.swing.JButton asignarButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton inactivoRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> jcomboboxEstudiantes;
    private javax.swing.JTextField jtxtRFID;
    private javax.swing.JButton nuevaButton;
    // End of variables declaration//GEN-END:variables
}
