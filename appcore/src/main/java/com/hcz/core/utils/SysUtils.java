package com.hcz.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * 基础信息类
 * 用于获取手机的基础信息
 */
public class SysUtils {
	private static final String OS = "android";

    private static String mManufacturer;
    private static String mModel;
    private static String mOs;
    private static long mTotalMemory;
    private static int mCpuCoreCount;
    private static long mCpuFrequence;

	/**
	 * 手机厂商
	 * @return
	 */
	public static String getManufacturer(){
        if(EmptyUtils.isEmpty(mManufacturer)) {
            mManufacturer = Build.MANUFACTURER;
        }
        return mManufacturer;
	}
	
	/**
	 * 手机型号
	 * @return
	 */
	public static String getModel(){
        if(EmptyUtils.isEmpty(mModel)) {
            mModel = Build.MODEL;
        }
        return mModel;
	}

    /**
     * 系统版本
     * @return   ps: android 4.1
     */
	public static String getOs(){
        if(EmptyUtils.isEmpty(mOs)) {
            mOs = OS + " " + Build.VERSION.RELEASE;
        }
        return mOs;
	}

    /**
     * 手机分辨率
     * @param context
     * @return   ps: 320x480
     */
	public static String getDisplay(Context context){
		return DisplayUtils.getDisplayW(context) + "x" + DisplayUtils.getDisplayH(context);
	}


	/**
	 * 获取可用内存
	 * @param context
	 * @return
	 */
	public static long getAvailMemory(Context context){
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		activityManager.getMemoryInfo(mi);
		return mi.availMem;
	}

    /**
     * 获取总内存大小
     * @param context
     * @return
     */
	public static long getTotalMemory(Context context) {
        if(mTotalMemory == 0) {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory = 0;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(
                        localFileReader, 8192);
                str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
                arrayOfString = str2.split("\\s+");
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close();
            } catch (IOException e) {
                Log.e("", "", e);
            }
            mTotalMemory = initial_memory;// Byte转换为KB或者MB，内存大小规格化
        }
        return mTotalMemory;
	}
	
	
	/**
	 * CPU核心数
	 * @return
	 */
	public static int getCpuCoreCount() {
        if(mCpuCoreCount == 0) {
            try {
                File dir = new File("/sys/devices/system/cpu/");
                File[] files = dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                            return true;
                        }
                        return false;
                    }
                });
                mCpuCoreCount = files.length;
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }
        return mCpuCoreCount;
	}
	
	/**
	 * CPU最大频率
	 * @return
	 */
	public static long getCpuFrequence() {
        if(mCpuFrequence == 0) {
            ProcessBuilder cmd;
            try {
                String[] args = {"/system/bin/cat",
                        "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
                cmd = new ProcessBuilder(args);
                Process process = cmd.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                String line = reader.readLine().trim();
                mCpuFrequence = Long.parseLong(line) * 1024;
            } catch (Exception ex) {
                Log.e("", "", ex);
            }
        }
		return mCpuFrequence;
	}

    /**
     * 将基础信息转为json
     * @param context
     * @return
     */
	public static JSONObject getJSON(Context context){
		JSONObject object = new JSONObject();
		try {
			object.put("manufacturer", getManufacturer());
			object.put("model", getModel());
			object.put("os", getOs());
			object.put("display", getDisplay(context));
			object.put("net", NetUtils.getNetStateStr(context));
			object.put("memory", getAvailMemory(context));
			object.put("cpuNums", getCpuCoreCount());
			object.put("cpuFrequence", getCpuFrequence());
		} catch (JSONException e) {
			Log.e("", "", e);
		}
		return object;
	}

}
