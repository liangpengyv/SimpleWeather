package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import online.laoliang.simpleweather.R;

/**
 * Created by liang on 1/24.
 */
public class ChooseThemeActivity extends Activity implements View.OnClickListener {

    private Button choose_theme_1;
    private Button choose_theme_2;
    private Button choose_theme_3;
    private Button choose_theme_4;
    private Button choose_theme_5;
    private Button choose_theme_6;
    private Button choose_theme_7;
    private Button choose_theme_8;
    private Button choose_theme_9;
    private Button choose_theme_10;
    private Button choose_theme_11;
    private Button choose_theme_12;

    private void findView() {
        choose_theme_1 = (Button) findViewById(R.id.choose_style_1);
        choose_theme_1.setOnClickListener(this);
        choose_theme_2 = (Button) findViewById(R.id.choose_style_2);
        choose_theme_2.setOnClickListener(this);
        choose_theme_3 = (Button) findViewById(R.id.choose_style_3);
        choose_theme_3.setOnClickListener(this);
        choose_theme_4 = (Button) findViewById(R.id.choose_style_4);
        choose_theme_4.setOnClickListener(this);
        choose_theme_5 = (Button) findViewById(R.id.choose_style_5);
        choose_theme_5.setOnClickListener(this);
        choose_theme_6 = (Button) findViewById(R.id.choose_style_6);
        choose_theme_6.setOnClickListener(this);
        choose_theme_7 = (Button) findViewById(R.id.choose_style_7);
        choose_theme_7.setOnClickListener(this);
        choose_theme_8 = (Button) findViewById(R.id.choose_style_8);
        choose_theme_8.setOnClickListener(this);
        choose_theme_9 = (Button) findViewById(R.id.choose_style_9);
        choose_theme_9.setOnClickListener(this);
        choose_theme_10 = (Button) findViewById(R.id.choose_style_10);
        choose_theme_10.setOnClickListener(this);
        choose_theme_11 = (Button) findViewById(R.id.choose_style_11);
        choose_theme_11.setOnClickListener(this);
        choose_theme_12 = (Button) findViewById(R.id.choose_style_12);
        choose_theme_12.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_theme);
        findView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
        Intent intent = new Intent(this, WeatherActivity.class);
        switch (id) {
            case R.id.choose_style_1:
                editor.putInt("choose_theme", 1);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_2:
                editor.putInt("choose_theme", 2);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_3:
                editor.putInt("choose_theme", 3);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_4:
                editor.putInt("choose_theme", 4);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_5:
                editor.putInt("choose_theme", 5);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_6:
                editor.putInt("choose_theme", 6);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_7:
                editor.putInt("choose_theme", 7);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_8:
                editor.putInt("choose_theme", 8);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_9:
                editor.putInt("choose_theme", 9);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_10:
                editor.putInt("choose_theme", 10);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_11:
                editor.putInt("choose_theme", 11);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
            case R.id.choose_style_12:
                editor.putInt("choose_theme", 12);
                finish();
                WeatherActivity.instance.finish();
                startActivity(intent);
                break;
        }
        editor.commit();
    }
}
