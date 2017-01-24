package online.laoliang.simpleweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import online.laoliang.simpleweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 因为定义了开机接收启动后台更新服务的广播
        // 所以要先检测是否打开了后台更新服务
        // 再决定是否发送启动后台更新服务的Intent命令
        // 至于这里为什么先要创建一个“data_setting”
        // 是为了防止用户安装完应用直接重启手机的极端情况
        // 哎，强迫症，想的也是蛮细的，哈哈哈！！！
        // 对了，这些操作，虽然很多，但也不会超过5秒
        SharedPreferences.Editor editor = context.getSharedPreferences("data_setting", Context.MODE_PRIVATE).edit();
        editor.commit();
        SharedPreferences prefs = context.getSharedPreferences("data_setting", Context.MODE_PRIVATE);
        boolean isBackUpdate = prefs.getBoolean("back_update", false);
        if (isBackUpdate) {
            Intent i = new Intent(context, AutoUpdateService.class);
            i.putExtra("anHour", -1);
            context.startService(i);
        }

    }

}
