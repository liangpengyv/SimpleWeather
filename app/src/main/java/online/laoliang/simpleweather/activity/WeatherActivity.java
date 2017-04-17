package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import online.laoliang.simpleweather.R;
import online.laoliang.simpleweather.model.CityList;
import online.laoliang.simpleweather.model.CityListAdapter;
import online.laoliang.simpleweather.service.AutoUpdateService;
import online.laoliang.simpleweather.util.CheckForUpdateUtil;
import online.laoliang.simpleweather.util.HttpCallbackListener;
import online.laoliang.simpleweather.util.HttpUtil;
import online.laoliang.simpleweather.util.ScreenShotUtils;
import online.laoliang.simpleweather.util.ShareUtils;
import online.laoliang.simpleweather.util.ToastUtil;
import online.laoliang.simpleweather.util.Utility;

public class WeatherActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {

    //定义当前活动的一个实例，用于在其他活动类中调用
    protected static Activity instance = null;

    // 滑动刷新
    private SwipeRefreshLayout swipe_container;

    // 进度对话框
    private ProgressDialog progressDialog;

    // 右侧菜单
    private Button share_weather;

    // 侧滑菜单
    private DrawerLayout drawer_layout;
    private View menu_list;
    private ListView city_list;
    private Button menu_left;
    private Button add_city;
    private Button choose_theme;
    private Button setting;
    private Button about;

    //首页天气信息页面视图
    private LinearLayout weather_info;

    // 已选城市列表
    private HashMap<String, String> cityName_weatherCode = new HashMap<String, String>(10);
    private ArrayList<CityList> cities = new ArrayList<CityList>(10);

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    // 用于显示城市名
    private TextView city_name_tv;
    private TextView nonce_city_name;

    // 用于显示当前时间
    private TextView current_date_tv;

    // 用于显示当前温度
    private TextView wendu_tv;

    // 用于显示天气信息对应的图片
    private ImageView ic_00_iv;
    private ImageView ic_0_iv;
    private ImageView ic_1_iv;
    private ImageView ic_2_iv;
    private ImageView ic_3_iv;
    private ImageView ic_4_iv;
    private ImageView ic_000_iv;

    // 其他信息
    private TextView high_00_tv;
    private TextView low_00_tv;
    private TextView date_00_tv;
    private TextView type_00_tv;
    private TextView fengli_00_tv;
    private TextView divide_00;

    private TextView high_0_tv;
    private TextView low_0_tv;
    private TextView date_0_tv;
    private TextView type_0_tv;
    private TextView fengli_0_tv;
    private TextView du_0;
    private TextView divide_0;
    private TextView type_000_tv;
    private TextView high_000_tv;
    private TextView low_000_tv;
    private TextView divide_000;

    private TextView high_1_tv;
    private TextView low_1_tv;
    private TextView date_1_tv;
    private TextView type_1_tv;
    private TextView fengli_1_tv;
    private TextView divide_1;

    private TextView high_2_tv;
    private TextView low_2_tv;
    private TextView date_2_tv;
    private TextView type_2_tv;
    private TextView fengli_2_tv;
    private TextView divide_2;

    private TextView high_3_tv;
    private TextView low_3_tv;
    private TextView date_3_tv;
    private TextView type_3_tv;
    private TextView fengli_3_tv;
    private TextView divide_3;

    private TextView high_4_tv;
    private TextView low_4_tv;
    private TextView date_4_tv;
    private TextView type_4_tv;
    private TextView fengli_4_tv;
    private TextView divide_4;

