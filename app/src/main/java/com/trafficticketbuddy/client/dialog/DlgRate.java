package com.trafficticketbuddy.client.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.trafficticketbuddy.client.BaseActivity;
import com.trafficticketbuddy.client.R;

public class DlgRate extends Dialog{
    private BaseActivity baseActivity;
    private LinearLayout llMain;
    private AppCompatButton submit_btn;
    public DlgRate(@NonNull BaseActivity baseActivity) {
        super(baseActivity);
        this.baseActivity = baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dialog_rating);

        llMain = (LinearLayout) findViewById(R.id.llMain);
        submit_btn = (AppCompatButton) findViewById(R.id.submit_btn);
        llMain.getLayoutParams().height = (int) (baseActivity.height * 0.70);
        llMain.getLayoutParams().width = (int) (baseActivity.width * 0.85);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
