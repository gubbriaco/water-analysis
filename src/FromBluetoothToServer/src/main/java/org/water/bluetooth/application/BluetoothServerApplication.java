package org.water.bluetooth.application;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;

public class BluetoothServerApplication implements BluetoothServer {

    /** I ensure that the class has only one instance and that this instance is easily accessible. Therefore, the class
     * itself ensures that no other instance can be created and, via the static and synchronized getIstance(), it
     * provides an easy way to access the only instance created. This technique used is the so-called Lazy
     * Initialisation.
     */
    private static BluetoothServerApplication INSTANCE = null;

    private BluetoothServerApplication() {}

    /**
     * Allows access to the only instance that can be created of the class. It is made synchronised to guarantee the
     * atomicity of the creation process in the case of concurrent access of the class.
     * @return Instance of the class {@link BluetoothServerApplication}
     */
    public static synchronized BluetoothServerApplication getIstance() {
        if(INSTANCE == null)
            INSTANCE = new BluetoothServerApplication();
        return INSTANCE;
    }

    @Override public void start() {

    }

    private static String deviceName = "HC05";

    public static void main(String[] args) {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.println("Server Bluetooth in esecuzione su: " + localDevice.getBluetoothAddress());

            // Crea un server con un nome univoco
            String url = "btspp://localhost:" + new UUID("0000110100001000800000805F9B34FB", false).toString() + ";name=BluetoothServer";
            StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open(url);

            while (true) {
                System.out.println("In attesa di connessioni...");
                StreamConnection connection = notifier.acceptAndOpen();

                // Controlla il nome del dispositivo remoto prima di accettare la connessione
                RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(connection);
                String remoteDeviceName = remoteDevice.getFriendlyName(true);

                if (deviceName.equals(remoteDeviceName) || deviceName.equals("Mi 9T Pro")) {
                    System.out.println("Dispositivo " + deviceName + " connesso: " + remoteDeviceName);

                    // Ricevi dati dal dispositivo connesso
                    InputStream inputStream = connection.openInputStream();
                    StringBuilder receivedData = new StringBuilder();
                    int character;

                    while ((character = inputStream.read()) != -1) {
                        char receivedChar = (char) character;

                        // Escludi il punto e virgola dalla stampa
                        if (receivedChar != ';') {
                            receivedData.append(receivedChar);
                        }

                        // Stampa i dati quando viene trovato il carattere ';'
                        if (receivedChar == ';') {
                            String data = receivedData.toString().trim();
                            System.out.println("Dati ricevuti: " + data);
                            receivedData.setLength(0); // Resetta il buffer per il prossimo messaggio
                        }
                    }

                    // Chiudi la connessione quando hai finito
                    connection.close();
                    System.out.println("Connessione chiusa.");

                } else {
                    System.out.println("Connessione rifiutata. Dispositivo non riconosciuto: " + remoteDeviceName);
                    connection.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