    private void findView() {

        instance = this;

        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(this);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu_list = findViewById(R.id.menu_list);
        city_list = (ListView) findViewById(R.id.city_list);

        //首页天气信息视图页面添加左右滑动监听，以实现切换城市
        weather_info = (LinearLayout) findViewById(R.id.weather_info);
        weather_info.setOnTouchListener(new View.OnTouchListener() {

            float x1 = 0;
            float x2 = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        ArrayList<String> temp = new ArrayList<String>(10);
                        temp.addAll(cityName_weatherCode.keySet());
                        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
                        int nonceCityIndex = temp.indexOf(prefs.getString("nonce_city", null));
                        if (x1 - x2 > 50 && temp.size() > 1) {
                            if (nonceCityIndex + 1 < temp.size()) {
                                showWeather(temp.get(nonceCityIndex + 1));
                            } else {
                                ToastUtil.showToast(WeatherActivity.this, "后面已经没有了 ☜", Toast.LENGTH_SHORT);
                            }
                        } else if (x2 - x1 > 50 && temp.size() > 1) {
                            if (nonceCityIndex - 1 >= 0) {
                                showWeather(temp.get(nonceCityIndex - 1));
                            } else {
                                ToastUtil.showToast(WeatherActivity.this, "☞ 前面已经没有了", Toast.LENGTH_SHORT);
                            }
                        }
                        break;
                }
                return true;
            }
        });

        share_weather = (Button) findViewById(R.id.share_weather);
        share_weather.setOnClickListener(this);
        menu_left = (Button) findViewById(R.id.menu_left);
        menu_left.setOnClickListener(this);
        add_city = (Button) findViewById(R.id.add_city);
        add_city.setOnClickListener(this);
        choose_theme = (Button) findViewById(R.id.choose_theme);
        choose_theme.setOnClickListener(this);
        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        about = (Button) findViewById(R.id.about);
        about.setOnClickListener(this);

        city_name_tv = (TextView) findViewById(R.id.city_name);
        nonce_city_name = (TextView) findViewById(R.id.nonce_city_name);
        current_date_tv = (TextView) findViewById(R.id.current_date);
        wendu_tv = (TextView) findViewById(R.id.wendu);

        ic_00_iv = (ImageView) findViewById(R.id.ic_00);
        ic_0_iv = (ImageView) findViewById(R.id.ic_0);
        ic_1_iv = (ImageView) findViewById(R.id.ic_1);
        ic_2_iv = (ImageView) findViewById(R.id.ic_2);
        ic_3_iv = (ImageView) findViewById(R.id.ic_3);
        ic_4_iv = (ImageView) findViewById(R.id.ic_4);
        ic_000_iv = (ImageView) findViewById(R.id.ic_000);

        high_00_tv = (TextView) findViewById(R.id.high_00);
        low_00_tv = (TextView) findViewById(R.id.low_00);
        date_00_tv = (TextView) findViewById(R.id.date_00);
        type_00_tv = (TextView) findViewById(R.id.type_00);
        fengli_00_tv = (TextView) findViewById(R.id.fengli_00);
        divide_00 = (TextView) findViewById(R.id.divide_00);

        high_0_tv = (TextView) findViewById(R.id.high_0);
        low_0_tv = (TextView) findViewById(R.id.low_0);
        date_0_tv = (TextView) findViewById(R.id.date_0);
        type_0_tv = (TextView) findViewById(R.id.type_0);
        fengli_0_tv = (TextView) findViewById(R.id.fengli_0);
        du_0 = (TextView) findViewById(R.id.du_0);
        divide_0 = (TextView) findViewById(R.id.divide_0);
        type_000_tv = (TextView) findViewById(R.id.type_000);
        high_000_tv = (TextView) findViewById(R.id.high_000);
        low_000_tv = (TextView) findViewById(R.id.low_000);
        divide_000 = (TextView) findViewById(R.id.divide_000);

        high_1_tv = (TextView) findViewById(R.id.high_1);
        low_1_tv = (TextView) findViewById(R.id.low_1);
        date_1_tv = (TextView) findViewById(R.id.date_1);
        type_1_tv = (TextView) findViewById(R.id.type_1);
        fengli_1_tv = (TextView) findViewById(R.id.fengli_1);
        divide_1 = (TextView) findViewById(R.id.divide_1);

        high_2_tv = (TextView) findViewById(R.id.high_2);
        low_2_tv = (TextView) findViewById(R.id.low_2);
        date_2_tv = (TextView) findViewById(R.id.date_2);
        type_2_tv = (TextView) findViewById(R.id.type_2);
        fengli_2_tv = (TextView) findViewById(R.id.fengli_2);
        divide_2 = (TextView) findViewById(R.id.divide_2);

        high_3_tv = (TextView) findViewById(R.id.high_3);
        low_3_tv = (TextView) findViewById(R.id.low_3);
        date_3_tv = (TextView) findViewById(R.id.date_3);
        type_3_tv = (TextView) findViewById(R.id.type_3);
        fengli_3_tv = (TextView) findViewById(R.id.fengli_3);
        divide_3 = (TextView) findViewById(R.id.divide_3);

        high_4_tv = (TextView) findViewById(R.id.high_4);
        low_4_tv = (TextView) findViewById(R.id.low_4);
        date_4_tv = (TextView) findViewById(R.id.date_4);
        type_4_tv = (TextView) findViewById(R.id.type_4);
        fengli_4_tv = (TextView) findViewById(R.id.fengli_4);
        divide_4 = (TextView) findViewById(R.id.divide_4);
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
        setContentView(R.layout.weather_layout);

        findView();

        // 更新当前城市列表
        updateCityList(null, null);

        // 判断是否为首次启动APP
        boolean isFirstStart = prefs.getBoolean("first_start", true);
        if (isFirstStart) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }

        // 发送桌面小部件启动的广播
        Intent intent = new Intent("com.simpleweather.app.MY_WIDGETPROVIDER_BROADCAST");
        sendBroadcast(intent);

        // 根据设置记录，判断是否需要启动后台更新服务
        prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        boolean isBackUpdate = prefs.getBoolean("back_update", true);
        if (isBackUpdate) {
            Intent i = new Intent(this, AutoUpdateService.class);
            i.putExtra("anHour", -1);
            startService(i);
        }

        // 根据设置记录，判断是否检查软件版本
        prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        boolean isVersionUpdate = prefs.getBoolean("version_update", true);
        if (isVersionUpdate) {
            CheckForUpdateUtil.checkForUpdateInFIR(this, true);
        }

        // 有县级代号时就去查询天气
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            //如果是前2次添加城市，就弹出下滑刷新的提示信息
            prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
            int isFirstAddCity = prefs.getInt("first_add_city", 1);
            if (isFirstAddCity == 1) {

                alert = null;
                builder = new AlertDialog.Builder(WeatherActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                alert = builder.setMessage("首页下滑可以刷新天气哦 ☟").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create(); // 创建AlertDialog对象
                alert.show(); // 显示对话框

                SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                editor.putInt("first_add_city", isFirstAddCity + 1);
                editor.commit();
            } else if (isFirstAddCity == 2) {

                alert = null;
                builder = new AlertDialog.Builder(WeatherActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                alert = builder.setMessage("左右滑动可以快速切换城市哦 ☜ ☞").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create(); // 创建AlertDialog对象
                alert.show(); // 显示对话框

                SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                editor.putInt("first_add_city", isFirstAddCity + 1);
                editor.commit();
            }

            queryWeatherCode(countyCode);
        }

    }

    /**
     * 查询县级代号所对应的天气代号
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:00
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode", null);
    }

    /**
     * 查询天气代号所对应的天气
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:17
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;
        queryFromServer(address, "weatherCode", weatherCode);
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:40
     */
    private void queryFromServer(final String address, final String type, final String weatherCode) {
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    // 处理服务器返回的天气信息
                    final String city = Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // 将得到的城市名与城市天气代号对应存储起来，刷新天气的时候会用到
                            SharedPreferences.Editor editor = getSharedPreferences("data_city", MODE_PRIVATE).edit();
                            editor.putString(city, weatherCode);
                            editor.commit();
                            // 更新城市列表并展示
                            updateCityList(city, "add");
                            closeProgressDialog();
                            ToastUtil.showToast(WeatherActivity.this, "天气已是最新  \\(^o^)/~", Toast.LENGTH_SHORT);

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.showToast(WeatherActivity.this, "Duang~ 没网了", Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     *
     * @author 梁鹏宇 2016-7-29 下午2:32:21
     */
    private void showWeather(String cityName) {
        if (cityName == null) {
            city_name_tv.setText("一个城市也没有");
            nonce_city_name.setText("N/A");
            current_date_tv.setText(null);
            wendu_tv.setText("N/A");

            high_00_tv.setText(null);
            low_00_tv.setText(null);
            date_00_tv.setText(null);
            type_00_tv.setText(null);
            fengli_00_tv.setText(null);
            ic_00_iv.setImageResource(R.mipmap.back);
            divide_00.setText(null);

            high_0_tv.setText(null);
            low_0_tv.setText(null);
            date_0_tv.setText(null);
            type_0_tv.setText(null);
            fengli_0_tv.setText(null);
            ic_0_iv.setImageResource(R.mipmap.back);
            du_0.setText(null);
            divide_0.setText(null);
            type_000_tv.setText(null);
            high_000_tv.setText(null);
            low_000_tv.setText(null);
            divide_000.setText(null);
            ic_000_iv.setImageResource(R.mipmap.ic_launcher);

            high_1_tv.setText(null);
            low_1_tv.setText(null);
            date_1_tv.setText(null);
            type_1_tv.setText(null);
            fengli_1_tv.setText(null);
            ic_1_iv.setImageResource(R.mipmap.back);
            divide_1.setText(null);

            high_2_tv.setText(null);
            low_2_tv.setText(null);
            date_2_tv.setText(null);
            type_2_tv.setText(null);
            fengli_2_tv.setText(null);
            ic_2_iv.setImageResource(R.mipmap.back);
            divide_2.setText(null);

            high_3_tv.setText(null);
            low_3_tv.setText(null);
            date_3_tv.setText(null);
            type_3_tv.setText(null);
            fengli_3_tv.setText(null);
            ic_3_iv.setImageResource(R.mipmap.back);
            divide_3.setText(null);

            high_4_tv.setText(null);
            low_4_tv.setText(null);
            date_4_tv.setText(null);
            type_4_tv.setText(null);
            fengli_4_tv.setText(null);
            ic_4_iv.setImageResource(R.mipmap.back);
            divide_4.setText(null);

        } else {
            // 存储当前城市名
            SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
            editor.putString("nonce_city", cityName);
            editor.commit();

            SharedPreferences prefs = getSharedPreferences(cityName, MODE_PRIVATE);
            city_name_tv.setText(prefs.getString("city", null));
            nonce_city_name.setText(prefs.getString("city", null));
            current_date_tv.setText(prefs.getString("current_date", null));
            wendu_tv.setText(prefs.getString("wendu", null));

            high_00_tv.setText(prefs.getString("high_00", null));
            low_00_tv.setText(prefs.getString("low_00", null));
            date_00_tv.setText(prefs.getString("date_00", null));
            type_00_tv.setText(prefs.getString("type_00", null));
            fengli_00_tv.setText(prefs.getString("fengli_00", null));
            ic_00_iv.setImageResource(selectImage(prefs.getString("type_00", null)));
            divide_00.setText("/");

            high_0_tv.setText(prefs.getString("high_0", null));
            low_0_tv.setText(prefs.getString("low_0", null));
            date_0_tv.setText(prefs.getString("date_0", null));
            type_0_tv.setText(prefs.getString("type_0", null));
            fengli_0_tv.setText(prefs.getString("fengli_0", null));
            ic_0_iv.setImageResource(selectImage(prefs.getString("type_0", null)));
            du_0.setText("°");
            divide_0.setText("/");
            type_000_tv.setText(prefs.getString("type_0", null));
            high_000_tv.setText(prefs.getString("high_0", null));
            low_000_tv.setText(prefs.getString("low_0", null));
            divide_000.setText("/");
            ic_000_iv.setImageResource(selectImage(prefs.getString("type_0", null)));

            high_1_tv.setText(prefs.getString("high_1", null));
            low_1_tv.setText(prefs.getString("low_1", null));
            date_1_tv.setText(prefs.getString("date_1", null));
            type_1_tv.setText(prefs.getString("type_1", null));
            fengli_1_tv.setText(prefs.getString("fengli_1", null));
            ic_1_iv.setImageResource(selectImage(prefs.getString("type_1", null)));
            divide_1.setText("/");

            high_2_tv.setText(prefs.getString("high_2", null));
            low_2_tv.setText(prefs.getString("low_2", null));
            date_2_tv.setText(prefs.getString("date_2", null));
            type_2_tv.setText(prefs.getString("type_2", null));
            fengli_2_tv.setText(prefs.getString("fengli_2", null));
            ic_2_iv.setImageResource(selectImage(prefs.getString("type_2", null)));
            divide_2.setText("/");

            high_3_tv.setText(prefs.getString("high_3", null));
            low_3_tv.setText(prefs.getString("low_3", null));
            date_3_tv.setText(prefs.getString("date_3", null));
            type_3_tv.setText(prefs.getString("type_3", null));
            fengli_3_tv.setText(prefs.getString("fengli_3", null));
            ic_3_iv.setImageResource(selectImage(prefs.getString("type_3", null)));
            divide_3.setText("/");

            high_4_tv.setText(prefs.getString("high_4", null));
            low_4_tv.setText(prefs.getString("low_4", null));
            date_4_tv.setText(prefs.getString("date_4", null));
            type_4_tv.setText(prefs.getString("type_4", null));
            fengli_4_tv.setText(prefs.getString("fengli_4", null));
            ic_4_iv.setImageResource(selectImage(prefs.getString("type_4", null)));
            divide_4.setText("/");
        }
    }

    /**
     * 根据得到的天气type，返回对应的天气图片Id
     *
     * @author 梁鹏宇 2016-7-30 下午8:26:35
     */
    public static int selectImage(String type) {
        int icId;
        switch (type) {
            case "阴":
                icId = R.mipmap.weather_cloudy_day;
                break;
            case "多云":
                icId = R.mipmap.weather_cloudy_weather;
                break;
            case "雾":
                icId = R.mipmap.weather_fog;
                break;
            case "霾":
                icId = R.mipmap.weather_haze;
                break;
            case "大雨":
                icId = R.mipmap.weather_rain_heavy;
                break;
            case "小雨":
                icId = R.mipmap.weather_rain_light;
                break;
            case "中雨":
                icId = R.mipmap.weather_rain_moderate;
                break;
            case "小到中雨":
                icId = R.mipmap.weather_rain_light_moderate;
                break;
            case "中到大雨":
                icId = R.mipmap.weather_rain_moderate_heavy;
                break;
            case "阵雨":
                icId = R.mipmap.weather_rain_shower;
                break;
            case "雷阵雨":
                icId = R.mipmap.weather_rain_thunderstorms;
                break;
            case "暴雨":
                icId = R.mipmap.weather_rain_torrential;
                break;
            case "雨夹雪":
                icId = R.mipmap.weather_sleet;
                break;
            case "大雪":
                icId = R.mipmap.weather_snow_heavy;
                break;
            case "小雪":
                icId = R.mipmap.weather_snow_light;
                break;
            case "中雪":
                icId = R.mipmap.weather_snow_moderate;
                break;
            case "阵雪":
                icId = R.mipmap.weather_snow_shower;
                break;
            case "暴雪":
                icId = R.mipmap.weather_snow_torrential;
                break;
            case "晴":
                icId = R.mipmap.weather_sunny_day;
                break;
            default:
                icId = R.mipmap.not_applicable;
                break;
        }
        return icId;
    }

    /**
     * 触发滑动刷新后执行的操作
     */
    @Override
    public void onRefresh() {
        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        String cityName = prefs.getString("nonce_city", null);
        prefs = getSharedPreferences("data_city", MODE_PRIVATE);
        final String weatherCode = prefs.getString(cityName, null);
        if (!TextUtils.isEmpty(weatherCode)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryWeatherInfo(weatherCode);
                    swipe_container.setRefreshing(false);
                }
            }, 0); // 0秒后发送消息，停止刷新
        } else {
            ToastUtil.showToast(WeatherActivity.this, "☜ 亲！先添加一个城市吧", Toast.LENGTH_SHORT);
            swipe_container.setRefreshing(false);
        }
    }

    /**
     * 为按键注册监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_weather:
                String fileName = "简约天气-分享.jpeg";
                SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
                String cityName = prefs.getString("nonce_city", null);
                prefs = getSharedPreferences("data_city", MODE_PRIVATE);
                final String weatherCode = prefs.getString(cityName, null);
                if (TextUtils.isEmpty(weatherCode)) {
                    ToastUtil.showToast(WeatherActivity.this, "☜ 亲！先添加一个城市吧", Toast.LENGTH_SHORT);
                } else if (ScreenShotUtils.shotBitmap(WeatherActivity.this, getExternalCacheDir() + File.separator + fileName)) {
                    ToastUtil.showToast(this, "分享天气给朋友", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                    ShareUtils.share(getExternalCacheDir() + File.separator + fileName, "来自简约天气的分享", WeatherActivity.this);
                } else {
                    ToastUtil.showToast(WeatherActivity.this, "        一键截图分享失败！\n\n请尝试打开存储空间权限哦", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.menu_left:
                drawer_layout.openDrawer(menu_list);
                break;
            case R.id.add_city:
                Intent intent_add_city = new Intent(this, ChooseAreaActivity.class);
                intent_add_city.putExtra("from_weather_activity", true);
                startActivity(intent_add_city);
                finish();
                break;
            case R.id.choose_theme:
                Intent intent_choose_theme = new Intent(this, ChooseThemeActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_choose_theme);
                break;
            case R.id.setting:
                Intent intent_setting = new Intent(this, SettingActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_setting);
                break;
            case R.id.about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_about);
                break;
            default:
                break;
        }
    }

    /**
     * 更新城市列表:
     * 1/(cityName, null)-删除cityName.
     * 2/(null, "remove")-删除cityName后更新列表，并指定第一个城市为nonce_city，然后展示当前城市.
     * 3/(cityName, "add")-添加cityName到城市列表，并指定为nonce_city，然后展示当前城市.
     * 4/(null, null)-更新城市列表，然后展示当前城市.
     *
     * @author 梁鹏宇 2016-8-7 下午2:07:58
     */
    private void updateCityList(String cityName, String weatherCode) {
        if (cityName != null && weatherCode == null) {
            // 只传入城市名时：删除该城市并更新
            SharedPreferences.Editor editor = getSharedPreferences("data_city", MODE_PRIVATE).edit();
            editor.remove(cityName);
            editor.commit();
            editor = getSharedPreferences(cityName, MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            updateCityList(null, "remove");
        } else {
            // 将新添加的城市更新到HashMap和ArrayList中
            SharedPreferences prefs = getSharedPreferences("data_city", MODE_PRIVATE);
            cityName_weatherCode.clear();
            cityName_weatherCode.putAll((HashMap<String, String>) prefs.getAll());
            cities.clear();
            ArrayList<String> temp = new ArrayList<String>(10);
            temp.addAll(cityName_weatherCode.keySet());

            // 初始化城市列表数据
            for (int i = 0; i < temp.size(); i++) {
                CityList t = new CityList(temp.get(i));
                cities.add(t);
            }

            // 如果有传入flag标记，则证明是刚刚删除了城市，所以要重新指定nonce_city
            if (weatherCode != null) {
                if (weatherCode.equals("remove")) {
                    // 更新当前城市的标记，并显示当前列表中第一个城市
                    SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                    if (temp.size() != 0) {
                        editor.putString("nonce_city", temp.get(0));
                        editor.commit();
                        showWeather(temp.get(0));
                    } else {
                        editor.putString("nonce_city", null);
                        editor.commit();
                        showWeather(null);
                    }
                } else if (weatherCode.equals("add")) {
                    SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                    editor.putString("nonce_city", cityName);
                    editor.commit();
                    showWeather(cityName);
                }
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("data_setting", MODE_PRIVATE).edit();
                editor.commit();
                prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
                showWeather(prefs.getString("nonce_city", null));
            }

            CityListAdapter adapter = new CityListAdapter(WeatherActivity.this, R.layout.citylist_item, cities);
            city_list.setAdapter(adapter);

            // 为城市列表建立点击监听事件（显示点击的城市）
            city_list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    drawer_layout.closeDrawers();
                    CityList t = cities.get(position);
                    showWeather(t.getCityName());
                }
            });

            // 为城市列表建立长按监听事件（删除长按的城市）
            city_list.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    // 弹出确认删除对话框
                    alert = null;
                    builder = new AlertDialog.Builder(WeatherActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    alert = builder.setMessage("真的要删除我吗~").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CityList t = cities.get(position);
                            updateCityList(t.getCityName(), null);
                        }
                    }).create(); // 创建AlertDialog对象
                    alert.show(); // 显示对话框

                    // 长按事件后不想继续执行点击事件时，应返回true
                    return true;
                }
            });
        }
    }

    /**
     * 判断Back按键，根据当前所在页面，决定返回到哪里
     */
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(menu_list)) {
            drawer_layout.closeDrawers();
        } else {
            finish();
        }
    }

    /**
     * 显示进度对话框
     *
     * @author 梁鹏宇 2016-7-21 下午11:51:42
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     *
     * @author 梁鹏宇 2016-7-21 下午11:51:58
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
