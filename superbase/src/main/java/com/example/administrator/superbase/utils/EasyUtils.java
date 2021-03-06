package com.example.administrator.superbase.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Environment;

import com.example.administrator.superbase.utils.common.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Created by v_chicunxiang on 2018/2/2.
 *
 * @史上最帅无敌创建者 ccx
 * @创建时间 2018/2/2 15:12
 */

public class EasyUtils {
    public static final String                    TAG           = "EasyUtils";
    private static      Hashtable<String, String> resourceTable = new Hashtable();

    public EasyUtils() {
    }

    public static boolean isAppRunningForeground(Context var0) {
        @SuppressLint("WrongConstant") ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");

        try {
            List var2 = var1.getRunningTasks(1);
            if(var2 != null && var2.size() >= 1) {
                boolean var3 = var0.getPackageName().equalsIgnoreCase(((RunningTaskInfo)var2.get(0)).baseActivity.getPackageName());
                LogUtil.d("utils", "app running in foregroud：" + var3);
                return var3;
            } else {
                return false;
            }
        } catch (SecurityException var4) {
            LogUtil.d("EasyUtils", "Apk doesn't hold GET_TASKS permission");
            var4.printStackTrace();
            return false;
        }
    }

    public static String getTopActivityName(Context var0) {
        @SuppressLint("WrongConstant") ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");

        try {
            List var2 = var1.getRunningTasks(1);
            return var2 != null && var2.size() >= 1?((RunningTaskInfo)var2.get(0)).topActivity.getClassName():"";
        } catch (SecurityException var3) {
            LogUtil.d("EasyUtils", "Apk doesn't hold GET_TASKS permission");
            var3.printStackTrace();
            return "";
        }
    }

    public static boolean isSingleActivity(Context var0) {
        @SuppressLint("WrongConstant") ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
        List var2 = null;

        try {
            var2 = var1.getRunningTasks(1);
        } catch (SecurityException var4) {
            var4.printStackTrace();
        }

        return var2 != null && var2.size() >= 1?((RunningTaskInfo)var2.get(0)).numRunning == 1:false;
    }

    public static List<String> getRunningApps(Context var0) {
        ArrayList var1 = new ArrayList();

        try {
            @SuppressLint("WrongConstant") ActivityManager var2 = (ActivityManager)var0.getSystemService("activity");
            List var3 = var2.getRunningAppProcesses();
            if(var3 == null) {
                return var1;
            }

            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
                ActivityManager.RunningAppProcessInfo var5 = (ActivityManager.RunningAppProcessInfo)var4.next();
                String var6 = var5.processName;
                if(var6.contains(":")) {
                    var6 = var6.substring(0, var6.indexOf(":"));
                }

                if(!var1.contains(var6)) {
                    var1.add(var6);
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return var1;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getTimeStamp() {
        Date var0 = new Date(System.currentTimeMillis());
        SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmss");
        return var1.format(var0);
    }

    public static boolean writeToZipFile(byte[] var0, String var1) {
        FileOutputStream var2 = null;
        GZIPOutputStream var3 = null;

        label110: {
            boolean var5;
            try {
                var2 = new FileOutputStream(var1);
                var3 = new GZIPOutputStream(new BufferedOutputStream(var2));
                var3.write(var0);
                break label110;
            } catch (Exception var20) {
                var20.printStackTrace();
                var5 = false;
            } finally {
                if(var3 != null) {
                    try {
                        var3.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }
                }

                if(var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }

            }

            return var5;
        }

        if(LogUtil.isDebug()) {
            File var4 = new File(var1);
            DecimalFormat var22 = new DecimalFormat("#.##");
            double var6 = (double)var4.length() / (double)var0.length * 100.0D;
            double var8 = Double.valueOf(var22.format(var6)).doubleValue();
            LogUtil.d("zip", "data size:" + var0.length + " zip file size:" + var4.length() + "zip file ratio%: " + var8);
        }

        return true;
    }

    public static String getAppResourceString(Context var0, String var1) {
        String var2 = (String)resourceTable.get(var1);
        if(var2 != null) {
            return var2;
        } else {
            int var3 = var0.getResources().getIdentifier(var1, "string", var0.getPackageName());
            var2 = var0.getString(var3);
            if(var2 != null) {
                resourceTable.put(var1, var2);
            }

            return var2;
        }
    }

    public static String convertByteArrayToString(byte[] var0) {
        StringBuilder var1 = new StringBuilder();
        byte[] var2 = var0;
        int var3 = var0.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            var1.append(String.format("0x%02X", new Object[]{Byte.valueOf(var5)}));
        }

        return var1.toString();
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean copyFile(String var0, String var1) {
        boolean var2 = true;

        try {
            int var3 = 0;
            boolean var4 = false;
            File var5 = new File(var0);
            if(var5.exists()) {
                FileInputStream var6 = new FileInputStream(var0);
                FileOutputStream var7 = new FileOutputStream(var1);
                byte[] var8 = new byte[1024];

                int var10;
                while((var10 = var6.read(var8)) != -1) {
                    var3 += var10;
                    var7.write(var8, 0, var10);
                }

                var7.flush();
                var7.close();
                var6.close();
            } else {
                var2 = false;
            }
        } catch (Exception var9) {
            var2 = false;
        }

        return var2;
    }

    public static boolean copyFolder(String var0, String var1) {
        boolean var2 = true;

        try {
            (new File(var1)).mkdirs();
            File var3 = new File(var0);
            String[] var4 = var3.list();
            File var5 = null;
            String[] var6 = var4;
            int var7 = var4.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String var9 = var6[var8];
                if(var0.endsWith(File.separator)) {
                    var5 = new File(var0 + var9);
                } else {
                    var5 = new File(var0 + File.separator + var9);
                }

                if(var5.isFile()) {
                    FileInputStream var10 = new FileInputStream(var5);
                    FileOutputStream var11 = new FileOutputStream(var1 + "/" + var5.getName());
                    byte[] var12 = new byte[5120];

                    int var13;
                    while((var13 = var10.read(var12)) != -1) {
                        var11.write(var12, 0, var13);
                    }

                    var11.flush();
                    var11.close();
                    var10.close();
                }

                if(var5.isDirectory()) {
                    copyFolder(var0 + "/" + var9, var1 + "/" + var9);
                }
            }
        } catch (Exception var14) {
            var2 = false;
        }

        return var2;
    }
}
