package com.beijing.chengxin.ui.bean;

import java.util.ArrayList;

public class HangyeInfo {

    public int id;
    public String title;
    public ArrayList<HangyeItemInfo> itemList;

    public HangyeInfo() {
    }

    public HangyeInfo(HangyeInfo bean) {
        this.id = bean.id;
        this.title = bean.title;
        this.itemList = bean.itemList;
    }

    static public class HangyeItemInfo {
        public int itemId;
        public String itemTitle;

        public HangyeItemInfo() {
        }

        public HangyeItemInfo(HangyeItemInfo itemInfo) {
            this.itemId = itemInfo.itemId;
            this.itemTitle = itemInfo.itemTitle;
        }
    }

}
