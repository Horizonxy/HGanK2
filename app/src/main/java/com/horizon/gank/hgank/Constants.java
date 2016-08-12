package com.horizon.gank.hgank;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public static final int PAGE_SIZE = 20;

	public static final String END_POIND = "http://gank.io";

	public static final String BASE_DIR = "horizon_gank_5.0";
	public static final String IMG_CACHE_DIR = BASE_DIR.concat("/image_cache");
	public static final String IMG_WEB_CACHE_DIR = BASE_DIR.concat("/image_web");
	public static final String IMG_DOWNLOAD_DIR = BASE_DIR.concat("/download");

	public final static String BUNDLE_WEBVIEW_URL = "bundle_webview_url";
	public final static String BUNDLE_WEBVIEW_VEDIO = "bundle_webview_vedio";

	public final static String BUNDLE_PIC_INFOS = "bundle_picture_infos";

	public final static String BUNDLE_FRAGMENT_TYPE = "bundle_fragment_type";

	public final static String BUNDLE_THEME = "bundle_theme";
	public final static String BUNDLE_OLD_THEME_COLOR = "bundle_theme_color";

	public static final int REQ_PERMISSIONS = 1000;

	public static final long TIME_OUT = 10;

	public static enum Theme {
		RED(R.style.red_theme, Application.application.getResources().getColor(R.color.red)),
		BLUE(R.style.blue_theme, Application.application.getResources().getColor(R.color.blue)),
		BLACK(R.style.black_theme, Application.application.getResources().getColor(R.color.black)),
		YELLOW(R.style.yellow_theme, Application.application.getResources().getColor(R.color.yellow)),
		GREEN(R.style.green_theme, Application.application.getResources().getColor(R.color.green)),
		PURPLE(R.style.purple_theme, Application.application.getResources().getColor(R.color.purple)),
		PURPLE_RED(R.style.purple_red_theme, Application.application.getResources().getColor(R.color.purple_red)),
		COFFEE(R.style.coffee_theme, Application.application.getResources().getColor(R.color.coffee));

		private int theme;
		private int color;

		Theme(int theme, int color) {
			this.theme = theme;
			this.color = color;
		}

		public static Theme byColor(int color){
			for (Theme theme:values()){
				if(theme.getColor() == color){
					return theme;
				}
			}
			return RED;
		}

		public static Theme byTheme(int t){
			for (Theme theme:values()){
				if(theme.getTheme() == t){
					return theme;
				}
			}
			return RED;
		}

		public int getTheme() {
			return theme;
		}

		public void setTheme(int theme) {
			this.theme = theme;
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public static List<Theme> list(){
			return Arrays.asList(values());
		}
	}

}
