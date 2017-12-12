package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import online.laoliang.simpleweather.R;
import online.laoliang.simpleweather.service.AutoUpdateService;

public class SettingActivity extends Activity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button back;
    private TextView title_text;
    private Switch back_update;
    private Spinner update_frequency;
    private ArrayAdapter<String> adapter;
    private static final String[] frequency = {"1小时", "2小时", "5小时", "8小时"};

    private void findView() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("设置");

        update_frequency = (Spinner) findViewById(R.id.update_frequency);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, frequency);
        update_frequency.setAdapter(adapter);
        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        update_frequency.setSelection(prefs.getInt("update_frequency", 1), true);
        update_frequency.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                editor.putInt("update_frequency", arg2);
                editor.commit();
                Intent intent = new Intent(SettingActivity.this, AutoUpdateService.class);
                intent.putExtra("anHour", ((TextView) arg1).getText().charAt(0) - 48);
                startService(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        back_update = (Switch) findViewById(R.id.back_update);
        back_update.setOnCheckedChangeListener(this);
        if (prefs.getBoolean("back_update", true)) {
            back_update.setChecked(true);
            update_frequency.setEnabled(true);
        } else {
            back_update.setChecked(false);
            update_frequency.setEnabled(false);
        }
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
        setContentView(R.layout.setting_page);

        findView();

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
            default:
                break;
        }
    }

    /**
     * 为开关按钮注册监听事件
     *
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        switch (compoundButton.getId()) {
            case R.id.back_update:
                if (compoundButton.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                    editor.putBoolean("back_update", true);
                    editor.commit();
                    update_frequency.setEnabled(true);
                    Intent intent = new Intent(this, AutoUpdateService.class);
                    switch (prefs.getInt("update_frequency", 2)) {
                        case 0:
                            intent.putExtra("anHour", 1);
                            break;
                        case 1:
                            intent.putExtra("anHour", 2);
                            break;
                        case 2:
                            intent.putExtra("anHour", 5);
                            break;
                        case 3:
                            intent.putExtra("anHour", 8);
                            break;
                        default:
                            break;
                    }
                    startService(intent);
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                    editor.putBoolean("back_update", false);
                    editor.commit();
                    update_frequency.setEnabled(false);
                    Intent intent = new Intent(this, AutoUpdateService.class);
                    stopService(intent);
                }
                break;
            default:
                break;
        }
    }
}
