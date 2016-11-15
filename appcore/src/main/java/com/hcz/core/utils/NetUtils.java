package com.hcz.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 网络工具类
 * 
 * @author David_kun
 * 
 */
public class NetUtils {
    private static final String NO_NET_STR = "nonet";
    private static final String WIFI_STR = "wifi";
    private static final String NET_2G_STR = "2g";
    private static final String NET_3G_STR = "3g";
    private static final String NET_4G_STR = "4g";
    private static final String NET_UNKNOWN_STR = "unknown";
    private static final int NO_NET = 0;
    private static final int WIFI = 1;
    private static final int NET_2G = 2;
    private static final int NET_3G = 3;
    private static final int NET_4G = 4;
    private static final int NET_UNKNOWN = -1;


    public static String getNetStateStr(Context context){
        switch(getNetState(context)){
            case NO_NET:
                return NO_NET_STR;
            case WIFI:
                return WIFI_STR;
            case NET_2G:
                return NET_2G_STR;
            case NET_3G:
                return NET_3G_STR;
            case NET_4G:
                return NET_4G_STR;
            default:
                return NET_UNKNOWN_STR;
        }
    }

    /**
     * 获取网络状态
     * @param context
     * @return   无网/wifi/2g/3g/4g/未知
     */
    public static int getNetState(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return NO_NET;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()){
            return NO_NET;
        }
        if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            return WIFI;
        }
        if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            return getNetworkClass(networkInfo.getSubtype());
        }
        return NET_UNKNOWN;
    }



    /** Network type is unknown */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /** Current network is GPRS */
    public static final int NETWORK_TYPE_GPRS = 1;
    /** Current network is EDGE */
    public static final int NETWORK_TYPE_EDGE = 2;
    /** Current network is UMTS */
    public static final int NETWORK_TYPE_UMTS = 3;
    /** Current network is CDMA: Either IS95A or IS95B*/
    public static final int NETWORK_TYPE_CDMA = 4;
    /** Current network is EVDO revision 0*/
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /** Current network is EVDO revision A*/
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /** Current network is 1xRTT*/
    public static final int NETWORK_TYPE_1xRTT = 7;
    /** Current network is HSDPA */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /** Current network is HSUPA */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /** Current network is HSPA */
    public static final int NETWORK_TYPE_HSPA = 10;
    /** Current network is iDen */
    public static final int NETWORK_TYPE_IDEN = 11;
    /** Current network is EVDO revision B*/
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /** Current network is LTE */
    public static final int NETWORK_TYPE_LTE = 13;
    /** Current network is eHRPD */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /** Current network is HSPA+ */
    public static final int NETWORK_TYPE_HSPAP = 15;
    /** Current network is GSM */
    public static final int NETWORK_TYPE_GSM = 16;

    /**
     * 判断手机网络的类型  2g/3g/4g/未知
     * @param networkType
     * @return    2g/3g/4g/未知
     */
    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_GSM:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NET_2G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NET_3G;
            case NETWORK_TYPE_LTE:
                return NET_4G;
            default:
                return NET_UNKNOWN;
        }
    }


	/**
	 * 检测是否接入Internet
	 * @param path  测试路径
	 * @return   网络状态
	 */
	public static boolean isAccessInternet(String path) 
	{
		try {
			URL url=new URL(path);
			HttpURLConnection  conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode()!=0)
			{
				return true;
			}
			conn.connect();
		} catch (Exception e) {
			return false;	
		}
		return false;

	}

	/**
	 * 判断用户是否联网
	 * @param act
	 * @return
	 */
	public static boolean isNetworkAvailable(Context act) {
		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

}
