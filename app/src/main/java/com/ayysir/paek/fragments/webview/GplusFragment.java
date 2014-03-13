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

public class GplusFragment extends Fragment {

    private static final String CHANGELOG_URL
            = "https://plus.google.com/app/basic/+AndreSaddler/posts";

    public WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        ViewGroup mViewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_gplus,
                container, false);

        if (mViewGroup != null) {
            mWebView = ((WebView) mViewGroup.findViewById(R.id.gplus));

            mWebView.getSettings().setJavaScriptEnabled(true);

            if (getActivity() != null) getActivity().setProgressBarVisibility(true);

            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onReceivedError(WebView view, int errorCode, String description,
                                            String failingUrl) {
                    if (getActivity() != null) getActivity().setProgressBarVisibility(true);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.changelog_error), Toast.LENGTH_SHORT).show();
                }
            });

            if (savedInstanceState == null) {
                mWebView.loadUrl(CHANGELOG_URL);
            }
        }


        return mViewGroup;
    }
}

