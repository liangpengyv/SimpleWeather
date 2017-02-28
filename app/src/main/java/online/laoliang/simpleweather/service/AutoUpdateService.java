package online.laoliang.simpleweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.Collection;
import java.util.HashMap;

import online.laoliang.simpleweather.receiver.AutoUpdateReceiver;
import online.laoliang.simpleweather.util.HttpCallbackListener;
import online.laoliang.simpleweather.util.HttpUtil;
import online.laoliang.simpleweather.util.Utility;

public class AutoUpdateService extends Service {

    private int anHour;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                updateWeather();
            }

        }).start();

        if (intent != null && intent.getIntExtra("anHour", 2) != -1) {
            anHour = intent.getIntExtra("anHour", 2) * 60 * 60 * 1000;
        } else {
            SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
            int temp = prefs.getInt("update_frequency", 1);
            switch (temp) {
                case 0:
                    anHour = 1 * 60 * 60 * 1000;
                    break;
                case 1:
                    anHour = 2 * 60 * 60 * 1000;
                    break;
                case 2:
                    anHour = 5 * 60 * 60 * 1000;
                    break;
                case 3:
                    anHour = 8 * 60 * 60 * 1000;
                    break;
                default:
                    break;
            }
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭自动更新服务时，需要将定时启动更新服务的定时器取消掉
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = getSharedPreferences("data_city", MODE_PRIVATE);
        @SuppressWarnings("unchecked") HashMap<String, String> temp = (HashMap<String, String>) prefs.getAll();
        Collection<String> temp_1 = temp.values();
        Object[] weatherCodes = temp_1.toArray();
        for (int i = 0; i < weatherCodes.length; i++) {
            final String weatherCode = weatherCodes[i].toString();
            String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;

            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

                @Override
                public void onFinish(String response) {
                    Utility.handleWeatherResponse(AutoUpdateService.this, response);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

}
