package com.segotech.ipetchat.settings;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.Toast;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.constants.GlobalConstant;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.view.SettingMenuItemView;

import java.io.FileNotFoundException;

public class PetInfoSettingActivity extends IPetChatNavigationActivity implements View.OnClickListener {
    private SettingMenuItemView settingSexItem;
    private SettingMenuItemView settingNickname;
    private SettingMenuItemView settingBreed;
    private SettingMenuItemView settingAge;
    private SettingMenuItemView settingColor;
    private SettingMenuItemView settingHeight;
    private SettingMenuItemView settingWeight;
    private SettingMenuItemView settingArea;
    private SettingMenuItemView settingPlayArea;

    private ImageView avatarView;

    private View uploadImageDlg;

    private Bitmap tmpBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content view
        setContentView(R.layout.pet_info_setting_activity_layout);
        setTitle(getString(R.string.pet_profile));

        settingSexItem = (SettingMenuItemView) findViewById(R.id.setting_sex);
        settingSexItem.setOnClickListener(this);

        settingNickname = (SettingMenuItemView) findViewById(R.id.setting_nickname);
        settingNickname.setOnClickListener(this);

        settingBreed = (SettingMenuItemView) findViewById(R.id.setting_breed);
        settingBreed.setOnClickListener(this);

        settingAge = (SettingMenuItemView) findViewById(R.id.setting_age);
        settingAge.setOnClickListener(this);

        settingColor = (SettingMenuItemView) findViewById(R.id.setting_color);
        settingColor.setOnClickListener(this);

        settingHeight = (SettingMenuItemView) findViewById(R.id.setting_height);
        settingHeight.setOnClickListener(this);

        settingWeight = (SettingMenuItemView) findViewById(R.id.setting_weight);
        settingWeight.setOnClickListener(this);

        settingArea = (SettingMenuItemView) findViewById(R.id.setting_area);
        settingArea.setOnClickListener(this);

        settingPlayArea = (SettingMenuItemView) findViewById(R.id.setting_play_area);
        settingPlayArea.setOnClickListener(this);

        View saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(this);

        avatarView = (ImageView) findViewById(R.id.pet_avatar_imageView);
        avatarView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_sex) {
            Intent intent = new Intent(this, ProfileItemSelectActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_SEX);
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_SEX);
        } else if (v.getId() == R.id.setting_nickname) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_NICKNAME);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingNickname.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_NAME);
        } else if (v.getId() == R.id.setting_breed) {
            Intent intent = new Intent(this, ProfileItemSelectActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_BREED);
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_BREED);
        } else if (v.getId() == R.id.setting_age) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_AGE);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingAge.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_AGE);
        } else if (v.getId() == R.id.setting_color) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_COLOR);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingColor.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_COLOR);
        } else if (v.getId() == R.id.setting_height) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_HEIGHT);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingHeight.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_HEIGHT);
        } else if (v.getId() == R.id.setting_weight) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_WEIGHT);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingWeight.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_WEIGHT);
        } else if (v.getId() == R.id.setting_area) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_AREA);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingArea.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_AREA);
        } else if (v.getId() == R.id.setting_play_area) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_PLAY_PLACE);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingPlayArea.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_PLAY_PLACE);
        } else if (v.getId() == R.id.save) {
            // todo: save the profile to server
        } else if (v.getId() == R.id.pet_avatar_imageView) {
            if (uploadImageDlg == null) {
                uploadImageDlg = ((ViewStub) findViewById(R.id.upload_image_dlg)).inflate();
                uploadImageDlg.bringToFront();
                View captureImageButton = uploadImageDlg.findViewById(R.id.capture_photo);
                captureImageButton.setOnClickListener(this);
                View uploadButton = uploadImageDlg.findViewById(R.id.upload);
                uploadButton.setOnClickListener(this);
                View cancel = uploadImageDlg.findViewById(R.id.cancel);
                cancel.setOnClickListener(this);
                View empty = uploadImageDlg.findViewById(R.id.empty);
                empty.setOnClickListener(this);
            }
            uploadImageDlg.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.capture_photo) {
            capturePhoto();
        } else if (v.getId() == R.id.upload) {
            browsePhotoGallery();
        } else if (v.getId() == R.id.cancel || v.getId() == R.id.empty) {
            uploadImageDlg.setVisibility(View.GONE);
        }
    }

    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, GlobalConstant.RequestCode.CAPTURE_PHOTO);
    }

    private void browsePhotoGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), GlobalConstant.RequestCode.SELECT_PHOTO);
    }

    private void cropPhoto(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, GlobalConstant.RequestCode.CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            String value = data.getStringExtra(GlobalConstant.NameConstant.VALUE);

            switch (requestCode) {
                case GlobalConstant.RequestCode.EDIT_SEX:
                    settingSexItem.setValueText(value);
                    settingSexItem.setValue(value);
                    settingSexItem.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_NAME:
                    settingNickname.setValueText(value);
                    settingNickname.setValue(value);
                    settingNickname.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_AGE:
                    try {
                        int age = Integer.parseInt(value);
                        settingAge.setValue(value);
                        settingAge.setValueText(getString(R.string.pet_age_value_format, age));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    settingAge.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_BREED:
                    settingBreed.setValueText(value);
                    settingBreed.setValue(value);
                    settingBreed.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_COLOR:
                    settingColor.setValueText(value);
                    settingColor.setValue(value);
                    settingColor.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_HEIGHT:
                    try {
                        float height = Float.parseFloat(value);
                        settingHeight.setValue(value);
                        settingHeight.setValueText(getString(R.string.pet_height_value_format, height));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    settingHeight.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_WEIGHT:
                    try {
                        float weight = Float.parseFloat(value);
                        settingWeight.setValue(value);
                        settingWeight.setValueText(getString(R.string.pet_weight_value_format, weight));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    settingWeight.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_AREA:
                    settingArea.setValueText(value);
                    settingArea.setValue(value);
                    settingArea.setStatus("");
                    break;
                case GlobalConstant.RequestCode.EDIT_PLAY_PLACE:
                    settingPlayArea.setValueText(value);
                    settingPlayArea.setValue(value);
                    settingPlayArea.setStatus("");
                    break;

                case GlobalConstant.RequestCode.CAPTURE_PHOTO:
                case GlobalConstant.RequestCode.SELECT_PHOTO:
//                    Bundle extras = data.getExtras();

//                    tmpBitmap = (Bitmap) extras.get("data");
                    Uri uri = data.getData();
                    if (uri == null) {
                        Toast.makeText(this, R.string.photo_taken_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cropPhoto(uri, 500);
//                    ContentResolver cr = this.getContentResolver();
//                    try {
//                        tmpBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                        avatarView.setImageBitmap(tmpBitmap);
//                        uploadImageDlg.setVisibility(View.GONE);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        Toast.makeText(this, R.string.photo_taken_failed, Toast.LENGTH_SHORT).show();
//                    }


                    break;

                case GlobalConstant.RequestCode.CROP_IMAGE:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = bundle.getParcelable("data");
                        if (photo != null) {
                            if (tmpBitmap != null) {
                                avatarView.setImageResource(R.drawable.add_avatar);
                                tmpBitmap.recycle();
                            }
                            tmpBitmap = photo;
                            avatarView.setImageBitmap(tmpBitmap);
                            uploadImageDlg.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    }
}
