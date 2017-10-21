package com.cjw.demo2_circleprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.value_et)
    EditText mValueEt;
    @BindView(R.id.update_bt)
    Button mUpdateBt;
    @BindView(R.id.progress_cpb)
    CircleProgressBar mProgressCpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.update_bt)
    public void onViewClicked() {
        String text = mValueEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        int value = Integer.parseInt(text);
        mProgressCpb.updateProgress(value);
    }
}
