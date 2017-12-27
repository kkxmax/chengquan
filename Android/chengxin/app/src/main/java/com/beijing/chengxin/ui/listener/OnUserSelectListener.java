package com.beijing.chengxin.ui.listener;

import com.beijing.chengxin.network.model.UserModel;

/**
 * Created by start on 2017.11.09.
 */

public interface OnUserSelectListener {
    void onCancel();
    void onUserSelected(UserModel user);
}
