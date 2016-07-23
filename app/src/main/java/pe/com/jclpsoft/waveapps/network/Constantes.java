package pe.com.jclpsoft.waveapps.network;

public class Constantes {
    /**
     * Puerto para la conexión
     */
    private static final String PUERTO_HOST = "";

    /**
     * Dirección IP de WebService
     */
    private static final String IP = "http://app.qaliwarma.gob.pe";
    /**
     * URLs del Web Service
     */
    public static final String GET = IP + PUERTO_HOST + "/waveapps/list_transact.php";
    public static final String DELETE = IP + PUERTO_HOST + "/waveapps/insert_transact.php";
    public static final String INSERT = IP + PUERTO_HOST + "/waveapps/delete_transact.php";

    /**
     * Clave para el valor extra que representa al identificador de una transaccion
     */
    public static final String EXTRA_ID = "IDEXTRA";

}