package com.clark.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clark.logger.R;
import com.clark.tools.GC;
import com.clark.tools.IO;
import coms.Shell;

public class MainShell extends Shell implements OnClickListener, Callback {
    static final String TAG = MainShell.class.getSimpleName();
    private static final int DLG_PROGRESS_GENERATE = 10;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy年MM月dd日HH时mm分ss秒_SSS");
    private Handler mHandler;

    private Button mCreateLogBtn;
    private Button mAddTagBtn;
    private Button mReadLogBtn;
    private EditText mAddEdit;
    private TextView mDisplayView;
    private Dialog mProgressDlg;
    private Spinner mSpinner;
    private Button mDeleteText;
    private SpinnerAdapter mSpinnerAdapter;

    private StateHolder mStateHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Window window = getWindow();
        final LayoutParams attributes = window.getAttributes();
        attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        window.setAttributes(attributes);

        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        setContentView(R.layout.main);
        ensureUI();
        readFrom();
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        writeTo();
    }

    private void ensureUI() {
        mCreateLogBtn = (Button) findViewById(R.id.generate);
        mCreateLogBtn.setOnClickListener(this);
        mAddTagBtn = (Button) findViewById(R.id.add);
        mAddTagBtn.setOnClickListener(this);
        mReadLogBtn = (Button) findViewById(R.id.look);
        mReadLogBtn.setOnClickListener(this);
        mAddEdit = (EditText) findViewById(R.id.add_edit);
        mDisplayView = (TextView) findViewById(R.id.display);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        //        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //
        //            public void onItemSelected(AdapterView<?> parent, View view,
        //                    int position, long id) {
        //            }
        //
        //            public void onNothingSelected(AdapterView<?> parent) {
        //            }
        //
        //        });
        mSpinnerAdapter = new SpinnerAdapter();
        mDeleteText = (Button) findViewById(R.id.del);
        mDeleteText.setOnClickListener(this);
    }

    private void updateDisplayText() {
        final StringBuilder builder = new StringBuilder("要显示的 Tag：\n");
        for (String s : mStateHolder.tags) {
            builder.append("\t").append(s).append("\n");
        }
        mDisplayView.setText(builder.toString());
    }

    private void updateLogFiles() {
        File file = null;
        final HashSet<String> needDel = new HashSet<String>();
        for (String path : mStateHolder.logFilePaths) {
            file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                needDel.add(path);
            }
        }
        mStateHolder.logFilePaths.removeAll(needDel);

        File[] files = getExternalCacheDir().listFiles(new FilenameFilter() {

            public boolean accept(File dir, String filename) {
                return filename.endsWith(".log");
            }
        });

        if (files != null) {
            for (File f : files) {
                mStateHolder.logFilePaths.add(f.getAbsolutePath());
            }
        }
    }

    private void readFrom() {
        Object cacheObj = null;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(
                    new File(getCacheDir(), "cf.dat")));
            cacheObj = objectInputStream.readObject();
        } catch (Exception e) {
        } finally {
            GC.close(objectInputStream);
        }
        if (cacheObj instanceof StateHolder) {
            mStateHolder = (StateHolder) cacheObj;
        } else {
            mStateHolder = new StateHolder();
        }
        updateDisplayText();
        updateLogFiles();
    }

    private void writeTo() {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(
                    new File(getCacheDir(), "cf.dat")));
            outputStream.writeObject(mStateHolder);
        } catch (Exception e) {
        } finally {
            GC.close(outputStream);
        }
    }

    @Override
    public Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
        case DLG_PROGRESS_GENERATE:
            mProgressDlg = ProgressDialog.show(getSelf(), null, "生成中……", true,
                    false);
            return mProgressDlg;

        default:
            return super.onCreateDialog(id, args);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case 1001: {
            GC.dismiss(mProgressDlg);
            final File file = (File) msg.obj;
            if (file != null && file.exists()) {
                mStateHolder.logFilePaths.add(file.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "生成成功！",
                        Toast.LENGTH_SHORT).show();

                mStateHolder.logFilePaths.add(file.getAbsolutePath());
            }
            return true;
        }

        case 1002: {
            GC.dismiss(mProgressDlg);
            Toast.makeText(getApplicationContext(), "生成失败！错误码：" + msg.arg1,
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        default:
            return false;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.generate: {
            showDialog(DLG_PROGRESS_GENERATE);
            new LoggingTask().start();
            break;
        }

        case R.id.add: {
            final String tag = mAddEdit.getText().toString().trim();
            if (TextUtils.isEmpty(tag)) {
                Toast.makeText(getApplicationContext(), "请输入要添加的 Tag!",
                        Toast.LENGTH_SHORT).show();
            } else {
                mSpinnerAdapter.notifyDataSetInvalidated();
                if (mStateHolder.tags.add(tag)) {
                    mStateHolder.tagList.add(tag);
                }
                mSpinnerAdapter.notifyDataSetChanged();
                updateDisplayText();
                mAddEdit.requestFocus();
            }
            break;
        }

        case R.id.look:
            readLogFiles();
            break;

        case R.id.del: {
            final int position = mSpinner.getSelectedItemPosition();
            mSpinnerAdapter.notifyDataSetInvalidated();
            if (mStateHolder.tags.remove(mStateHolder.tagList.get(position))) {
                mStateHolder.tagList.remove(position);
            }
            mSpinnerAdapter.notifyDataSetChanged();
            updateDisplayText();
            break;
        }
        }

    }

    private void readLogFiles() {
        updateLogFiles();

        final int size = mStateHolder.logFilePaths.size();
        if (size == 0) {
            Toast.makeText(getApplicationContext(), "没有可用的 Log 文件!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        final Intent intent = new Intent(getApplication(), ListShell.class);
        intent.putExtra("list", (Serializable) mStateHolder.logFilePaths);
        startComponent(intent);

        //        final File[] files = new File[size];
        //        final String[] names = new String[size];
        //        int index = 0;
        //        for (String s : mStateHolder.logFilePaths) {
        //            files[index++] = new File(s);
        //        }
        //
        //        AlertDialog.Builder builder = new AlertDialog.Builder(getSelf());
        //        builder.setTitle("请选择");
        //        builder.setItems(names, new DialogInterface.OnClickListener() {
        //
        //            public void onClick(DialogInterface dialog, int which) {
        //                final Intent intent = new Intent(Intent.ACTION_VIEW);
        //                intent.setData(Uri.fromFile(files[which]));
        //                startActivity(intent);
        //            }
        //        });
        //        builder.show();
    }

    private class LoggingTask extends Thread {
        private File mLogFile;

        public LoggingTask() {
            this.mLogFile = new File(getExternalCacheDir(),
                    DATE_FORMAT.format(new Date()) + ".log");
        }

        @Override
        public void run() {
            int res = 0;
            if ((res = catchLogError(mLogFile,
                    mStateHolder.tags.toArray(new String[0]))) == 0) {
                mHandler.obtainMessage(1001, mLogFile).sendToTarget();
            } else {
                final Message msg = mHandler.obtainMessage(1002);
                msg.arg1 = res;
                msg.sendToTarget();
            }
        }

        private int catchLogError(File file, String... tags) {
            try {
                ArrayList<String> commandLine = new ArrayList<String>();
                commandLine.add("logcat");
                commandLine.add("-d");
                if (tags != null && tags.length > 0) {
                    commandLine.add("-s");
                    for (String tag : tags) {
                        commandLine.add(tag.trim());
                    }
                }
                Process process = Runtime.getRuntime().exec(
                        (String[]) commandLine.toArray(new String[commandLine
                                .size()]));
                IO.copyAndClose(process.getInputStream(), new FileOutputStream(
                        file));
                IO.copyAndClose(process.getErrorStream(), new FileOutputStream(
                        file, true));
                return process.waitFor();
            } catch (Exception e) {
                Log.e(TAG, "Failed to catch log");
            }
            return 1;
        }
    }

    private class SpinnerAdapter extends BaseAdapter {

        public int getCount() {
            return mStateHolder.tagList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.simple_list_item_1, parent, false);
            }
            final TextView textView = (TextView) convertView
                    .findViewById(R.id.text1);
            textView.setText(mStateHolder.tagList.get(position));
            return convertView;
        }

    }

    private static class StateHolder implements Serializable {
        private static final long serialVersionUID = 5489945058524609105L;

        public Set<String> logFilePaths = new LinkedHashSet<String>();

        public Set<String> tags = new LinkedHashSet<String>();
        public List<String> tagList = new ArrayList<String>();
    }

}
