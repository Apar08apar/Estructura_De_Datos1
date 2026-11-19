package vista;

/**
 * Esta clase representa el JFrame Form creado con el editor gráfico
 * (GUI Builder) de NetBeans. El método initComponents() es el bloque
 * que el propio IDE genera automáticamente cuando arrastras los
 * componentes desde la Paleta; por eso no se debe editar a mano
 * dentro del bloque marcado como "Generated Code".
 */
public class ReproductorVista extends javax.swing.JFrame {

    /**
     * Creates new form ReproductorVista
     */
    public ReproductorVista() {
        initComponents();
        this.setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblArtista = new javax.swing.JLabel();
        barraProgreso = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCanciones = new javax.swing.JList<>();
        btnAbrir = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnPausa = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnReanudar = new javax.swing.JButton();
        btnCargarCsv = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        lblTitulo.setBackground(new java.awt.Color(0, 0, 0));
        lblTitulo.setForeground(new java.awt.Color(255, 0, 102));
        lblTitulo.setText("Titulo");
        lblTitulo.setOpaque(true);

        lblArtista.setBackground(new java.awt.Color(0, 0, 0));
        lblArtista.setForeground(new java.awt.Color(255, 0, 102));
        lblArtista.setText("Artista");
        lblArtista.setOpaque(true);

        barraProgreso.setBackground(new java.awt.Color(0, 0, 0));
        barraProgreso.setForeground(new java.awt.Color(255, 0, 204));
        barraProgreso.setOpaque(true);
        barraProgreso.setStringPainted(true);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(0, 0, 0));

        listaCanciones.setBackground(new java.awt.Color(0, 0, 0));
        listaCanciones.setForeground(new java.awt.Color(255, 153, 204));
        listaCanciones.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listaCanciones);

        btnAbrir.setBackground(new java.awt.Color(255, 204, 255));
        btnAbrir.setForeground(new java.awt.Color(0, 0, 0));
        btnAbrir.setText("Agregar Cancion");

        btnAnterior.setBackground(new java.awt.Color(255, 0, 102));
        btnAnterior.setForeground(new java.awt.Color(0, 0, 0));
        btnAnterior.setText("Anterior");

        btnPlay.setBackground(new java.awt.Color(0, 0, 0));
        btnPlay.setForeground(new java.awt.Color(255, 0, 102));
        btnPlay.setText("Reproducir");

        btnPausa.setBackground(new java.awt.Color(255, 0, 102));
        btnPausa.setForeground(new java.awt.Color(0, 0, 0));
        btnPausa.setText("Pausar");

        btnSiguiente.setBackground(new java.awt.Color(255, 0, 102));
        btnSiguiente.setForeground(new java.awt.Color(0, 0, 0));
        btnSiguiente.setText("Siguiente");

        btnReanudar.setBackground(new java.awt.Color(255, 0, 102));
        btnReanudar.setForeground(new java.awt.Color(0, 0, 0));
        btnReanudar.setText("Reanudar");

        btnCargarCsv.setBackground(new java.awt.Color(255, 204, 255));
        btnCargarCsv.setForeground(new java.awt.Color(0, 0, 0));
        btnCargarCsv.setText("Cargar CSV");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAbrir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCargarCsv))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblArtista)
                        .addComponent(lblTitulo)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(btnAnterior)
                            .addGap(18, 18, 18)
                            .addComponent(btnPausa)
                            .addGap(18, 18, 18)
                            .addComponent(btnPlay)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnReanudar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnSiguiente))
                        .addComponent(jScrollPane1)
                        .addComponent(barraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblArtista)
                .addGap(18, 18, 18)
                .addComponent(barraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAbrir)
                    .addComponent(btnCargarCsv))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPlay)
                    .addComponent(btnPausa)
                    .addComponent(btnReanudar)
                    .addComponent(btnAnterior)
                    .addComponent(btnSiguiente))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barraProgreso;
    public javax.swing.JButton btnAbrir;
    public javax.swing.JButton btnAnterior;
    public javax.swing.JButton btnCargarCsv;
    public javax.swing.JButton btnPausa;
    public javax.swing.JButton btnPlay;
    public javax.swing.JButton btnReanudar;
    public javax.swing.JButton btnSiguiente;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblArtista;
    public javax.swing.JLabel lblTitulo;
    public javax.swing.JList<String> listaCanciones;
    // End of variables declaration//GEN-END:variables
}
