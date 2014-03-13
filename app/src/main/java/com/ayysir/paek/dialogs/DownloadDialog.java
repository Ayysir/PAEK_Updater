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

package com.ayysir.paek.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Utils;

public class DownloadDialog extends DialogFragment {

    Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.attention));
        builder.setMessage(R.string.download_dialog)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
                                if (Utils.isG2Suppported(mContext)) {
                                    webPageIntent.setData(Uri.parse("http://91.hostingsharedbox.com:3000/ayysir/kernels/g2/"));
                                } else if (Utils.isN4Suppported(mContext)) {
                                    //n4 support coming soon
                                } else {
                                    webPageIntent.setData(Uri.parse("https://www.google.com"));
                                }

                                try {
                                    startActivity(webPageIntent);
                                } catch (ActivityNotFoundException ex) {

                                }
                            }
                        }
                )
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //getActivity().finish();
                            }
                        }
                );
        return builder.create();
    }
}