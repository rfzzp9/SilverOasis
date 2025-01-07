package com.example.capstonproject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
    private Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGetLocation = false;
    Location location;
    double lat;
    double lon;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L;
    private static final long MIN_TIME_BW_UPDATES = 60000L;
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        this.getLocation();
    }

    @TargetApi(23)
    public Location getLocation() {
        if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") != 0 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return null;
        } else {
            try {
                this.locationManager = (LocationManager)this.mContext.getSystemService("location");
                this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
                this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
                if (this.isGPSEnabled || this.isNetworkEnabled) {
                    this.isGetLocation = true;
                    if (this.isNetworkEnabled) {
                        this.locationManager.requestLocationUpdates("network", 60000L, 10.0F, this);
                        if (this.locationManager != null) {
                            this.location = this.locationManager.getLastKnownLocation("network");
                            if (this.location != null) {
                                this.lat = this.location.getLatitude();
                                this.lon = this.location.getLongitude();
                                Log.v("알림", "위도 : " + this.lat + "경도 " + this.lon);
                            }
                        }
                    }

                    if (this.isGPSEnabled && this.location == null) {
                        this.locationManager.requestLocationUpdates("gps", 60000L, 10.0F, this);
                        if (this.locationManager != null) {
                            this.location = this.locationManager.getLastKnownLocation("gps");
                            if (this.location != null) {
                                this.lat = this.location.getLatitude();
                                this.lon = this.location.getLongitude();
                            }
                        }
                    }
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }

            return this.location;
        }
    }

    public void stopUsingGPS() {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }

    }

    public double getLatitude() {
        if (this.location != null) {
            this.lat = this.location.getLatitude();
        }

        return this.lat;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.lon = this.location.getLongitude();
        }

        return this.lon;
    }

    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    public void showSettingsAlert() {
        this.makeDialog();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    private void makeDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this.mContext);
        alt_bld.setMessage("GPS 사용이 필요합니다. \n설정창으로 가시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                GPSTracker.this.mContext.startActivity(intent);
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("GPS 사용 허가");
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 62, 79, 92)));
        alert.show();
    }
}