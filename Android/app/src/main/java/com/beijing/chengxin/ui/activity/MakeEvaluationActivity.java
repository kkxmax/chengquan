package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;

public class MakeEvaluationActivity extends ParentFragmentActivity {

    public final  int MAKE_EVAL_TYPE = 1;
    public final  int MAKE_EVAL_MODE = 2;
    private  static int MAX_LETTER_COUNT = 100;
    public final String TAG = MakeEvaluationActivity.class.getName();


    private ImageButton  btnBack;
    private  ToggleButton btnPerson , btnEnterprise;
    private ToggleButton btnTypeFront , btnTypeBack , btnModeFront , btnModeBack;
    private EditText editReason, editContent;
    private  TextView txtReason , txtContent;
    private LinearLayout layoutReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_evalution);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.make_eval));

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        btnPerson = (ToggleButton)findViewById(R.id.btn_eval_person);
        btnEnterprise = (ToggleButton)findViewById(R.id.btn_eval_enterprise);

        btnTypeFront = (ToggleButton)findViewById(R.id.btn_make_eval_type_front);
        btnTypeBack = (ToggleButton)findViewById(R.id.btn_make_eval_type_back);
        btnModeFront = (ToggleButton)findViewById(R.id.btn_make_eval_mode_front);
        btnModeBack = (ToggleButton)findViewById(R.id.btn_make_eval_mode_back);

        btnPerson.setOnClickListener(mToggleButtonClick);
        btnEnterprise.setOnClickListener(mToggleButtonClick);
        btnTypeFront.setOnClickListener(mToggleButtonClick);
        btnTypeBack.setOnClickListener(mToggleButtonClick);
        btnModeFront.setOnClickListener(mToggleButtonClick);
        btnModeBack.setOnClickListener(mToggleButtonClick);

        txtReason= (TextView)findViewById(R.id.txt_eval_reason);
        txtContent= (TextView)findViewById(R.id.txt_eval_content);

        txtReason.setText(String.format("0/%d", MAX_LETTER_COUNT));
        txtContent.setText(String.format("0/%d", MAX_LETTER_COUNT));

        editContent = (EditText)findViewById(R.id.edit_eval_content);
        editReason= (EditText)findViewById(R.id.edit_eval_reason);

        editReason.setOnKeyListener(keyListener);
        editContent.setOnKeyListener(keyListener);

        layoutReason = (LinearLayout)findViewById(R.id.layout_eval_reason);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    View.OnClickListener mToggleButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_eval_person:
                    btnEnterprise.setChecked(false);
                    btnPerson.setChecked(true);
                    break;
                case R.id.btn_eval_enterprise:
                    btnEnterprise.setChecked(true);
                    btnPerson.setChecked(false);
                    break;
                case R.id.btn_make_eval_type_front:
                    selectRadioBtn(MAKE_EVAL_TYPE ,0);
                    break;
                case R.id.btn_make_eval_type_back:
                    selectRadioBtn(MAKE_EVAL_TYPE , 1);
                    break;
                case R.id.btn_make_eval_mode_front:
                    selectRadioBtn(MAKE_EVAL_MODE , 0);
                    layoutReason.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_make_eval_mode_back:
                    selectRadioBtn(MAKE_EVAL_MODE , 1);
                    layoutReason.setVisibility(View.GONE);
                    break;

            }
        }
    };

    private void selectRadioBtn(int type , int position){
        if(type == MAKE_EVAL_TYPE){
            resetRadioBtn(MAKE_EVAL_TYPE);
            switch (position){
                case 0:
                    btnTypeFront.setChecked(true);
                    break;
                case 1:
                    btnTypeBack.setChecked(true);
                    break;
            }
        }else{
            resetRadioBtn(MAKE_EVAL_MODE);
            switch (position){
                case 0:
                    btnModeFront.setChecked(true);
                    break;
                case 1:
                    btnModeBack.setChecked(true);
                    break;
            }
        }
    }

    private void resetRadioBtn(int type){
        if(type == MAKE_EVAL_TYPE){
            btnTypeFront.setChecked(false);
            btnTypeBack.setChecked(false);
        }else{
            btnModeFront.setChecked(false);
            btnModeBack.setChecked(false);
        }
    }

    View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String content;
            int length;
            switch (v.getId()) {
                case R.id.edit_eval_content:
                content = editContent.getText().toString();
                length = content.length();
                txtContent.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    editContent.setText(content.substring(0, MAX_LETTER_COUNT));
                    return true;
                }
                return false;

                default:
                    content = editReason.getText().toString();
                    length = content.length();
                    txtReason.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                    if (length >= MAX_LETTER_COUNT) {
                        editReason.setText(content.substring(0, MAX_LETTER_COUNT));
                        return true;
                    }
                    return false;

            }
        }
    };

}
