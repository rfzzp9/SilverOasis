package com.example.capstonproject;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScrapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    public Adapter_scrap adapter;
    public ProgressBar progressBar;
    public TextView explainView;
    public TextView cntView;
    public TextView explain;
    public ArrayList<JobListData> items_scrap = new ArrayList();

    public ScrapFragment() {
    }

    public static ScrapFragment newInstance(String param1, String param2) {
        ScrapFragment fragment = new ScrapFragment();
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
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);
//        this.cntView = (TextView)view.findViewById(R.id.cnt);
//        this.cntView.setVisibility(View.GONE);
//        this.explain = (TextView)view.findViewById(R.id.text);
//        this.explain.setVisibility(View.GONE);
        this.recyclerView = (RecyclerView)view.findViewById(R.id.jobScrapList);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this.getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.scrollToPosition(0);
        this.progressBar = (ProgressBar)view.findViewById(R.id.circularProgressBar);
        this.explainView = (TextView)view.findViewById(R.id.explain);
//        this.progressBar.setVisibility(View.VISIBLE);
//        this.explainView.setVisibility(View.VISIBLE);
        //this.sendPost();

        ScrapFragment.this.items_scrap.add(new JobListData(R.id.starBtn, "보현요양원", "1", "경상북도 경산시", "사회복지법인"));
        ScrapFragment.this.items_scrap.add(new JobListData(R.id.starBtn, "서린요양원", "4", "경상북도 경산시", "사회복지법인"));
        this.adapter = new Adapter_scrap(this.items_scrap);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void sendPost() {
        String url = "https://pi.imdhson.com/scrap";
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        final String cookie = preferences.getString("current_login_session", (String)null);
        JSONObject jsonBody = new JSONObject();

        try {
//            jsonBody.put("AI_List_num", "int");
//            jsonBody.put("_id", "string");
//            jsonBody.put("사업장명", "string");
//            jsonBody.put("임금형태", "string");
//            jsonBody.put("임금", "int");
//            jsonBody.put("사업장 주소", "string");
//            jsonBody.put("고용형태", "string");

            jsonBody.put("AI_List_num", "int");
            jsonBody.put("_id", "string");
            jsonBody.put("시설명", "string");
            jsonBody.put("시설종류", "string");
            jsonBody.put("소재지", "string");
            jsonBody.put("운영주체", "string");
            jsonBody.put("viewCount", "int");
        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(1, url, (JSONArray)null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("@@@@@@@", "@@@@@ response1 >> " + response.toString());

                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String jobId = jsonObject.getString("_id");
                        String name = jsonObject.getString("시설명");
                        String money = jsonObject.getString("시설종류");
                        String address = jsonObject.getString("viewCount");
                        String hireType = jsonObject.getString("소재지");
                        String hireType2 = jsonObject.getString("운영주체");
                        Log.e("@@@@@@@", "@@@@@ response2 >> " + name + "," + money + "," + address + "," + hireType);
                        ScrapFragment.this.items_scrap.add(new JobListData(R.id.starBtn, name, address, hireType, hireType2));
                    }

                    ScrapFragment.this.adapter.notifyDataSetChanged();
                    ScrapFragment.this.cntView.setText(Integer.toString(ScrapFragment.this.items_scrap.size()));
                    ScrapFragment.this.progressBar.setVisibility(View.GONE);
                    ScrapFragment.this.explainView.setVisibility(View.GONE);
                    ScrapFragment.this.cntView.setVisibility(View.VISIBLE);
                    ScrapFragment.this.explain.setVisibility(View.VISIBLE);
                } catch (JSONException var10) {
                    throw new RuntimeException(var10);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                ScrapFragment.this.createpopUp("@@@@@@@@@@@@@@@ error1 " + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap();
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            public int getCurrentTimeout() {
                return 60000;
            }

            public int getCurrentRetryCount() {
                return 60000;
            }

            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Volley.newRequestQueue(this.getContext()).add(jsonArrayRequest);
    }

    public void createpopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}