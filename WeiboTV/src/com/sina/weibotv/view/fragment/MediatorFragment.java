package com.sina.weibotv.view.fragment;

import static com.clark.mvc.MultiCore.registerView;
import static com.clark.mvc.MultiCore.removeView;
import android.app.Fragment;
import android.os.Bundle;

public class MediatorFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerView(this);
    }

    @Override
    public void onDestroy() {
        removeView(this);
        super.onDestroy();
    }

}
