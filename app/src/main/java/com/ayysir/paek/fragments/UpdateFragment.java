/*
 * Copyright © 2014 Andre "ayysir" Saddler & Parthipan Ramesh
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

package com.ayysir.paek.fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Constants;
import com.ayysir.paek.tools.UpdateChecker;
import com.ayysir.paek.tools.Utils;

import org.json.JSONException;

import java.io.File;

public class UpdateFragment extends Fragment {

    private Button mUpdateButton;
    private TextView mStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // retains the instance after orientation changes

        View mView = inflater.inflate(R.layout.updates, container, false);

        if (mView != null) {
            mUpdateButton = (Button) mView.findViewById(R.id.dwl);
            mStatus = (TextView) mView.findViewById(R.id.status);
        }

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });

        check();

        return mView;
    }

    private void check() {
        if (Utils.isPAEKinstalled() && Utils.isNetworkAvailable(getActivity()) || Utils.isOnWifi(getActivity())) {
            new UpdateChecker(new FragmentCallback() {
                @Override
                public void onTaskDone(boolean mUpdateAvailable) {
                    if (mUpdateAvailable) {
                        mStatus.setText(getString(R.string.update_available));
                        mUpdateButton.setVisibility(View.VISIBLE);
                    } else mStatus.setText(getString(R.string.no_update_available));
                }
            }).execute();
        } else if (!Utils.isPAEKinstalled()) {
            mStatus.setText(getString(R.string.not_paek));
        }
    }

    private void startDownload() {
        if (getActivity() != null) {
            try {
                File folder = new File(Environment.getExternalStorageDirectory() + "/PAEK/");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }
                if (!success) {
                }
                DownloadManager mDownloadManager = (DownloadManager) getActivity().getSystemService(Activity.DOWNLOAD_SERVICE);
                DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(Utils.SERVER_JSON.getString("url")));
                mRequest.setVisibleInDownloadsUi(false);
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                mRequest.setDestinationInExternalPublicDir("PAEK", Utils.SERVER_JSON.getString("name"));
                mRequest.setTitle(getResources().getString(R.string.app_name));
                mDownloadManager.enqueue(mRequest);
            } catch (JSONException e) {
                Log.v(Constants.LOG_TAG, e.toString());
            }
        }

    }

    // this is a callback for the asynctask
    public interface FragmentCallback {
        public void onTaskDone(boolean mUpdateAvailable);
    }
}
