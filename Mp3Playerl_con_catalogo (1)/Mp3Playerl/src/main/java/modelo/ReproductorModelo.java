package modelo;
 
import java.io.File;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
 
/**
 * Modelo del reproductor. Envuelve al BasicPlayer (motor de audio) y
 * ahora además guarda el listado de canciones (playlist) usando la
 * lista doblemente enlazada ListaCanciones.
 */
public class ReproductorModelo {
 
    private BasicPlayer reproductor;
    private String ruta;
    private BasicPlayerListener listener;
    private ListaCanciones listaCanciones;
 
    // Constructor
    public ReproductorModelo() {
        this.reproductor = new BasicPlayer();
        this.listaCanciones = new ListaCanciones();
    }
 
    // Método SET para iniciar el atributo de la ruta
    public void setRuta(String ruta) throws Exception {
        this.ruta = ruta;
        this.reproductor.open(new File(this.ruta));
    }
 
    // Método para que el controlador se suscriba a los eventos
    public void setControlador(BasicPlayerListener entradaListener) {
        this.listener = entradaListener;
        this.reproductor.addBasicPlayerListener(this.listener);
    }
 
    // Método para reproducir
    public void reproducir() throws Exception {
        this.reproductor.play();
    }
 
    // Método para pausar la reproducción
    public void pausar() throws Exception {
        this.reproductor.pause();
    }
 
    // Método para reanudar la reproducción
    public void reanudar() throws Exception {
        this.reproductor.resume();
    }
 
    // Método para detener la reproducción
    public void detener() throws Exception {
        this.reproductor.stop();
    }
 
    // ---------------------------------------------------------
    // Manejo del listado de canciones (lista doblemente enlazada)
    // ---------------------------------------------------------
 
    /** Agrega una canción nueva al final de la playlist. */
    public void agregarCancionALista(String ruta) {
        String nombre = new File(ruta).getName();
        listaCanciones.agregarCancion(ruta, nombre);
    }
 
    public ListaCanciones getListaCanciones() {
        return listaCanciones;
    }
 
    public NodoCancion obtenerCancionActual() {
        return listaCanciones.obtenerActual();
    }
 
    public NodoCancion avanzarCancion() {
        return listaCanciones.siguienteCancion();
    }
 
    public NodoCancion retrocederCancion() {
        return listaCanciones.anteriorCancion();
    }
 
    /** Carga (abre) y reproduce la canción que está marcada como "actual". */
    public void reproducirCancionActual() throws Exception {
        NodoCancion cancion = listaCanciones.obtenerActual();
        if (cancion != null) {
            setRuta(cancion.getRuta());
            reproducir();
        }
    }
}

