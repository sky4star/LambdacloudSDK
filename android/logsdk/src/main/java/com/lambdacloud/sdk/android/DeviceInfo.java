/**
 * Copyright (c) 2015, LambdaCloud
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.lambdacloud.sdk.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceInfo {
    protected static ConnectivityManager connManager = null;
    protected static TelephonyManager teleManager = null;
    private static Context appContext;

    public static void init(Context context) {
        try {
            if (context != null) {
                appContext = context;
                connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            }
        } catch (Exception e) {
            Log.d(LogSdkConfig.LOG_TAG, "get exception while initializing DeviceInformation, detail is " + e.toString());
        }
    }

    public static String getInternetConnectionStatus() {
        Log.d(LogSdkConfig.LOG_TAG, "Read connection status");
        if (connManager != null) {
            try {
                NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    return DeviceInfoConstant.NETWORK_REACHABLE_VIA_WIFI;
                }

                state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    return DeviceInfoConstant.NETWORK_REACHABLE_VIA_WWAN;
                }
            } catch (Exception e) {
                Log.d(LogSdkConfig.LOG_TAG, "get exception while getting connection status, detail is " + e.toString());
                return DeviceInfoConstant.UNKNOWN;
            }
        }

        return DeviceInfoConstant.NETWORK_NOT_REACHABLE;
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (manufacturer.equals(Build.UNKNOWN)) {
                return model;
            } else if (model.equals(Build.UNKNOWN)) {
                return manufacturer;
            } else if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
                return model;
            } else {
                return manufacturer + " " + model;
            }
        } catch (Exception e) {
            Log.d(LogSdkConfig.LOG_TAG, "get exception while reading device name, detail is " + e.toString());
            return DeviceInfoConstant.UNKNOWN;
        }
    }

    public static String getOperationInfo() {
        if (teleManager != null) {
            try {
                return teleManager.getNetworkOperatorName();
            } catch (Exception e) {
                Log.d(LogSdkConfig.LOG_TAG, "get exception while getting operation name, detail is " + e.toString());
            }
        }

        return DeviceInfoConstant.UNKNOWN;
    }

}


