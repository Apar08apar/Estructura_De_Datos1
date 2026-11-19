package catalogo;

import java.util.List;

/**
 * SECCIÓN 3: CATÁLOGO (lógica que une el conversor con el árbol B)
 * ------------------------------------------------------------------
 * Esta clase es la que usan el Controlador y la ventana. Así ni la vista
 * ni el controlador tocan el árbol directamente (respeta la arquitectura
 * Modelo - Vista - Controlador del proyecto).
 *
 * Su trabajo principal:
 *   - agregar(titulo):  título -> número (clave) -> insertar en el árbol B
 *   - eliminar(clave):  borrar del árbol B
 *   - cargar(lista):    meter muchos títulos de una vez (el CSV)
 *
 * COLISIONES: dos títulos distintos podrían dar el mismo número (es
 * rarísimo con 64 bits, pero posible en teoría). Si pasa, se prueba con
 * clave+1, clave+2... hasta encontrar un hueco libre. Si el título que
 * ya está en esa clave es el MISMO (normalizado), es un duplicado y se
 * rechaza.
 */
public class CatalogoCanciones {

    private final ArbolB arbol;

    public CatalogoCanciones() {
        this.arbol = new ArbolB(3);   // grado mínimo 3 -> 2 a 5 claves por nodo
    }

    /**
     * Agrega una canción. Devuelve la clave asignada, o -1 si el título
     * está vacío o ya existe.
     */
    public long agregar(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return -1;
        }
        String tituloLimpio = titulo.trim();
        long clave = ConversorTitulo.aNumero(tituloLimpio);

        while (true) {
            String existente = arbol.buscar(clave);
            if (existente == null) {
                arbol.insertar(clave, tituloLimpio);   // hueco libre: se guarda
                return clave;
            }
            if (ConversorTitulo.normalizar(existente)
                    .equals(ConversorTitulo.normalizar(tituloLimpio))) {
                return -1;                              // misma canción: duplicada
            }
            clave++;                                    // colisión: probar la siguiente
        }
    }

    /** Elimina por clave (la que se ve en la tabla). */
    public boolean eliminarPorClave(long clave) {
        return arbol.eliminar(clave);
    }

    /**
     * Carga una lista de títulos (por ejemplo los leídos del CSV).
     * Devuelve cuántos se agregaron realmente (los repetidos se omiten).
     */
    public int cargar(List<String> titulos) {
        int agregados = 0;
        for (String titulo : titulos) {
            if (agregar(titulo) != -1) {
                agregados++;
            }
        }
        return agregados;
    }

    /** Canciones ordenadas por clave: resultado del recorrido in-order. */
    public List<ArbolB.Entrada> listar() {
        return arbol.recorridoInOrden();
    }

    public int total() {
        return arbol.tamano();
    }

    public int alturaArbol() {
        return arbol.altura();
    }
}
