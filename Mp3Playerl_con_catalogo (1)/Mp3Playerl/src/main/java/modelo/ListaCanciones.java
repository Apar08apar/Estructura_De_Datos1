package modelo;
 
import java.util.ArrayList;//Nomas es una lista temporal para devolverle la lista a quien la pida
import java.util.List;
 
//Atributos
public class ListaCanciones {
 
    private NodoCancion cabeza;   // primer nodo de la lista
    private NodoCancion cola;     // último nodo de la lista
    private NodoCancion actual;   // nodo que representa la canción actual
    private int tamano;
    //Constructor
    //Aun no hay ninguna cancion, entonces:
    public ListaCanciones() {
        this.cabeza = null;
        this.cola = null;
        this.actual = null;
        this.tamano = 0;
    }
 
    //Agregar una cancion al final
    public void agregarCancion(String ruta, String nombre) {
        NodoCancion nuevo = new NodoCancion(ruta, nombre);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
            actual = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }
        tamano++;
    }
    //Consultar o mover la cancion actual
    public NodoCancion obtenerActual() {
        return actual;
    }
 
    //
    public NodoCancion siguienteCancion() {
        if (actual != null && actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual;
    }
 
    /**
     * Retrocede el puntero "actual" al nodo anterior de la lista (atrás).
     */
    public NodoCancion anteriorCancion() {
        if (actual != null && actual.getAnterior() != null) {
            actual = actual.getAnterior();
        }
        return actual;
    }
 
    public boolean hayCancionSiguiente() {
        return actual != null && actual.getSiguiente() != null;
    }
 
    public boolean hayCancionAnterior() {
        return actual != null && actual.getAnterior() != null;
    }
 
    public boolean estaVacia() {
        return cabeza == null;
    }
 
    public int getTamano() {
        return tamano;
    }
 
    public NodoCancion getCabeza() {
        return cabeza;
    }
 
    /**
     * Ubica el puntero "actual" según la posición (índice) elegida,
     * recorriendo la lista desde la cabeza. Útil cuando el usuario
     * hace doble clic sobre una canción del JList.
     */
    public void establecerActualPorIndice(int indice) {
        NodoCancion temp = cabeza;
        int i = 0;
        while (temp != null && i < indice) {
            temp = temp.getSiguiente();
            i++;
        }
        if (temp != null) {
            actual = temp;
        }
    }
 
    /**
     * Devuelve los nombres de todas las canciones guardadas, en orden,
     * para poder mostrarlos en el JList de la vista.
     */
    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        NodoCancion temp = cabeza;
        while (temp != null) {
            nombres.add(temp.getNombre());
            temp = temp.getSiguiente();
        }
        return nombres;
    }
 
    public void vaciar() {
        cabeza = null;
        cola = null;
        actual = null;
        tamano = 0;
    }
}

