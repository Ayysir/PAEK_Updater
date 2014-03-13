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

package com.ayysir.paek.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHelper {

    public static final String PROPERTY_CPU_GOVERNOR = "cpu_governor";
    public static final String PROPERTY_CPU_MIN_FREQ = "cpu_min_freq";
    public static final String PROPERTY_CPU_MAX_FREQ = "cpu_max_freq";
    public static final String PROPERTY_CPU_MAX_SCREEN_OFF_FREQ = "cpu_max_screen_off_freq";
    public static final String PROPERTY_CPU_MIN_SCREEN_ON_FREQ = "cpu_min_screen_on_freq";

    //    CPU Settings
    private static final String PROPERTY_CPU_APPLY_ON_BOOT = "apply_on_boot";
    private static final String PROPERTY_CPU_GRACE_PERIOD = "apply_grace_period";
    //TODO: setup checkbox to toggle apply on boot
    private static final boolean DEFAULT_CPU_APPLY_ON_BOOT = true;
    private static final String DEFAULT_CPU_GRACE_PERIOD = "60";
    private SharedPreferences settings;

    public SettingsHelper(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getApplyOnBoot() {
        return settings.getBoolean(PROPERTY_CPU_APPLY_ON_BOOT, DEFAULT_CPU_APPLY_ON_BOOT);
    }

    public int getGracePeriod() {
        return Integer.parseInt(settings.getString(PROPERTY_CPU_GRACE_PERIOD, DEFAULT_CPU_GRACE_PERIOD));
    }

    public String getCpuGovernor() {
        return settings.getString(PROPERTY_CPU_GOVERNOR, null);
    }

    public String getCpuMinFreq() {
        return settings.getString(PROPERTY_CPU_MIN_FREQ, null);
    }

    public String getCpuMaxFreq() {
        return settings.getString(PROPERTY_CPU_MAX_FREQ, null);
    }

    public String getCpuMaxScreenOffFreq() {
        return settings.getString(PROPERTY_CPU_MAX_SCREEN_OFF_FREQ, null);
    }

    public String getCpuMinScreenOnFreq() {
        return settings.getString(PROPERTY_CPU_MIN_SCREEN_ON_FREQ, null);
    }

    public void savePreference(String key, String mString) {
        settings.edit().putString(key, mString).commit();
    }
}
