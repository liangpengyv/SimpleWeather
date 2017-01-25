package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import online.laoliang.simpleweather.R;

public class WelcomeActivity extends Activity implements OnClickListener, OnTouchListener {

    private Button welcome_start;
    private ViewFlipper welcome_page;
    private float touchDownX; // 手指按下的X坐标
    private float touchUpX; // 手指松开的X坐标

    private void findView() {
        welcome_start = (Button) findViewById(R.id.welcome_start);
        welcome_start.setOnClickListener(this);
        welcome_page = (ViewFlipper) findViewById(R.id.welcome_page);
        welcome_page.setOnTouchListener(this);
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
        setContentView(R.layout.welcome_page);

        findView();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.welcome_start:
                SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                editor.putBoolean("first_start", false);
                editor.commit();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 重写Back按键响应
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 取得左右滑动时手指按下的X坐标
            touchDownX = event.getX();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 取得左右滑动时手指松开的X坐标
            v.performClick();
            touchUpX = event.getX();
            // 从左往右，看前一个View
            if (touchUpX - touchDownX > 100) {
                // 显示上一屏动画
                welcome_page.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                welcome_page.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                // 显示上一屏View
                View temp = findViewById(R.id.welcome_view_1);
                if (welcome_page.getCurrentView() != temp) {
                    welcome_page.showPrevious();
                }
                // 从左往右，看后一个View
            } else if (touchDownX - touchUpX > 100) {
                // 显示下一屏动画
                welcome_page.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                welcome_page.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                // 显示下一屏View
                View temp = findViewById(R.id.welcome_view_4);
                if (welcome_page.getCurrentView() != temp) {
                    welcome_page.showNext();
                }
            }
            return true;
        }
        return false;
    }

}
