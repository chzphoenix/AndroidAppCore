package com.hcz.core.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by chz on 2016/6/25.
 */

public class ViewUtils {
    public static void setTextView(TextView view, Object text){
        if(text != null){
            view.setText(String.valueOf(text));
        }
        else{
            view.setText("");
        }
    }

    public static void setTextViewByVisible(TextView view, Object text){
        if(text != null){
            view.setText(String.valueOf(text));
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setVisibility(View.GONE);
        }
    }

    public static void setEditText(EditText view, Object text){
        if(text != null){
            view.setText(String.valueOf(text));
        }
        else{
            view.setText("");
        }
    }
}
