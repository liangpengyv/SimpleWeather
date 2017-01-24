package online.laoliang.simpleweather.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtils {

    /**
     * 进行屏幕截图
     *
     * @param pActivity
     * @return
     */
    public static Bitmap takeScreenShot(Activity pActivity) {
        Bitmap bitmap = null;
        View view = pActivity.getWindow().getDecorView();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        bitmap = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;

        @SuppressWarnings("deprecation") int width = pActivity.getWindowManager().getDefaultDisplay().getWidth();
        @SuppressWarnings("deprecation") int height = pActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
        return bitmap;
    }

    /**
     * 保存图片到SD卡指定目录
     *
     * @param pBitmap
     * @param strName
     * @return
     */
    private static boolean savePic(Bitmap pBitmap, String strName) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strName);
            if (null != fos) {
                pBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 传入上下文和图片保存路径， 截图并保存成功，则返回true
     *
     * @param pActivity
     * @param strName
     * @return
     */
    public static boolean shotBitmap(Activity pActivity, String strName) {
        return ScreenShotUtils.savePic(takeScreenShot(pActivity), strName);
    }

}