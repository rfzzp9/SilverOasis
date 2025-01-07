package com.example.capstonproject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Fragment aiFragment;
    Fragment boardFragment;
    Fragment scrapFragment;
    ProgressBar progressBar;
    static String[] i = new String[]{""};
    ImageButton mypageButton;
    TextView explainView;
    private AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.8F);
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.aiMenu:
                    selectedFragment = new AiSelectHomeFragment();
                    break;
                case R.id.boardMenu:
                    selectedFragment = new BoardFragment();
                    break;
                case R.id.scrapMenu:
                    selectedFragment = new ScrapFragment();
            }

            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, (Fragment)selectedFragment).commit();
            return true;
        }
    };

    public MainActivity() {
    }

    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //customActionBar 보이게
        this.getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);     //toolbar_title_layout xml파일
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        this.mypageButton = (ImageButton)this.findViewById(R.id.mypageBtn);
        this.mypageButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //page button 마우스 누를 때
                        MainActivity.this.mypageButton.setImageResource(R.drawable.img_57);
                        MainActivity.this.mypageButton.invalidate();
                        MyPageFragment myPageFragment = new MyPageFragment();
                        MainActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.frame, myPageFragment).commit();
                        break;
                    case MotionEvent.ACTION_UP: //page button 마우스 뗄 때
                        MainActivity.this.mypageButton.setImageResource(R.drawable.img_56);
                        MainActivity.this.mypageButton.invalidate();
                }

                return false;
            }
        });
        this.setContentView(R.layout.activity_main);
        Log.i("MainActivity@@", "1111");
        BottomNavigationView bottomNav = (BottomNavigationView)this.findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(this.navListener);
        bottomNav.setItemIconTintList((ColorStateList)null);
        this.progressBar = (ProgressBar)this.findViewById(R.id.circularProgressBar);
        this.explainView = (TextView)this.findViewById(R.id.explain);
        this.progressBar.setVisibility(View.VISIBLE); //보이게
        this.explainView.setVisibility(View.VISIBLE); //보이게
        this.DoOrNotLogin();    //TODO 서버 구축 후 활성화해야함
        //createpopUp2(); //TODO TEST 후 비활성화해야함

    }

    public void DoOrNotLogin() {
        SharedPreferences preferences = this.getSharedPreferences("pref_loginSession", 0);
        final String cookie = preferences.getString("current_login_session", (String)null);
        String url = "https://pi.imdhson.com/session";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("ID", "string");
            jsonBody.put("Email", "string");
            jsonBody.put("LastLogin", "string");
            jsonBody.put("ScrapList", "string");
            JSONObject settings = new JSONObject();
            settings.put("Loc", "");
            settings.put("Service1", "");
            settings.put("Service2", "");
            settings.put("Service3", "");
            jsonBody.put("Settings", settings);
        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonBody, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.e("@@@@@@@@@@@@", "@@@@@ response >> " + response.toString());
                if (response.toString().contains("Not LOGIN")) {
                    Log.e("@@@@@@@@@@@@", "loginFragment >> " + response.toString());
                    LoginFragment loginFragment = new LoginFragment();
                    MainActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.frame, loginFragment).commit();
                    MainActivity.this.progressBar.setVisibility(View.GONE);
                    MainActivity.this.explainView.setVisibility(View.GONE);
                    MainActivity.this.createpopUp2();
                } else {
                    Log.e("@@@@@@@@@@@@", "aiSelectHomeFragment >> " + response.toString());
                    AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                    MainActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.frame, aiSelectHomeFragment).commit();
                    MainActivity.this.progressBar.setVisibility(View.GONE);
                    MainActivity.this.explainView.setVisibility(View.GONE);
                    String email = null;

                    try {
                        email = response.getString("Email");
                        MainActivity.this.createpopUp1(email);
                    } catch (JSONException var5) {
                        throw new RuntimeException(var5);
                    }
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("@@@@@@@@@@@@", "@@@@@ error " + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap();
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            public int getCurrentTimeout() {
                return 50000;
            }

            public int getCurrentRetryCount() {
                return 50000;
            }

            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void createpopUp1(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.show_dialog, (LinearLayout)this.findViewById(R.id.layoutDialog));
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(" Silver Oasis");
        ((TextView)view.findViewById(R.id.textMessage_id)).setText(str);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void createpopUp2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.show_dialog2, (LinearLayout)this.findViewById(R.id.layoutDialog));
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(" Silver Oasis");
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}