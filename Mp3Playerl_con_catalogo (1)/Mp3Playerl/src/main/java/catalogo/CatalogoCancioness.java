package catalogo;

import java.util.List;

//Lo que une el conversor cvon e arbol B
public class CatalogoCancioness {

    private final ArbolB arbol;

    public CatalogoCancioness() {
        this.arbol = new ArbolB(3);   // grado mínimo 3 -> 2 a 5 claves por nodo
    }

    //Agrega una canción y devuelve la clave asignada, o -1 si el títuloestá vacío o ya existe.
    public long agregar(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return -1;
        }
        String tituloLimpio = titulo.trim();
        long clave = ConversorTitulo.aNumero(tituloLimpio);

        while (true) {
            String existente = arbol.buscar(clave);
            if (existente == null) {
                arbol.insertar(clave, tituloLimpio);   // hueco libre y se guarda
                return clave;
            }
            if (ConversorTitulo.normalizar(existente)
                    .equals(ConversorTitulo.normalizar(tituloLimpio))) {
                return -1;                              // misma canción y se uplica
            }
            clave++;                                    // colisión y se prueba la siguiente 
        }
    }

    // Elimina por clave, es la que se ve en la tabla
    public boolean eliminarPorClave(long clave) {
        return arbol.eliminar(clave);
    }

    //lista de titulos
    public int cargar(List<String> titulos) {
        int agregados = 0;
        for (String titulo : titulos) {
            if (agregar(titulo) != -1) {
                agregados++;
            }
        }
        return agregados;
    }

    //Canciones ordenadas por clave, es el resultado del recorrido in-order
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
