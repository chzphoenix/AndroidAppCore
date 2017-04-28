package com.hcz.appcore.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.hcz.core.utils.SimplifiedChineseSorter;
import com.hcz.core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sort();
    }

    private void sort(){
        List<SortModel> list = new ArrayList<SortModel>();
        list.add(new SortModel(1, "ji"));
        list.add(new SortModel(2, "网红"));
        list.add(new SortModel(3, "12"));
        list.add(new SortModel(4, "李水"));
        list.add(new SortModel(5, "ad"));
        list.add(new SortModel(6, " 刘风"));
        list.add(new SortModel(7, "就接"));
        list.add(new SortModel(8, "Si"));

        List<SortModel> sortlist = SimplifiedChineseSorter.sortByFieldName(list, "name", true);
        List<SortModel> sortlist1 = SimplifiedChineseSorter.sortByFieldName(list, "name", false);
        List<SortModel> sortlist2 = Ordering.from(SimplifiedChineseSorter.getSortComparatorByFieldAnnotation(SortModel.class, true)).sortedCopy(list);
        List<SortModel> sortlist3 = Ordering.from(SimplifiedChineseSorter.getSortComparatorByFieldAnnotation(SortModel.class, false)).sortedCopy(list);

//        List<SortModel> sortlist = SimplifiedChineseSorter.sortByProvider(list, new SimplifiedChineseSorter.SortStringProvider<SortModel>() {
//            @Override
//            public String getSortString(SortModel obj) {
//                return obj.getName();
//            }
//        }, true);
//        List<SortModel> sortlist1 = SimplifiedChineseSorter.sortByMethodName(list, "getName", false);
//        List<SortModel> sortlist2 = Ordering.from(SimplifiedChineseSorter.getSortComparatorByMethodAnnotation(SortModel.class, true)).sortedCopy
//                (list);
//        List<SortModel> sortlist3 = Ordering.from(SimplifiedChineseSorter.getSortComparatorByMethodAnnotation(SortModel.class, false)).sortedCopy(list);

        for (SortModel model : sortlist) {
            Log.e("sort list", model.toString());
        }
        Log.d("sort list", "finish");
        for (SortModel model : sortlist1) {
            Log.e("sort list1", model.toString());
        }
        Log.d("sort list", "finish");
        for (SortModel model : sortlist2) {
            Log.e("sort list2", model.toString());
        }
        Log.d("sort list", "finish");
        for (SortModel model : sortlist3) {
            Log.e("sort list3", model.toString());
        }
    }
}
