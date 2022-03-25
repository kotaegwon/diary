package org.techtown.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.techtown.mydiary.data.WeatherItem;
import org.techtown.mydiary.data.WeatherResult;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener, OnRequestListener, MyApplication.OnResponseListener {

    private static final String TAG = "MainActivity";

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    BottomNavigationView bottomNavigationView;

    Location currentLocation;
    GPSListener gpsListener;


    int locationCount = 0;
    String currentWeather;
    String currentAddress;
    String currentDateString;
    Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1=new Fragment1();
        fragment2=new Fragment2();
        fragment3=new Fragment3();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "목록", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();
                        return true;
                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "작성", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment2).commit();
                        return true;
                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "통계", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment3).commit();
                        return true;
                }
                return false;
            }
        });

        setPicturePath();

        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();

    }

    public void setPicturePath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        AppConstants.FOLDER_PHOTO = sdcardPath + File.separator + "photo";
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void onTabSelected(int position){
        if(position ==0){
            bottomNavigationView.setSelectedItemId(R.id.tab1);
        }else if(position==1){
            bottomNavigationView.setSelectedItemId(R.id.tab2);
        }else if(position==2){
            bottomNavigationView.setSelectedItemId(R.id.tab3);
        }
    }

    //Fragment2에서 호출
    @Override
    public void onRequest(String command) {
        if(command != null){
            if(command.equals("getCurrentLocation")){
                getCurrentFocus();
            }
        }
    }

    //onRequest메서드 호출 시
    //위치 확인 시작
    //현재 일자를 확인하여 두 번째 프레그먼트에 설정한 후 LocationManager객체에게 현재 위치를 요청
    public void getCurrentLocation() {
        currentDate = new Date();
        currentDateString = AppConstants.dateFormat3.format(currentDate);

        if (fragment2 != null) {
            fragment2.setDateString(currentDateString);
        }

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            currentLocation=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(currentLocation!=null){
                double latitude=currentLocation.getLatitude(); //위도
                double longtitude=currentLocation.getLongitude(); //경도
                
                String message="마지막 위치 -> 위도 : "+latitude+"\n 경도 : "+longtitude;
                println(message);

                getCurrentWeather();
                getCuurentAddress();

            }
            gpsListener=new GPSListener();
            long minTime=10000;
            float minDistance=0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            println("요청된 현재 위치");
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void stopLocationService(){
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            manager.removeUpdates(gpsListener);
            println("요청된 현재 위치");
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    //요청된 위치를 수신하기 위해 만듬
    //위치 확인 시 getCurrentWeather과 getCurrentAddress 메서드 호출
    class GPSListener implements LocationListener{
        public void onLocationChanged(Location location) {
            currentLocation=location;

            locationCount++;

            Double latitude=location.getLatitude();
            Double longtitude= location.getLongitude();

            String message="현재 위치 -> 위도 : "+latitude+"\n 경로 : "+longtitude;
            println(message);

            getCurrentWeather();
            getCuurentAddress();
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled (@NonNull String provider){

        }

        @Override
        public void onProviderDisabled (@NonNull String provider){
        }
    }

    //Geocoder클래스를 이용해 현재 위치를 주소로 변환하는 것을 확인할 수 있음
    public void getCuurentAddress(){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address>addresses=null;

        try {
            addresses=geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses != null && addresses.size()>0) {
            currentAddress = null;

            Address address = addresses.get(0);
            if (address.getLocality() != null) {
                currentAddress = address.getLocality();

                if (address.getSubLocality() != null) {
                    currentAddress += " " + address.getSubLocality();
                } else {
                    currentAddress = address.getSubLocality();
                }
            }
            String adminArea=address.getAdminArea();
            String country=address.getCountryName();
            println("Address : "+country+" "+adminArea+" "+currentAddress);

            if(fragment2!=null){
                fragment2.setAddress(currentAddress);
            }
        }
    }

    //GridUtil 객체의 getGrid 메서드를 이용해 격자 번호를 확인
    //그리고 sendLocationWeatherReq 메서드를 호출하여 기상청 날씨 서버로 요청 전송
    //옹답을 받으면 processResponse 메서드가 호출-> 그 메서드 안에서 XML응답 데이터를 자바 객체로 만들어줌
    //날씨 데이터를 파싱하기 위한 클래스들은 data 패키지 안에 들어있음
    public void getCurrentWeather(){
        Map<String, Double>gridMap=GridUtil.getGrid(currentLocation.getLatitude(), currentLocation.getLongitude());
        
        double gridX=gridMap.get("x");
        double gridY=gridMap.get("y");
        println("x -> "+gridX+", y -> "+gridY);
        
        sendLocalWeather(gridX, gridY);
            
        }

    private void sendLocalWeather(double gridX, double gridY) {
        String url="http://www.kma.go.kr/wid/queryDFS.jsp";
        url+="?gridx="+Math.round(gridX);
        url+="?gridy="+Math.round(gridY);

        Map<String, String>params=new HashMap<String, String>();

        MyApplication.send(AppConstants.REQ_WEATHER_BY_GRID, Request.Method.GET, url, params, this);
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode==AppConstants.REQ_WEATHER_BY_GRID){
                XmlParserCreator parserCreator=new XmlParserCreator() {
                    @Override
                    public XmlPullParser createParser() {
                        try {
                            return XmlPullParserFactory.newInstance().newPullParser();
                        } catch (XmlPullParserException e) {

                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                };
                GsonXml gsonXml=new GsonXmlBuilder().setXmlParserCreator(parserCreator).setSameNameLists(true).create();

                WeatherResult weatherResult=gsonXml.fromXml(response, WeatherResult.class);

                try {
                    Date tmDate = AppConstants.dateFormat.parse(weatherResult.header.tm);
                    String tmDateText=AppConstants.dateFormat2.format(tmDate);
                    println("기준 시간: "+tmDate);

                    for(int i=0;i<weatherResult.body.datas.size();i++){
                        WeatherItem item=weatherResult.body.datas.get(i);
                        println("#"+i+" 시간: "+item.hour+"시, "+item.day+"일째");
                        println(" 날씨: "+item.wfKor);
                        println(" 기온: "+item.temp+" C");
                        println(" 강수확률: "+item.pop+" %");

                        println("debug 1 : "+(int)Math.round(item.ws*10));
                        float ws=Float.valueOf(String.valueOf((int)Math.round(item.ws*10)))/10.0f;
                        println(" 풍속: "+ws+"m/s");
                    }

                    WeatherItem item=weatherResult.body.datas.get(0);
                    currentWeather=item.wfKor;
                    if(fragment2!=null){
                        fragment2.setWeather(item.wfKor);
                    }
                    if(locationCount>0){
                        stopLocationService();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                println("Unknown request code : "+requestCode);
            }
        }else{
            println("Failure response code : "+responseCode);
        }
    }
    private void println(String data){
        Log.d(TAG,data);
    }
}


