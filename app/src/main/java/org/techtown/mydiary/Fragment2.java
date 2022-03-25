package org.techtown.mydiary;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.channguyen.rsv.RangeSliderView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Fragment2 extends Fragment {

    private final String TAG="Fragment2";

    Context context;
    OnTabItemSelectedListener listener;
    OnRequestListener requestListener;
    Button save_btn,del_btn,close_btn;
    RangeSliderView rangeSliderView;
    TextView dateTextView ,locationTextView;
    ImageView weatherIcon, pictureImageView;
    EditText contentsInput;

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    boolean isPhotoCanceled;

    int selectedPhotoMenu;


    Uri uri;
    File file;
    Bitmap resultPhotoBitmap;

    //전달받은 액티비티 객체가 OnRequestListener 인터페이스를 구현하고 있는 경우
    //requestListener 변수에 할당하도록 함
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;

        if(context instanceof OnTabItemSelectedListener){
            listener=(OnTabItemSelectedListener) context;
        }
        if(context instanceof OnRequestListener){
            requestListener=(OnRequestListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(context!=null){
            context=null;
            listener=null;
            requestListener = null;
        }
    }

    //onCreateView 메서드 안에서 OnRequestListener 객체의 onRequest 호출 이렇게 하면 입력 화면이 보일 때마다 현재위치를
    //확인 할 수 있도록 MainActivity 클래스의 onRequest메서드가 호출
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment2,container,false);


        initUI(rootview);

        if(requestListener != null){
            requestListener.onRequest("getCurrentLocation");
        }
        return rootview;
    }

    //버튼 눌렀을때 화면 이동
    private void initUI(ViewGroup rootview) {
        save_btn=rootview.findViewById(R.id.saveButton);
        del_btn=rootview.findViewById(R.id.deleteButton);
        close_btn=rootview.findViewById(R.id.closeButton);

        weatherIcon=rootview.findViewById(R.id.weatherIcon);
        dateTextView=rootview.findViewById(R.id.dateTextView);
        locationTextView=rootview.findViewById(R.id.locationTextView);

        contentsInput=rootview.findViewById(R.id.contentsInput);
        pictureImageView=rootview.findViewById(R.id.pictureImageView);
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhotoCaptured || isPhotoFileSaved){
                    showDialog(AppConstants.CONTENT_PHOTO_EX);
                }else{
                    showDialog(AppConstants.CONTENT_PHOTO);
                }

            }
        });

        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //각 버튼 클릭시 목록으로
                    case R.id.saveButton:
                        if(listener != null){
                            listener.onTabSelected(0);
                        }
                        break;
                    case R.id.deleteButton:
                        if(listener != null){
                            listener.onTabSelected(0);
                        }
                        break;
                    case R.id.closeButton:
                        if(listener != null){
                            listener.onTabSelected(0);
                        }
                        break;
                }
            }
        };
        save_btn.setOnClickListener(clickListener);
        del_btn.setOnClickListener(clickListener);
        close_btn.setOnClickListener(clickListener);



        //기분 전환시 호출
        rangeSliderView=rootview.findViewById(R.id.sliderView);
        rangeSliderView.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Toast.makeText(context, "moodIndex change to "+index, Toast.LENGTH_SHORT).show();
            }
        });
        //기본값
        rangeSliderView.setInitialIndex(2);
    }

    public void showDialog(int id){
        AlertDialog.Builder builder=null;

        switch (id){
            case AppConstants.CONTENT_PHOTO:
                builder=new AlertDialog.Builder(context);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectedPhotoMenu=whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(selectedPhotoMenu==0){
                            showPhotoCaptureActivity();
                        }else if(selectedPhotoMenu==1){
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                break;

                case AppConstants.CONTENT_PHOTO_EX:
                    builder=new AlertDialog.Builder(context);

                    builder.setTitle("사진 메뉴 선택");
                    builder.setSingleChoiceItems(R.array.array_photo_ex, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            selectedPhotoMenu=whichButton;
                        }
                    });
                    builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(selectedPhotoMenu==0){
                                showPhotoCaptureActivity();
                            }else if(selectedPhotoMenu==1){
                                showPhotoSelectionActivity();
                            }else if(selectedPhotoMenu==2){
                                isPhotoCanceled=true;
                                isPhotoCaptured=false;

                                pictureImageView.setImageResource(R.drawable.picture1);
                            }
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    break;


            default:
                break;
        }

        AlertDialog dialog=builder.create();
        dialog.show();
    }


    public void showPhotoCaptureActivity() {

        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }
                file.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        if(Build.VERSION.SDK_INT>=24){
            uri= FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        }else{
            uri=Uri.fromFile(file);
        }
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);

        }

    private File createFile() {
        String filename = createFilename();
        File outFile = new File(context.getFilesDir(), filename);
        Log.d("Main", "File path : " + outFile.getAbsolutePath());

        return outFile;
    }
    public void showPhotoSelectionActivity(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, AppConstants.REQ_PHOTO_SELECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data !=null){
            switch (requestCode){
                case AppConstants.REQ_PHOTO_CAPTURE:
                    Log.d(TAG, "onActivityResult() for REQ_PHOTO_CAPTURE.");

                    Log.d(TAG, "resultCode : "+resultCode);

                    resultPhotoBitmap=decodeSampleBitmapReomResource(file, pictureImageView.getWidth(), pictureImageView.getHeight());
                    pictureImageView.setImageBitmap(resultPhotoBitmap);
                    break;

                    case AppConstants.REQ_PHOTO_SELECTION: //사진을 앨범에서 선택하는 경우
                        Log.d(TAG, "onActivityResult() for REQ_PHOTO_SELECTION.");

                        Uri fileuri=data.getData();
                        ContentResolver resolver=context.getContentResolver();

                        try {
                            InputStream inputStream=resolver.openInputStream(fileuri);
                            resultPhotoBitmap= BitmapFactory.decodeStream(inputStream);
                            pictureImageView.setImageBitmap(resultPhotoBitmap);

                            inputStream.close();

                            isPhotoCaptured=true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
            }
        }
    }

    public static Bitmap decodeSampleBitmapReomResource(File res, int reqWidth, int reqHeight){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(res.getAbsolutePath(), options);

        options.inSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds=false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(), options);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSampleSize=1;

        if(height>reqHeight || width>reqWidth){
            final int halfHeigth=height;
            final int halfWidth=width;

            while((halfHeigth/inSampleSize)>=reqHeight && (halfWidth/inSampleSize)>=reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }

    private String createFilename(){
        Date curDate=new Date();
        String curDatestr=String.valueOf(curDate.getTime());

        return curDatestr;

    }


    public void setWeather(String data){
        if(data != null){
            if(data.equals("맑음")){
                weatherIcon.setImageResource(R.drawable.weather_icon_1);
            }else if(data.equals("구름 조금")){
                weatherIcon.setImageResource(R.drawable.weather_icon_2);
            }else if(data.equals("구름 많음")){
                weatherIcon.setImageResource(R.drawable.weather_icon_3);
            }else if(data.equals("흐림")){
                weatherIcon.setImageResource(R.drawable.weather_icon_4);
            }else if(data.equals("비")){
                weatherIcon.setImageResource(R.drawable.weather_icon_5);
            }else if(data.equals("눈/비")){
                weatherIcon.setImageResource(R.drawable.weather_icon_6);
            }else if(data.equals("눈")) {
                weatherIcon.setImageResource(R.drawable.weather_icon_7);
            }else{
                Log.d("Fragment2","Unknown weather string"+data);
            }
        }
    }

    public void setAddress(String data) {
        locationTextView.setText(data);
    }

    public void setDateString(String dateString) {
        dateTextView.setText(dateString);
    }
}
