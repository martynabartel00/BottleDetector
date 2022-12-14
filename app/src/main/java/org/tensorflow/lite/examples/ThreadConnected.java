package org.tensorflow.lite.examples;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
public class ThreadConnected extends Thread {
    private final BluetoothSocket connectedBluetoothSocket;
    private final InputStream connectedInputStream;
    private final OutputStream connectedOutputStream;

    public ThreadConnected(BluetoothSocket socket) {
        connectedBluetoothSocket = socket;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        connectedInputStream = in;
        connectedOutputStream = out;
    }

    @Override
    public void run() {
    }

    public void send(String text){
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        try {
            connectedOutputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            connectedBluetoothSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
