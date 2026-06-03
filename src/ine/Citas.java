/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ine;

import ine.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gudin
 */
public class Citas extends javax.swing.JFrame {

    /**
     * Creates new form citas
     */
    public Citas() {
        initComponents();
        cargarEstadosResidencia();
        cargarEstadosCita();
        cargarMunicipios();
        cargarModulos();
        cargarTram();
        cargarIdentificacion();
        cargarNacionalidad();
        cargarDomicilio();
        cargarCitas();
        jDateChooser2.setDate(new java.util.Date());
    }
    
    public void cargarCitas() {
    DefaultTableModel dtm = (DefaultTableModel) jtCitas.getModel();
    dtm.setRowCount(0); // limpiar tabla

    try {
        Connection c = Conexion.getConnection();
        String sql = "SELECT c.curp, c.fecha, c.hora, t.nombre AS tramite, m.nombre AS modulo, mu.nombre AS municipio, e.nombre AS estado " +
                     "FROM [127.0.0.1].[CITAS_INE].[dbo].[citas] c " +
                     "LEFT JOIN Tramites t ON c.idTramite = t.id " +
                     "LEFT JOIN [127.0.0.1].[CITAS_INE].[dbo].[Modulo] m ON c.idModulo = m.id " +
                     "LEFT JOIN [127.0.0.1].[CITAS_INE].[dbo].[Municipio] mu ON m.idMunicipio = mu.id " +
                     "LEFT JOIN [127.0.0.1].[CITAS_INE].[dbo].[Entidades] e ON mu.idEntidad = e.id " +
                     "WHERE c.estado = 'Agendada'" +
                     "ORDER BY c.fecha, c.hora";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        boolean hayResultados = false;
        while (rs.next()) {
            Object[] fila = new Object[7];
            fila[0] = rs.getString("curp");
            fila[1] = rs.getDate("fecha").toString();
            fila[2] = rs.getString("hora");
            fila[3] = rs.getString("tramite");
            fila[4] = rs.getString("modulo");
            fila[5] = rs.getString("municipio");
            fila[6] = rs.getString("estado");
            dtm.addRow(fila);
            hayResultados = true;
        }

        if (!hayResultados) {
            JOptionPane.showMessageDialog(this, "No hay citas agendadas para mostrar.");
        }

        rs.close();
        ps.close();
        c.close();
    } catch (SQLException ex) {
        Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error al cargar citas: " + ex.getMessage());
    }
}


    public void cargarCitasPorFecha(java.util.Date fecha) {
    DefaultTableModel dtm = (DefaultTableModel) jtCitas.getModel();
    dtm.setRowCount(0); // limpiar tabla

    try {
        Connection c = Conexion.getConnection();
        String sql = "SELECT c.curp, c.fecha, c.hora, t.nombre AS tramite, m.nombre AS modulo, mu.nombre AS municipio, e.nombre AS estado " +
                     "FROM Citas c " +
                     "LEFT JOIN Tramites t ON c.idTramite = t.id " +
                     "LEFT JOIN Modulo m ON c.idModulo = m.id " +
                     "LEFT JOIN Municipio mu ON m.idMunicipio = mu.id " +
                     "LEFT JOIN Entidades e ON mu.idEntidad = e.id " +
                     "WHERE c.estado = 'Agendada' AND CAST(c.fecha AS DATE) = ? " +
                     "ORDER BY c.hora";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setDate(1, new java.sql.Date(fecha.getTime()));
        ResultSet rs = ps.executeQuery();

        boolean hayResultados = false;
        while (rs.next()) {
            Object[] fila = new Object[7];
            fila[0] = rs.getString("curp");
            fila[1] = rs.getDate("fecha").toString();
            fila[2] = rs.getString("hora");
            fila[3] = rs.getString("tramite");
            fila[4] = rs.getString("modulo");
            fila[5] = rs.getString("municipio");
            fila[6] = rs.getString("estado");
            dtm.addRow(fila);
            hayResultados = true;
        }

        if (!hayResultados) {
            JOptionPane.showMessageDialog(this, "No hay citas agendadas para la fecha seleccionada.");
        }

        rs.close();
        ps.close();
        c.close();
    } catch (SQLException ex) {
        Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error al cargar citas: " + ex.getMessage());
    }
}


