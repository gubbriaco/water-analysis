package org.water.bluetooth;
import org.water.bluetooth.application.Server;
import org.water.bluetooth.application.ServerApplication;

public class Main {

    public static void main(String...strings) {

        Server bs = ServerApplication.getInstance();
        bs.start();

    }

}