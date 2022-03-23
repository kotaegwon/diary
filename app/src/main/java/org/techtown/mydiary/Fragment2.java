package org.techtown.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    Context context;
    OnTabItemSelectedListener listener;
    Button save_btn,del_btn,close_btn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;

        if(context instanceof OnTabItemSelectedListener){
            listener=(OnTabItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(context!=null){
            context=null;
            listener=null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment2,container,false);
        initUI(rootview);
        return rootview;
    }

    //버튼 눌렀을때 화면 이동
    private void initUI(ViewGroup rootview) {
        save_btn=rootview.findViewById(R.id.saveButton);
        del_btn=rootview.findViewById(R.id.deleteButton);
        close_btn=rootview.findViewById(R.id.closeButton);

        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.saveButton:
                        break;
                    case R.id.deleteButton:
                        break;
                    case R.id.closeButton:
                        break;
                }
            }
        };
    }
}
