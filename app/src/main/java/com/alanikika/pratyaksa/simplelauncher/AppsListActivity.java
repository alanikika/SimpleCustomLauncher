package com.alanikika.pratyaksa.simplelauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppsListActivity extends AppCompatActivity {

    private PackageManager manager;
    private List<AppInfo> apps;
    private GridView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        loadApps();
        loadListView();
        clickListener();
    }

    private void clickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                AppsListActivity.this.startActivity(intent);
            }
        });
    }

    private void loadListView() {
        list = (GridView) findViewById(R.id.apps_grid);

        ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>(this, R.layout.list_item, apps){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolderItem viewHolder = null;

                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                    viewHolder = new ViewHolderItem();
                    viewHolder.label = (TextView) convertView.findViewById(R.id.apps_name);
                    viewHolder.icon = (ImageView) convertView.findViewById(R.id.img_item_photo);

                    convertView.setTag(viewHolder);
                }
                else {
                    viewHolder = (ViewHolderItem) convertView.getTag();
                }

                AppInfo appInfo = apps.get(position);

                if(appInfo != null){
                    viewHolder.icon.setImageDrawable(appInfo.icon);
                    viewHolder.label.setText(appInfo.label);
                }

                return convertView;
            }

            final class ViewHolderItem {
                TextView label;
                ImageView icon;
            }
        };

        list.setAdapter(adapter);
    }

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<AppInfo>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        //Load Apps
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

        Log.d("Available Act: ", ""+availableActivities);

        for(ResolveInfo ri : availableActivities){
            AppInfo appInfo = new AppInfo();
            appInfo.label = ri.loadLabel(manager);
            appInfo.name = ri.activityInfo.packageName;
            appInfo.icon = ri.activityInfo.loadIcon(manager);

            apps.add(appInfo);
        }

        Log.d("List Apps", ""+apps);
    }
}
