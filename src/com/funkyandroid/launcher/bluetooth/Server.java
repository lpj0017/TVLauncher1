package com.funkyandroid.launcher.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import com.funkyandroid.launcher.Launcher;

public class Server extends Thread {
	/**
	 * The name associated with the bluetooth socket
	 */

	private static final String NAME = "FunkyLauncher";

	/**
	 * The UUID for the server socket
	 */

	public static final UUID MY_UUID = new UUID(0x50484f4e45L, 0x4c392084L);

	/**
	 * The server socket for bluetooth connections
	 */

	private final BluetoothServerSocket mmServerSocket;

    private Context context;

	/**
	 * Initialise
	 * @param adapter
	 */
	public Server(final Context context, final BluetoothAdapter adapter)
		throws IOException {
        this.context = context;
       	mmServerSocket = adapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
    }

    @Override
	public void run() {
        Log.i(Launcher.LOG_TAG, "Server running");
        try {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    (new ConnectionHandlerThread(context, socket)).start();
                }
            }
        } catch(Exception ex) {
            Log.e(Launcher.LOG_TAG, "Server bailed", ex);
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
