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

package com.ayysir.paek.fragments.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Utils;

public class ChangelogFragment extends Fragment {

    private static final String CHANGELOG_G2
            = "file:///android_asset/g2.html";
    private static final String CHANGELOG_MAKO
            = "file:///android_asset/mako.html";

    public WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        ViewGroup mViewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_changelog,
                container, false);


        if (mViewGroup != null) {
            mWebView = ((WebView) mViewGroup.findViewById(R.id.changelog));

            mWebView.getSettings().setJavaScriptEnabled(true);
            if (getActivity() != null) getActivity().setProgressBarVisibility(false);


            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onReceivedError(WebView view, int errorCode, String description,
                                            String failingUrl) {
                    getActivity().setProgressBarVisibility(false);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.changelog_error), Toast.LENGTH_SHORT).show();
                }
            });

            if (!Utils.getDevice().equals("mako")) {
                if (savedInstanceState == null)
                    mWebView.loadUrl(CHANGELOG_G2);
                else if (!Utils.getDevice().equals("g2"))
                    mWebView.loadUrl(CHANGELOG_MAKO);
            }
        }


        return mViewGroup;
    }

}



