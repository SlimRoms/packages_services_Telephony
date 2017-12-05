/*
 * Copyright (C) 2017 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.phone.testapps.embmsmw;

import android.telephony.mbms.IStreamingServiceCallback;
import android.telephony.mbms.StreamingService;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

// Singleton that keeps track of streaming states for all apps using the middleware.
public class StreamStateTracker {
    private static final String LOG_TAG = "MbmsStreamStateTracker";

    private static final Map<StreamingAppIdentifier, AppActiveStreams>
            sPerAppStreamStates = new HashMap<>();

    public static int getStreamingState(StreamingAppIdentifier appIdentifier, String serviceId) {
        AppActiveStreams appStreams = sPerAppStreamStates.get(appIdentifier);
        if (appStreams == null) {
            return StreamingService.STATE_STOPPED;
        }
        return appStreams.getStateForService(serviceId);
    }

    public static void startStreaming(StreamingAppIdentifier appIdentifier, String serviceId,
            IStreamingServiceCallback callback) {
        AppActiveStreams appStreams = sPerAppStreamStates.get(appIdentifier);
        if (appStreams == null) {
            appStreams = new AppActiveStreams(appIdentifier);
            sPerAppStreamStates.put(appIdentifier, appStreams);
        }

        appStreams.startStreaming(serviceId, callback);
    }

    public static void stopStreaming(StreamingAppIdentifier appIdentifier, String serviceId) {
        Log.i(LOG_TAG, "Stopping stream " + serviceId);
        AppActiveStreams appStreams = sPerAppStreamStates.get(appIdentifier);
        if (appStreams == null) {
            // It was never started, so don't bother stopping.
            return;
        }
        appStreams.stopStreaming(serviceId);
    }

    public static void dispose(StreamingAppIdentifier appIdentifier, String serviceId) {
        AppActiveStreams appStreams = sPerAppStreamStates.get(appIdentifier);
        if (appStreams == null) {
            // We have no record of this app, so we can just move on.
            return;
        }
        appStreams.dispose(serviceId);
    }

    public static void disposeAll(StreamingAppIdentifier appIdentifier) {
        sPerAppStreamStates.remove(appIdentifier);
    }

    // Do not instantiate
    private StreamStateTracker() {}
}
