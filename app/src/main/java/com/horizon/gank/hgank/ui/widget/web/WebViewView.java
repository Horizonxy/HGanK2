package com.horizon.gank.hgank.ui.widget.web;

public interface WebViewView {
    void onReceivedTitle(String title);
    void onProgressChanged(int newProgress);
    void firstLoadAfter();
}
