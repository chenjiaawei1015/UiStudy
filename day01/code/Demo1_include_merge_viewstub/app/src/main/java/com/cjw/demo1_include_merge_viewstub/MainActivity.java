package com.cjw.demo1_include_merge_viewstub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt1)
    Button mBt1;
    @BindView(R.id.bt2)
    Button mBt2;
    @BindView(R.id.view_stub_vs)
    ViewStub mViewStubVs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt1)
    public void onBt1Clicked() {
        mViewStubVs.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt2)
    public void onBt2Clicked() {
    }
}
