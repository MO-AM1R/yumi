package com.example.yumi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public enum NetworkMonitor {

    INSTANCE;

    private ConnectivityManager connectivityManager;
    private boolean isConnected = false;
    private final List<NetworkListener> listeners = new CopyOnWriteArrayList<>();
    private final ConnectivityManager.NetworkCallback networkCallback =
            new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NonNull Network network) {
                    updateState();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    updateState();
                }
            };

    public interface NetworkListener {
        void onNetworkAvailable();

        void onNetworkLost();
    }

    public void init(Context context) {
        if (connectivityManager != null) return;

        connectivityManager =
                (ConnectivityManager) context.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build();

        connectivityManager.registerNetworkCallback(request, networkCallback);

        isConnected = checkNetwork();
    }

    private void updateState() {
        boolean newState = checkNetwork();
        if (newState != isConnected) {
            isConnected = newState;
            notifyListeners();
        }
    }

    private boolean checkNetwork() {
        if (connectivityManager == null) return false;

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities caps =
                connectivityManager.getNetworkCapabilities(network);

        return caps != null
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void addListener(NetworkListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NetworkListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (NetworkListener listener : listeners) {
            if (isConnected) {
                listener.onNetworkAvailable();
            } else {
                listener.onNetworkLost();
            }
        }
    }
}
