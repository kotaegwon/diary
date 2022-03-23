package org.techtown.mydiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements OnNoteItemClickListener{
    ArrayList<Note> items=new ArrayList<Note>();

    OnNoteItemClickListener listener;

    //내용 중심인지 사진 중심인지 판단하기 위한 변수
    int layoutType=0;

    //어댑터 클래스 상속시 구현해야 할 함수 3가지 : onCreateViewHoloder, onBindViewHolder, getItemCount
    //리사이클뷰에 들어갈 뷰  홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adapter<>
    //에서 <>안에 들어가는 타입을 따른다
    @NonNull
    @Override
    //뷰홀더 생성
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View itemview=inflater.inflate(R.layout.note_item,viewGroup,false);
        return  new ViewHolder(itemview,this,layoutType);
    }

    //실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Note item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.setLayoutType(layoutType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(ArrayList<Note> items){
        this.items=items;
    }
    //아이템 반환
    public Note getItem(int position){
        return items.get(position);
    }
    //아이템 추가
    public void addItem(Note item){
        items.add(item);
    }

    public void setOnitemClickListener(OnNoteItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public void switchLayout(int position){
        layoutType=position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll1;
        LinearLayout ll2;

        ImageView moodIV1;
        ImageView moodTV2;

        ImageView pictureExistsIV;
        ImageView pictureIV;

        ImageView weatherIV01;
        ImageView weatherIV02;

        TextView contentsTV1;
        TextView contentsTV2;

        TextView locationTV1;
        TextView locationTV2;

        TextView dateTV1;
        TextView dateTV2;

        public ViewHolder(@NonNull View itemView, NoteAdapter noteAdapter, int layoutType) {
            super(itemView);
            ll1=itemView.findViewById(R.id.ll_01);
            ll2=itemView.findViewById(R.id.ll_02);

            moodIV1=itemView.findViewById(R.id.moodIV);
            moodTV2=itemView.findViewById(R.id.moodIV02);

            pictureExistsIV=itemView.findViewById(R.id.pictureExistsIV);
            pictureIV=itemView.findViewById(R.id.pictureIV);

            weatherIV01= itemView.findViewById(R.id.weatherIV01);
            weatherIV02= itemView.findViewById(R.id.weatherIV02);

            contentsTV1=itemView.findViewById(R.id.contentTV_1);
            contentsTV2=itemView.findViewById(R.id.contentTV_2);

            locationTV1=itemView.findViewById(R.id.locationTv01);
            locationTV2=itemView.findViewById(R.id.locationTv02);

            dateTV1=itemView.findViewById(R.id.dateTv01);
            dateTV2=itemView.findViewById(R.id.dateTv02);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
            setLayoutType(layoutType);
        }

        public void setItem(Note item) {
            String mood=item.getMood();
            int moodIndex=Integer.parseInt(mood);
            setMoodImage(moodIndex);
        }

        private void setMoodImage(int moodIndex) {
        }

        public void setLayoutType(int layoutType) {
        }
    }
}
