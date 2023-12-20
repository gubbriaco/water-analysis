package org.bluetooth;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;

public interface IScanHandler {
    void handle(BluetoothDevice device, ScanData data);
}
