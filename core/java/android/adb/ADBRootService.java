/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.adb;

import android.adbroot.IADBRootService;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;

/**
 * {@hide}
 */
public class ADBRootService {
    private static final String TAG = "ADBRootService";

    private static final String ADB_ROOT_SERVICE = "adbroot_service";

    private IADBRootService mService;

    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mService != null) {
                Slog.e(TAG, "not null");
                mService.asBinder().unlinkToDeath(this, 0);
            }
            Slog.e(TAG, "null");
            mService = null;
        }
    };

    private synchronized IADBRootService getService()
            throws RemoteException {
        if (mService != null) {
            Slog.e(TAG, "getService not null");
            return mService;
        }

        final IBinder service = ServiceManager.getService(ADB_ROOT_SERVICE);
        if (service != null) {
            service.linkToDeath(mDeathRecipient, 0);
            mService = IADBRootService.Stub.asInterface(service);
            Slog.e(TAG, "getService not null 2");
            return mService;
        }

        Slog.e(TAG, "Unable to acquire ADBRootService");
        return null;
    }

    /**
     * @hide
     */
    public boolean isSupported() {
        try {
            final IADBRootService svc = getService();
            if (svc != null) {
                if(svc.isSupported()){
                   Slog.e(TAG, "svc.isSupported");
                } else {
                   Slog.e(TAG, "svc.isSupported not");
                }
                return true;//svc.isSupported();
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "svc isSupported");
            Slog.e(TAG, e.getMessage());
            throw e.rethrowFromSystemServer();
        }
        return false;
    }

    /**
     * @hide
     */
    public void setEnabled(boolean enable) {
        try {
            final IADBRootService svc = getService();
            if (svc != null) {
                Slog.e(TAG, "svc.setEnabled");
                //svc.setEnabled(enable);
                svc.setEnabled(true);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "svc setEnabled");
            Slog.e(TAG, e.getMessage());
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * @hide
     */
    public boolean getEnabled() {
        try {
            final IADBRootService svc = getService();
            if (svc != null) {
                Slog.e(TAG, "svc.setEnabled");
                //svc.setEnabled(enable);
                svc.setEnabled(true);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "svc setEnabled");
            Slog.e(TAG, e.getMessage());
            //throw e.rethrowFromSystemServer();
        }
        try {
            final IADBRootService svc = getService();
            if (svc != null) {
                Slog.e(TAG, "svc.getEnabled");
                if(svc.getEnabled()){
                   Slog.e(TAG, "svc.getEnabled");
                } else {
                   Slog.e(TAG, "svc.getEnabled not");
                }
                return svc.getEnabled();
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "svc getEnabled");
            Slog.e(TAG, e.getMessage());
            throw e.rethrowFromSystemServer();
        }
        return false;
    }
}
