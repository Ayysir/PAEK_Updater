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

package com.ayysir.paek.tools;

import com.ayysir.paek.interfaces.KernelInfo;

public class Constants implements KernelInfo {

    public static final String LOG_TAG = "PAEK"; //$NON-NLS-1$
    public static final String TARGET_BASE_PATH = "/sdcard/PAEK";
    public static final String[] SUPPORTED_DEVICES = new String[]{"d800", "d801", "d802", "d803", "ls980", "vs980", "mako", "hammerhead"};
    public static final String[] G2_VARIANTS = new String[]{"d800", "d801", "d802", "d803", "ls980", "vs980"};
    public static final String[] NEXUS = new String[]{"mako", "hammerhead"};

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Constants() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }

    //READ CPU TEMP
    public static int getCpuTemperature() {
        int temp = Integer.parseInt(IOUtils.readOneLine(CPU_TEMP_PATH).trim());
        temp = (temp < 0 ? 0 : temp);
        temp = (temp > 100 ? 100 : temp);
        return temp;
    }

}
