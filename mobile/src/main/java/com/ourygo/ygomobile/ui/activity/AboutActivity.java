package com.ourygo.ygomobile.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feihua.dialogutils.bean.UpdateLog;
import com.feihua.dialogutils.util.DialogUtils;
import com.ourygo.ygomobile.util.IntentUtil;
import com.ourygo.ygomobile.util.OYUtil;
import com.ourygo.ygomobile.util.Record;
import com.ourygo.ygomobile.util.StatUtil;
//import com.tencent.bugly.beta.Beta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.garymb.ygomobile.App;
import cn.garymb.ygomobile.lite.R;
import cn.garymb.ygomobile.ui.activities.BaseActivity;
import cn.garymb.ygomobile.ui.mycard.mcchat.management.UserManagement;
import cn.garymb.ygomobile.utils.DownloadUtil;

public class AboutActivity extends BaseActivity {
    private static final int TYPE_NO_UPDATE = 0;
    private static final int TYPE_YES_UPDATE = 1;
    private static final int TYPE_CHECK_UPDATE_EXCEPTION = 2;

    private static final int DOWNLOAD_OK = 3;
    private static final int DOWNLOAD_PROGRESS = 4;
    private static final int DOWNLOAD_EXCEPTION = 5;
    TextView tv_check_update, tv_version, tv_qq_group;
    private LinearLayout ll_version;
    private List<UpdateLog> updateList;
    private ImageView iv_icon;

    private DialogUtils du;
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_NO_UPDATE:
                    OYUtil.snackShow(tv_check_update, OYUtil.s(R.string.no_update));
                    break;
                case TYPE_CHECK_UPDATE_EXCEPTION:
                    OYUtil.snackExceptionToast(AboutActivity.this, tv_check_update, OYUtil.s(R.string.exception), msg.obj.toString());
                    break;
                case TYPE_YES_UPDATE:
//                    downloadApp((App) msg.obj);
                    break;
                case DOWNLOAD_OK:
                    du.dis();
                    startActivity(IntentUtil.getApkFileIntent(AboutActivity.this, ((File) msg.obj).getPath()));
                    break;
                case DOWNLOAD_PROGRESS:
                    du.setMessage(OYUtil.s(R.string.downloading) + "(" + msg.arg1 + ")");
                    break;
                case DOWNLOAD_EXCEPTION:
                    du.dis();
                    OYUtil.snackExceptionToast(AboutActivity.this, tv_check_update, OYUtil.s(R.string.download_exception), msg.obj.toString());
                    break;
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatUtil.onPause(this);
    }

    private void initView() {
        tv_check_update = findViewById(R.id.tv_check_update);
        tv_qq_group = findViewById(R.id.tv_qq_group);
        tv_version = findViewById(R.id.tv_version);
        iv_icon = findViewById(R.id.iv_icon);
        ll_version = findViewById(R.id.ll_version);

        du = DialogUtils.getInstance(this);
        updateList = new ArrayList<>();
        initToolbar(OYUtil.s(R.string.about));

        iv_icon.setOnClickListener(new View.OnClickListener() {


            private long clickFirstTime;
            private int clickNum;

            @Override
            public void onClick(View v) {

                if (clickFirstTime + 1000 > System.currentTimeMillis()) {
                    Log.e("AboutActivity", "加一" + clickNum);
                    clickFirstTime = System.currentTimeMillis();
                    clickNum++;
                    if (clickNum >= 5) {
                        clickNum = 0;

                        return;
                    }
                } else {
                    Log.e("AboutActivity", "重置");
                    clickNum = 0;
                    clickFirstTime = System.currentTimeMillis();
                }
            }
        });


        tv_check_update.setOnClickListener(p1 -> checkUpdate());
        tv_qq_group.setOnClickListener(v -> OYUtil.joinQQGroup(AboutActivity.this, Record.ARG_QQ_GROUP_KEY));
        ll_version.setOnClickListener(p1 -> {
            View[] v = du.dialogUpdateLog(OYUtil.s(R.string.update_log), updateList);
            v[1].setOnClickListener(p11 -> du.dis());
        });
        initUpdateLog();


    }

    private void initUpdateLog() {
       updateList.add(UpdateLog.toUpdateLog("1.0.2", "更新ygo内核\n" +
               "更新卡包SD43\n" +
               "更新2021.1 OCG禁卡表"));
       updateList.add(UpdateLog.toUpdateLog("1.0.1", "更新ygo内核\n新卡DP+T1106+VJ\n其他优化"));
       updateList.add(UpdateLog.toUpdateLog("1.0", "初始功能"));
    }

//    private void downloadApp(final App app) {
//        View[] v = du.dialogAppUpdate(app.getVersion_name(), app.getMessage());
//        Button bt1, bt2;
//        bt1 = (Button) v[0];
//        bt2 = (Button) v[1];
//        bt1.setText(SCUtil.s(R.string.cancel));
//        bt2.setText(SCUtil.s(R.string.download));
//        bt1.setOnClickListener(p1 -> du.dis());
//        du.setCanceledOnTouchOutside(true);
//        bt2.setOnClickListener(p1 -> {
//            du.dialogj1(null, SCUtil.s(R.string.downloading));
//            DownloadUtil.get().download(app.getPath()
//                    , Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
//                    , SCUtil.s(R.string.app_name) + "_" + app.getVersion_name() + ".apk"
//                    , new DownloadUtil.OnDownloadListener() {
//
//                        @Override
//                        public void onDownloadSuccess(File file) {
//                            Message me = new Message();
//                            me.obj = file;
//                            me.what = DOWNLOAD_OK;
//                            han.sendMessage(me);
//
//                        }
//
//                        @Override
//                        public void onDownloading(int progress) {
//                            Message me = new Message();
//                            me.arg1 = progress;
//                            me.what = DOWNLOAD_PROGRESS;
//                            han.sendMessage(me);
//
//                        }
//
//                        @Override
//                        public void onDownloadFailed(Exception e) {
//                            Message me = new Message();
//                            me.obj = e.toString();
//                            me.what = DOWNLOAD_EXCEPTION;
//                            han.sendMessage(me);
//
//                        }
//                    });
//
//        });
//
//
//    }

    private void checkUpdate() {
        OYUtil.checkUpdate(true,true);
//		QueryUtil.checkUpdate(new OnCheckUpdateListener(){
//
//				@Override
//				public void onCheckUpdate(App app, String exception) {
//					Message me=new Message();
//					if(TextUtils.isEmpty(exception)){
//						if(app==null){
//							me.what=TYPE_NO_UPDATE;
//						}else{
//							me.what=TYPE_YES_UPDATE;
//							me.obj=app;
//						}
//					}else{
//						me.what=TYPE_CHECK_UPDATE_EXCEPTION;
//						me.obj=exception;
//					}
//					han.sendMessage(me);
//
//				}
//			});

    }

}