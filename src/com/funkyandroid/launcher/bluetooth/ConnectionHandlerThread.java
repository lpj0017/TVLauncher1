package com.funkyandroid.launcher.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import com.funkyandroid.launcher.Launcher;

/**
 * Class to handle a connection from a bluetooth client
 */

class ConnectionHandlerThread extends Thread {

    /**
     * The broadcast to handle a keypress
     */

    public static final String INTENT_ACTION_KEYPRESS = "com.funkyandroid.launcher.KEY_PRESS";

    /**
     * The context in which we're running.
     */

    private Context context;

	/**
	 * The socket the client connected to.
	 */

	private final BluetoothSocket mSocket;


	public ConnectionHandlerThread(BluetoothSocket socket) {
		mSocket = socket;
	}

	@Override
	public void run() {
		// First read the string holding the action to be performed
        try {
            DataInputStream dis = new DataInputStream(mSocket.getInputStream());
            try {
                DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
                try {
                    while(true) {
                        int keyCode = dis.readInt();
                        if(keyCode == -1) {
                            break;
                        }

                        Intent broadcast = new Intent(INTENT_ACTION_KEYPRESS);
                        broadcast.putExtra("keycode", keyCode);
                        context.sendBroadcast(broadcast);
                        dos.writeInt(0);
                    }
                    dos.writeInt(-1);
                } finally {
                    dos.close();
                }
            } finally {
                dis.close();
            }
		} catch(IOException ioe) {
			Log.w(Launcher.LOG_TAG, "Problem getting stream", ioe);
		}
	}
}
