package com.hcz.core.utils;

import android.util.Log;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Simplified Chinese item sorter
 *
 * Created by hcui on 4/7/17.
 */
public class SimplifiedChineseSorter {
    private static final String SORTINGREGEX = "[^\\p{L}\\p{N}]+|^(The|A|An)\\b";
    private static final Collator stringComparator = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);

    public static <T> List<T> sortByFieldName(List<T> items, String fieldName, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        return sortByField(items, getSortStringField(items.get(0).getClass(), fieldName), isIgnoreCase);
    }

    public static <T> List<T> sortByFieldAnnotation(List<T> items, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        return sortByField(items, getSortStringField(items.get(0).getClass()), isIgnoreCase);
    }

    private static <T> List<T> sortByField(List<T> items, final Field sortField, final boolean isIgnoreCase) {
        sortField.setAccessible(true);
        final List<T> chinieseList = new ArrayList<T>();
        final List<T> nonChineseList = new ArrayList<T>();
        for (T item : items) {
            try {
                if (isChineseCharStart((String) sortField.get(item))) {
                    chinieseList.add(item);
                } else {
                    nonChineseList.add(item);
                }
            } catch (IllegalAccessException e) {
                Log.e("SimplifiedChineseSorter", "initsortlist", e);
                return null;
            }
        }
        List<T> sortedChineseList = Ordering.from(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                try {
                    return stringComparator.compare(format((String) sortField.get(lhs), isIgnoreCase),
                            format((String) sortField.get(rhs), isIgnoreCase));
                } catch (IllegalAccessException e) {
                    Log.e("SimplifiedChineseSorter", "sortedChineseList", e);
                    return 0;
                }
            }
        }).sortedCopy(chinieseList);
        List<T> sortedNonChineseList = Ordering.from(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                try {
                    return format((String) sortField.get(lhs), isIgnoreCase)
                                  .compareTo(format((String) sortField.get(rhs), isIgnoreCase));
                } catch (IllegalAccessException e) {
                    Log.e("SimplifiedChineseSorter", "sortedNonChineseList", e);
                    return 0;
                }
            }
        }).sortedCopy(nonChineseList);
        sortedChineseList.addAll(sortedNonChineseList);
        return sortedChineseList;
    }

    public static <T> Comparator<T> getSortComparatorByFieldName(final Class clazz, final String fieldName, boolean isIgnoreCase) {
        return getSortComparator(getSortStringField(clazz, fieldName), isIgnoreCase);
    }

    public static <T> Comparator<T> getSortComparatorByFieldAnnotation(final Class clazz, boolean isIgnoreCase) {
        return getSortComparator(getSortStringField(clazz), isIgnoreCase);
    }

    private static <T> Comparator<T> getSortComparator(final Field sortField, final boolean isIgnoreCase) {
        sortField.setAccessible(true);
        return new Comparator<T>() {
            @Override
            public int compare(final T left, final T right) {
                try {
                    String leftStr = format((String) sortField.get(left), isIgnoreCase);
                    String rightStr = format((String) sortField.get(right), isIgnoreCase);
                    if (SimplifiedChineseSorter.isChineseCharStart(leftStr) &&
                            SimplifiedChineseSorter.isChineseCharStart(rightStr)) {
                        return stringComparator.compare(leftStr, rightStr);
                    } else {
                        return ComparisonChain.start()
                                              .compareTrueFirst(SimplifiedChineseSorter.isChineseCharStart(leftStr),
                                                      SimplifiedChineseSorter.isChineseCharStart(rightStr))
                                              .compare(leftStr, rightStr, Ordering.natural().nullsFirst())
                                              .result();
                    }
                } catch (IllegalAccessException e) {
                    Log.e("SimplifiedChineseSorter", "sortedNonChineseList", e);
                    return 0;
                }
            }
        };
    }

    public static boolean isChineseCharStart(String str) {
        return !str.matches("[A-Za-z0-9\"“”]+.*");
    }

    private static <T> Field getSortStringField(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(SortString.class) && field.getType() == String.class) {
                    return field;
                }
            }
        }
        throw new RuntimeException("The model doesn't have a @SortString field or the type of @SortString field is not a String");
    }

    private static <T> Field getSortStringField(Class<T> tClass, String sortFieldName) {
        try {
            Field field = tClass.getDeclaredField(sortFieldName);
            if (field != null && field.getType() == String.class) {
                return field;
            }
            throw new RuntimeException("The model doesn't have a field named " + sortFieldName);
        } catch (NoSuchFieldException e) {
            Log.e("SimplifiedChineseSorter", "getSortStringField", e);
            throw new RuntimeException("The model doesn't have a field named " + sortFieldName);
        }
    }

    private static String format(String data, boolean isIgnoreCase) {
        Pattern pattern = Pattern.compile(SORTINGREGEX, Pattern.CASE_INSENSITIVE);
        if(data == null){
            return "";
        }
        else if(isIgnoreCase){
            return pattern.matcher(data.trim()).replaceAll("").toLowerCase();
        }
        else{
            return pattern.matcher(data.trim()).replaceAll("");
        }
    }
}
