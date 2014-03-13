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

import android.os.AsyncTask;
import android.util.Log;

import com.ayysir.paek.fragments.UpdateFragment;
import com.ayysir.paek.helper.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateChecker extends AsyncTask<Object, Object, Object> {

    private UpdateFragment.FragmentCallback mFragmentCallback;
    private boolean mUpdateAvailable = false;

    public UpdateChecker(UpdateFragment.FragmentCallback mFragmentCallback) {
        this.mFragmentCallback = mFragmentCallback;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String mServerVersion = "";
        JSONObject mJSONObject = new JSONParser().getJSONFromUrl(Utils.G2 + Utils.getDevice());

        if (mJSONObject != null) {
            try {

                for (int i = 0; i < mJSONObject.getJSONArray("g2k").length(); i++) {
                    JSONObject mObject = mJSONObject.getJSONArray("g2k").getJSONObject(i);
                    if (mObject.has("name")) {
                        mServerVersion = mObject.getString("name");
                        Utils.SERVER_JSON = mJSONObject.getJSONArray("g2k").getJSONObject(i);
                    }
                }

                if (Utils.getSystemPAEKVersion() < Utils.getServerVersion(mServerVersion))
                    mUpdateAvailable = true;

            } catch (JSONException e) {
                Log.v(Constants.LOG_TAG, e.toString());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (mFragmentCallback != null)
            mFragmentCallback.onTaskDone(mUpdateAvailable); // calls callback which is located in UpdateFragment
    }
}
