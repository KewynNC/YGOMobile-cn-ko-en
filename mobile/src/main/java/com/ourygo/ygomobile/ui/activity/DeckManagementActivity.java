package com.ourygo.ygomobile.ui.activity;

import static cn.garymb.ygomobile.core.IrrlichtBridge.ACTION_SHARE_FILE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feihua.dialogutils.util.DialogUtils;
import com.ourygo.lib.duelassistant.util.DADeckUtil;
import com.ourygo.lib.duelassistant.util.YGODAUtil;
import com.ourygo.ygomobile.adapter.DeckListBQAdapter;
import com.ourygo.ygomobile.util.HandlerUtil;
import com.ourygo.ygomobile.util.IntentUtil;
import com.ourygo.ygomobile.util.LogUtil;
import com.ourygo.ygomobile.util.OYUtil;
import com.ourygo.ygomobile.util.Record;
import com.ourygo.ygomobile.util.ShareUtil;
import com.ourygo.ygomobile.util.SharedPreferenceUtil;
import com.ourygo.ygomobile.util.YGOUtil;

import java.util.ArrayList;
import java.util.List;

import cn.garymb.ygomobile.AppsSettings;
import cn.garymb.ygomobile.Constants;
import cn.garymb.ygomobile.bean.Deck;
import cn.garymb.ygomobile.bean.events.DeckFile;
import cn.garymb.ygomobile.core.IrrlichtBridge;
import cn.garymb.ygomobile.lite.R;
import cn.garymb.ygomobile.loader.CardLoader;
import cn.garymb.ygomobile.loader.DeckLoader;
import cn.garymb.ygomobile.loader.ImageLoader;
import cn.garymb.ygomobile.utils.DeckUtil;

/**
 * Create By feihua  On 2021/11/10
 */
public class DeckManagementActivity extends ListAndUpdateActivity {

    private static final int SHARE_DECK_URI_OK = 0;
    private static final int SHARE_DECK_FILE_OK = 1;
    private static final int DECK_LIST_LOAD_OK = 2;

    private DeckListBQAdapter deckListAdp;
    private ImageLoader imageLoader;
    private DialogUtils dialogUtils;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHARE_DECK_URI_OK:
                    dialogUtils.dis();
                    ShareUtil.share(DeckManagementActivity.this, msg.obj.toString());
                    break;
                case SHARE_DECK_FILE_OK:

                    break;
                case DECK_LIST_LOAD_OK:

