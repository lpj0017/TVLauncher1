package com.funkyandroid.launcher.bluetooth;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.funkyandroid.launcher.Launcher;

public class ListenerService
	extends Service {
	/**
	 * The intent broadcast if the listener fails to start
	 */

	public static final String LISTENER_START_FAILED_INTENT = "com.funkyandroid.launcher.LISTENER_STARTUP_FAILED";

	/**
	 * The Bluetooth Listener
	 */

	private final BluetoothListener bluetoothListener = new BluetoothListener();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		try {
			bluetoothListener.activate();
		} catch(IOException ex) {
			Log.e(Launcher.LOG_TAG, "Error starting listener", ex);
			sendBroadcast(new Intent(LISTENER_START_FAILED_INTENT));
		}

		return Service.START_STICKY;
	}
}
