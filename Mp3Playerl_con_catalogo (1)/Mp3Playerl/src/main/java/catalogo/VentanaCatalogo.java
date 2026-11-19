package catalogo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

//Arriba:  campo de texto + botón "Agregar canción" (nueva clave, - Centro:  JTable con #, clave numérica y título. Sus filas salen, del RECORRIDO IN-ORDER del árbol B (catalogo.listar, Abajo:   botón "Eliminar seleccionada", "Cerrar" y un resumen con
 
public class VentanaCatalogo extends JDialog {

    private final CatalogoCanciones catalogo;

    // Componentes de la interfaz
    private final JTextField txtNuevaCancion = new JTextField(28);
    private final JButton btnAgregar = new JButton("Agregar canción");
    private final JButton btnEliminar = new JButton("Eliminar seleccionada");
    private final JButton btnCerrar = new JButton("Cerrar");
    private final JLabel lblResumen = new JLabel(" ");
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    //padre    ventana principal (el JFrame del reproductor). Puede ser, null; se usa para centrar y para que quede "encima", catalogo el catálogo ya cargado con el CSV.
    public VentanaCatalogo(Frame padre, CatalogoCanciones catalogo) {
        super(padre, "Catálogo de canciones (Árbol B)", true); // true = modal
        this.catalogo = catalogo;

        //tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"#", "Clave numérica", "Título"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(45);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(380);

        //Panel superior: agregar
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Nueva canción"));
        panelAgregar.add(new JLabel("Título:"));
        panelAgregar.add(txtNuevaCancion);
        panelAgregar.add(btnAgregar);

        //Panel inferior: eliminar,cerrar,resumen
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnCerrar);
        panelAcciones.add(lblResumen);

        //Armado de la ventana
        setLayout(new BorderLayout(6, 6));
        add(panelAgregar, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        //Eventos de los botones 
        btnAgregar.addActionListener(e -> agregarCancion());
        txtNuevaCancion.addActionListener(e -> agregarCancion()); // Enter también agrega
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        btnCerrar.addActionListener(e -> dispose());

        refrescarTabla();       // primer dibujo de la tabla
        setSize(720, 520);
        setLocationRelativeTo(padre);
    }

    //Vuelve a llenar la tabla recorriendo el árbol B en in-order. Se llama al abrir la ventana y después de cada agregar/eliminar.
    
    private void refrescarTabla() {
        modeloTabla.setRowCount(0);                       // vaciar la tabla
        List<ArbolB.Entrada> entradas = catalogo.listar(); // recorrido in-order
        int numero = 1;
        for (ArbolB.Entrada e : entradas) {
            modeloTabla.addRow(new Object[]{numero++, e.clave, e.titulo});
        }
        lblResumen.setText("   Total: " + catalogo.total()
                + " canciones  |  Altura del árbol: " + catalogo.alturaArbol());
    }

    //Lee el texto, lo convierte en clave y lo inserta en el árbol B. 
    private void agregarCancion() {
        String titulo = txtNuevaCancion.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el título de la canción.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long clave = catalogo.agregar(titulo);
        if (clave == -1) {
            JOptionPane.showMessageDialog(this, "Esa canción ya existe en el catálogo.",
                    "Duplicada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtNuevaCancion.setText("");
        refrescarTabla();
        seleccionarClave(clave);   // deja resaltada la canción recién agregada
        txtNuevaCancion.requestFocusInWindow();
    }

    //Elimina del árbol B la clave de la fila seleccionada. 
    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una canción de la tabla.",
                    "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        long clave = (Long) modeloTabla.getValueAt(fila, 1);
        String titulo = (String) modeloTabla.getValueAt(fila, 2);

        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + titulo + "\"?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            catalogo.eliminarPorClave(clave);
            refrescarTabla();
        }
    }

    //Busca la fila con esa clave y la resalta / hace visible. 
    private void seleccionarClave(long clave) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((Long) modeloTabla.getValueAt(i, 1) == clave) {
                tabla.setRowSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                return;
            }
        }
    }

    //Método de ayuda para abrir la ventana desde cualquier, VentanaCatalogo.mostrar(this, catalogo
    public static void mostrar(Component origen, CatalogoCanciones catalogo) {
        // Si 'origen' ya es el JFrame se usa directo; si es un componente interno, se busca su ventana
        Frame padre = (origen instanceof Frame)
                ? (Frame) origen
                : (Frame) SwingUtilities.getAncestorOfClass(Frame.class, origen);
        new VentanaCatalogo(padre, catalogo).setVisible(true);
    }
}
