package controlador;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import catalogo.CatalogoCanciones;
import catalogo.LectorCsv;
import catalogo.VentanaCatalogo;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import modelo.NodoCancion;
import modelo.ReproductorModelo;
import vista.ReproductorVista;
 
/**
 *
 * @author diotallevi
 */
public class Controlador implements ActionListener, BasicPlayerListener {
 
    private ReproductorModelo modelo;
    private ReproductorVista vista;
    private boolean estaPausado = false;
    private long tamanoArchivo;
    private DefaultListModel<String> modeloLista; // modelo visual enlazado al JList de la vista
    private final CatalogoCanciones catalogo = new CatalogoCanciones(); // catálogo CSV guardado en un árbol B
 
    // Constructor
    public Controlador(ReproductorModelo entradaObjetoModelo, ReproductorVista entradaObjetoVista) {
        this.modelo = entradaObjetoModelo;
        this.vista = entradaObjetoVista;
        this.modeloLista = new DefaultListModel<>();
        this.vista.listaCanciones.setModel(this.modeloLista);
 
        this.modelo.setControlador(this); // Activar eventos de la librería de audio
 
        // Agregando Eventos a los botones del JFrame Form
        this.vista.btnPlay.addActionListener(this);
        this.vista.btnPausa.addActionListener(this);
        this.vista.btnAbrir.addActionListener(this);
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnAnterior.addActionListener(this);
        this.vista.btnReanudar.addActionListener(this);
        this.vista.btnCargarCsv.addActionListener(this); // botón nuevo: cargar el CSV del catálogo
 
        // Doble clic sobre una canción de la lista -> reproducirla directamente
        this.vista.listaCanciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    reproducirCancionSeleccionada();
                }
            }
        });
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == vista.btnAbrir) {
                agregarCancionesALaLista();
            } else if (e.getSource() == vista.btnPlay) {
    if (modelo.obtenerCancionActual() != null) {
        modelo.reproducirCancionActual();
        marcarSeleccionEnLista();
        estaPausado = false;
    } else {
        JOptionPane.showMessageDialog(vista, "Primero agrega canciones con el botón 'Agregar Canciones'.");
    }
            } else if (e.getSource() == vista.btnPausa) {
                modelo.pausar();
                estaPausado = true;
            } else if (e.getSource() == vista.btnReanudar) {
                modelo.reanudar();
                estaPausado = false;
            } else if (e.getSource() == vista.btnSiguiente) {
                reproducirSiguiente();
            } else if (e.getSource() == vista.btnAnterior) {
                reproducirAnterior();
            } else if (e.getSource() == vista.btnCargarCsv) {
                cargarCatalogoCsv();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }
 
    /**
     * Abre el JFileChooser (permitiendo selección múltiple), agrega cada
     * archivo elegido al final de la lista doblemente enlazada y a la
     * representación visual (JList). Si la lista estaba vacía, reproduce
     * automáticamente la primera canción agregada.
     */
    private void agregarCancionesALaLista() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
        int seleccion = fileChooser.showOpenDialog(vista);
 
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File[] archivos = fileChooser.getSelectedFiles();
            boolean listaEstabaVacia = modelo.getListaCanciones().estaVacia();
 
            for (File archivo : archivos) {
                modelo.agregarCancionALista(archivo.getAbsolutePath());
                modeloLista.addElement(archivo.getName());
            }
 
            if (listaEstabaVacia && !modelo.getListaCanciones().estaVacia()) {
                try {
                    modelo.reproducirCancionActual();
                    marcarSeleccionEnLista();
                    estaPausado = false;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al abrir el archivo: " + ex.getMessage());
                }
            }
        }
    }
 
    /** Avanza a la siguiente canción de la lista doblemente enlazada. */
    private void reproducirSiguiente() {
        if (modelo.getListaCanciones().hayCancionSiguiente()) {
            try {
                modelo.detener();
                NodoCancion cancion = modelo.avanzarCancion();
                modelo.setRuta(cancion.getRuta());
                modelo.reproducir();
                marcarSeleccionEnLista();
                estaPausado = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al cambiar de canción: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Ya estás en la última canción de la lista.");
        }
    }
 
    /** Retrocede a la canción anterior de la lista doblemente enlazada. */
    private void reproducirAnterior() {
        if (modelo.getListaCanciones().hayCancionAnterior()) {
            try {
                modelo.detener();
                NodoCancion cancion = modelo.retrocederCancion();
                modelo.setRuta(cancion.getRuta());
                modelo.reproducir();
                marcarSeleccionEnLista();
                estaPausado = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al cambiar de canción: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Ya estás en la primera canción de la lista.");
        }
    }
 
    /** Reproduce la canción sobre la que el usuario hizo doble clic en el JList. */
    private void reproducirCancionSeleccionada() {
        int indice = vista.listaCanciones.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        try {
            modelo.detener();
            modelo.getListaCanciones().establecerActualPorIndice(indice);
            modelo.reproducirCancionActual();
            estaPausado = false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al reproducir la canción: " + ex.getMessage());
        }
    }
 
    /** Sincroniza la selección visual del JList con la canción actual del modelo. */
    private void marcarSeleccionEnLista() {
        int indice = obtenerIndiceActual();
        if (indice >= 0) {
            vista.listaCanciones.setSelectedIndex(indice);
        }
    }
 
    /** Recorre la lista doblemente enlazada desde la cabeza para hallar el índice de la canción actual. */
    private int obtenerIndiceActual() {
        NodoCancion actual = modelo.obtenerCancionActual();
        if (actual == null) {
            return -1;
        }
        NodoCancion nodo = modelo.getListaCanciones().getCabeza();
        int indice = 0;
        while (nodo != null) {
            if (nodo == actual) {
                return indice;
            }
            nodo = nodo.getSiguiente();
            indice++;
        }
        return -1;
    }
 
    @Override
    public void opened(Object source, Map properties) {
        
        String titulo = obtenerPropiedad(properties, "title", null);
String artista = obtenerPropiedad(properties, "author", "Desconocido");

if (titulo == null) {
    NodoCancion cancion = modelo.obtenerCancionActual();

    if (cancion != null) {
        titulo = new File(cancion.getRuta()).getName();
    } else {
        titulo = "Desconocido";
    }
}

vista.lblTitulo.setText("Título: " + titulo);
vista.lblArtista.setText("Artista: " + artista);
        if (properties.containsKey("audio.length.bytes")) {
            tamanoArchivo = Long.parseLong(properties.get("audio.length.bytes").toString());
        }
    }
 
    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        if (tamanoArchivo > 0) {
            float progreso = (bytesread * 100.0f) / tamanoArchivo;
            vista.barraProgreso.setValue((int) progreso);
        }
 
        long segundosTotales = microseconds / 1000000;
        long minutos = segundosTotales / 60;
        long segundos = segundosTotales % 60;
        vista.barraProgreso.setString(String.format("%02d:%02d", minutos, segundos));
    }
 
    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        if (bpe.getCode() == BasicPlayerEvent.STOPPED) {
            estaPausado = false;
            vista.barraProgreso.setValue(0);
        }
        // Cuando la canción termina sola (no por STOP manual), avanza automáticamente
        if (bpe.getCode() == BasicPlayerEvent.EOM && modelo.getListaCanciones().hayCancionSiguiente()) {
            reproducirSiguiente();
        }
    }
 
    @Override
    public void setController(BasicController bc) {
        throw new UnsupportedOperationException("");
    }
 
    private String obtenerPropiedad(Map props, String llave, String valorDefecto) {
        Object valor = props.get(llave);
        return (valor != null && !valor.toString().isEmpty()) ? valor.toString() : valorDefecto;
    }
 
    public void limpiarLabels() {
        vista.lblTitulo.setText("Título: -");
        vista.lblArtista.setText("Artista: -");
        vista.barraProgreso.setValue(0);
        vista.barraProgreso.setString("00:00");
    }

    // ---------------------------------------------------------
    // Catálogo de canciones desde CSV (árbol B)
    // ---------------------------------------------------------

    /**
     * 1) Pide al usuario el archivo .csv con JFileChooser.
     * 2) Lee los títulos (LectorCsv).
     * 3) Cada título se convierte en número y se inserta en el árbol B
     *    (CatalogoCanciones).
     * 4) Abre la ventana emergente con la tabla (recorrido in-order),
     *    desde donde se pueden agregar y eliminar canciones.
     */
    private void cargarCatalogoCsv() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        if (selector.showOpenDialog(vista) != JFileChooser.APPROVE_OPTION) {
            return; // el usuario canceló
        }
        File archivo = selector.getSelectedFile();
        try {
            List<String> titulos = LectorCsv.leerTitulos(archivo);
            int agregadas = catalogo.cargar(titulos);
            JOptionPane.showMessageDialog(vista, "Se cargaron " + agregadas
                    + " canciones (de " + titulos.size() + " filas del CSV).");
            VentanaCatalogo.mostrar(vista, catalogo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "No se pudo leer el CSV: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
 

