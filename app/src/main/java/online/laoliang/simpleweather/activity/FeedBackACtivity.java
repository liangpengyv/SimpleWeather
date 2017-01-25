package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import online.laoliang.simpleweather.R;

public class FeedBackACtivity extends Activity implements OnClickListener {

    private Button back;
    private TextView title_text;
    private WebView feed_back;

    private void findView() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("反馈");
        feed_back = (WebView) findViewById(R.id.web_view);
        feed_back.getSettings().setJavaScriptEnabled(true);
        feed_back.setWebViewClient(new WebViewClient());
        feed_back.loadUrl("http://form.mikecrm.com/Vgc2aM");
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
        setContentView(R.layout.web_view);

        findView();
    }

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

}
