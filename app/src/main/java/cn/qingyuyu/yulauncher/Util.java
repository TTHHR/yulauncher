package cn.qingyuyu.yulauncher;

import java.util.*;

import android.content.pm.*;
import android.content.*;
import android.util.*;


public class Util
{
    public static String machineId="";
   static  ArrayList<AppInfo> data_list = new ArrayList<AppInfo>();
    public static ArrayList<AppInfo> getAppInfoList(Context cont)
	{
	    if(!data_list.isEmpty())
	        data_list.clear();
		List<PackageInfo> packages = cont.getPackageManager().getInstalledPackages(0);   

		for(int i=0;i<packages.size();i++) {   

			PackageInfo packageInfo = packages.get(i);   

			AppInfo tmpInfo = new AppInfo();   

			tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(cont.getPackageManager()).toString());  

			tmpInfo.setPackageName( packageInfo.packageName);   
			tmpInfo.setIcon(packageInfo.applicationInfo.loadIcon(cont.getPackageManager()));  
			data_list.add(tmpInfo);
			}
			
			return data_list;
	}
	/** 通过包名去启动一个Activity*/
	public static boolean openApp(Context cont, String packageName) {
        // TODO 把应用杀掉然后再启动，保证进入的是第一个页面
        PackageInfo pi = null;
        try {
            pi = cont.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            Log.e("openApp",e.toString());
            return false;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = cont.getApplicationContext().getPackageManager();
        List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent,0);
        ResolveInfo ri =null;
        if(apps.iterator().hasNext())
         ri=apps.iterator().next();
        if (ri != null) {
            String startappName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Log.d("openapp","启动了"+startappName+"::"+className);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(startappName, className);

            intent.setComponent(cn);
            cont.getApplicationContext().startActivity(intent);
			return true;
        }
		return false;
    }


}
