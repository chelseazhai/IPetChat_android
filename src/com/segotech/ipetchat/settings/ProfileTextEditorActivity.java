package com.segotech.ipetchat.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.constants.GlobalConstant;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class ProfileTextEditorActivity extends IPetChatNavigationActivity implements View.OnClickListener{
    private BarButtonItem okButton;
    private EditText editText;
    private int type = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_profile_text_editor);
        editText = (EditText) findViewById(R.id.edit_text);
        okButton = new BarButtonItem(this, getString(R.string.ok), this);
        setRightBarButtonItem(okButton);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(GlobalConstant.NameConstant.EDIT_TYPE, -1);
            String value = intent.getStringExtra(GlobalConstant.NameConstant.VALUE);
            initView(type, value);
        }
    }

    private void initView(int type, String value) {
        editText.setText(value != null ? value : "");

        switch (type) {
            case GlobalConstant.EditType.EDIT_NICKNAME:
                setTitle(R.string.nickname_setting);
                break;
            case GlobalConstant.EditType.EDIT_AGE:
                setTitle(R.string.age_setting);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
            case GlobalConstant.EditType.EDIT_COLOR:
                setTitle(R.string.color_setting);
                break;
            case GlobalConstant.EditType.EDIT_HEIGHT:
                setTitle(R.string.height_setting);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
            case GlobalConstant.EditType.EDIT_WEIGHT:
                setTitle(R.string.weight_setting);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
            case GlobalConstant.EditType.EDIT_AREA:
                setTitle(R.string.area_setting);
                break;
            case GlobalConstant.EditType.EDIT_PLAY_PLACE:
                setTitle(R.string.play_place_setting);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (v == okButton) {
            String value = editText.getText().toString().trim();

            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.NameConstant.VALUE, value);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
