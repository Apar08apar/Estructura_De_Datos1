package catalogo;

import java.util.ArrayList;
import java.util.List;

//Arbol B
public class ArbolB {

    //Nodo del arbol
    private static class Nodo {
        int n;               // la cantidad de claves que ahora puede tener el nodo
        long[] claves;       // menor a mayor
        String[] titulos;    
        Nodo[] hijos;        // punteros de los hijos
        boolean hoja;        // por si no tiene hijos

        Nodo(int t, boolean hoja) {
            this.hoja = hoja;
            this.claves = new long[2 * t - 1];
            this.titulos = new String[2 * t - 1];
            this.hijos = new Nodo[2 * t];
            this.n = 0;
        }
    }

    //para recorrer el arbol
    public static class Entrada {
        public final long clave;
        public final String titulo;

        public Entrada(long clave, String titulo) {
            this.clave = clave;
            this.titulo = titulo;
        }
    }

    private final int t;     // grado mas pequeño
    private Nodo raiz;
    private int tamano;      // canciones totales

    public ArbolB(int gradoMinimo) {
        if (gradoMinimo < 2) {
            throw new IllegalArgumentException("El grado mínimo debe ser >= 2");
        }
        this.t = gradoMinimo;
        this.raiz = new Nodo(t, true);   // árbol vacío: raíz = hoja sin claves
        this.tamano = 0;
    }

    public int tamano() {
        return tamano;
    }

    // Busqueda
    public String buscar(long clave) {
        return buscar(raiz, clave);
    }

    private String buscar(Nodo x, long clave) {
        int i = 0;
        // solo se busca cuando la clave es mayor al nodo
        while (i < x.n && clave > x.claves[i]) {
            i++;
        }
        if (i < x.n && clave == x.claves[i]) {
            return x.titulos[i];                 // se encuentra en ese nodo
        }
        if (x.hoja) {
            return null;                         // ya no hay donde uscar
        }
        return buscar(x.hijos[i], clave);        // bajamos por el hijo 
    }

    //insertar
    public boolean insertar(long clave, String titulo) {
        if (buscar(clave) != null) {
            return false;
        }
        Nodo r = raiz;
        if (r.n == 2 * t - 1) {
            // La raíz está llena, crece y se divide
            Nodo nuevaRaiz = new Nodo(t, false);
            nuevaRaiz.hijos[0] = r;
            raiz = nuevaRaiz;
            dividirHijo(nuevaRaiz, 0);
            insertarEnNodoNoLleno(nuevaRaiz, clave, titulo);
        } else {
            insertarEnNodoNoLleno(r, clave, titulo);
        }
        tamano++;
        return true;
    }

    //se divide cuando esta lleno
    private void dividirHijo(Nodo padre, int i) {
        Nodo y = padre.hijos[i];             // hijo lleno
        Nodo z = new Nodo(t, y.hoja);        // nuevo hermano derecho
        z.n = t - 1;

        // z se queda con las t-1 claves MAYORES de y
        for (int j = 0; j < t - 1; j++) {
            z.claves[j] = y.claves[j + t];
            z.titulos[j] = y.titulos[j + t];
        }
        // y si no es hoja, también con sus t hijos de la derecha
        if (!y.hoja) {
            for (int j = 0; j < t; j++) {
                z.hijos[j] = y.hijos[j + t];
                y.hijos[j + t] = null;
            }
        }

        long claveMedia = y.claves[t - 1];   // esta sube al padre
        String tituloMedio = y.titulos[t - 1];
        y.n = t - 1;                         // y conserva las t-1 menores

        // hacemos espacio en el padre para el nuevo hijo z y la clave media
        for (int j = padre.n; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }
        padre.hijos[i + 1] = z;
        for (int j = padre.n - 1; j >= i; j--) {
            padre.claves[j + 1] = padre.claves[j];
            padre.titulos[j + 1] = padre.titulos[j];
        }
        padre.claves[i] = claveMedia;
        padre.titulos[i] = tituloMedio;
        padre.n++;
    }

    //Inserta en el subárbol de x sabiendo que x NO está lleno
    private void insertarEnNodoNoLleno(Nodo x, long clave, String titulo) {
        int i = x.n - 1;
        if (x.hoja) {
            // Hoja: corremos a la derecha las claves mayores y ponemos la nueva
            while (i >= 0 && clave < x.claves[i]) {
                x.claves[i + 1] = x.claves[i];
                x.titulos[i + 1] = x.titulos[i];
                i--;
            }
            x.claves[i + 1] = clave;
            x.titulos[i + 1] = titulo;
            x.n++;
        } else {
            // Interno: buscamos por cuál hijo bajar
            while (i >= 0 && clave < x.claves[i]) {
                i--;
            }
            i++;
            if (x.hijos[i].n == 2 * t - 1) {     // hijo lleno -> se divide antes
                dividirHijo(x, i);
                if (clave > x.claves[i]) {       // la clave subida decide el lado
                    i++;
                }
            }
            insertarEnNodoNoLleno(x.hijos[i], clave, titulo);
        }
    }

