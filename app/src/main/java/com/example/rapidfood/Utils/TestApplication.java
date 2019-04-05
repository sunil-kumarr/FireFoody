package com.example.rapidfood.Utils;

import android.app.Application;

public class TestApplication extends Application {

        private static TestApplication mInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            mInstance = this;
        }

        public static synchronized TestApplication getInstance() {
            return mInstance;
        }

        public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
            ConnectionReceiver.connectionReceiverListener = listener;
        }
    }