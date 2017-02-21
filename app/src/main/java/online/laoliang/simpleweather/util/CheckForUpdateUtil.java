package online.laoliang.simpleweather.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

public class CheckForUpdateUtil {

    private static final String firToken = "343ad971b9278143e9787f07895ee09c";

    private static AlertDialog alert = null;
    private static AlertDialog.Builder builder = null;
    private static ProgressDialog progressDialog;

    public static void checkForUpdateInFIR(final Context context, final boolean isAuto) {

        FIR.checkForUpdateInFIR(firToken, new VersionCheckCallback() {
            @SuppressLint("InlinedApi")
            @Override
            public void onSuccess(String versionJson) {

                try {
                    JSONObject jsonObject = new JSONObject(versionJson);
                    int version = jsonObject.getInt("version");
                    String changelog = jsonObject.getString("changelog");
                    String versionShort = jsonObject.getString("versionShort");
                    String build = jsonObject.getString("build");
                    final String direct_install_url = jsonObject.getString("direct_install_url");
                    //final String update_url = jsonObject.getString("update_url");
                    final String update_url = "http://www.coolapk.com/apk/online.laoliang.simpleweather";
                    JSONObject binary = jsonObject.getJSONObject("binary");
                    int fsize = binary.getInt("fsize") / 1024;

                    if (version > getLocalVersion(context)) {
                        alert = null;
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                        alert = builder.setTitle("检查到新版本：" + versionShort).setMessage(changelog + "\nBuild：" + build + "\n文件大小：" + fsize + "KB").setNeutralButton("以后再说", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("查看详情", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(update_url));
                                context.startActivity(intent);

                            }
                        }).setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(direct_install_url));
                                context.startActivity(intent);

                            }
                        }).create(); // 创建AlertDialog对象
                        alert.show(); // 显示对话框
                    } else {
                        if (!isAuto) {
                            ToastUtil.showToast(context, "已是最新版本", Toast.LENGTH_SHORT);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Exception exception) {
                if (!isAuto) {
                    ToastUtil.showToast(context, "网络慢  请重试一次", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onStart() {
                if (!isAuto) {
                    showProgressDialog(context);
                }
            }

            @Override
            public void onFinish() {
                if (!isAuto) {
                    closeProgressDialog();
                }
            }
        });

    }

    /**
     * 获取应用当前版本号
     *
     * @param context
     * @return
     */
    private static int getLocalVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 显示进度对话框
     *
     * @param context
     */
    private static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
