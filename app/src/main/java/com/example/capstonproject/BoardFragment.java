package com.example.capstonproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BoardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public WebView webView;
    public ProgressBar progressBar;
    public TextView explainView;

    public BoardFragment() {
    }

    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        this.progressBar = (ProgressBar)view.findViewById(R.id.circularProgressBar);
        this.explainView = (TextView)view.findViewById(R.id.explain);
        this.progressBar.setVisibility(View.VISIBLE);
        this.explainView.setVisibility(View.VISIBLE);
        this.webView = (WebView)view.findViewById(R.id.BoardWeb);
        this.sendPostBoard();
        return view;
    }

    public void sendPostBoard() {
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        String cookie = preferences.getString("current_login_session", (String)null);
        String url = "https://pi.imdhson.com/articles";
        Log.e("@@@@@", "@@@@json 요청 url " + url);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                BoardFragment.this.progressBar.setVisibility(View.GONE);
                BoardFragment.this.explainView.setVisibility(View.GONE);
            }
        });
        this.webView.requestFocus(130);
        this.webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                    case 1:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                    default:
                        return false;
                }
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        this.webView.loadUrl(url);
    }
}