    //Eliminacion
    public boolean eliminar(long clave) {
        if (buscar(clave) == null) {
            return false;
        }
        eliminar(raiz, clave);
        // Si la raíz quedó sin claves, el árbol se hace más bajo
        if (raiz.n == 0 && !raiz.hoja) {
            raiz = raiz.hijos[0];
        }
        tamano--;
        return true;
    }

    private void eliminar(Nodo x, long clave) {
        int idx = 0;
        while (idx < x.n && x.claves[idx] < clave) {
            idx++;
        }

        if (idx < x.n && x.claves[idx] == clave) {
            // si la clave está en este nodo
            if (x.hoja) {
                quitarDeHoja(x, idx);            //s hoja, solo se quita
            } else {
                eliminarDeNodoInterno(x, idx);   //es interno, se reemplaza
            }
        } else {
            // si la clave debe estar en un hijo; hay que bajar
            boolean eraUltimoHijo = (idx == x.n);
            if (x.hijos[idx].n < t) {
                llenar(x, idx);                  // asegura >= t claves en el hijo
            }
            // si el ultimo hijo se fusionó con el anterior, bajamos por idx-1
            if (eraUltimoHijo && idx > x.n) {
                eliminar(x.hijos[idx - 1], clave);
            } else {
                eliminar(x.hijos[idx], clave);
            }
        }
    }

    private void quitarDeHoja(Nodo x, int idx) {
        for (int i = idx + 1; i < x.n; i++) {
            x.claves[i - 1] = x.claves[i];
            x.titulos[i - 1] = x.titulos[i];
        }
        x.n--;
    }

    private void eliminarDeNodoInterno(Nodo x, int idx) {
        long clave = x.claves[idx];
        if (x.hijos[idx].n >= t) {
            // El hijo izquierdo tiene claves de sobr asi usamos el predes3sor, la clacve mas amplia de la ixquierda sera
            
            Nodo cur = x.hijos[idx];
            while (!cur.hoja) {
                cur = cur.hijos[cur.n];
            }
            long clavePred = cur.claves[cur.n - 1];
            x.claves[idx] = clavePred;
            x.titulos[idx] = cur.titulos[cur.n - 1];
            eliminar(x.hijos[idx], clavePred);
        } else if (x.hijos[idx + 1].n >= t) {
            // El hijo derecho tiene de sobra: usamos el sucesor la clave más pequeña del subárbol derecho
            Nodo cur = x.hijos[idx + 1];
            while (!cur.hoja) {
                cur = cur.hijos[0];
            }
            long claveSuc = cur.claves[0];
            x.claves[idx] = claveSuc;
            x.titulos[idx] = cur.titulos[0];
            eliminar(x.hijos[idx + 1], claveSuc);
        } else {
            // Ambos hijos están al mínimo: se fusionan con loa clav d el mitad y se borran
            fusionar(x, idx);
            eliminar(x.hijos[idx], clave);
        }
    }

    //Garantiza que el hijho teng aal menos una clave
    private void llenar(Nodo x, int idx) {
        if (idx != 0 && x.hijos[idx - 1].n >= t) {
            prestarDelAnterior(x, idx);
        } else if (idx != x.n && x.hijos[idx + 1].n >= t) {
            prestarDelSiguiente(x, idx);
        } else if (idx != x.n) {
            fusionar(x, idx);
        } else {
            fusionar(x, idx - 1);
        }
    }

    //Rotacion a la derecha
    private void prestarDelAnterior(Nodo x, int idx) {
        Nodo hijo = x.hijos[idx];
        Nodo hermano = x.hijos[idx - 1];

        for (int i = hijo.n - 1; i >= 0; i--) {
            hijo.claves[i + 1] = hijo.claves[i];
            hijo.titulos[i + 1] = hijo.titulos[i];
        }
        if (!hijo.hoja) {
            for (int i = hijo.n; i >= 0; i--) {
                hijo.hijos[i + 1] = hijo.hijos[i];
            }
        }
        // la clave del padre baja al hijo, la mayor del hermano sube al padre
        hijo.claves[0] = x.claves[idx - 1];
        hijo.titulos[0] = x.titulos[idx - 1];
        if (!hijo.hoja) {
            hijo.hijos[0] = hermano.hijos[hermano.n];
            hermano.hijos[hermano.n] = null;
        }
        x.claves[idx - 1] = hermano.claves[hermano.n - 1];
        x.titulos[idx - 1] = hermano.titulos[hermano.n - 1];
        hijo.n++;
        hermano.n--;
    }