    private void limpiar() {
    jtfDireccion.setText("");
    jtfReferencia.setText("");
    jtfHorario.setText("");
    jtfCurp.setText("");
    jtfNombre.setText("");
    jtfApellidoP.setText("");
    jtdApellidoM.setText("");
    jtfTramite.setText("");

    jDateChooser1.setDate(null);
    jcbEntidadReferencia.setSelectedIndex(1);
    jcbEntidadCita.setSelectedIndex(1);
    jcbMunicipio.setSelectedIndex(1);
    jcbModulos.setSelectedIndex(1);
    jcbHorario.removeAllItems();
    jcbTramite.setSelectedIndex(1);
    jcbNacionalidad.setSelectedIndex(1);
    jcbIdentificacion.setSelectedIndex(1);
    jcbDomicilio.setSelectedIndex(1);
}


    public void cargarModulos() {
        jcbModulos.removeAllItems();
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT mo.nombre AS modulo "
                    + "FROM [127.0.0.1].[CITAS_INE].[dbo].[Modulo] mo "
                    + "INNER JOIN [127.0.0.1].[CITAS_INE].[dbo].[Municipio] m ON mo.idMunicipio = m.id "
                    + "INNER JOIN [127.0.0.1].[CITAS_INE].[dbo].[Entidades] e ON m.idEntidad = e.id "
                    + "WHERE m.nombre = ?"
            );
            if (jcbMunicipio.getSelectedItem() != null) {
                ps.setString(1, jcbMunicipio.getSelectedItem().toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    jcbModulos.addItem(rs.getString("modulo"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Municipios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarEstadosResidencia() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select id,nombre,clave from [127.0.0.1].[CITAS_INE].[dbo].[Entidades]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbEntidadReferencia.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Estados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarEstadosCita() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from [127.0.0.1].[INE_SUR].[dbo].[Entidades] \n" +
"UNION\n" +
"SELECT * FROM [192.168.0.232].[INE_NORTE].[dbo].[Entidades]\n" +
"UNION\n" +
"SELECT * FROM [192.168.0.108].[INE_CENTRO].[dbo].[Entidades]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbEntidadCita.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Estados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarTram() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select nombre from [127.0.0.1].[CITAS_INE].[dbo].[Tramites]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbTramite.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarIdentificacion() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select descripcion from [127.0.0.1].[CITAS_INE].[dbo].[Documentos] where tipo like 'Identidicación con fotografía'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbIdentificacion.addItem(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarDomicilio() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select descripcion from [127.0.0.1].[CITAS_INE].[dbo].[Documentos] where tipo like 'comprobante%'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbDomicilio.addItem(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarNacionalidad() {
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("select descripcion from [127.0.0.1].[CITAS_INE].[dbo].[Documentos] where tipo like 'Documento%'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbNacionalidad.addItem(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarMunicipios() {
        jcbMunicipio.removeAllItems();
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement("Select m.nombre Municipio from [127.0.0.1].[CITAS_INE].[dbo].[Municipio] m inner join [127.0.0.1].[CITAS_INE].[dbo].[Entidades] e on m.idEntidad = e.id where e.nombre like ?");
            ps.setString(1, jcbEntidadCita.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbMunicipio.addItem(rs.getString("Municipio").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Estados.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcbEntidadReferencia = new javax.swing.JComboBox<>();
        jcbMunicipio = new javax.swing.JComboBox<>();
        jcbEntidadCita = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jcbModulos = new javax.swing.JComboBox<>();
        jtfDireccion = new javax.swing.JTextField();
        jtfReferencia = new javax.swing.JTextField();
        jtfHorario = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfNombre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtfApellidoP = new javax.swing.JTextField();
        jtdApellidoM = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jtfCurp = new javax.swing.JTextField();
        jbValidar = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jcbHorario = new javax.swing.JComboBox<>();
        jcbTramite = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jcbNacionalidad = new javax.swing.JComboBox<>();
        jcbIdentificacion = new javax.swing.JComboBox<>();
        jcbDomicilio = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtCitas = new javax.swing.JTable();
        btnAgendar = new javax.swing.JButton();
        jtfTramite = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("*Entidad de residencia");

        jLabel2.setText("*Entidad de cita");

        jLabel3.setText("*Alcaldia/Municipio");

        jcbEntidadReferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elige una opcion..." }));

        jcbMunicipio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elige una opcion.." }));
        jcbMunicipio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMunicipioActionPerformed(evt);
            }
        });

        jcbEntidadCita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elige una opcion" }));
        jcbEntidadCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbEntidadCitaActionPerformed(evt);
            }
        });

        jLabel4.setText("Modulo");

        jLabel5.setText("Direccion");

        jLabel6.setText("Referencia");

        jLabel7.setText("Horario de Atención");

        jcbModulos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elige un modulo" }));
        jcbModulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbModulosActionPerformed(evt);
            }
        });

        jLabel8.setText("Nombre");

        jtfNombre.setEditable(false);

        jLabel9.setText("Apellido P.");

        jtfApellidoP.setEditable(false);

        jtdApellidoM.setEditable(false);

        jLabel10.setText("Apellido M.");

        jLabel11.setText("CURP");

        jtfCurp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCurpActionPerformed(evt);
            }
        });

        jbValidar.setText("Validar");
        jbValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbValidarActionPerformed(evt);
            }
        });

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel12.setText("Fecha");

        jLabel13.setText("Hora");

        jcbHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbHorarioActionPerformed(evt);
            }
        });

        jcbTramite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbTramiteActionPerformed(evt);
            }
        });

        jLabel14.setText("Trámite");

        jLabel15.setText("Documento de Nacionalidad");

        jLabel16.setText("Comprobante de Domicilio");

        jLabel17.setText("Identificación con Fotografía");

        jcbNacionalidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNacionalidadActionPerformed(evt);
            }
        });

        jcbIdentificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbIdentificacionActionPerformed(evt);
            }
        });

        jcbDomicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDomicilioActionPerformed(evt);
            }
        });

        jtCitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "CURP", "Fecha", "Hora", "Trámite", "Modulo", "Municipio", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtCitas);

        btnAgendar.setText("Agendar");
        btnAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarActionPerformed(evt);
            }
        });

        jtfTramite.setEditable(false);

        jLabel18.setText("Filtrar por fecha");

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jcbNacionalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(25, 25, 25)
                                .addComponent(jcbDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(jcbIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14))
                                .addGap(63, 63, 63)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jcbModulos, javax.swing.GroupLayout.Alignment.LEADING, 0, 423, Short.MAX_VALUE)
                                        .addComponent(jtfDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jtfReferencia, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jtfHorario, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jtfCurp, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jtfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel9)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jtfApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(19, 19, 19)
                                                .addComponent(jLabel10)
                                                .addGap(18, 18, 18)
                                                .addComponent(jtdApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jbValidar)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addGap(18, 18, 18)
                                                .addComponent(jcbHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jcbTramite, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jtfTramite, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(26, 26, 26))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(632, 632, 632)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgendar)
                .addGap(61, 61, 61))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnAgendar)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jcbHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jcbModulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jtfDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jtfReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jtfHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jtfCurp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbValidar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jtfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jtfApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtdApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbTramite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfTramite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbNacionalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jcbIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jcbDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbEntidadReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbEntidadCita, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbMunicipio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiar)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbEntidadReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpiar))
                    .addComponent(jcbEntidadCita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(433, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 899, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbEntidadCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbEntidadCitaActionPerformed
        // TODO add your handling code here:
        cargarMunicipios();
    }//GEN-LAST:event_jcbEntidadCitaActionPerformed

    private void jcbMunicipioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMunicipioActionPerformed
        // TODO add your handling code here:
        cargarModulos();
    }//GEN-LAST:event_jcbMunicipioActionPerformed

    private void jcbModulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbModulosActionPerformed
        String sql = "SELECT direccion, referencia, horarioSem FROM [127.0.0.1].[CITAS_INE].[dbo].[Modulo]  WHERE nombre = ?";
        jtfDireccion.setText("");
        jtfReferencia.setText("");
        jtfHorario.setText("");
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            if (jcbModulos.getSelectedItem() != null) {
                ps.setString(1, jcbModulos.getSelectedItem().toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    jtfDireccion.setText(rs.getString("direccion"));
                    jtfReferencia.setText(rs.getString("referencia"));
                    jtfHorario.setText(rs.getString("horarioSem"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jcbModulosActionPerformed

    private void jtfCurpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCurpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfCurpActionPerformed

    private void jcbHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbHorarioActionPerformed

    private void jcbTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbTramiteActionPerformed
        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT descripcion FROM [127.0.0.1].[CITAS_INE].[dbo].[Tramites] WHERE nombre = ?"
            );
            ps.setString(1, jcbTramite.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jtfTramite.setText(rs.getString("descripcion"));
            }

            rs.close();
            ps.close();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jcbTramiteActionPerformed

    private void jcbNacionalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNacionalidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNacionalidadActionPerformed

    private void jcbIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbIdentificacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbIdentificacionActionPerformed

    private void jcbDomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDomicilioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbDomicilioActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
    limpiar();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarActionPerformed
        try {
        Connection c = Conexion.getConnection();

        // Obtener valores del formulario
        String curp = jtfCurp.getText().trim();
        java.sql.Date fechaSQL = new java.sql.Date(jDateChooser1.getDate().getTime());
        String hora = jcbHorario.getSelectedItem().toString();

        // Obtener idModulo
        int idModulo = 0;
        PreparedStatement psModulo = c.prepareStatement("SELECT id FROM [127.0.0.1].[CITAS_INE].[dbo].[Modulo] WHERE nombre = ?");
        psModulo.setString(1, jcbModulos.getSelectedItem().toString());
        ResultSet rsModulo = psModulo.executeQuery();
        if (rsModulo.next()) {
            idModulo = rsModulo.getInt("id");
        }
        rsModulo.close();
        psModulo.close();

        // Obtener idTramite
        int idTramite = 0;
        PreparedStatement psTram = c.prepareStatement("SELECT id FROM [127.0.0.1].[CITAS_INE].[dbo].[Tramites] WHERE nombre = ?");
        psTram.setString(1, jcbTramite.getSelectedItem().toString());
        ResultSet rsTram = psTram.executeQuery();
        if (rsTram.next()) {
            idTramite = rsTram.getInt("id");
        }
        rsTram.close();
        psTram.close();

        // UPDATE de la cita
        String sql = "UPDATE [127.0.0.1].[CITAS_INE].[dbo].[Citas] SET curp = ?, idTramite = ?, nacionalidad = ?, identificacion = ?, domicilio = ?, estado = 'Agendada' " +
                     "WHERE fecha = ? AND hora = ? AND idModulo = ? AND estado = 'Disponible'";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, curp);
        ps.setInt(2, idTramite);
        ps.setInt(3, jcbNacionalidad.getSelectedIndex());   // aquí puedes mapear al id real
        ps.setInt(4, jcbIdentificacion.getSelectedIndex()); // idem
        ps.setInt(5, jcbDomicilio.getSelectedIndex());      // idem
        ps.setDate(6, fechaSQL);
        ps.setString(7, hora);
        ps.setInt(8, idModulo);

        int filas = ps.executeUpdate();
        if (filas > 0) {
            JOptionPane.showMessageDialog(this, "Cita agendada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agendar la cita. Verifica los datos.");
        }

        ps.close();
        c.close();
        limpiar();
        cargarCitas();

    } catch (SQLException ex) {
        Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error al agendar cita: " + ex.getMessage());
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgendarActionPerformed

    private void jbValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbValidarActionPerformed
        String curp = jtfCurp.getText().trim();
        if (curp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una CURP para validar.");
            return;
        }

        try {
            Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT nombre, aPaterno, aMaterno FROM [127.0.0.1].[CITAS_INE].[dbo].[Usuarios] WHERE curp = ?"
            );
            ps.setString(1, curp);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jtfNombre.setText(rs.getString("nombre"));
                jtfApellidoP.setText(rs.getString("aPaterno"));
                jtdApellidoM.setText(rs.getString("aMaterno"));
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un usuario con esa CURP.");
                jtfNombre.setText("");
                jtfApellidoP.setText("");
                jtdApellidoM.setText("");
            }

            rs.close();
            ps.close();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error al validar CURP: " + ex.getMessage());
        }

    }//GEN-LAST:event_jbValidarActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if ("date".equals(evt.getPropertyName())) {
            java.util.Date fechaSeleccionada = jDateChooser1.getDate();
            if (fechaSeleccionada == null || jcbModulos.getSelectedItem() == null) {
                return;
            }

            // Convertir fecha a java.sql.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());

            try {
                Connection c = Conexion.getConnection();

                // Obtener idModulo según el nombre seleccionado
                int idModulo = 0;
                PreparedStatement psModulo = c.prepareStatement("SELECT id FROM [127.0.0.1].[CITAS_INE].[dbo].[Modulo] WHERE nombre = ?");
                psModulo.setString(1, jcbModulos.getSelectedItem().toString());
                ResultSet rsModulo = psModulo.executeQuery();
                if (rsModulo.next()) {
                    idModulo = rsModulo.getInt("id");
                }
                rsModulo.close();
                psModulo.close();

                // Limpiar combo de horarios
                jcbHorario.removeAllItems();

                // Consultar horas disponibles
                PreparedStatement ps = c.prepareStatement(
                "SELECT hora FROM [127.0.0.1].[CITAS_INE].[dbo].[Citas] WHERE fecha = ? AND idModulo = ? AND estado like 'Disponible'"
                );
                ps.setDate(1, fechaSQL);
                ps.setInt(2, idModulo);
                ResultSet rs = ps.executeQuery();

                jcbHorario.removeAllItems();

                boolean hayHorarios = false;
                while (rs.next()) {
                    jcbHorario.addItem(rs.getString("hora"));
                    hayHorarios = true;
                }

                if (!hayHorarios) {
                    JOptionPane.showMessageDialog(this, "No hay citas disponibles para la fecha seleccionada");
                }

                rs.close();
                ps.close();
                c.close();

            } catch (SQLException ex) {
                Logger.getLogger(Citas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
                               
    if ("date".equals(evt.getPropertyName())) {
        java.util.Date fechaSeleccionada = jDateChooser2.getDate();
        if (fechaSeleccionada != null) {
            cargarCitasPorFecha(fechaSeleccionada);
        }
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser2PropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Citas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgendar;
    private javax.swing.JButton btnLimpiar;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbValidar;
    private javax.swing.JComboBox<String> jcbDomicilio;
    private javax.swing.JComboBox<String> jcbEntidadCita;
    private javax.swing.JComboBox<String> jcbEntidadReferencia;
    private javax.swing.JComboBox<String> jcbHorario;
    private javax.swing.JComboBox<String> jcbIdentificacion;
    private javax.swing.JComboBox<String> jcbModulos;
    private javax.swing.JComboBox<String> jcbMunicipio;
    private javax.swing.JComboBox<String> jcbNacionalidad;
    private javax.swing.JComboBox<String> jcbTramite;
    private javax.swing.JTable jtCitas;
    private javax.swing.JTextField jtdApellidoM;
    private javax.swing.JTextField jtfApellidoP;
    private javax.swing.JTextField jtfCurp;
    private javax.swing.JTextField jtfDireccion;
    private javax.swing.JTextField jtfHorario;
    private javax.swing.JTextField jtfNombre;
    private javax.swing.JTextField jtfReferencia;
    private javax.swing.JTextField jtfTramite;
    // End of variables declaration//GEN-END:variables
}
