package cn.qingyuyu.yulauncher;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements MainActivityInterface{
    private GridView gview;
    List<AppInfo> appList = null;
    MyGridViewAdapter adapter;
    private MyReceiver myReceiver=null;
    TextView info;
    String ip="unknow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gview =  findViewById(R.id.gview);
        appList = Util.getAppInfoList(MainActivity.this);
        setView();

        Util.machineId = Build.SERIAL;//唯一号

        info=findViewById(R.id.info);

    }

    @Override
    protected void onStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NetworkInterface networkInterface = getActiveNetwork();
                    if (networkInterface != null) {
                       ip=" IP:"+getIpAddress(networkInterface, true);
                    }
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        Log.e("wwwwwwww",""+Settings.canDrawOverlays(MainActivity.this));
                                        if (Settings.canDrawOverlays(MainActivity.this)) {
                                            Intent floatService = new Intent(MainActivity.this, FloatService.class);
                                            startService(floatService);//启动服务
                                        } else {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                            startActivity(intent);
                                        }
                                    }
                                    myReceiver = new MyReceiver(MainActivity.this);
                                    IntentFilter filter = new IntentFilter();

                                    filter.addAction("android.intent.action.PACKAGE_ADDED");
                                    filter.addAction("android.intent.action.PACKAGE_REMOVED");
                                    filter.addDataScheme("package");
                                   MainActivity. this.registerReceiver(myReceiver, filter);

                                }
                            }
                    );
                    while(true)
                    {
                        Thread.sleep(1000);
                        File f=new File("/sys/class/thermal/thermal_zone0/temp");
                        InputStream is=new FileInputStream(f);
                        byte[] b=new byte[]{0,0,'.',0,0,0};
                        is.read(b);
                        b[5]=b[4];
                        b[4]=b[3];
                        b[3]=b[2];
                        b[2]='.';
                        String s=new String(b);
                        final float temp=Float.parseFloat(s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                info.setText(ip+" temp:"+temp);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onStart();
    }
    private static NetworkInterface getActiveNetwork() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (interfaces != null && !interfaces.isEmpty()) {
                int count = interfaces.size();
                for (int i = 0; i < count; i++) {
                    NetworkInterface networkInterface = interfaces.get(i);
                    if (networkInterface.isUp() && (networkInterface.getName().contains("wlan") || networkInterface.getName().contains("eth"))) {
                        return networkInterface;
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return null;
    }
    private String getIpAddress(NetworkInterface networkInterface, boolean useIPv4) {
        List<InetAddress> addrs = Collections.list(networkInterface.getInetAddresses());
        for (InetAddress addr : addrs) {
            if (!addr.isLoopbackAddress()) {
                String sAddr = addr.getHostAddress();
                //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                boolean isIPv4 = sAddr.indexOf(':') < 0;

                if (useIPv4) {
                    if (isIPv4)
                        return sAddr;
                } else {
                    if (!isIPv4) {
                        int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                        return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                    }
                }
            }
        }
        return null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            Intent floatservice = new Intent(this, FloatService.class);
            stopService(floatservice);//停止服务
            finish();
            return true;
        }
        if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
            intent.putExtra(Intent.EXTRA_TEXT, "Android things开发者桌面 http://blog.qingyuyu.cn/");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));
            return true;
        } else if (id == R.id.nav_feedback) {
            Intent i=new Intent(MainActivity.this,WebActivity.class);
            i.putExtra("url",  "http://blog.qingyuyu.cn/read.php/yulauncher-feedback/" );
            startActivity(i);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setView() {

         adapter = new MyGridViewAdapter(MainActivity.this, appList);
        gview.setAdapter(adapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Util.openApp(MainActivity.this, appList.get(position).getPackageName()) == false) {
                    Toast.makeText(MainActivity.this, "run error！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext());
                ab.setTitle("").setIcon(R.mipmap.ic_launcher).setMessage("what are you want to do?")
                        .setNegativeButton("uninstall app", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent uninstall_intent = new Intent();
                                uninstall_intent.setAction(Intent.ACTION_DELETE);
                                uninstall_intent.setData(Uri.parse("package:" + appList.get(position).getPackageName()));
                                startActivity(uninstall_intent);
                            }
                        }).setPositiveButton("stop app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(appList.get(position).getPackageName());//给目标app发广播
                        i.putExtra("cmd", "stop");
                        sendBroadcast(i);
                    }
                })
                        .show();
                return true;
            }
        });
    }

    @Override
    public void update() {
        appList.clear();
        appList=Util.getAppInfoList(MainActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}

