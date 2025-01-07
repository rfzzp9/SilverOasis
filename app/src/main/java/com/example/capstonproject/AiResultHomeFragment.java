package com.example.capstonproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AiResultHomeFragment extends Fragment implements View.OnClickListener {
    public RecyclerView recyclerView;
    public Adapter adapter;
    public ArrayList<JobListData> items = new ArrayList();
    public LinearLayoutManager layoutManager;
    public ProgressBar progressBar;
    public TextView explainView;
    public ImageButton settingBtn;
    public TextView jobCnt;
    public SearchView searchView;
    public TextView resultArea;
    public String resultDisabled1;
    public String resultDisabled2;
    public String resultDisabled3;
    public TextView resultDis;
    public static ArrayList<Integer> arrayList = new ArrayList();
    HashSet<Integer> starPos;
    public static String key;
    static HashMap<String, ArrayList<Integer>> integerHashMap = new HashMap();
    HashMap<String, String> jobList = new HashMap();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static ImageButton star;
    public static boolean i = true;
    public static ArrayList<Integer> arrayList_remove = new ArrayList();

    public AiResultHomeFragment() {
    }

    public static AiResultHomeFragment newInstance(String param1, String param2) {
        AiResultHomeFragment fragment = new AiResultHomeFragment();
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

    public void onPause() {
        super.onPause();
        Log.e("@@@@@@@@@@@@@@hello", "onPause");
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("kbs", 0);
        SharedPreferences.Editor editor = preferences.edit();
        String area = AiSelectHomeFragment.editArea.getText().toString();
        String dis1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
        String dis2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();
        String dis3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();
        key = area + "_" + dis1 + "_" + dis2 + "_" + dis3;
        if (arrayList.size() > 0 || arrayList_remove.size() > 0) {
            ArrayList<Integer> _arrayList = new ArrayList();
            Iterator var8 = arrayList.iterator();

            while(var8.hasNext()) {
                int i = (Integer)var8.next();
                if (!_arrayList.contains(i)) {
                    _arrayList.add(i);
                }
            }

            Log.e("@@@@", "@@@@  _arrayList 중복제거 후-> " + _arrayList);
            ArrayList valueList;
            if (!integerHashMap.containsKey(key)) {
                Log.e("@@@@value List ", "@@@@ value List2 ->" + _arrayList);
                integerHashMap.put(key, _arrayList);
                Log.e("arraylist @@@@", "@@@@new key value" + integerHashMap);
            } else {
                Log.e("@@@@value List ", "@@@@ value List1 ->" + key);
                valueList = (ArrayList)integerHashMap.get(key);
                Log.e("@@@@value List ", "@@@@ value List ->" + valueList);
                Iterator var13 = _arrayList.iterator();

                while(var13.hasNext()) {
                    int i = (Integer)var13.next();
                    valueList.add(i);
                }

                Log.e("@@@@value List ", "@@@@ value List 대입 후 ->" + valueList);
                if (arrayList_remove.size() > 0) {
                    valueList.removeAll(arrayList_remove);
                }

                arrayList_remove.clear();
                integerHashMap.put(key, valueList);
                Log.e("@@@@value List ", "중복 제거 완전히 한 후 ->" + valueList);
            }

            Log.e("integerhashmap@@@@@", "test");
            Log.e("integerhashmap@@@@@", "@@@@" + integerHashMap);
            arrayList.clear();
            Log.e("@@@@@", "@@@@!!! last map" + integerHashMap);
            valueList = (ArrayList)integerHashMap.get(key);
            ArrayList<Integer> arr2 = new ArrayList();
            Iterator var15 = valueList.iterator();

            while(var15.hasNext()) {
                int i = (Integer)var15.next();
                if (!arr2.contains(i)) {
                    arr2.add(i);
                }
            }

            integerHashMap.put(key, arr2);
            Log.e("arr2", "@@@@" + arr2);
            Log.e("@@@@", "@@@@" + integerHashMap);
            JSONObject jsonObject = new JSONObject(integerHashMap);
            String jsonString = jsonObject.toString();
            editor.putString("key", jsonString);
            editor.commit();
        }

        Log.e("@@@@@@@@", "onPause");
    }

    public void onResume() {
        super.onResume();
        Log.e("@@@@@@@@@@@@@@hello", "onResume");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("@@@@@@@@@@@@@@hello", "1");
        View view = inflater.inflate(R.layout.fragment_ai_result_home, container, false);
        this.settingBtn = (ImageButton)view.findViewById(R.id.setting);
        this.settingBtn.setOnClickListener(this);
        this.progressBar = (ProgressBar)view.findViewById(R.id.circularProgressBar);
        this.explainView = (TextView)view.findViewById(R.id.explain);
        this.progressBar.setVisibility(View.VISIBLE);
        this.explainView.setVisibility(View.VISIBLE);
        this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this.getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.scrollToPosition(0);
        Log.e("@@@@@@@@@@@@@@starPos", "@@@@@@@@@@@" + this.starPos);
        Log.e("@@@@@@@@@@@@@@hello", "2");
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        String cookie = preferences.getString("current_login_session", (String)null);
        this.sendPost(cookie);
        this.adapter = new Adapter(this.items);
        this.recyclerView.setAdapter(this.adapter);
        Log.e("@@@@@@@@@@@@@", "@@@sendPost result -> " + this.items);
        Log.e("@@@@@@@@@@@@@@hello", "3");
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.recyclerView.getContext(), 1);
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.recyclerView.getContext(), 2131230910));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.e("@@@@@@@@@@@@@@hello", "4");
        this.adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            public void onItemClick(View v, int pos) {
                v.setBackgroundColor(Color.rgb(211, 211, 211));
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!");
                String id = null;
                TextView textView_jobName = (TextView)v.findViewById(R.id.jobName);  //2131361961
                Iterator var5 = AiResultHomeFragment.this.jobList.keySet().iterator();

                while(var5.hasNext()) {
                    String key = (String)var5.next();
                    String name = (String)AiResultHomeFragment.this.jobList.get(key);
                    if (textView_jobName.getText().equals(name)) {
                        id = key;
                        Log.e("@@@@@@@ job id", "@@@@" + key + ", " + name);
                    }
                }

                ImageButton imgBtn = (ImageButton)v.findViewById(R.id.starBtn);
                Integer resource = (Integer)imgBtn.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("StarTag", resource.toString());
                bundle.putString("JobID", id);
                AiJobDetailsFragment jobDetailsFragment = new AiJobDetailsFragment();
                FragmentManager fragmentManager = AiResultHomeFragment.this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                jobDetailsFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame3, jobDetailsFragment);
                fragmentTransaction.commit();
            }

            @SuppressLint({"ResourceType"})
            public void onButtonClick(View v, int pos) {
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!" + v.findViewById(R.id.starBtn).getTag());
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!");
                ImageButton img = (ImageButton)v.findViewById(R.id.starBtn);  //2131362092
                TextView textView_jobName = (TextView)v.findViewById(R.id.jobName);
                if (AiResultHomeFragment.i) {
                    img.setTag(R.drawable.img_2);
                    AiResultHomeFragment.i = false;
                }

                Integer resource = (Integer)img.getTag();
                Log.e("aaaaaaaaaaa", "img tag : " + resource);
                String id;
                TextView textViewJobName;
                Iterator var8;
                String key;
                String name;
                if (resource != 2131230835 && resource != 2131230828) {
                    Log.e("aaaaa2", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + AiResultHomeFragment.arrayList);
                    Log.e("aaaaa2", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + pos);

                    for(int i = 0; i < AiResultHomeFragment.arrayList.size(); ++i) {
                        if ((Integer)AiResultHomeFragment.arrayList.get(i) == pos) {
                            AiResultHomeFragment.arrayList.remove(i);
                        }
                    }

                    AiResultHomeFragment.arrayList_remove.add(pos);
                    Log.e("aaaaaaaaaaaaaaarraylist", "!!!!!!!!!!!" + AiResultHomeFragment.arrayList);
                    Log.e("@@@@@@@@@@@@@@", "!!!!!" + AiResultHomeFragment.this.starPos);
                    img.setImageResource(R.drawable.img_2);  //2131230835
                    img.setTag(R.drawable.img_2);  //2131230835
                    id = null;
                    textViewJobName = (TextView)AiResultHomeFragment.this.recyclerView.getLayoutManager().findViewByPosition(pos).findViewById(R.id.jobName);
                    var8 = AiResultHomeFragment.this.jobList.keySet().iterator();

                    while(var8.hasNext()) {
                        key = (String)var8.next();
                        name = (String)AiResultHomeFragment.this.jobList.get(key);
                        if (textViewJobName.getText().equals(name)) {
                            id = key;
                        }
                    }

                    AiResultHomeFragment.this.sendRequest("https://pi.imdhson.com/scrap/del", id);
                } else {
                    Log.e("aaaaa 1", "@@@@@@@@@@@@@@@@@@@@@@@@@@" + pos);
                    AiResultHomeFragment.arrayList.add(pos);
                    img.setImageResource(R.drawable.img_1); //빈별
                    img.setTag(R.drawable.img_1); //빈별
                    id = null;
                    textViewJobName = (TextView)AiResultHomeFragment.this.recyclerView.getLayoutManager().findViewByPosition(pos).findViewById(R.id.jobName);
                    var8 = AiResultHomeFragment.this.jobList.keySet().iterator();

                    while(var8.hasNext()) {
                        key = (String)var8.next();
                        name = (String)AiResultHomeFragment.this.jobList.get(key);
                        if (textViewJobName.getText().equals(name)) {
                            id = key;
                        }
                    }

                    AiResultHomeFragment.this.sendRequest("https://pi.imdhson.com/scrap/add", id);
                }

            }
        });
