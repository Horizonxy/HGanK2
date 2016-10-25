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

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.TypefaceTextView;
import com.horizon.gank.hgank.util.DisplayUtils;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/8.
 */
public class SharePopupWindow extends PopupWindow {

    private Activity aty;
    private List<ShareVo> list;
    private int themeColor;
    private String url, content, title, imageUrl;
    private UMShareListener umShareListener;

    public SharePopupWindow(Activity aty, String url, String content, String title, String imageUrl, UMShareListener umShareListener) {
        super(aty);
        this.aty = aty;
        this.url = url;
        this.content = content;
        this.title = title;
        this.imageUrl = imageUrl;
        this.umShareListener = umShareListener;
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
        wxMoments.shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
        wxMoments.platform = "朋友圈";
        wxMoments.iconId = R.string.ico_friends;
        list.add(wxMoments);
        ShareVo wechat = new ShareVo();
        wechat.shareMedia = SHARE_MEDIA.WEIXIN;
        wechat.platform = "微信好友";
        wechat.iconId = R.string.ico_wechat;
        list.add(wechat);
        ShareVo qq = new ShareVo();
        qq.shareMedia = SHARE_MEDIA.QQ;
        qq.platform = "QQ";
        qq.iconId = R.string.ico_qq;
        list.add(qq);
        ShareVo qzone = new ShareVo();
        qzone.shareMedia = SHARE_MEDIA.QZONE;
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
        SHARE_MEDIA shareMedia;
        String platform;
        int iconId;
    }

    class ShareItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShareVo shareVo = list.get(position);
            Logger.d("platform: "+shareVo.platform);

            ShareAction shareAction = new ShareAction(aty);
            if(content != null){
                shareAction.withText(content);
            }
            if(title != null){
                shareAction.withTitle(title);
            }
            if(url != null){
                shareAction.withTargetUrl(url);
            }
            shareAction.withMedia(new UMImage(Application.application.getApplicationContext(), R.mipmap.icon));
            shareAction.setPlatform(shareVo.shareMedia).setCallback(umShareListener).share();

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
