package com.segotech.ipetchat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segotech.ipetchat.R;

/**
 * Created by sk on 13-11-2.
 */
public class SettingMenuItemView extends RelativeLayout {
    private String title;
    private String status;
    private String value = "";
    public SettingMenuItemView(Context context) {
        super(context);
        init(context, null);
    }

    public SettingMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_menu_item, this);
        TypedArray arr = null;
        try {
            arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SettingMenuItemView, 0, 0);
            title = arr.getString(R.styleable.SettingMenuItemView_title);
            status = arr.getString(R.styleable.SettingMenuItemView_status);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (arr != null) {
                arr.recycle();
            }
        }

        TextView valueTv = (TextView) findViewById(R.id.value);
        valueTv.setText("");

        TextView titleTV = (TextView) findViewById(R.id.name);
        TextView statusTV = (TextView) findViewById(R.id.setting_status);
        titleTV.setText(title != null ? title : "");
        statusTV.setText(status != null ? status : "");
    }

    public void setTitle(String title) {
        this.title = title;
        TextView titleTV = (TextView) findViewById(R.id.name);
        titleTV.setText(title);
    }

    public void setStatus(String status) {
        this.status = status;
        TextView statusTV = (TextView) findViewById(R.id.setting_status);
        statusTV.setText(status);
    }

    public void setValueText(String value) {
        TextView valueTv = (TextView) findViewById(R.id.value);
        valueTv.setText(value);
    }

    public String getValueText() {
        TextView valueTv = (TextView) findViewById(R.id.value);
        return valueTv.getText().toString().trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
