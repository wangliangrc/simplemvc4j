package com.sina.weibotv.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sina.weibotv.R;
import com.sina.weibotv.view.layout.IndexPageUtils;

public class PageIndex extends ApplicationModeActivity {

    static final String TAG = PageIndex.class.getSimpleName();
    private IndexPageUtils utils;
    private ImageView functionKeyPeomptImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        utils = new IndexPageUtils(getApplication());
        functionKeyPeomptImage = (ImageView) findViewById(R.id.functionKeyPrompt);
        functionKeyPeomptImage.setImageDrawable(utils.functionKeysPrompt(
                getText(R.string.favorites), getText(R.string.forwarding),
                getText(R.string.comment), getText(R.string.refresh)));

        RadioButton radioButton = (RadioButton) findViewById(R.id.radio);
        radioButton.setButtonDrawable(utils.mainTabBackground());
    }

}