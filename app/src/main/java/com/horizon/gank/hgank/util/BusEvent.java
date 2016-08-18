package com.horizon.gank.hgank.util;

public class BusEvent {

    @com.mcxiaoke.bus.annotation.BusEvent
    public static class NetEvent{
        private boolean hasNet;

        public boolean isHasNet() {
            return hasNet;
        }

        public void setHasNet(boolean hasNet) {
            this.hasNet = hasNet;
        }
    }

    @com.mcxiaoke.bus.annotation.BusEvent
    public static class ThemeColorEvent{
        private int color;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    @com.mcxiaoke.bus.annotation.BusEvent
    public static class FileDownLoadEvent{
        private long progress, total;

        public FileDownLoadEvent(long progress, long total) {
            this.progress = progress;
            this.total = total;
        }

        public long getProgress() {
            return progress;
        }

        public void setProgress(long progress) {
            this.progress = progress;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }
    }
}
