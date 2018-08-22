package cn.qingyuyu.yulauncher;
import android.graphics.drawable.*;

public class AppInfo
{
	private Drawable icon;
	private String appName;
	private String packageName;

	public void setIcon(Drawable icon)
	{
		this.icon = icon;
	}

	public Drawable getIcon()
	{
		return icon;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getPackageName()
	{
		return packageName;
	}
	


	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getAppName()
	{
		return appName;
	}
	
	}
