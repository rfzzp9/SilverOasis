package com.example.capstonproject;

import android.annotation.SuppressLint;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AiJobDetailsFragment extends Fragment {
    private WebView webView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ImageButton starBtn;
    public String starTag;
    public String jobID;
    public HashSet<String> fillStars;
    public int textCount = 0;
    TextView address;
    TextView employmentType;
    TextView hireType;
    TextView pay;
    TextView phoneNumber;
    TextView jobName;
    ImageButton backwardsBtn;
    ProgressBar progressBar1;
    TextView explainView1;
    ProgressBar progressBar2;
    TextView explainView2;
    public String fillStarList;

    public AiJobDetailsFragment() {
    }

    public static AiJobDetailsFragment newInstance(String param1, String param2) {
        AiJobDetailsFragment fragment = new AiJobDetailsFragment();
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

    @SuppressLint({"MissingInflatedId"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_job_details, container, false);
        this.address = (TextView)view.findViewById(R.id.employmentTypeResult);     //소재지
        this.employmentType = (TextView)view.findViewById(R.id.addressResult);     //설치일
        this.hireType = (TextView)view.findViewById(R.id.hireTypeResult);         //입소정원
        this.pay = (TextView)view.findViewById(R.id.payResult);                  //운영주체
        this.phoneNumber = (TextView)view.findViewById(R.id.phoneNumberResult);  //전화번호
        this.jobName = (TextView)view.findViewById(R.id.companyName);             //시설명
        this.progressBar1 = (ProgressBar)view.findViewById(R.id.circularProgressBar);
        this.explainView1 = (TextView)view.findViewById(R.id.explain);
        this.progressBar2 = (ProgressBar)view.findViewById(R.id.circularProgressBar1);
        this.explainView2 = (TextView)view.findViewById(R.id.explain1);
        this.progressBar1.setVisibility(View.VISIBLE);
        this.explainView1.setVisibility(View.VISIBLE);
        this.backwardsBtn = (ImageButton)view.findViewById(R.id.backwards);
        this.backwardsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AiResultHomeFragment aiResultHomeFragment = new AiResultHomeFragment();
                FragmentManager fragmentManager = AiJobDetailsFragment.this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AiJobDetailsFragment.this.backwardsBtn.setImageResource(R.drawable.img_60);
                fragmentTransaction.replace(R.id.frame5, aiResultHomeFragment);
                fragmentTransaction.addToBackStack((String)null);
                fragmentTransaction.commit();
            }
        });
        this.starBtn = (ImageButton)view.findViewById(R.id.starButton);
        if (this.getArguments() != null) {
            this.starTag = this.getArguments().getString("StarTag");
            this.jobID = this.getArguments().getString("JobID");
            int star = Integer.parseInt(this.starTag);
            this.starBtn.setTag(star);
            this.starBtn.setImageResource(star);
        }

        this.sendPost();
        this.webView = (WebView)view.findViewById(R.id.commentView);
        this.sendPostChat();
        return view;
    }

    public void sendPost() {
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        final String cookie = preferences.getString("current_login_session", (String)null);
        String url = "https://pi.imdhson.com/jobs/" + this.jobID;
        Log.e("@@@@@", "@@@@json 요청 url " + url);
        final JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("_id", "string");
            jsonBody.put("시설종류", "string");
            jsonBody.put("지역구분1", "string");
            jsonBody.put("지역구분2", "string");
            jsonBody.put("시설명", "string");
            jsonBody.put("시설장명", "string");
            jsonBody.put("입소-정원", "int");
            jsonBody.put("현원-계", "int");
            jsonBody.put("현원-남", "int");
            jsonBody.put("현원-여", "int");
            jsonBody.put("종사자-계", "int");
            jsonBody.put("종사자-남", "int");
            jsonBody.put("종사자-여", "int");
            jsonBody.put("소재지", "string");
            jsonBody.put("전화번호", "string");
            jsonBody.put("설치일", "string");
            jsonBody.put("운영주체", "string");
            jsonBody.put("viewCount", "int");
        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonBody, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.e("@@@@@@@jobDetail", "@@@@@response> " + jsonBody.toString());
                //createpopUp("@@@@"+response);
                try {
                    String job = response.getString("시설명");
                    String jobAddress = response.getString("입소-정원");
                    String tel = response.getString("소재지");
                    String payment = response.getString("전화번호");
                    String hiringType = response.getString("설치일");
                    String employType = response.getString("운영주체");
                    Log.e("@@@@@@@", "@@@@@ response result " + AiJobDetailsFragment.this.jobName + "," + jobAddress + "," + tel + "," + payment + "," + hiringType + "," + employType);
                    String money1 = String.format("%,d", Integer.parseInt(jobAddress));
                    AiJobDetailsFragment.this.address.setText(tel); //소재지
                    AiJobDetailsFragment.this.employmentType.setText(hiringType); //설치일
                    AiJobDetailsFragment.this.hireType.setText(jobAddress); //입소-정원
                    AiJobDetailsFragment.this.pay.setText(employType);  //운영주체
                    AiJobDetailsFragment.this.phoneNumber.setText(payment);  //전화번호
                    AiJobDetailsFragment.this.jobName.setText(job); //시설명
                    AiJobDetailsFragment.this.progressBar1.setVisibility(View.GONE);
                    AiJobDetailsFragment.this.explainView1.setVisibility(View.GONE);
                } catch (JSONException var9) {
                    var9.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("@@@@@@@@@aiJobDetails", "@@@error " + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap();
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }

    public void createpopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPostChat() {
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        String cookie = preferences.getString("current_login_session", (String)null);
        String url = "https://pi.imdhson.com/articles/" + this.jobID;
        Log.e("@@@@@", "@@@@json 요청 url " + url);
        this.progressBar2.setVisibility(View.VISIBLE);
        this.explainView2.setVisibility(View.VISIBLE);
        this.webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        this.webView.loadUrl(url);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                AiJobDetailsFragment.this.progressBar2.setVisibility(View.GONE);
                AiJobDetailsFragment.this.explainView2.setVisibility(View.GONE);
            }
        });
    }
}