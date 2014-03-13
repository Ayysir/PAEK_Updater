/*
 * Copyright © 2014 Andre "ayysir" Saddler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ayysir.paek.intentservices;

import android.app.IntentService;
import android.content.Intent;

import com.ayysir.paek.tools.Utils;

import org.json.JSONException;

import eu.chainfire.libsuperuser.Shell;

public class RecoveryIntentService extends IntentService {

    public RecoveryIntentService() {
        super("RecoveryIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Shell.SU.run("echo install /sdcard/PAEK/" + Utils.SERVER_JSON.getString("name") + " >> /cache/recovery/openrecoveryscript");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Shell.SU.run("echo wipe cache >> /cache/recovery/openrecoveryscript");
        Shell.SU.run("echo wipe dalvik >> /cache/recovery/openrecoveryscript");
        Shell.SU.run("reboot recovery");
    }
}