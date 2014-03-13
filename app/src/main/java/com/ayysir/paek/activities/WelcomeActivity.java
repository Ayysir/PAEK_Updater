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

package com.ayysir.paek.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ayysir.paek.MainActivity;
import com.ayysir.paek.R;

public class WelcomeActivity extends Activity {

    private static final String CHANGELOG_APP
            = "file:///android_asset/app.html";

    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = this.getLayoutInflater();
        ViewGroup mViewGroup = (ViewGroup) inflater.inflate(R.layout.welcome, null);

        if (mViewGroup != null) {
            mWebView = ((WebView) mViewGroup.findViewById(R.id.appchnglog));


            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onReceivedError(WebView view, int errorCode, String description,
                                            String failingUrl) {
                    setProgressBarVisibility(false);
                }
            });

            if (savedInstanceState == null)
                mWebView.loadUrl(CHANGELOG_APP);
        }


        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(this);
        mAlertDialogBuilder.setView(mViewGroup).setCancelable(false)
                .setPositiveButton(getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean welcome = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("welcome", true);
                                if (welcome) {
                                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                            .edit()
                                            .putBoolean("welcome", false)
                                            .commit();
                                }
                            }
                        }
                );
        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
        mAlertDialog.show();
    }

    public void setView(View view) {
    }
}
