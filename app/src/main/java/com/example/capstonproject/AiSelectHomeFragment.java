package com.example.capstonproject;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AiSelectHomeFragment extends Fragment implements View.OnClickListener {
    ImageButton nextBtn;
    public static Spinner spinner_disabled1;
    public static Spinner spinner_disabled2;
    public static Spinner spinner_disabled3;
    private List<String> list;
    public LinearLayout layout;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GPSTracker gps;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L;
    private static final long MIN_TIME_BW_UPDATES = 60000L;
    protected LocationManager locationManager;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ImageButton searchBtn;
    public ImageButton resetBtn;
    static AutoCompleteTextView editArea;
    public ImageButton setLocation;
    static String sharedKey = null;

    public AiSelectHomeFragment() {
    }

    public static AiSelectHomeFragment newInstance(String param1, String param2) {
        AiSelectHomeFragment fragment = new AiSelectHomeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("@@@@@@@@@@@@@", "1212");
        if (this.getArguments() != null) {
            this.mParam1 = this.getArguments().getString("param1");
            this.mParam2 = this.getArguments().getString("param2");
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_select_home, container, false);
        this.layout = (LinearLayout)view.findViewById(R.id.layout);
        this.setLocation = (ImageButton)view.findViewById(R.id.input_area2);
        this.setLocation.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AiSelectHomeFragment.this.setLocation.setImageResource(R.drawable.img_44);
                        AiSelectHomeFragment.this.setLocation.invalidate();
                        if (!AiSelectHomeFragment.this.isPermission) {
                            AiSelectHomeFragment.this.callPermission();
                        }

                        AiSelectHomeFragment.this.gps = new GPSTracker(AiSelectHomeFragment.this.getContext());
                        if (AiSelectHomeFragment.this.gps.isGetLocation()) {
                            double latitude = AiSelectHomeFragment.this.gps.getLatitude();
                            double longitude = AiSelectHomeFragment.this.gps.getLongitude();
                            Geocoder gCoder = new Geocoder(AiSelectHomeFragment.this.getContext(), Locale.getDefault());
                            List<Address> addr = null;

                            try {
                                addr = gCoder.getFromLocation(latitude, longitude, 1);
                                Address a = (Address)addr.get(0);

                                for(int ix = 0; ix <= a.getMaxAddressLineIndex(); ++ix) {
                                    Log.v("알림", "AddressLine(" + ix + ")" + a.getAddressLine(ix) + "\n");
                                }

                                String degree = a.getAdminArea();
                                String city = a.getLocality();
                                ArrayList<String> address = new ArrayList();
                                address.add(degree);
                                address.add(city);
                                String result = "";

                                for(int i = 0; i < 2; ++i) {
                                    if (address.get(i) != null) {
                                        result = result + (String)address.get(i);
                                        if (i != 1) {
                                            result = result + " ";
                                        }
                                    }
                                }

                                AiSelectHomeFragment.editArea.setText(result);
                            } catch (IOException var15) {
                                var15.printStackTrace();
                            }

                            if (addr != null && addr.size() == 0) {
                                Log.e("@@@@@@@@@@@@@", "@@@@주소정보없음");
                            }
                        } else {
                            AiSelectHomeFragment.this.gps.showSettingsAlert();
                        }

                        AiSelectHomeFragment.this.callPermission();
                        break;
                    case MotionEvent.ACTION_UP:
                        AiSelectHomeFragment.this.setLocation.setImageResource(R.drawable.img_43);
                        AiSelectHomeFragment.this.setLocation.invalidate();
                }

                return false;
            }
        });
        this.layout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AiSelectHomeFragment.this.hideKeyboard();
                return false;
            }
        });
        this.list = new ArrayList();
        this.settingList();
        editArea = (AutoCompleteTextView)view.findViewById(R.id.edit_area);
        editArea.setAdapter(new ArrayAdapter(view.getContext(), android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item, this.list));
        this.nextBtn = (ImageButton)view.findViewById(R.id.next_btn);
        spinner_disabled1 = (Spinner)view.findViewById(R.id.spinner_disabled1);
        spinner_disabled2 = (Spinner)view.findViewById(R.id.spinner_disabled2);
        spinner_disabled3 = (Spinner)view.findViewById(R.id.spinner_disabled3);
        this.resetBtn = (ImageButton)view.findViewById(R.id.reset_btn);
        this.searchBtn = (ImageButton)view.findViewById(R.id.searchBtn);
        this.searchBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AiSelectHomeFragment.this.showKeyboard();
                AiSelectHomeFragment.editArea.requestFocus();
                return false;
            }
        });

        //reset버튼 사용 x
