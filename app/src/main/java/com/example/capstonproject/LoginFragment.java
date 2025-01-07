package com.example.capstonproject;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class LoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    WebView loginView;
    WebSettings webSettings;
    ProgressBar progressBar;
    TextView textView;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            this.mParam1 = this.getArguments().getString("param1");
            this.mParam2 = this.getArguments().getString("param2");
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pr", 0);
        SharedPreferences.Editor editor = preferences.edit();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.progressBar = (ProgressBar)view.findViewById(R.id.circularProgressBar);
        this.textView = (TextView)view.findViewById(R.id.explain);
        this.progressBar.setVisibility(View.GONE);
        this.textView.setVisibility(View.GONE);
        this.loginView = (WebView)view.findViewById(R.id.LoginWeb);
        this.loginView.setWebViewClient(new WebViewClient());
        this.loginView.setWebViewClient(new WebViewClient());
        this.webSettings = this.loginView.getSettings();
        this.webSettings.setJavaScriptEnabled(true);
        this.webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webSettings.setAllowFileAccess(true);
        this.webSettings.setLoadWithOverviewMode(true);
        this.webSettings.setUseWideViewPort(true);
        this.webSettings.setSupportZoom(true);
        this.webSettings.setBuiltInZoomControls(true);
        this.webSettings.setDisplayZoomControls(true);
        this.webSettings.setJavaScriptEnabled(true);
        this.loginView.loadUrl("https://pi.imdhson.com/login");
        this.loginView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("exit")) {
                    LoginFragment.this.progressBar.setVisibility(View.VISIBLE);
                    LoginFragment.this.textView.setVisibility(View.VISIBLE);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    String myCookies = cookieManager.getCookie(url);
                    String[] _myCookies = myCookies.split(";");
                    String str = _myCookies[_myCookies.length - 1];
                    FragmentActivity var10000 = LoginFragment.this.getActivity();
                    LoginFragment.this.getActivity();
                    SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("current_login_session", str);
                    editor.commit();
                    Log.e("@@@@@@@@@@@@@", "@@@@cookie 마지막값 " + str);
                    Log.e("@@@@@@@@@@@@@", "@@@@shared preferences save " + preferences.getAll());
                    LoginFragment.this.sendPost(str);
                }

            }
        });
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        this.loginView.destroy();
    }

    public void createpopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("로그인 완료").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPost(final String cookie) {
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
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonBody, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    String email = response.getString("Email");
                    AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                    FragmentManager fragmentManager = LoginFragment.this.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    LoginFragment.this.progressBar.setVisibility(View.GONE);
                    LoginFragment.this.textView.setVisibility(View.GONE);
                    fragmentTransaction.replace(R.id.frame6, aiSelectHomeFragment);
                    fragmentTransaction.commit();
                } catch (JSONException var6) {
                    throw new RuntimeException(var6);
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
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }
}