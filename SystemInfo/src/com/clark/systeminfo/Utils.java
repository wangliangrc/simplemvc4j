package com.clark.systeminfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

class Utils {

    static abstract class BackgroundTask extends AsyncTask<Void, Void, Void> {
        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            activity = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog
                    .show(activity, "注意", "加载数据……", true);
        }

        private Activity activity;
        private ProgressDialog progressDialog;

    }
}
