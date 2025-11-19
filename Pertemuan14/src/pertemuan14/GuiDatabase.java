/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pertemuan14;

/**
 *
 * @author Faiq
 */
import java.awt.Window;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import jnafilechooser.api.JnaFileChooser;

public class GuiDatabase extends javax.swing.JFrame {

    private EntityManagerFactory emf;
    private EntityManager em;

    String kodeLama, namaLama, semesterLama, sksLama;
    String idLama, namaDosenlama, nipLama, emailLama;
    String kdMengajarLama, pengajarLama, matkulLama, tahunLama;

    public void connect() {
        try {
            emf = Persistence.createEntityManagerFactory("Pertemuan14PU");
            em = emf.createEntityManager();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal: " + e.getMessage());
        }
    }

    public GuiDatabase() {
        initComponents();
        setLocationRelativeTo(null);
        connect();
        showTableMatkul();
        showTableDosen();
        showTableMengajar();

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();

                kodeLama = jTable1.getValueAt(row, 0).toString();
                namaLama = jTable1.getValueAt(row, 1).toString();
                sksLama = jTable1.getValueAt(row, 2).toString();
                semesterLama = jTable1.getValueAt(row, 3).toString();

            }
        });

        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable2.getSelectedRow();

                idLama = jTable2.getValueAt(row, 0).toString();
                namaDosenlama = jTable2.getValueAt(row, 1).toString();
                nipLama = jTable2.getValueAt(row, 2).toString();
                emailLama = jTable2.getValueAt(row, 3).toString();

            }
        });
    }

    public void exportToCSV(JTable table, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            TableModel model = table.getModel();

            // Tulis header
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.append(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    writer.append(";");
                }
            }
            writer.append("\n");

            // Tulis data baris
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    writer.append(value == null ? "" : value.toString());
                    if (col < model.getColumnCount() - 1) {
                        writer.append(";");
                    }
                }
                writer.append("\n");
            }

            writer.flush();
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke CSV!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor CSV: " + e.getMessage());
        }
    }

    public void exportToExcel(JTable table, String filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            TableModel model = table.getModel();
            XSSFSheet sheet = workbook.createSheet("Data");

            // Header kolom
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            // Data baris
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    Cell cell = row.createCell(j);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // Auto-size kolom
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(new File(filePath))) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke file Excel!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor ke Excel: " + e.getMessage());
        }
    }

    public void showTableMatkul() {
        try {
            em.clear();

            List<MataKuliah> hasil = em.createNamedQuery("MataKuliah.findAll", MataKuliah.class).getResultList();

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"                Kode", "    Nama Mata Kuliah", "                SKS", "            Semester"}, 0
            );

            for (MataKuliah rs : hasil) {
                model.addRow(new Object[]{
                    rs.getKodeMatkul(),
                    rs.getNamaMatkul(),
                    rs.getSksMatkul(),
                    rs.getSemesterMatkul()
                });
            }

            jTable1.setModel(model);
            jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jTable1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jTable1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    public void showTableDosen() {
        try {
            em.clear();

            List<Dosen> hasil = em.createNamedQuery("Dosen.findAll", Dosen.class).getResultList();

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"              ID Dosen", "     Nama Dosen", "               NIP", "            Email"}, 0
            );

            for (Dosen rs : hasil) {
                model.addRow(new Object[]{
                    rs.getIdDosen(),
                    rs.getNamaDosen(),
                    rs.getNip(),
                    rs.getEmail()
                });
            }

            jTable2.setModel(model);
            jTable2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    public void showTableMengajar() {
        try {
            em.clear();

            List<Mengajar> hasil = em.createNamedQuery("Mengajar.findAll", Mengajar.class).getResultList();

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"        Kode Mengajar", "     Nama Mata Kuliah", "     Dosen Pengajar", "      Tahun Ajaran"}, 0
            );

            for (Mengajar rs : hasil) {
                model.addRow(new Object[]{
                    rs.getKodeMengajar(),
                    rs.getKodeMatkul().getNamaMatkul(),
                    rs.getIdDosen().getNamaDosen(),
                    rs.getTahunAjaran()
                });
            }

            jTable3.setModel(model);
            jTable3.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jTable3.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    private void imporCsvKeDatabaseMatkul() {
        JnaFileChooser fileChooser = new JnaFileChooser();
        fileChooser.setTitle("Import Data Mata Kuliah");
        Window Window = null;
        boolean action = fileChooser.showOpenDialog(Window);

        if (action) {
            System.out.println(fileChooser.getSelectedFile());
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();

            if (!fileName.toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this,
                        "File yang dipilih bukan file CSV!\nSilakan pilih file dengan ekstensi .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                br.readLine();

                em.getTransaction().begin();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 4) {
                        MataKuliah mk = new MataKuliah();
                        mk.setKodeMatkul(data[0].trim());
                        mk.setNamaMatkul(data[1].trim());
                        mk.setSksMatkul(Integer.parseInt(data[2].trim()));
                        mk.setSemesterMatkul(Integer.parseInt(data[3].trim()));

                        em.persist(mk);
                    }
                }

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari CSV!");
                showTableMatkul();
                showTableDosen();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void imporCsvKeDatabaseDosen() {
        JnaFileChooser fileChooser = new JnaFileChooser();
        fileChooser.setTitle("Import Data Dosen");
        Window Window = null;
        boolean action = fileChooser.showOpenDialog(Window);

        if (action) {
            System.out.println(fileChooser.getSelectedFile());
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();

            if (!fileName.toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this,
                        "File yang dipilih bukan file CSV!\nSilakan pilih file dengan ekstensi .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                br.readLine();

                em.getTransaction().begin();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 4) {
                        Dosen d = new Dosen();
                        d.setIdDosen(data[0].trim());
                        d.setNamaDosen(data[1].trim());
                        d.setNip(data[2].trim());
                        d.setEmail(data[3].trim());

                        em.persist(d);
                    }
                }

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari CSV!");
                showTableMatkul();
                showTableDosen();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void imporCsvKeDatabaseMengajar() {
        JnaFileChooser fileChooser = new JnaFileChooser();
        fileChooser.setTitle("Import Data Mengajar");
        Window Window = null;
        boolean action = fileChooser.showOpenDialog(Window);

        if (action) {
            System.out.println(fileChooser.getSelectedFile());
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();

            if (!fileName.toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this,
                        "File yang dipilih bukan file CSV!\nSilakan pilih file dengan ekstensi .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                br.readLine();

                em.getTransaction().begin();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 4) {

                        String kodeMengajar = data[0].trim();
                        String namaMatkulCSV = data[1].trim();
                        String namaDosenCSV = data[2].trim();
                        String tahunAjaran = data[3].trim();

                        MataKuliah mk = null;
                        try {
                            mk = em.createNamedQuery("MataKuliah.findByNamaMatkul", MataKuliah.class)
                                    .setParameter("namaMatkul", namaMatkulCSV)
                                    .getSingleResult();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this,
                                    "Nama mata kuliah '" + namaMatkulCSV + "' tidak ditemukan di database!",
                                    "Error Mata Kuliah",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        Dosen d = null;
                        try {
                            d = em.createNamedQuery("Dosen.findByNamaDosen", Dosen.class)
                                    .setParameter("namaDosen", namaDosenCSV)
                                    .getSingleResult();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this,
                                    "Nama dosen '" + namaDosenCSV + "' tidak ditemukan di database!",
                                    "Error Dosen",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        Mengajar m = new Mengajar();
                        m.setKodeMengajar(kodeMengajar);
                        m.setKodeMatkul(mk);  // Foreign Key Matkul
                        m.setIdDosen(d);      // Foreign Key Dosen
                        m.setTahunAjaran(tahunAjaran);

                        em.persist(m);
                    }
                }

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari CSV!");
                showTableMatkul();
                showTableDosen();
                showTableMengajar();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + e.getMessage());
                e.printStackTrace();
            }
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(58, 54, 154));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 10));

        jPanel3.setBackground(new java.awt.Color(58, 54, 154));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 5));

        jPanel6.setBackground(new java.awt.Color(58, 54, 154));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 5));

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Mata Kuliah", "Nama Mata Kuliah", "Semester", "Dosen Pengampu"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton5.setBackground(new java.awt.Color(255, 51, 51));
        jButton5.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(51, 51, 51));
        jButton5.setText("Hapus Data");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 255, 102));
        jButton4.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 51, 51));
        jButton4.setText("Menu Simpan Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 204, 102));
        jButton8.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 0, 0));
        jButton8.setText("Menu Perbarui Data");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton1.setText("Cetak");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(102, 255, 204));
        jButton11.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(0, 0, 0));
        jButton11.setText("Upload");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(102, 255, 204));
        jButton16.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(0, 0, 0));
        jButton16.setText("Download");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton16))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Mata Kuliah", jPanel2);

        jPanel7.setBackground(new java.awt.Color(58, 54, 154));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 5));

        jTable2.setBackground(new java.awt.Color(204, 204, 204));
        jTable2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Dosen", "Nama Dosen", "NIP", "Email"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton6.setBackground(new java.awt.Color(255, 51, 51));
        jButton6.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 51, 51));
        jButton6.setText("Hapus Data");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(102, 255, 102));
        jButton7.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 51, 51));
        jButton7.setText("Menu Simpan Data");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 204, 102));
        jButton9.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(0, 0, 0));
        jButton9.setText("Menu Perbarui Data");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton3.setText("Cetak");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(102, 255, 204));
        jButton10.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 0, 0));
        jButton10.setText("Upload");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(102, 255, 204));
        jButton18.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton18.setForeground(new java.awt.Color(0, 0, 0));
        jButton18.setText("Download");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton3))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton18))
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Dosen", jPanel5);

        jPanel9.setBackground(new java.awt.Color(58, 54, 154));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 5));

        jTable3.setBackground(new java.awt.Color(204, 204, 204));
        jTable3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Mata Kuliah", "Nama Mata Kuliah", "Semester", "Dosen Pengampu"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jButton12.setBackground(new java.awt.Color(255, 51, 51));
        jButton12.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(51, 51, 51));
        jButton12.setText("Hapus Data");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(102, 255, 102));
        jButton13.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(0, 51, 51));
        jButton13.setText("Menu Simpan Data");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(255, 204, 102));
        jButton14.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(0, 0, 0));
        jButton14.setText("Menu Perbarui Data");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton2.setText("Cetak");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(102, 255, 204));
        jButton15.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(0, 0, 0));
        jButton15.setText("Upload");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(102, 255, 204));
        jButton17.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jButton17.setForeground(new java.awt.Color(0, 0, 0));
        jButton17.setText("Download");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton15)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton17))
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Mengajar", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Perpetua", 2, 50)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(58, 54, 154));
        jLabel1.setText("DATA KULIAH");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int[] row = jTable1.getSelectedRows();
        if (row.length == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }
        String[] kodeLama = new String[row.length];
        String[] namaLama = new String[row.length];
        String[] sksLama = new String[row.length];
        String[] semesterLama = new String[row.length];

        for (int i = 0; i < row.length; i++) {
            kodeLama[i] = jTable1.getValueAt(row[i], 0).toString();
            namaLama[i] = jTable1.getValueAt(row[i], 1).toString();
            sksLama[i] = jTable1.getValueAt(row[i], 2).toString();
            semesterLama[i] = jTable1.getValueAt(row[i], 3).toString();
        }

        DeleteMatkul dialog = new DeleteMatkul(this, true, kodeLama, namaLama, sksLama, semesterLama);
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        InsertMatkul dialog = new InsertMatkul(this, true); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        kodeLama = jTable1.getValueAt(row, 0).toString();
        namaLama = jTable1.getValueAt(row, 1).toString();
        sksLama = jTable1.getValueAt(row, 2).toString();
        semesterLama = jTable1.getValueAt(row, 3).toString();

        UpdateMatkul dialog = new UpdateMatkul(this, true, kodeLama, namaLama, sksLama, semesterLama); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String path = "src/pertemuan14/ReportMataKuliah.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PBO_P14", "postgres", "1985");

            JasperPrint jprint = JasperFillManager.fillReport(path, parameters, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            jviewer.setSize(800, 600);
            jviewer.setLocationRelativeTo(this);
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int[] row = jTable2.getSelectedRows();
        if (row.length == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        String[] idLama = new String[row.length];
        String[] namaDosenlama = new String[row.length];
        String[] nipLama = new String[row.length];
        String[] emailLama = new String[row.length];

        for (int i = 0; i < row.length; i++) {
            idLama[i] = jTable2.getValueAt(row[i], 0).toString();
            namaDosenlama[i] = jTable2.getValueAt(row[i], 1).toString();
            nipLama[i] = jTable2.getValueAt(row[i], 2).toString();
            emailLama[i] = jTable2.getValueAt(row[i], 3).toString();
        }

        DeleteDosen dialog = new DeleteDosen(this, true, idLama, namaDosenlama, nipLama, emailLama);
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        InsertDosen dialog = new InsertDosen(this, true); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int row = jTable2.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        idLama = jTable2.getValueAt(row, 0).toString();
        namaDosenlama = jTable2.getValueAt(row, 1).toString();
        nipLama = jTable2.getValueAt(row, 2).toString();
        emailLama = jTable2.getValueAt(row, 3).toString();

        UpdateDosen dialog = new UpdateDosen(this, true, idLama, namaDosenlama, nipLama, emailLama); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            String path = "src/pertemuan14/ReportDosen.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PBO_P14", "postgres", "1985");

            JasperPrint jprint = JasperFillManager.fillReport(path, parameters, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            jviewer.setSize(800, 600);
            jviewer.setLocationRelativeTo(this);
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        imporCsvKeDatabaseDosen();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        imporCsvKeDatabaseMatkul();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int[] row = jTable3.getSelectedRows();
        if (row.length == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }
        String[] kdMengajarLama = new String[row.length];
        String[] pengajarLama = new String[row.length];
        String[] matkulLama = new String[row.length];
        String[] tahunLama = new String[row.length];

        for (int i = 0; i < row.length; i++) {
            kdMengajarLama[i] = jTable3.getValueAt(row[i], 0).toString();
            pengajarLama[i] = jTable3.getValueAt(row[i], 1).toString();
            matkulLama[i] = jTable3.getValueAt(row[i], 2).toString();
            tahunLama[i] = jTable3.getValueAt(row[i], 3).toString();
        }

        DeleteMengajar dialog = new DeleteMengajar(this, true, kdMengajarLama, pengajarLama, matkulLama, tahunLama);
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        InsertMengajar dialog = new InsertMengajar(this, true); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        int row = jTable3.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        kdMengajarLama = jTable3.getValueAt(row, 0).toString();
        pengajarLama = jTable3.getValueAt(row, 1).toString();
        matkulLama = jTable3.getValueAt(row, 2).toString();
        tahunLama = jTable3.getValueAt(row, 3).toString();

        UpdateMengajar dialog = new UpdateMengajar(this, true, kdMengajarLama, pengajarLama, matkulLama, tahunLama); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTableMatkul();
        showTableDosen();
        showTableMengajar();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String path = "src/pertemuan14/ReportMengajar.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PBO_P14", "postgres", "1985");

            JasperPrint jprint = JasperFillManager.fillReport(path, parameters, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            jviewer.setSize(800, 600);
            jviewer.setLocationRelativeTo(this);
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        imporCsvKeDatabaseMengajar();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        try {
            if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor!");
                return;
            }

            JnaFileChooser chooser = new JnaFileChooser();
            chooser.setMode(JnaFileChooser.Mode.Files);
            chooser.setTitle("Simpan Data Mata Kuliah");
            chooser.setDefaultFileName("Data_Matkul");
            chooser.setMultiSelectionEnabled(false);
            Window Window = null;
            boolean action = chooser.showOpenDialog(Window);

            if (action) {

                String fileString = chooser.getSelectedFile().getAbsolutePath() + ".csv";
//                exportToExcel(jTable1, fileString);
                exportToCSV(jTable1, fileString);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
            if (jTable3.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor!");
                return;
            }

            JnaFileChooser chooser = new JnaFileChooser();
            chooser.setMode(JnaFileChooser.Mode.Files);
            chooser.setTitle("Simpan Data Mengajar");
            chooser.setDefaultFileName("Data_Mengajar");
            chooser.setMultiSelectionEnabled(false);
            Window Window = null;
            boolean action = chooser.showOpenDialog(Window);

            if (action) {

                String fileString = chooser.getSelectedFile().getAbsolutePath() + ".xlsx";
                exportToExcel(jTable3, fileString);
//                exportToCSV(jTable3, fileString);                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            if (jTable2.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor!");
                return;
            }

            JnaFileChooser chooser = new JnaFileChooser();
            chooser.setMode(JnaFileChooser.Mode.Files);
            chooser.setTitle("Simpan Data Dosen");
            chooser.setDefaultFileName("Data_Dosen");
            chooser.setMultiSelectionEnabled(false);
            Window Window = null;
            boolean action = chooser.showOpenDialog(Window);

            if (action) {

                String fileString = chooser.getSelectedFile().getAbsolutePath() + ".xlsx";
                exportToExcel(jTable2, fileString);
//                exportToCSV(jTable2, fileString);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton18ActionPerformed

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
            java.util.logging.Logger.getLogger(GuiDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiDatabase().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
