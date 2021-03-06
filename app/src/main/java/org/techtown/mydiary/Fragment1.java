package org.techtown.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lib.kingja.switchbutton.SwitchMultiButton;

public class Fragment1 extends Fragment {

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    Button todayWriteButton;
    SwitchMultiButton switchButton;

    Context context;
    OnTabItemSelectedListener listener;

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
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment1,container,false);
        initUI(rootview);
        return rootview;
    }
    private void initUI(ViewGroup rootview) {
        todayWriteButton=rootview.findViewById(R.id.todayWriteButton);
        todayWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onTabSelected(1);
                }
            }
        });
        switchButton=rootview.findViewById(R.id.switchButton);
        switchButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(getContext(), tabText, Toast.LENGTH_SHORT).show();
                noteAdapter.switchLayout(position);
                noteAdapter.notifyDataSetChanged();
            }
        });

        recyclerView=rootview.findViewById(R.id.recyclerView);

        //??????????????? ??? ???????????? ??????
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //????????? ?????? ??????
        noteAdapter=new NoteAdapter();

        noteAdapter.addItem(new Note(0,"0","????????? ?????????","","","?????? ?????? ?????????!","4",
                "capture1.jpg","2??? 10???"));
        noteAdapter.addItem(new Note(1,"1","????????? ?????????","","","????????? ???????????? ?????????","4",
                "capture1.jpg","2??? 11???"));
        noteAdapter.addItem(new Note(2,"0","????????? ?????????","","","?????? ????????? ?????? ?????????", "4",
                null,"2??? 12???"));

        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                Note item= noteAdapter.getItem(position);
                Toast.makeText(getContext(),"????????? ?????????: "+item.getContents(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
