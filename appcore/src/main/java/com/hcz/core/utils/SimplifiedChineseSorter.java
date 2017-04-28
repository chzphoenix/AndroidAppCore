package com.hcz.core.utils;

import android.util.Log;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public static <T> List<T> sortByProvider(List<T> items, SortStringProvider<T> provider) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        return sortList(items, provider);
    }

    public static <T> List<T> sortByFieldName(List<T> items, String fieldName, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        Field field = getSortStringField(items.get(0).getClass(), fieldName);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(field, isIgnoreCase);
        return sortList(items, provider);
    }

    public static <T> List<T> sortByFieldAnnotation(List<T> items, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        Field field = getSortStringField(items.get(0).getClass());
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(field, isIgnoreCase);
        return sortList(items, provider);
    }

    public static <T> List<T> sortByMethodName(List<T> items, String methodName, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        Method method = getSortStringMethod(items.get(0).getClass(), methodName);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(method, isIgnoreCase);
        return sortList(items, provider);
    }

    public static <T> List<T> sortByMethodAnnotation(List<T> items, boolean isIgnoreCase) {
        if (items == null || items.size() <= 0) {
            return null;
        }
        Method method = getSortStringMethod(items.get(0).getClass());
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(method, isIgnoreCase);
        return sortList(items, provider);
    }

    private static <T> List<T> sortList(List<T> items, final SortStringProvider<T> provider) {
        if(provider == null){
            return items;
        }
        final List<T> chinieseList = new ArrayList<T>();
        final List<T> nonChineseList = new ArrayList<T>();
        for (T item : items) {
            if (isChineseCharStart(provider.getSortString(item))) {
                chinieseList.add(item);
            } else {
                nonChineseList.add(item);
            }
        }
        List<T> sortedChineseList = Ordering.from(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return stringComparator.compare(provider.getSortString(lhs), provider.getSortString(rhs));
            }
        }).sortedCopy(chinieseList);
        List<T> sortedNonChineseList = Ordering.from(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return provider.getSortString(lhs).compareTo(provider.getSortString(rhs));
            }
        }).sortedCopy(nonChineseList);
        sortedChineseList.addAll(sortedNonChineseList);
        return sortedChineseList;
    }

    public static <T> Comparator<T> getSortComparatorByProvider(final Class clazz, SortStringProvider<T> provider) {
        return getSortComparator(provider);
    }

    public static <T> Comparator<T> getSortComparatorByFieldName(final Class clazz, final String fieldName, boolean isIgnoreCase) {
        Field field = getSortStringField(clazz, fieldName);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(field, isIgnoreCase);
        return getSortComparator(provider);
    }

    public static <T> Comparator<T> getSortComparatorByFieldAnnotation(final Class clazz, boolean isIgnoreCase) {
        Field field = getSortStringField(clazz);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(field, isIgnoreCase);
        return getSortComparator(provider);
    }

    public static <T> Comparator<T> getSortComparatorByMethodName(final Class clazz, final String methodName, boolean isIgnoreCase) {
        Method method = getSortStringMethod(clazz, methodName);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(method, isIgnoreCase);
        return getSortComparator(provider);
    }

    public static <T> Comparator<T> getSortComparatorByMethodAnnotation(final Class clazz, boolean isIgnoreCase) {
        Method method = getSortStringMethod(clazz);
        DefualtSortStringProvider<T> provider = new DefualtSortStringProvider<T>(method, isIgnoreCase);
        return getSortComparator(provider);
    }

    private static <T> Comparator<T> getSortComparator(final SortStringProvider<T> provider) {
        return new Comparator<T>() {
            @Override
            public int compare(final T left, final T right) {
                String leftStr = provider.getSortString(left);
                String rightStr = provider.getSortString(right);
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
                    field.setAccessible(true);
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
                field.setAccessible(true);
                return field;
            }
            throw new RuntimeException("The model doesn't have a field named " + sortFieldName);
        } catch (NoSuchFieldException e) {
            Log.e("SimplifiedChineseSorter", "getSortStringField", e);
            throw new RuntimeException("The model doesn't have a field named " + sortFieldName);
        }
    }

    private static <T> Method getSortStringMethod(Class<T> tClass) {
        Method[] methods = tClass.getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(SortString.class) && method.getReturnType() == String.class) {
                    method.setAccessible(true);
                    return method;
                }
            }
        }
        throw new RuntimeException("The model doesn't have a @SortString method or the returnning type of @SortString method is not a String");
    }

    private static <T> Method getSortStringMethod(Class<T> tClass, String sortMethodName) {
        try {
            Method method = tClass.getDeclaredMethod(sortMethodName);
            if (method != null && method.getReturnType() == String.class) {
                method.setAccessible(true);
                return method;
            }
            throw new RuntimeException("The model doesn't have a method named " + sortMethodName);
        } catch (NoSuchMethodException e) {
            Log.e("SimplifiedChineseSorter", "getSortStringMethod", e);
            throw new RuntimeException("The model doesn't have a method named " + sortMethodName);
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

    static class DefualtSortStringProvider<T> implements SortStringProvider<T>{
        private Object orderBy;
        private boolean isIgnoreCase;

        DefualtSortStringProvider(Object orderBy, boolean isIgnoreCase){
            this.orderBy = orderBy;
            this.isIgnoreCase = isIgnoreCase;
        }

        public String getSortString(T obj){
            try {
                if (orderBy instanceof Field) {
                    return format((String) ((Field) orderBy).get(obj), isIgnoreCase);
                } else if (orderBy instanceof Method) {
                    return format((String) ((Method)orderBy).invoke(obj), isIgnoreCase);
                }
            }
            catch (Exception e){
                Log.e("SimplifiedChineseSorter", "getSortString", e);
            }
            return "";
        }
    }

    interface SortStringProvider<T>{
        String getSortString(T obj);
    }
}
