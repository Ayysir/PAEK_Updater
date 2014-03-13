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

package com.ayysir.paek.interfaces;

/**
 * not done
 */
public interface KernelInfo {
    //   public static final String TAG = "PAEK";

    // CPU info
    public static String KERNEL_INFO_PATH = "/proc/version";
    public static String CPU_INFO_PATH = "/proc/cpuinfo";
    public static String MEM_INFO_PATH = "/proc/meminfo";

    // Time in state
    public static final String TIME_IN_STATE_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    public static final String TIME_IN_STATE_TAIL = "/cpufreq/stats/time_in_state";
    public static final String PREF_OFFSETS = "pref_offsets";

    // Temp
    public static final String CPU_TEMP_PATH = "/sys/class/thermal/thermal_zone0/temp";
}
