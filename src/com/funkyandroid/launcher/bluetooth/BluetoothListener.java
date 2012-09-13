package com.funkyandroid.launcher.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.funkyandroid.launcher.Launcher;

public class BluetoothListener
	extends Thread {

	/**
	 * The currently active listener
	 */

	private static Server currentlyActive = null;

	/**
	 * Activate the bluetooth listener.
	 */
	public void activate()
		throws IOException {
		synchronized (BluetoothListener.class) {
			if(currentlyActive != null) {
				return;
			}

			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if(adapter != null) {
				currentlyActive = new Server(adapter);
				currentlyActive.start();
			} else {
				Log.e(Launcher.LOG_TAG, "Bluetooth adapter not found");
			}
		}
	}

	/**
	 * Deactivate the bluetooth listener
	 */

	public void deactivate() {
		synchronized (BluetoothListener.class) {
			if(currentlyActive == null) {
				return;
			}

			currentlyActive.cancel();
		}
	}
}
