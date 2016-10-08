package com.horizon.gank.hgank.ui.popup;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.TypefaceTextView;
import com.horizon.gank.hgank.util.DisplayUtils;
import com.horizon.gank.hgank.util.LogUtils;
import com.horizon.gank.hgank.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

import static u.aly.au.O;
import static u.aly.au.l;

/**
 * Created by Administrator on 2016/10/8.
 */
public class SharePopupWindow extends PopupWindow {

    private Activity aty;
    private List<ShareVo> list;
    private int themeColor;

    public SharePopupWindow(Activity aty/*, String url, String content, String title, String imageUrl*/) {
        super(aty);
        this.aty = aty;
        this.themeColor = ThemeUtils.getThemeColor(aty, R.attr.colorPrimary);

        buildList();

        GridView gridView = (GridView) aty.getLayoutInflater().inflate(R.layout.view_share_window, null);
        gridView.setBackgroundColor(themeColor);
        gridView.setAdapter(new ShareAdapter());
        gridView.setOnItemClickListener(new ShareItemClickListener());

        setContentView(gridView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(aty.getResources().getColor(R.color.white));
        this.setBackgroundDrawable(dw);
    }

    private void buildList() {
        this.list = new ArrayList<ShareVo>();

        ShareVo wxMoments = new ShareVo();
        wxMoments.platform = "朋友圈";
        wxMoments.iconId = R.string.ico_friends;
        list.add(wxMoments);
        ShareVo wechat = new ShareVo();
        wechat.platform = "微信好友";
        wechat.iconId = R.string.ico_wechat;
        list.add(wechat);
        ShareVo qq = new ShareVo();
        qq.platform = "QQ";
        qq.iconId = R.string.ico_qq;
        list.add(qq);
        ShareVo qzone = new ShareVo();
        qzone.platform = "QQ空间";
        qzone.iconId = R.string.ico_space;
        list.add(qzone);
    }

    public void show() {
        showAtLocation(aty.getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
        DisplayUtils.backgroundAlpha(aty, 0.8f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        DisplayUtils.backgroundAlpha(aty, 1f);
    }

    class ShareVo {
        String platform;
        int iconId;
    }

    class ShareItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShareVo shareVo = list.get(position);
            LogUtils.e("platform: "+shareVo.platform);
            dismiss();
        }
    }

    class ShareAdapter extends BaseAdapter {

        private Resources res = aty.getResources();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = aty.getLayoutInflater().inflate(R.layout.item_share_content, null);
                holder.tvIcon = (TypefaceTextView) convertView.findViewById(R.id.tv_icon);
                holder.tvIcon.setTextColor(themeColor);
                holder.tvPlatform = (TextView) convertView.findViewById(R.id.tv_platform);
                holder.tvPlatform.setTextColor(themeColor);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            ShareVo shareVo = list.get(position);
            holder.tvIcon.setText(res.getString(shareVo.iconId));
            holder.tvPlatform.setText(shareVo.platform);
            return convertView;
        }

        class Holder {
            TypefaceTextView tvIcon;
            TextView tvPlatform;
        }
    }
}