                    LogUtil.time("DeckManagement","刷新完毕");
                    deckListAdp.removeAllHeaderView();
                    initHeadView();
                    deckListAdp.setList((List<DeckFile>) msg.obj);
                    srl_update.setRefreshing(false);
                    break;
            }
        }
    };
    private View headerView, visitView;
    private TextView tv_download, tv_close, tv_visit, tv_close_visit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        LogUtil.setLastTime();
        LogUtil.time("DeckManagement","组件初始化");
        imageLoader = new ImageLoader();

        LogUtil.time("DeckManagement","图片初始化");
        deckListAdp = new DeckListBQAdapter(imageLoader, new ArrayList<>());
        dialogUtils = DialogUtils.getInstance(this);

        rv_list.setAdapter(deckListAdp);

        deckListAdp.addChildClickViewIds(R.id.iv_edit, R.id.iv_share, R.id.iv_del);
        deckListAdp.setOnItemChildClickListener((adapter, view, position) -> {
            DeckFile deckFile = deckListAdp.getItem(position);
            switch (view.getId()) {
                case R.id.iv_edit:
                    IntentUtil.startYGODeck(DeckManagementActivity.this, deckFile.getTypeName(),deckFile.getName());
                    break;
                case R.id.iv_share:
                    shareDeck(deckFile);
                    break;
                case R.id.iv_del:
                    deckFile.getPathFile().delete();
                    deckListAdp.removeAt(position);
                    OYUtil.snackShow(toolbar, "删除成功");
                    break;
            }
        });

        initToolbar("卡组管理");

        LogUtil.time("DeckManagement","其他初始化");
        onRefresh();
    }

    public void initHeadView(){
        if (SharedPreferenceUtil.isShowEz() && !OYUtil.isApp(Record.PACKAGE_NAME_EZ)) {
            headerView = LayoutInflater.from(this).inflate(R.layout.deck_management_header, null);

            tv_download = headerView.findViewById(R.id.tv_download);
            tv_close = headerView.findViewById(R.id.tv_close);
            tv_download.setOnClickListener(v -> startActivity(IntentUtil.getWebIntent(DeckManagementActivity.this, "http://ez.ourygo.top/")));
            tv_close.setOnClickListener(v -> {
                SharedPreferenceUtil.setIsShowEz(false);
                deckListAdp.removeHeaderView(headerView);
            });
            deckListAdp.addHeaderView(headerView);
        }

        if (SharedPreferenceUtil.isShowVisitDeck()) {
            visitView = LayoutInflater.from(this).inflate(R.layout.deck_management_header1, null);
            tv_visit = visitView.findViewById(R.id.tv_visit);
            tv_close_visit = visitView.findViewById(R.id.tv_close_visit);

            tv_visit.setOnClickListener(v -> {
                dialogUtils.dialogt1(null, "YGO-OY储存路径为内部储存/ygocore，如果你之前有使用过原版" +
                        "，可以打开原版软件，点击下边栏的卡组选项——功能菜单——备份/还原来导入或导出原版ygo中的卡组");
                TextView tv_message = dialogUtils.getMessageTextView();
                tv_message.setLineSpacing(OYUtil.dp2px(3), 1f);
            });
            tv_close_visit.setOnClickListener(v -> {
                SharedPreferenceUtil.setShowVisitDeck(false);
                deckListAdp.removeHeaderView(visitView);
            });
            deckListAdp.addHeaderView(visitView);
        }
    }

    private void shareDeck(DeckFile deckFile) {
        dialogUtils.dialogl("分享方式", new String[]{"卡组码分享", "文件分享"}).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogUtils.dis();
                switch (position) {
                    case 0:
                        shareDeck2Uri(deckFile);
                        break;
                    case 1:
                        shareDeck2File(deckFile);
                        break;
                }
            }
        });
    }

    private void shareDeck2File(DeckFile deckFile) {
        dialogUtils.dis();
        String category = deckFile.getPathFile().getParent();
        String fname = deckFile.getFileName();
        Intent intent = new Intent(ACTION_SHARE_FILE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(IrrlichtBridge.EXTRA_SHARE_TYPE, "ydk");
        if (TextUtils.equals(category, AppsSettings.get().getDeckDir())) {
            intent.putExtra(IrrlichtBridge.EXTRA_SHARE_FILE, fname);
        } else if (TextUtils.equals(category, AppsSettings.get().getPackDeckDir())) {
            intent.putExtra(IrrlichtBridge.EXTRA_SHARE_FILE, Constants.CORE_PACK_PATH + "/" + fname);
        } else {
            String cname = DeckUtil.getDeckTypeName(deckFile.getPathFile().getAbsolutePath());
            intent.putExtra(IrrlichtBridge.EXTRA_SHARE_FILE, cname + "/" + fname);
        }
        intent.setPackage(getPackageName());
        try {
            startActivity(intent);
        } catch (Throwable e) {
            Toast.makeText(getContext(), "dev error:not found activity.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareDeck2Uri(DeckFile deckFile) {
        dialogUtils.dialogj1(null, "卡组码生成中，请稍等");
        new Thread(() -> {
            Deck deck = DeckLoader.readDeck(new CardLoader(DeckManagementActivity.this), deckFile.getPathFile(), null).toDeck();
            String message = deck.toUri().toString();

            Log.d("feihua","卡组路径："+deckFile.getPathFile()+"  "+deck.getDeckCount()+"  "+deck.getMainCount()+"  "+deck.getExtraCount()+"  "+deck.getSideCount());
            message = "点击或复制打开YGO查看卡组《"+deckFile.getName()+"》：\n" + message;
            HandlerUtil.sendMessage(handler, SHARE_DECK_URI_OK, message);
        }).start();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        LogUtil.time("DeckManagement","开始刷新");
        new Thread(()->{
            HandlerUtil.sendMessage(handler,DECK_LIST_LOAD_OK,DeckUtil.getDeckAllList());
        }).start();

    }
}
