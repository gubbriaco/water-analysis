package org.water.bluetooth;
import org.water.bluetooth.application.BluetoothServer;
import org.water.bluetooth.application.BluetoothServerApplication;

public class Main {

    public static void main(String...strings) {

        BluetoothServer bs = BluetoothServerApplication.getIstance();
        bs.start();

    }

}