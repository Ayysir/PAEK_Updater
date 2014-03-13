package com.ayysir.paek.intentservices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ayysir.paek.R;
import com.ayysir.paek.helper.JSONParser;
import com.ayysir.paek.tools.Constants;
import com.ayysir.paek.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCheckIntent extends IntentService {

    public UpdateCheckIntent() {
        super("UpdateCheckIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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
                    Utils.createNotification(this, getString(R.string.tap_to_start_paek));

            } catch (JSONException e) {
                Log.v(Constants.LOG_TAG, e.toString());
            }
        }
    }
}