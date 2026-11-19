package controlador;
 
import modelo.ReproductorModelo;
import vista.ReproductorVista;
 
/**
 *
 * @author diotallevi
 */
public class Mp3Playerl {
 
    public static void main(String[] args) {
        // Establecer el nivel de log a solo advertencias o errores
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
 
        // Los componentes Swing siempre deben crearse en el Event Dispatch Thread
        java.awt.EventQueue.invokeLater(() -> {
            ReproductorModelo objetoModelo = new ReproductorModelo();
            ReproductorVista objetoVista = new ReproductorVista();
            new Controlador(objetoModelo, objetoVista);
            objetoVista.setVisible(true);
        });
    }
}
