package com.ourygo.ygomobile.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.king.view.circleprogressview.CircleProgressView;
import com.ourygo.ygomobile.base.listener.BaseDuelInfoFragment;
import com.ourygo.ygomobile.bean.McDuelInfo;
import com.ourygo.ygomobile.util.StatUtil;

import cn.garymb.ygomobile.base.BaseFragemnt;
import cn.garymb.ygomobile.lite.R;

/**
 * Create By feihua  On 2021/10/19
 */
public class MatchRecordFragment extends BaseFragemnt implements BaseDuelInfoFragment {


    private CircleProgressView cpv_rank;
    private TextView tv_rank, tv_win, tv_lose;
    private McDuelInfo mcDuelInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view;
        if (isHorizontal)
            view = inflater.inflate(R.layout.mycard_match_rank_horizontal_fragment, container, false);
        else
            view = inflater.inflate(R.layout.mycard_match_rank_fragment, container, false);
        Log.e("MycardFragment","重新初始化"+(savedInstanceState!=null));
        initView(view);
        return view;
    }

    private void initView(View view) {
        isStat=false;
        cpv_rank = view.findViewById(R.id.cpv_rank);
        tv_rank = view.findViewById(R.id.tv_rank);
        tv_win = view.findViewById(R.id.tv_win);
        tv_lose = view.findViewById(R.id.tv_lose);
    }


    private void initData() {
        Log.e("MycardFragment","重新加载数据");
        cpv_rank.setMax(10000);
        onBaseDuelInfo(mcDuelInfo, null);
        //设置进度改变监听
        cpv_rank.setOnChangeListener((progress, max) -> {

        });

//        cpv_rank.gette
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        initData();
    }

    @Override
    public void onBaseDuelInfo(McDuelInfo mcDuelInfo, String exception) {
        Log.e("MycardFragment",(cpv_rank!=null)+" 回调 "+(mcDuelInfo!=null?mcDuelInfo.getMatchRank():"空"));
        if (!TextUtils.isEmpty(exception))
            return;
        this.mcDuelInfo = mcDuelInfo;
        if (cpv_rank == null)
            return;
        if (mcDuelInfo == null) {
            cpv_rank.setProgress(0);
            cpv_rank.setLabelText("");
            tv_lose.setText("");
            tv_win.setText("");
            tv_rank.setText("");
            return;
        }
        int pro = (int) (mcDuelInfo.getMatchWinRatio() * 100);
        Log.e("MatchFragment", "进度" + pro);
        //显示进度动画，进度，动画时长
        cpv_rank.showAnimation(pro, 600);
        //设置当前进度
        cpv_rank.setProgress(pro);
        cpv_rank.setLabelText(mcDuelInfo.getMatchWinRatio() + "%");
        tv_lose.setText(mcDuelInfo.getMatchLose() + "");
        tv_win.setText(mcDuelInfo.getMatchWin() + "");
        tv_rank.setText(mcDuelInfo.getMatchRank() + "");
    }
}