//        this.jobCnt = (TextView)view.findViewById(2131361960);
        Log.e("@@@@@@@@@@@@@@hello", "5");
        this.searchView = (SearchView)view.findViewById(R.id.searchText);
        this.searchView.clearFocus();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            public boolean onQueryTextChange(String s) {
                AiResultHomeFragment.this.filterList(AiResultHomeFragment.this.items, s);
//                AiResultHomeFragment.this.jobCnt.setText(Integer.toString(AiResultHomeFragment.this.items.size() - 1));
                return true;
            }
        });
        this.resultArea = (TextView)view.findViewById(R.id.resultAnswer_area);
        this.resultArea.setText(AiSelectHomeFragment.editArea.getText().toString());
        this.resultDis = (TextView)view.findViewById(R.id.resultAnswer_disabled);
        if (AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString().equals("선택")) {
            this.resultDisabled1 = "";
        } else {
            this.resultDisabled1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
        }

        if (AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString().equals("선택")) {
            this.resultDisabled2 = "";
        } else {
            this.resultDisabled2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();
        }

        if (AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택")) {
            this.resultDisabled3 = "";
        } else {
            this.resultDisabled3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();
        }

        if (AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString().equals("선택") && AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택") && AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택")) {
            this.resultDis.setText("해당없음");
        } else {
            this.resultDis.setText(this.resultDisabled1 + " " + this.resultDisabled2 + " " + this.resultDisabled3);
        }

        Log.e("@@@@@@@@@@@@@@hello", "6");
        return view;
    }

    public void onClick(View view) {
        Log.e("@@@@@@@@@@@@@@hello", "onclick");
        switch (view.getId()) {
            case R.id.setting:
//                view.setBackgroundColor(Color.rgb(211, 211, 211));
                AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                FragmentManager fragmentManager = this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                this.settingBtn.setImageResource(R.drawable.img_59);
                fragmentTransaction.replace(R.id.frame3, aiSelectHomeFragment);
                fragmentTransaction.commit();
            default:
        }
    }

    public void filterList(ArrayList<JobListData> _sortedItems, String text) {
        Log.e("@@@@@@@@@@@@@@hello", "filterList");
        Log.w("hello", "filterList");
        ArrayList<JobListData> filteredList = new ArrayList();
        Iterator var4 = _sortedItems.iterator();

        while(var4.hasNext()) {
            JobListData item = (JobListData)var4.next();
            if (item.getJob_name().contains(text)) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Log.e("filtering : ", "no filtering");
        } else {
            this.adapter.setFilteredList(filteredList);
        }

    }

    public void createpopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPost(final String cookie) {
        Log.e("@@@@@@@@@@@@@@hello", "sendPost");
        String url = "https://pi.imdhson.com/ailist";
        new ArrayList();
        JSONObject jsonBody = new JSONObject();

        try {
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
                createpopUp("@@@@"+response);
                try {
                    String dis2;
                    String dis3;
                    String _key;
                    String searchCnt;
                    String jsonString;
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        dis2 = jsonObject.getString("_id");
                        dis3 = jsonObject.getString("시설명"); //사업장명
                        _key = jsonObject.getString("시설종류");    //임금
                        searchCnt = jsonObject.getString("viewCount");
                        String address = jsonObject.getString("소재지");
                        String hireType = jsonObject.getString("운영주체");
                        Log.e("@@@@@@@", "@@@@@ response2 >> " + dis3 + "," + _key + "," + address + "," + hireType);
                        //jsonString = String.format("%,d", Integer.parseInt(_key));
                        items.add(new JobListData(R.id.starBtn, dis3, searchCnt, address, hireType));
                        jobList.put(dis2, dis3);
                    }

                    AiResultHomeFragment.this.adapter.notifyDataSetChanged();
                    AiResultHomeFragment.this.progressBar.setVisibility(View.GONE);
                    AiResultHomeFragment.this.explainView.setVisibility(View.GONE);
                    String area = AiSelectHomeFragment.editArea.getText().toString();
                    String dis1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
                    dis2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();
                    dis3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();
                    _key = area + "_" + dis1 + "_" + dis2 + "_" + dis3;
                    AiResultHomeFragment.key = area + "_" + dis1 + "_" + dis2 + "_" + dis3;
                    Log.e("@@@@@@@@@@", "@@@@key" + AiResultHomeFragment.key);
                    Log.e("@@@@@@@@@@", "@@@@_key" + _key);
                    FragmentActivity var10000 = AiResultHomeFragment.this.getActivity();
                    AiResultHomeFragment.this.getActivity();
                    SharedPreferences preferences = var10000.getSharedPreferences("kbs", 0);
                    HashMap<String, ArrayList<Integer>> retrievedHashMap = new HashMap();
                    Log.e("@@@@@@@@@@@@@@hello", "onResume1");

                    try {
                        if (retrievedHashMap != null) {
                            jsonString = preferences.getString("key", (new JSONObject()).toString());
                            JSONObject jsonObjectx = new JSONObject(jsonString);
                            Log.e("@@@@1", "@@@jsonString :" + jsonString.toString());
                            Iterator<String> keys = jsonObjectx.keys();

                            while(keys.hasNext()) {
                                String keyx = (String)keys.next();
                                Log.e("@@@@2", "@@@ key" + keyx);
                                JSONArray jsonArrayx = jsonObjectx.getJSONArray(keyx);
                                ArrayList<Integer> arrayList = new ArrayList();
                                Log.e("@@@@3", "@@@ key" + keyx);

                                for(int ix = 0; ix < jsonArrayx.length(); ++ix) {
                                    int value = jsonArrayx.getInt(ix);
                                    arrayList.add(value);
                                }

                                retrievedHashMap.put(keyx, arrayList);
                                Log.e("@@@@4", "@@@ key" + keyx);
                            }

                            Log.e("@@@@", "@@@hashmap to string" + retrievedHashMap);
                            final ArrayList<Integer> key_list = new ArrayList();
                            Set set = retrievedHashMap.keySet();
                            Iterator iterator = set.iterator();

                            label47:
                            while(true) {
                                String key;
                                do {
                                    if (!iterator.hasNext()) {
                                        Log.e("@@@@", "@@ key list" + key_list);
                                        Log.e("@@@@@@@@@@@@@@hello", "onResume2");
                                        if (key_list.size() > 0) {
                                            (new Handler()).postDelayed(new Runnable() {
                                                public void run() {
                                                    Iterator var1 = key_list.iterator();

                                                    while(var1.hasNext()) {
                                                        int i = (Integer)var1.next();
                                                        ImageButton img = (ImageButton)AiResultHomeFragment.this.recyclerView.getLayoutManager().findViewByPosition(i).findViewById(R.id.starBtn);
                                                        Log.e("@@@", "i" + i);
                                                        int resource = (Integer)img.getTag();
                                                        img.setImageResource(R.drawable.img_1);
                                                        img.setTag(R.drawable.img_1);
                                                        Log.e("get tag", "@@@@@@@@@@@@" + resource);
                                                    }

                                                }
                                            }, 500L);
                                        }
                                        break label47;
                                    }

                                    key = (String)iterator.next();
                                    Log.e("@@@@@@@@@@", "@@@@@@@@@@@@" + key);
                                    Log.e("@@@@@@@@@@", "@@@@@@@@@@@@" + _key);
                                } while(!key.equals(_key));

                                JSONArray jsonArray = jsonObjectx.getJSONArray(key);

                                for(int ixx = 0; ixx < jsonArray.length(); ++ixx) {
                                    int valuex = jsonArray.getInt(ixx);
                                    key_list.add(valuex);
                                }
                            }
                        }
                    } catch (Exception var19) {
                    }

                    Log.e("@@@@@@@@@@@@@@hello", "onResume3");
//                    AiResultHomeFragment.this.jobCnt.setText(Integer.toString(AiResultHomeFragment.this.items.size() - 1));
                } catch (JSONException var20) {
                    throw new RuntimeException(var20);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("@@@@@@@@@@@@", "@@@@@ error1 " + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap();
                Log.e("@@@@@@@@@@@@@@hello", "getHeaders");
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            public int getCurrentTimeout() {
                return 50000;
            }

            public int getCurrentRetryCount() {
                return 50000;
            }

            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Volley.newRequestQueue(this.getContext()).add(jsonArrayRequest);
    }

    public void sendRequest(String url, String jobID) {
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        final String cookie = preferences.getString("current_login_session", (String)null);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", jobID);
        } catch (JSONException var7) {
            var7.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.e("@@@@", "response" + response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("@@@@@@@@@@@@", "@@@@@@@@@ error " + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap();
                Log.e("@@@@", "response1");
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }
}