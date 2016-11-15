package com.hcz.core.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chz on 2016/11/15.
 */

public class ToastUtils {
    private static Toast mToast;

    public static Toast show(Context context, String msg,
                            int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(context);
        mToast.setText(msg);
        mToast.setDuration(duration);
        mToast.show();
        return mToast;
    }
}
