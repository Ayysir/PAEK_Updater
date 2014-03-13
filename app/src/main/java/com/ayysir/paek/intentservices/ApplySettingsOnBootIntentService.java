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
import android.util.Log;

import com.ayysir.paek.R;
import com.ayysir.paek.helper.SettingsHelper;
import com.ayysir.paek.tools.Utils;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class ApplySettingsOnBootIntentService extends IntentService {

    private List<String> commands;

    public ApplySettingsOnBootIntentService() {
        super("ApplySettingsOnBootIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        commands = new ArrayList<String>();
        SettingsHelper mSettingsHelper = new SettingsHelper(this);

        if (mSettingsHelper.getCpuGovernor() != null)
            addCommand(mSettingsHelper.getCpuGovernor(), "scaling_governor");

        if (mSettingsHelper.getCpuMinFreq() != null)
            addCommand(mSettingsHelper.getCpuMinFreq(), "scaling_min_freq");

        if (mSettingsHelper.getCpuMaxFreq() != null)
            addCommand(mSettingsHelper.getCpuMaxFreq(), "scaling_max_freq");

        if (mSettingsHelper.getCpuMaxScreenOffFreq() != null)
            addCommand(mSettingsHelper.getCpuGovernor(), "screen_off_max_freq");

        if (mSettingsHelper.getCpuMinScreenOnFreq() != null)
            addCommand(mSettingsHelper.getCpuGovernor(), "screen_on_min_freq");

        try {
            Thread.sleep(mSettingsHelper.getGracePeriod() * 100);
        } catch (InterruptedException e) {
            Log.v("Error: ", e.toString());
        }

        Shell.SU.run(commands);

        Utils.createNotification(this, getString(R.string.previous_settings_applied));
    }

    private void addCommand(String value, String file) {
        commands.add("echo " + value + " > " + "/sys/devices/system/cpu/cpu0/cpufreq/" + file);
        commands.add("echo " + value + " > " + "/sys/devices/system/cpu/cpu1/cpufreq/" + file);
        commands.add("echo " + value + " > " + "/sys/devices/system/cpu/cpu2/cpufreq/" + file);
        commands.add("echo " + value + " > " + "/sys/devices/system/cpu/cpu3/cpufreq/" + file);
    }
}
