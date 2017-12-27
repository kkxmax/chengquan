package com.beijing.chengxin.ui.listener;

import java.util.List;

/**
 * Created by start on 2017.11.09.
 */

public interface OnConditionClickListener {
    void onClickReset();
    void onClickOk(String cityName, int kind, List<Integer>typeList);
}
