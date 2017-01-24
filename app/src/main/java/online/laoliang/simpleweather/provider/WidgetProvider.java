package online.laoliang.simpleweather.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import online.laoliang.simpleweather.service.TimerService;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收开机和启动程序时候的广播时执行
        super.onReceive(context, intent);
        context.startService(new Intent(context, TimerService.class));
    }

    @Override
    public void onEnabled(Context context) {
        // Widget添加到屏幕时执行
        super.onEnabled(context);
        context.startService(new Intent(context, TimerService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Widget更新时执行，需要通过remoteViews和AppWidgetManager
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Widget从屏幕移除时执行
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        // 最后一次Widget从屏幕移除时执行
        super.onDisabled(context);
        context.stopService(new Intent(context, TimerService.class));
    }

}
