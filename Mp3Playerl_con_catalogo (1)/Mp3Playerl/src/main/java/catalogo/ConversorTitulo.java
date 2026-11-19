package catalogo;

import java.text.Normalizer;


public class ConversorTitulo {

    // Clase de utilidad, solo usa métodos estáticos, no se instancia.
    private ConversorTitulo() { }

    //Edita el texto para que se vea bien
    public static String normalizar(String titulo) {
        if (titulo == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    //Convierte el título en un número positivo de tipo long.Esa es la "clave" que se guarda en el árbol B.
    public static long aNumero(String titulo) {
        String limpio = normalizar(titulo);
        long h = 1125899906842597L;          // semilla (un primo grande)
        for (int i = 0; i < limpio.length(); i++) {
            h = 31 * h + limpio.charAt(i);   // acumula carácter por carácter
        }
        return h & Long.MAX_VALUE;           // fuerza que el número sea positivo
    }
}
