package com.ikrok.systemapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainLayoutActivity extends ActionBarActivity {

    @InjectView (R.id.list_item)
    ListView listView;
    private ArrayList<AppInformation> applist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.inject(this);
        applist = getInstalledAppList(getAllApplicationList());
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this, applist);
        listView.setAdapter(arrayAdapter);
        wifiLock();
    }

    private void wifiLock() {
        WifiManager wManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wManager.setWifiEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_copy) {
            copyToClipboard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void copyToClipboard() {
        String list = appListToString(applist);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(list);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",list);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, "Copy to clipboard", Toast.LENGTH_SHORT).show();
    }

    private String appListToString(ArrayList<AppInformation> applist) {
        StringBuffer stringBuffer = new StringBuffer();
        for (AppInformation information : applist) {
            stringBuffer.append(information.toString());
        }
        return stringBuffer.toString();
    }

    private ArrayList<String> getAppList() {
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<String> apps = new ArrayList<String>();
        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packs = packageManager.getInstalledPackages(1);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            if(!((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1))
            {
                continue;
            }
            apps.add(p.packageName);
        }
        return apps;
    }

    private ArrayList<String> getAllApplicationList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<String> apps = new ArrayList<String>();
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (Object object : pkgAppsList) {
            ResolveInfo info = (ResolveInfo) object;
            String strPackageName = info.activityInfo.applicationInfo.packageName;
            apps.add(strPackageName);
        }
        return apps;
    }

    ArrayList<AppInformation> getInstalledAppList(ArrayList<String> systemApps) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<AppInformation> apps = new ArrayList<AppInformation>();
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (Object object : pkgAppsList) {
            ResolveInfo info = (ResolveInfo) object;
            AppInformation appInformation = new AppInformation();
            Drawable icon = getBaseContext().getPackageManager().getApplicationIcon(info.activityInfo.applicationInfo);
            appInformation.setIcon(icon);
            String strAppName = info.activityInfo.applicationInfo.publicSourceDir;

            String strPackageName = info.activityInfo.applicationInfo.packageName;
            appInformation.setPackageName(strPackageName);
            final String title = (String) ((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");
            appInformation.setAppName(title);

            if(systemApps.contains(strPackageName)){
                apps.add(appInformation);
            }

        }
        return apps;
    }
}
