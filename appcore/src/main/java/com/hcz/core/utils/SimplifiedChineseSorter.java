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

    public static <T> List<T> sortByFieldName(List<T> items, String fieldName) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        return sortByField(items, getSortStringField(items.get(0).getClass(), fieldName));
    }

    public static <T> List<T> sortByFieldAnnotation(List<T> items) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        return sortByField(items, getSortStringField(items.get(0).getClass()));
    }

    private static <T> List<T> sortByField(List<T> items, final Field sortField) {
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
        final Pattern pattern = Pattern.compile(SORTINGREGEX, Pattern.CASE_INSENSITIVE);
        List<T> sortedChineseList = Ordering.from(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                try {
                    return stringComparator.compare(toLowerCase(pattern.matcher((String) sortField.get(lhs)).replaceAll("")),
                            toLowerCase(pattern.matcher((String) sortField.get(rhs)).replaceAll("")));
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
                    return toLowerCase(pattern.matcher((String) sortField.get(lhs)).replaceAll(""))
                                  .compareTo(toLowerCase(pattern.matcher((String) sortField.get(rhs)).replaceAll("")));
                } catch (IllegalAccessException e) {
                    Log.e("SimplifiedChineseSorter", "sortedNonChineseList", e);
                    return 0;
                }
            }
        }).sortedCopy(nonChineseList);
        sortedChineseList.addAll(sortedNonChineseList);
        return sortedChineseList;
    }

    public static <T> Comparator<T> getSortComparatorByFieldName(final Class clazz, final String fieldName) {
        return getSortComparator(getSortStringField(clazz, fieldName));
    }

    public static <T> Comparator<T> getSortComparatorByFieldAnnotation(final Class clazz) {
        return getSortComparator(getSortStringField(clazz));
    }

    private static <T> Comparator<T> getSortComparator(final Field sortField) {
        sortField.setAccessible(true);
        return new Comparator<T>() {
            @Override
            public int compare(final T left, final T right) {
                try {
                    String leftStr = (String) sortField.get(left);
                    String rightStr = (String) sortField.get(right);
                    if (SimplifiedChineseSorter.isChineseCharStart(leftStr) &&
                            SimplifiedChineseSorter.isChineseCharStart(rightStr)) {
                        return stringComparator.compare(toLowerCase(leftStr),
                                toLowerCase(rightStr));
                    } else {
                        return ComparisonChain.start()
                                              .compareTrueFirst(SimplifiedChineseSorter.isChineseCharStart(leftStr),
                                                      SimplifiedChineseSorter.isChineseCharStart(rightStr))
                                              .compare(toLowerCase(leftStr),
                                                      toLowerCase(rightStr),
                                                      Ordering.natural().nullsFirst())
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

    private static String toLowerCase(String data) {
        return data != null ? data.trim().toLowerCase() : "";
    }
}
