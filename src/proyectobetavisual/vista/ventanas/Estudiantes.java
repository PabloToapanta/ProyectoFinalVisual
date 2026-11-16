/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyectobetavisual.vista.ventanas;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import proyectobetavisual.modelo.Estudiante;
import proyectobetavisual.servicio.EstudianteServicio;

/**
 *
 * @author pablo
 */
public class Estudiantes
        extends javax.swing.JDialog {

    private final EstudianteServicio estudianteServicio = new EstudianteServicio();
    private int idEstudianteSeleccionado = 0;
    private String estadoEstudianteSeleccionado = Estudiante.ACTIVO;


    /**
     * Creates new form Estudiantes
     */
    public Estudiantes(java.awt.Frame parent,
            boolean modal) {
        super(parent, modal);
        initComponents();
        cargarModeloEstudiantesTabla();
        cargarEstudiantesEnTabla();
        configurarSeleccionDeTabla();
    }

    private void cargarModeloEstudiantesTabla() {
        String[] cabecera = {"ID", "Cédula", "Nombres", "Carrera", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(
                null, cabecera) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        jTable1.setModel(modelo);
    }

    private DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) jTable1.getModel();
    }

    private void cargarEstudiantesEnTabla() {
        DefaultTableModel modelo = getModeloTabla();
        modelo.setRowCount(0);

        try {
            for (Estudiante estudiante : estudianteServicio.listarTodos()) {
                modelo.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getCedula(),
                    estudiante.getNombres(),
                    estudiante.getCarrera(),
                    estudiante.getEstado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los estudiantes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarSeleccionDeTabla() {
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evento) {
                if (!evento.getValueIsAdjusting()) {
                    sincronizarEstudianteDeTablaAFormulario();
                }
            }
        });
    }

    private void sincronizarEstudianteDeTablaAFormulario() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idEstudianteSeleccionado = (Integer) jTable1.getValueAt(fila, 0);
        jtxtCedula.setText(jTable1.getValueAt(fila, 1).toString());
        jtxtNombre.setText(jTable1.getValueAt(fila, 2).toString());
        jtxtCarrera.setText(jTable1.getValueAt(fila, 3).toString());
        estadoEstudianteSeleccionado = jTable1.getValueAt(fila, 4).toString();
    }

    private void limpiarFormulario() {
        jTable1.clearSelection();
        idEstudianteSeleccionado = 0;
        estadoEstudianteSeleccionado = Estudiante.ACTIVO;
        jtxtCedula.setText("");
        jtxtNombre.setText("");
        jtxtCarrera.setText("");
        jtxtCedula.requestFocusInWindow();
    }

    private void registrarEstudiante() {
        Estudiante estudiante = new Estudiante(0, jtxtCedula.getText(),
                jtxtNombre.getText(), jtxtCarrera.getText(), Estudiante.ACTIVO);
        try {
            if (estudianteServicio.registrar(estudiante)) {
                cargarEstudiantesEnTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Estudiante registrado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se registró el estudiante.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstudiante() {
        if (idEstudianteSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante para actualizar.");
            return;
        }

        Estudiante estudiante = new Estudiante(idEstudianteSeleccionado,
                jtxtCedula.getText(), jtxtNombre.getText(),
                jtxtCarrera.getText(), estadoEstudianteSeleccionado);
        try {
            if (estudianteServicio.actualizar(estudiante)) {
                cargarEstudiantesEnTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el estudiante.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo actualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desactivarEstudiante() {
        if (idEstudianteSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante para desactivar.");
            return;
        }
        if (Estudiante.INACTIVO.equals(estadoEstudianteSeleccionado)) {
            JOptionPane.showMessageDialog(this, "El estudiante ya está inactivo.");
            return;
        }

        try {
            if (estudianteServicio.desactivar(idEstudianteSeleccionado)) {
                cargarEstudiantesEnTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Estudiante desactivado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el estudiante.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo desactivar", JOptionPane.ERROR_MESSAGE);
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtCarrera = new javax.swing.JTextField();
        nuevoButton = new javax.swing.JButton();
        registrarButton = new javax.swing.JButton();
        actualizarButton = new javax.swing.JButton();
        desactivarButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        estudiantesTable = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Estudiantes");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Estudiante", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        jLabel2.setText("Cedula");

        jLabel3.setText("Nombres");

        jLabel4.setText("Carrera");

        nuevoButton.setText("Nuevo");
        nuevoButton.addActionListener(this::nuevoButtonActionPerformed);

        registrarButton.setText("Registrar");
        registrarButton.addActionListener(this::registrarButtonActionPerformed);

        actualizarButton.setText("Actualizar");
        actualizarButton.addActionListener(this::actualizarButtonActionPerformed);

        desactivarButton.setText("Desactivar");
        desactivarButton.addActionListener(this::desactivarButtonActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtCedula)
                            .addComponent(jtxtNombre)
                            .addComponent(jtxtCarrera, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                    .addComponent(nuevoButton)
                    .addComponent(desactivarButton)
                    .addComponent(actualizarButton)
                    .addComponent(registrarButton))
                .addContainerGap(172, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {actualizarButton, desactivarButton, nuevoButton, registrarButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nuevoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registrarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actualizarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desactivarButton)
                .addGap(18, 18, 18))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        estudiantesTable.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(estudiantesTable, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(estudiantesTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoButtonActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_nuevoButtonActionPerformed

    private void registrarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarButtonActionPerformed
        registrarEstudiante();
    }//GEN-LAST:event_registrarButtonActionPerformed

    private void actualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarButtonActionPerformed
        actualizarEstudiante();
    }//GEN-LAST:event_actualizarButtonActionPerformed

    private void desactivarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desactivarButtonActionPerformed
        desactivarEstudiante();
    }//GEN-LAST:event_desactivarButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizarButton;
    private javax.swing.JButton desactivarButton;
    private javax.swing.JScrollPane estudiantesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jtxtCarrera;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JButton nuevoButton;
    private javax.swing.JButton registrarButton;
    // End of variables declaration//GEN-END:variables
}