//        this.resetBtn.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        AiSelectHomeFragment.this.resetBtn.setImageResource(2131230869);
//                        AiSelectHomeFragment.this.resetBtn.invalidate();
//                        AiSelectHomeFragment.editArea.setText("");
//                        AiSelectHomeFragment.spinner_disabled1.setSelection(0);
//                        AiSelectHomeFragment.spinner_disabled2.setSelection(0);
//                        AiSelectHomeFragment.spinner_disabled3.setSelection(0);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        AiSelectHomeFragment.this.resetBtn.setImageResource(2131230874);
//                        AiSelectHomeFragment.this.resetBtn.invalidate();
//                }
//
//                return false;
//            }
//        });
        this.nextBtn.setOnClickListener(this);
        spinner_disabled1.setSelection(0);
        spinner_disabled2.setSelection(0);
        spinner_disabled3.setSelection(0);
        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                Log.e("selectHome", "nextbtn click@@@@@@@@@@@@@@");
                String dis1 = "";
                String dis2 = "";
                String dis3 = "";
                if (spinner_disabled1.getSelectedItem().toString().equals("선택")) {
                    dis1 = "";
                } else {
                    dis1 = spinner_disabled1.getSelectedItem().toString();
                }

                if (spinner_disabled2.getSelectedItem().toString().equals("선택")) {
                    dis2 = "";
                } else {
                    dis2 = spinner_disabled2.getSelectedItem().toString();
                }

                if (spinner_disabled3.getSelectedItem().toString().equals("선택")) {
                    dis3 = "";
                } else {
                    dis3 = spinner_disabled3.getSelectedItem().toString();
                }

                this.sendRequest(editArea.getText().toString(), dis1, dis2, dis3);
                AiResultHomeFragment aiResultFragment = new AiResultHomeFragment();
                FragmentManager fragmentManager = this.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                this.nextBtn.setImageResource(R.drawable.img_58);
                fragmentTransaction.replace(R.id.frame2, aiResultFragment);
                fragmentTransaction.commit();
            default:
        }
    }

    private void hideKeyboard() {
        if (this.getActivity() != null && this.getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager)this.getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)this.getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editArea, 0);
    }

    public void sendRequest(String _area, String _dis1, String _dis2, String _dis3) {
        String url = "https://pi.imdhson.com/users/settings/submit/";
        FragmentActivity var10000 = this.getActivity();
        this.getActivity();
        SharedPreferences preferences = var10000.getSharedPreferences("pref_loginSession", 0);
        final String cookie = preferences.getString("current_login_session", (String)null);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("loc", _area);
            jsonObject.put("service1", _dis1);
            jsonObject.put("service2", _dis2);
            jsonObject.put("service3", _dis3);
        } catch (JSONException var10) {
            var10.printStackTrace();
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

    public void createpopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void settingList() {
        this.list.add("강원도");
        this.list.add("강원도 강릉시");
        this.list.add("강릉시");
        this.list.add("강원도 동해시");
        this.list.add("동해시");
        this.list.add("강원도 삼척시");
        this.list.add("삼척시");
        this.list.add("양양군");
        this.list.add("강원도 양양군");
        this.list.add("강원도 원주시");
        this.list.add("원주시");
        this.list.add("강원도 정선군");
        this.list.add("정선군");
        this.list.add("강원도 춘천시");
        this.list.add("춘천시");
        this.list.add("강원도 평창군");
        this.list.add("경기도 고양시");
        this.list.add("고양시");
        this.list.add("경기도 과천시");
        this.list.add("과천시");
        this.list.add("경기도 광주시");
        this.list.add("경기도 군포시");
        this.list.add("경기도 김포시");
        this.list.add("김포시");
        this.list.add("경기도 남양주시");
        this.list.add("남양주시");
        this.list.add("경기도 부천시");
        this.list.add("부천시");
        this.list.add("경기도 성남시");
        this.list.add("성남시");
        this.list.add("경기도 수원시");
        this.list.add("수원시");
        this.list.add("경기도 시흥시");
        this.list.add("시흥시");
        this.list.add("경기도 안산시");
        this.list.add("안산시");
        this.list.add("안성시");
        this.list.add("경기도 안성시");
        this.list.add("경기도 안양시");
        this.list.add("안양시");
        this.list.add("경기도 양주시");
        this.list.add("양주시");
        this.list.add("경기도 오산시");
        this.list.add("오산시");
        this.list.add("경기도 용인시");
        this.list.add("용인시");
        this.list.add("경기도 의정부시");
        this.list.add("의정부시");
        this.list.add("이천시");
        this.list.add("경기도 이천시");
        this.list.add("경기도 파주시");
        this.list.add("파주시");
        this.list.add("경기도 평택시");
        this.list.add("평택시");
        this.list.add("포천시");
        this.list.add("경기도 포천시");
        this.list.add("경기도 하남시");
        this.list.add("하남시");
        this.list.add("화성시");
        this.list.add("경기도 화성시");
        this.list.add("경상남도 거제시");
        this.list.add("거제시");
        this.list.add("경상남도 김해시");
        this.list.add("김해시");
        this.list.add("경상남도 밀양시");
        this.list.add("밀양시");
        this.list.add("경상남도 사천시");
        this.list.add("사천시");
        this.list.add("경상남도 양산시");
        this.list.add("양산시");
        this.list.add("경상남도 진주시");
        this.list.add("진주시");
        this.list.add("경상남도 창녕군");
        this.list.add("경상남도 창원시");
        this.list.add("창원시");
        this.list.add("경상남도 통영시");
        this.list.add("통영시");
        this.list.add("경상남도 합천군");
        this.list.add("경상북도 경산시");
        this.list.add("경산시");
        this.list.add("경상북도 경주시");
        this.list.add("경주시");
        this.list.add("경상북도 구미시");
        this.list.add("구미시");
        this.list.add("경상북도 김천시");
        this.list.add("김천시");
        this.list.add("문경시");
        this.list.add("경상북도 문경시");
        this.list.add("경상북도 상주시");
        this.list.add("상주시");
        this.list.add("경상북도 안동시");
        this.list.add("안동시");
        this.list.add("영천시");
        this.list.add("경상북도 영천시");
        this.list.add("경상북도 의성군");
        this.list.add("경상북도 칠곡군");
        this.list.add("칠곡군");
        this.list.add("포항시");
        this.list.add("경상북도 포항시");
        this.list.add("광주광역시");
        this.list.add("대구광역시");
        this.list.add("대구");
        this.list.add("대전");
        this.list.add("대전광역시");
        this.list.add("부산광역시");
        this.list.add("부산");
        this.list.add("서울");
        this.list.add("서울특별시");
        this.list.add("세종");
        this.list.add("세종특별자치시");
        this.list.add("울산광역시");
        this.list.add("울산");
        this.list.add("인천광역시");
        this.list.add("인천");
        this.list.add("전라남도");
        this.list.add("경기도");
        this.list.add("경상북도");
        this.list.add("경상남도");
        this.list.add("고흥군");
        this.list.add("전라남도 고흥군");
        this.list.add("전라남도 광양시");
        this.list.add("광양시");
        this.list.add("나주시");
        this.list.add("전라남도 나주시");
        this.list.add("전라남도 목포시");
        this.list.add("목포시");
        this.list.add("전라남도 무안군");
        this.list.add("무안군");
        this.list.add("순천시");
        this.list.add("전라남도 순천시");
        this.list.add("전라남도 여수시");
        this.list.add("여수시");
        this.list.add("전라남도 영암군");
        this.list.add("영암군");
        this.list.add("전라남도 장흥군");
        this.list.add("장흥군");
        this.list.add("전라남도 함평군");
        this.list.add("함평군");
        this.list.add("전라북도");
        this.list.add("군산시");
        this.list.add("전라북도 군산시");
        this.list.add("전라북도 김제시");
        this.list.add("김제시");
        this.list.add("전라북도 남원시");
        this.list.add("남원시");
        this.list.add("전라북도 순창군");
        this.list.add("순창군");
        this.list.add("전라북도 완주군");
        this.list.add("완주군");
        this.list.add("전라북도 익산시");
        this.list.add("익산시");
        this.list.add("전라북도 전주시");
        this.list.add("전주시");
        this.list.add("전라북도 진안군");
        this.list.add("진안군");
        this.list.add("제주특별자치도");
        this.list.add("제주특별자치도 제주시");
        this.list.add("제주시");
        this.list.add("충청남도");
        this.list.add("충청남도 계룡시");
        this.list.add("계룡시");
        this.list.add("충청남도 공주시");
        this.list.add("공주시");
        this.list.add("충청남도 금산군");
        this.list.add("금산군");
        this.list.add("충청남도 논산시");
        this.list.add("논산시");
        this.list.add("충청남도 당진시");
        this.list.add("당진시");
        this.list.add("충청남도 서산시");
        this.list.add("서산시");
        this.list.add("충청남도 서천군");
        this.list.add("서천군");
        this.list.add("충청남도 아산시");
        this.list.add("아산시");
        this.list.add("충청남도 천안시");
        this.list.add("천안시");
        this.list.add("충청남도 청양군");
        this.list.add("충청남도 태안군");
        this.list.add("충청북도");
        this.list.add("충청북도 음성군");
        this.list.add("음성군");
        this.list.add("충청북도 제천시");
        this.list.add("제천시");
        this.list.add("충청북도 증평군");
        this.list.add("증평군");
        this.list.add("충청북도 진천군");
        this.list.add("진천군");
        this.list.add("충청북도 청주시");
        this.list.add("청주시");
        this.list.add("충청북도 충주시");
        this.list.add("충주시");
    }

    private void callPermission() {
        if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1000);
        } else if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.getContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 1001);
        } else {
            this.isPermission = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000 && grantResults[0] == 0) {
            this.isAccessFineLocation = true;
        } else if (requestCode == 1001 && grantResults[0] == 0) {
            this.isAccessCoarseLocation = true;
        }
        if (this.isAccessFineLocation && this.isAccessCoarseLocation) {
            this.isPermission = true;
        }

    }
}