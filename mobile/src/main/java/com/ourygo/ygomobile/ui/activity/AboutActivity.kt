package com.ourygo.ygomobile.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.garymb.ygomobile.lite.R
import cn.garymb.ygomobile.ui.activities.BaseActivity
import com.feihua.dialogutils.bean.UpdateLog
import com.feihua.dialogutils.util.DialogUtils
import com.ourygo.ygomobile.util.OYUtil
import com.ourygo.ygomobile.util.Record

class AboutActivity : BaseActivity() {
    private lateinit var tv_check_update: TextView
    private lateinit var tv_version: TextView
    private lateinit var tv_qq_group: TextView
    private lateinit var ll_version: LinearLayout
    private lateinit var iv_icon: ImageView
    private val du by lazy {
        DialogUtils.getInstance(this)
    }
    private val updateList by lazy {
        arrayListOf(
            UpdateLog.toUpdateLog(
                "1.2.6",
                """
                更新ygo内核
                更新卡包1202+T1201+DBVS+SR14+WPP4+VJ
                OCG禁卡表更新至2023.10
                TCG禁卡表更新至2023.9
                增加扩展卡包.ini文件服务器记录
                其他优化
                """.trimIndent()
            ),
            UpdateLog.toUpdateLog(
                "1.2.5", """
                修复卡组码内容缺失时有几率无法检测的问题
                修复卡包卡组分享卡组码异常的问题
                优化卡组分享交互
                """.trimIndent()
            ),
            UpdateLog.toUpdateLog(
                "1.2.4", """
                更新ygo内核
                更新卡包DP28+AC03+SD46+VJ
                OCG禁卡表更新至2023.7
                TCG禁卡表更新至2023.6
                """.trimIndent()
            ),
            UpdateLog.toUpdateLog(
                "1.2.3", """
               更新ygo内核
               更新卡包1111+T1110+VJ
               软件名简化为YGO-OY
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.2.2", """
               更新ygo内核
               更新卡包WPP3+VJ
               修复服务器列表切换显示模式后点击无反应的问题
               暂时去除本地AI
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.2.1", """
               更新ygo内核
               更新卡包DBAD+VJ+YCSW
               OCG禁卡表更新至2022.10.1
               修复分享卡组码崩溃的问题
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.2.0", """
               更新ygo内核
               更新卡包SR13+T1109
               服务器列表可选择表格布局
               修复部分机型对话框关闭时输入法未关闭的问题
               MC萌卡邮箱未验证时支持跳转验证邮箱
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.1.4", """
               更新ygo内核
               更新卡包DP27+VP22+T1108+AC02+VJ
               OCG禁卡表更新至2022.7.1
               TCG禁卡表更新至2022.5.17
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.1.3", """
                         更新ygo内核
                         更新卡包1109+KC01+VJ
                         """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.1.2", """
                        更新ygo内核
                        更新卡包DBTM+VX+VJ
                        """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.1.1", """
               更新ygo内核
               更新卡包HC01+T1107+VJ
               修复卡组码不完整时的闪退问题
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.1.0", """
               更新ygo内核
               更新卡包1108+VJ
               适配平板布局
               设置背景时可选择背景模糊
               修复卡组分类排序错误的问题
               其他优化
               """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.0.3", """
                    更新ygo内核
                    更新卡包22PP+SSB1+VJ
                    """.trimIndent()
            ),

            UpdateLog.toUpdateLog(
                "1.0.2", """
               更新ygo内核
               更新卡包SD43
               更新2021.1 OCG禁卡表
               """.trimIndent()
            ),
            UpdateLog.toUpdateLog(
                "1.0.1",
                """
                更新ygo内核
                新卡DP+T1106+VJ
                其他优化 
                """.trimIndent()
            ),
            UpdateLog.toUpdateLog(
                "1.0",
                "初始功能"
            )

        )
    }

//    @SuppressLint("HandlerLeak")
//    var han: Handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            when (msg.what) {
//                TYPE_NO_UPDATE -> OYUtil.snackShow(tv_check_update, OYUtil.s(R.string.no_update))
//                TYPE_CHECK_UPDATE_EXCEPTION -> OYUtil.snackExceptionToast(
//                    this@AboutActivity, tv_check_update, OYUtil.s(
//                        R.string.exception
//                    ), msg.obj.toString()
//                )
//
//                TYPE_YES_UPDATE -> {}
//                DOWNLOAD_OK -> {
//                    du!!.dis()
//                    startActivity(
//                        IntentUtil.getApkFileIntent(
//                            this@AboutActivity,
//                            (msg.obj as File).path
//                        )
//                    )
//                }
//
//                DOWNLOAD_PROGRESS -> du!!.setMessage(OYUtil.s(R.string.downloading) + "(" + msg.arg1 + ")")
//                DOWNLOAD_EXCEPTION -> {
//                    du!!.dis()
//                    OYUtil.snackExceptionToast(
//                        this@AboutActivity,
//                        tv_check_update,
//                        OYUtil.s(R.string.download_exception),
//                        msg.obj.toString()
//                    )
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        initView()
    }

    private fun initView() {
        tv_check_update = findViewById(R.id.tv_check_update)
        tv_qq_group = findViewById(R.id.tv_qq_group)
        tv_version = findViewById(R.id.tv_version)
        iv_icon = findViewById(R.id.iv_icon)
        ll_version = findViewById(R.id.ll_version)
        initToolbar(OYUtil.s(R.string.about))
        iv_icon.setOnClickListener(object : View.OnClickListener {
            private var clickFirstTime: Long = 0
            private var clickNum = 0
            override fun onClick(v: View) {
                if (clickFirstTime + 1000 > System.currentTimeMillis()) {
                    Log.e("AboutActivity", "加一$clickNum")
                    clickFirstTime = System.currentTimeMillis()
                    clickNum++
                    if (clickNum >= 5) {
                        clickNum = 0
                        return
                    }
                } else {
                    Log.e("AboutActivity", "重置")
                    clickNum = 0
                    clickFirstTime = System.currentTimeMillis()
                }
            }
        })
        tv_check_update.setOnClickListener { checkUpdate() }
        tv_qq_group.setOnClickListener {
            OYUtil.joinQQGroup(
                this@AboutActivity,
                Record.ARG_QQ_GROUP_KEY
            )
        }
        ll_version.setOnClickListener {
            val v = du.dialogUpdateLog(OYUtil.s(R.string.update_log), updateList)
            v[1].setOnClickListener { du.dis() }
        }
        initUpdateLog()
    }

    private fun initUpdateLog() {

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
    private fun checkUpdate() {
        OYUtil.checkUpdate(this, true)
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

//    companion object {
//        private const val TYPE_NO_UPDATE = 0
//        private const val TYPE_YES_UPDATE = 1
//        private const val TYPE_CHECK_UPDATE_EXCEPTION = 2
//        private const val DOWNLOAD_OK = 3
//        private const val DOWNLOAD_PROGRESS = 4
//        private const val DOWNLOAD_EXCEPTION = 5
//    }
}