    // Rotación a la izquierda, el hermano derecho da su menor clave.
    private void prestarDelSiguiente(Nodo x, int idx) {
        Nodo hijo = x.hijos[idx];
        Nodo hermano = x.hijos[idx + 1];

        hijo.claves[hijo.n] = x.claves[idx];
        hijo.titulos[hijo.n] = x.titulos[idx];
        if (!hijo.hoja) {
            hijo.hijos[hijo.n + 1] = hermano.hijos[0];
        }
        x.claves[idx] = hermano.claves[0];
        x.titulos[idx] = hermano.titulos[0];

        for (int i = 1; i < hermano.n; i++) {
            hermano.claves[i - 1] = hermano.claves[i];
            hermano.titulos[i - 1] = hermano.titulos[i];
        }
        if (!hermano.hoja) {
            for (int i = 1; i <= hermano.n; i++) {
                hermano.hijos[i - 1] = hermano.hijos[i];
            }
            hermano.hijos[hermano.n] = null;
        }
        hijo.n++;
        hermano.n--;
    }

    //Une hijos la clave del padre y los hijos en un solo 
    private void fusionar(Nodo x, int idx) {
        Nodo hijo = x.hijos[idx];
        Nodo hermano = x.hijos[idx + 1];

        // la clave del padre baja al medio del nodo fusionado
        hijo.claves[t - 1] = x.claves[idx];
        hijo.titulos[t - 1] = x.titulos[idx];
        // se copian las claves y los hijs del hermano
        for (int i = 0; i < hermano.n; i++) {
            hijo.claves[i + t] = hermano.claves[i];
            hijo.titulos[i + t] = hermano.titulos[i];
        }
        if (!hijo.hoja) {
            for (int i = 0; i <= hermano.n; i++) {
                hijo.hijos[i + t] = hermano.hijos[i];
            }
        }
        // se cierra el hueco que dejó en el padre
        for (int i = idx + 1; i < x.n; i++) {
            x.claves[i - 1] = x.claves[i];
            x.titulos[i - 1] = x.titulos[i];
        }
        for (int i = idx + 2; i <= x.n; i++) {
            x.hijos[i - 1] = x.hijos[i];
        }
        x.hijos[x.n] = null;
        hijo.n += hermano.n + 1;
        x.n--;
    }

    //orden de las canciones numericas ne ineorder 
    public List<Entrada> recorridoInOrden() {
        List<Entrada> lista = new ArrayList<>();
        recorrer(raiz, lista);
        return lista;
    }

    private void recorrer(Nodo x, List<Entrada> salida) {
        for (int i = 0; i < x.n; i++) {
            if (!x.hoja) {
                recorrer(x.hijos[i], salida);    // primero todo lo menor a claves[i]
            }
            salida.add(new Entrada(x.claves[i], x.titulos[i]));
        }
        if (!x.hoja) {
            recorrer(x.hijos[x.n], salida);      // y al final el último hijo
        }
    }

    //Altura del arbol
    public int altura() {
        int h = 1;
        Nodo x = raiz;
        while (!x.hoja) {
            x = x.hijos[0];
            h++;
        }
        return h;
    }

    // verificar
    public void verificarInvariantes() {
        int[] alturaHojas = {-1};
        verificar(raiz, true, 1, alturaHojas, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private void verificar(Nodo x, boolean esRaiz, int nivel, int[] alturaHojas,
                           long min, long max) {
        if (!esRaiz && x.n < t - 1) {
            throw new IllegalStateException("Nodo con menos de t-1 claves");
        }
        if (x.n > 2 * t - 1) {
            throw new IllegalStateException("Nodo con más de 2t-1 claves");
        }
        for (int i = 0; i < x.n; i++) {
            if (x.claves[i] < min || x.claves[i] > max
                    || (i > 0 && x.claves[i - 1] >= x.claves[i])) {
                throw new IllegalStateException("Claves desordenadas");
            }
        }
        if (x.hoja) {
            if (alturaHojas[0] == -1) {
                alturaHojas[0] = nivel;
            } else if (alturaHojas[0] != nivel) {
                throw new IllegalStateException("Hojas a distinta altura");
            }
            return;
        }
        for (int i = 0; i <= x.n; i++) {
            long lo = (i == 0) ? min : x.claves[i - 1];
            long hi = (i == x.n) ? max : x.claves[i];
            verificar(x.hijos[i], false, nivel + 1, alturaHojas, lo, hi);
        }
    }
}
