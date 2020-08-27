package com.hwindiapp.passenger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.fonts.Text.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareRefCodeActivity extends AppCompatActivity {

    private CallbackManager sCallbackManager;
    TextView text_header;

    private Toolbar mToolbar;

    String json_responseString_profile;

    MyTextView titleTxt;
    MyTextView subTitleTxt;
    MyTextView inviteLabelTxt;
    MyTextView inviteLabelSmallTxt;
    MyTextView refCodeTxt;
    MyTextView refCodeValueTxt;

    String userRefCode = "";

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_INVITE_PAGE_TITLE_TXT_str = "";
    String LBL_INVITE_PAGE_NOTE1_TXT_str = "";
    String LBL_INVITE_PAGE_NOTE2_TXT_str = "";
    String LBL_INVITE_PAGE_BIG_TXT_str = "";
    String LBL_INVITE_PAGE_SMALL_TXT_str = "";
    String LBL_INVITE_PAGE_SHARE_CODE_TXT_str = "";
    String LBL_SHARE_REF_TXT_str = "";
    String LBL_BTN_REFER_TXT_str = "";

    RippleStyleButton refer_btn,refer_fb_btn;
    AppInviteDialog mInvititeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_share_ref_code);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        Bundle bundle = getIntent().getExtras();
        json_responseString_profile = bundle.getString("Json");

        dbConnect = new DBConnect(this, "UC_labels.db");

        text_header = (TextView) findViewById(R.id.text_header);
        titleTxt = (MyTextView) findViewById(R.id.titleTxt);
        subTitleTxt = (MyTextView) findViewById(R.id.subTitleTxt);
        inviteLabelTxt = (MyTextView) findViewById(R.id.inviteLabelTxt);
        inviteLabelSmallTxt = (MyTextView) findViewById(R.id.inviteLabelSmallTxt);
        refCodeTxt = (MyTextView) findViewById(R.id.refCodeTxt);
        refCodeValueTxt = (MyTextView) findViewById(R.id.refCodeValueTxt);
        refer_btn = (RippleStyleButton) findViewById(R.id.refer_btn);
        refer_fb_btn = (RippleStyleButton) findViewById(R.id.refer_fb_btn);

        refer_btn.setOnClickListener(new setOnClickList());
        refer_fb_btn.setOnClickListener(new setOnClickList());
        /* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        parse_json_string(json_responseString_profile);

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShareRefCodeActivity.super.onBackPressed();
            }
        });

        sCallbackManager = CallbackManager.Factory.create();

        mInvititeDialog = new AppInviteDialog(this);

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.refer_btn:

                    shareCode(LBL_SHARE_REF_TXT_str + " " + userRefCode);

                    break;
                case  R.id.refer_fb_btn:

                    InviteFriend();

//                    openDialogInvite(ShareRefCodeActivity.this);

                    break;

            }
        }
    }

    public void InviteFriend(){

        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/669997526498844";
//        previewImageUrl = "http://2.bp.blogspot.com/-99shOruuadw/VQsG2T233sI/AAAAAAAAEi0/noFTxUBh_rg/s1600/appscripts.png";

        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(appLinkUrl)
//                .setPreviewImageUrl(previewImageUrl)
                .build();
        AppInviteDialog.show(ShareRefCodeActivity.this, content);


    }



    public void openDialogInvite(final Activity activity) {
        String AppURl = "https://fb.me/669997526498844";  //Generated from //fb developers

//        String previewImageUrl = "http://someurl/13_dp.png";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl("App link url created via the Facebook AppLink creator tool")
                    .setPreviewImageUrl("http://www.redacted.com/previewImage.png")
                    .build();

            mInvititeDialog.show(ShareRefCodeActivity.this, content);

        }

        mInvititeDialog.registerCallback(sCallbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {

                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Log.d("", "onSuccess: Invitation Sent Successfully");
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Result", "Cancelled");
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Result", "Error " + exception.getMessage());
                        finish();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        sCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_INVITE_PAGE_TITLE_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_TITLE_TXT");
            LBL_INVITE_PAGE_NOTE1_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_NOTE1_TXT");
            LBL_INVITE_PAGE_NOTE2_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_NOTE2_TXT");
            LBL_INVITE_PAGE_BIG_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_BIG_TXT");
            LBL_INVITE_PAGE_SMALL_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_SMALL_TXT");
            LBL_INVITE_PAGE_SHARE_CODE_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_SHARE_CODE_TXT");
            LBL_SHARE_REF_TXT_str = obj_language_labels.getString("LBL_SHARE_REF_TXT");
            LBL_BTN_REFER_TXT_str = obj_language_labels.getString("LBL_BTN_REFER_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            text_header.setText("" + LBL_INVITE_PAGE_TITLE_TXT_str);
            titleTxt.setText("" + LBL_INVITE_PAGE_NOTE1_TXT_str);
            subTitleTxt.setText("" + LBL_INVITE_PAGE_NOTE2_TXT_str);
            inviteLabelTxt.setText(Html.fromHtml(LBL_INVITE_PAGE_BIG_TXT_str));
            inviteLabelSmallTxt.setText("" + LBL_INVITE_PAGE_SMALL_TXT_str);
            refCodeTxt.setText("" + LBL_INVITE_PAGE_SHARE_CODE_TXT_str + ": ");

            refer_btn.setText(LBL_BTN_REFER_TXT_str);

        }

    }

    public void parse_json_string(String json_str) {

        JSONObject jsonObject_userDetail = null;
        try {
            jsonObject_userDetail = new JSONObject(json_str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            userRefCode = jsonObject_userDetail.getString("vReferralCode");

            refCodeValueTxt.setText(userRefCode);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void shareCode(String shareTxt) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareTxt);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));

    }
}
