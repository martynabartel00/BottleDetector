package org.tensorflow.lite.examples;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import androidx.core.app.ActivityCompat;

public class ThreadConnectBTdevice extends Thread {

    private BluetoothSocket bluetoothSocket = null;
    private final BluetoothDevice bluetoothDevice;
    private final Context context;
    private final Listener listener;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public interface Listener {
        void onConnected(BluetoothSocket bluetoothSocket);
    }

    public ThreadConnectBTdevice(Context context, BluetoothDevice device, Listener listener) {
        this.bluetoothDevice = device;
        this.context = context;
        this.listener = listener;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean success = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket.connect();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();

            final String eMessage = e.getMessage();
            Log.d("LOG", "something wrong bluetoothSocket.connect()");

            try {
                bluetoothSocket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if(success){
            //connect successful
            final String msgconnected = "connect successful:\n"
                    + "BluetoothSocket: " + bluetoothSocket + "\n"
                    + "BluetoothDevice: " + bluetoothDevice;

            Log.d("LOG", "BLUETOOTH CONNECTED");

            listener.onConnected(bluetoothSocket);
        }else{
            Log.d("LOG", "BLUETOOTH COULDNT CONNECT");
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
