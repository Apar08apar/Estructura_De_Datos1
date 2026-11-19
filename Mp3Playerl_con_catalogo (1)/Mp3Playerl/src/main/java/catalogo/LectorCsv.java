package catalogo;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//Lee el archivo y devuelve SOLO la lista de títulos. Es flexible:
public class LectorCsv {

    private LectorCsv() { }

    public static List<String> leerTitulos(File archivo) throws IOException {
        String contenido = decodificar(Files.readAllBytes(archivo.toPath()));
        // quitar el BOM, marca invisible que Excel pone al inici
        if (!contenido.isEmpty() && contenido.charAt(0) == '﻿') {
            contenido = contenido.substring(1);
        }

        String[] lineas = contenido.split("\\r?\\n");
        List<String> titulos = new ArrayList<>();
        if (lineas.length == 0) {
            return titulos;
        }

        char separador = detectarSeparador(lineas[0]);
        int columnaTitulo = 0;
        int primeraFila = 0;

        // ¿La primera fila es un encabezado?
        List<String> cabecera = dividirLinea(lineas[0], separador);
        for (int i = 0; i < cabecera.size(); i++) {
            String c = ConversorTitulo.normalizar(cabecera.get(i));
            if (c.equals("titulo") || c.equals("cancion") || c.equals("nombre")
                    || c.equals("title") || c.equals("song") || c.equals("name")) {
                columnaTitulo = i;
                primeraFila = 1;        // se salta la fila de encabezado
                break;
            }
        }

        for (int f = primeraFila; f < lineas.length; f++) {
            if (lineas[f].trim().isEmpty()) {
                continue;               // líneas vacías se ignoran
            }
            List<String> celdas = dividirLinea(lineas[f], separador);
            if (columnaTitulo < celdas.size()) {
                String titulo = celdas.get(columnaTitulo).trim();
                if (!titulo.isEmpty()) {
                    titulos.add(titulo);
                }
            }
        }
        return titulos;
    }

    //Intenta UTF-8 estricto, si hay bytes inválidos, usa Windows-1252. */
    private static String decodificar(byte[] bytes) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            return new String(bytes, Charset.forName("windows-1252"));
        }
    }

    //Elige ';' o ',' según cuál aparezca más en la primera línea
    private static char detectarSeparador(String linea) {
        int comas = 0;
        int puntoYComa = 0;
        for (char c : linea.toCharArray()) {
            if (c == ',') comas++;
            if (c == ';') puntoYComa++;
        }
        return puntoYComa > comas ? ';' : ',';
    }

    //Divide una línea en celdas respetando las comillas dobles
    private static List<String> dividirLinea(String linea, char separador) {
        List<String> celdas = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                // "" dentro de comillas representa una comilla literal
                if (entreComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    actual.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (c == separador && !entreComillas) {
                celdas.add(actual.toString());   // fin de celda
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        celdas.add(actual.toString());           // última celda
        return celdas;
    }
}
