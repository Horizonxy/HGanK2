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
}
