package online.laoliang.simpleweather.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import online.laoliang.simpleweather.R;
import online.laoliang.simpleweather.activity.WeatherActivity;
import online.laoliang.simpleweather.provider.WidgetProvider;
import online.laoliang.simpleweather.util.Lunar;

public class TimerService extends Service {

    // RemoteView对象
    public RemoteViews rv;

    // 组件名
    public ComponentName cn;
    private ComponentName componentName = null;
    private ComponentName componentName1 = new ComponentName("com.android.deskclock", "com.android.deskclock.DeskClock");
    private ComponentName componentName2 = new ComponentName("com.android.deskclock", "com.android.deskclock.AlarmsMainActivity");
    private ComponentName componentName3 = new ComponentName("com.android.deskclock", "com.android.deskclock.DeskClockTabActivity");
    private ComponentName componentName4 = new ComponentName("com.android.alarmclock", "com.android.alarmclock.DeskClock");
    private ComponentName componentName5 = new ComponentName("com.android.alarmclock", "com.meizu.flyme.alarmclock.DeskClock");
    private ComponentName componentName6 = new ComponentName("com.google.android.deskclock", "com.android.deskclock.DeskClock");

    // AppWidget管理器
    public AppWidgetManager manager;

    // 定义定时器
    private Timer timer;

    // 定义格式化日期时间
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("HH : mm-MM / dd   E");

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        findClock();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                updateViews();
            }
        }, 0, 1000);
    }

    private void findClock() {
        try {
            getPackageManager().getResourcesForActivity(componentName1);
            componentName = componentName1;
        } catch (PackageManager.NameNotFoundException e1) {
            try {
                getPackageManager().getResourcesForActivity(componentName2);
                componentName = componentName2;
            } catch (PackageManager.NameNotFoundException e2) {
                try {
                    getPackageManager().getResourcesForActivity(componentName3);
                    componentName = componentName3;
                } catch (PackageManager.NameNotFoundException e3) {
                    try {
                        getPackageManager().getResourcesForActivity(componentName4);
                        componentName = componentName4;
                    } catch (PackageManager.NameNotFoundException e4) {
                        try {
                            getPackageManager().getResourcesForActivity(componentName5);
                            componentName = componentName5;
                        } catch (PackageManager.NameNotFoundException e5) {
                            try {
                                getPackageManager().getResourcesForActivity(componentName6);
                                componentName = componentName6;
                            } catch (PackageManager.NameNotFoundException e6) {
                                e5.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateViews() {

        rv = new RemoteViews(getPackageName(), R.layout.widget_1);
        cn = new ComponentName(getApplicationContext(), WidgetProvider.class);
        manager = AppWidgetManager.getInstance(getApplicationContext());

        // 为天气图标视图注册监听事件，打开简约天气
        Intent intent_1 = new Intent(this, WeatherActivity.class);
        PendingIntent pendingIntent_1 = PendingIntent.getActivity(this, 0, intent_1, 0);
        rv.setOnClickPendingIntent(R.id.widget1_weather_icon, pendingIntent_1);

        // 为时间视图注册监听事件，打开系统时钟
        Intent intent_2 = new Intent();
        intent_2.setComponent(componentName);
        PendingIntent pendingIntent_2 = PendingIntent.getActivity(this, 0, intent_2, 0);
        rv.setOnClickPendingIntent(R.id.widget1_time, pendingIntent_2);

        String str = sdf.format(new Date());
        // 当前时间
        String time = str.substring(0, 7);
        // 当前日期
        Lunar lunar = new Lunar(Calendar.getInstance());
        String date = str.substring(8) + "   |   " + lunar.toString().substring(5);

        SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
        editor.commit();
        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);

        rv.setTextViewText(R.id.widget1_time, time);
        rv.setTextViewText(R.id.widget1_date, date);
        if (prefs.getString("nonce_city", null) == null) {
            int weather_icon = R.mipmap.not_applicable;
            rv.setImageViewResource(R.id.widget1_weather_icon, weather_icon);
            rv.setTextViewText(R.id.widget1, "一个城市也木有~");
        } else {
            // 城市名
            String city_name = prefs.getString("nonce_city", null);
            prefs = getSharedPreferences(city_name, MODE_PRIVATE);
            if (prefs.getString("type_0", null) == null) {
                int weather_icon = R.mipmap.not_applicable;
                rv.setImageViewResource(R.id.widget1_weather_icon, weather_icon);
                rv.setTextViewText(R.id.widget1, "一个城市也木有~");
            } else {
                // 天气类型
                String weather_type = prefs.getString("type_0", null);
                // 当前温度
                String weather_wendu = prefs.getString("wendu", null);
                // 天气信息汇总
                String weather = city_name + " | " + weather_type + " " + weather_wendu + "°";
                // 天气图标
                int weather_icon = WeatherActivity.selectImage(weather_type);
                rv.setImageViewResource(R.id.widget1_weather_icon, weather_icon);
                rv.setTextViewText(R.id.widget1, weather);
            }

        }
        manager.updateAppWidget(cn, rv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }

}
