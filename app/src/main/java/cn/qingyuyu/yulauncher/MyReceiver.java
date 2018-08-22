package cn.qingyuyu.yulauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    MainActivityInterface mai;
    public MyReceiver(MainActivityInterface mai){
        this.mai=mai;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断收到的是什么广播
        String action = intent.getAction();
        //获取安装更新卸载的是什么应用
        Uri uri = intent.getData();
        if(Intent.ACTION_PACKAGE_ADDED.equals(action)){
            Toast.makeText(context, uri + "被安装了", Toast.LENGTH_SHORT).show();
            mai.update();
        }
        else if(Intent.ACTION_PACKAGE_REMOVED.equals(action)){
            Toast.makeText(context, uri + "被删除了", Toast.LENGTH_SHORT).show();
            mai.update();
        }
    }
}
