//Representa una sola cancion 

package modelo;
public class NodoCancion {
    //Atributos
    private String ruta;
    private String nombre;
    private NodoCancion siguiente;
    private NodoCancion anterior;
    //Constructor
    public NodoCancion (String ruta, String nombre){
        this.ruta = ruta;
        this.nombre = nombre;
        this.siguiente = null;
        this.anterior = null;
    }
    //Getter y setter para ver o modificar los tipos de datos a la hora de la salida
    public String getRuta(){
        return ruta;
    }
    public String getNombre() {
        return nombre;
    }
 
    public NodoCancion getSiguiente() {
        return siguiente;
    }
 
    public void setSiguiente(NodoCancion siguiente) {
        this.siguiente = siguiente;
    }
 
    public NodoCancion getAnterior() {
        return anterior;
    }
 
    public void setAnterior(NodoCancion anterior) {
        this.anterior = anterior;
    }
}