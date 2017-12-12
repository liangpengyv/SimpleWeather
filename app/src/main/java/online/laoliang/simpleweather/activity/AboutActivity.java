package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import online.laoliang.simpleweather.R;
import online.laoliang.simpleweather.util.ShareUtils;

public class AboutActivity extends Activity implements OnClickListener {

    private Button back;
    private TextView title_text;
    private TextView version;
    private Button share_app;
    private Button check_welcome;
    private Button feed_back;
    private Button project_address;
    private Button my_blog;
    private Button qq_qun;

    private void findView() throws NameNotFoundException {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("关于");
        version = (TextView) findViewById(R.id.version);
        PackageManager manager = getPackageManager();
        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
        String versionCode = info.versionName;
        version.setText("Version：" + versionCode);
        share_app = (Button) findViewById(R.id.share_app);
        share_app.setOnClickListener(this);
        check_welcome = (Button) findViewById(R.id.check_welcome);
        check_welcome.setOnClickListener(this);
        feed_back = (Button) findViewById(R.id.feed_back);
        feed_back.setOnClickListener(this);
        project_address = (Button) findViewById(R.id.project_address);
        project_address.setOnClickListener(this);
        my_blog = (Button) findViewById(R.id.my_blog);
        my_blog.setOnClickListener(this);
        qq_qun = (Button) findViewById(R.id.qq_qun);
        qq_qun.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        int chooseTheme = prefs.getInt("choose_theme", 1);
        switch (chooseTheme) {
            case 1:
                setTheme(R.style.AppTheme1);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            case 11:
                setTheme(R.style.AppTheme11);
                break;
            case 12:
                setTheme(R.style.AppTheme12);
                break;
        }
        setContentView(R.layout.about_page);

        try {
            findView();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 为按键注册监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share_app:
                ShareUtils.share(null, "最简洁、最轻巧的天气软件：简约天气\n快来下载吧~\n\n点击下载：http://www.coolapk.com/apk/online.laoliang.simpleweather", this);
                break;
            case R.id.check_welcome:
                Intent intent_welcome = new Intent(this, WelcomeActivity.class);
                startActivity(intent_welcome);
                finish();
                break;
            case R.id.feed_back:
                Intent intent_feedback = new Intent(this, FeedBackACtivity.class);
                startActivity(intent_feedback);
                break;
            case R.id.project_address:
                Intent intent_project = new Intent(Intent.ACTION_VIEW);
                intent_project.setData(Uri.parse("https://github.com/liangpengyv/SimpleWeather"));
                startActivity(intent_project);
                break;
            case R.id.my_blog:
                Intent intent_blog = new Intent(Intent.ACTION_VIEW);
                intent_blog.setData(Uri.parse("http://laoliang.online/"));
                startActivity(intent_blog);
                break;
            case R.id.qq_qun:
                Intent intent_qq = new Intent(Intent.ACTION_VIEW);
                intent_qq.setData(Uri.parse("http://jq.qq.com/?_wv=1027&k=2AgKvcH"));
                startActivity(intent_qq);
                break;
            default:
                break;
        }
    }

}
