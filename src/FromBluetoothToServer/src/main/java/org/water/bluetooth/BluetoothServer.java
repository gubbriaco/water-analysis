package org.water.bluetooth;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;

public class BluetoothServer {

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

                if ("Mi 9T Pro".equals(remoteDeviceName)) {
                    System.out.println("Dispositivo Mi 9T Pro connesso: " + remoteDeviceName);

                    // Ricevi dati dal dispositivo connesso
                    InputStream inputStream = connection.openInputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, bytesRead);
                        System.out.println("Dati ricevuti: " + data);
                    }

                    // Chiudi la connessione quando hai finito
                    connection.close();
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
