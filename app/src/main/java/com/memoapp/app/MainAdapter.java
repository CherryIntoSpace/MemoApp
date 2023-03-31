package com.memoapp.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
    private ArrayList<MainData> arrayList;
    /*MainAdapter 생성자 생성*/
    public MainAdapter(ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    /*MainAdapter의 CustomViewHolder를 사용하여 ViewHolder가 생성되었을 때의 생명 주기 onCreateViewHolder
    * 즉, MainActivity에서 mainAdapter = new MainAdapter(arrayList); 가 선언되었을 때 다음 생명 주기가
    * 발동됨*/
    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater의 from을 통해 LayoutInflater 생성, parent에서 LayoutInflater를 가져옴
        * 이후 item_list.xml를 inflate 하여 parent에 넣어 View 클래스의 view 생성*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,
                false);
        /*이렇게 item_list.xml의 레이아웃들을 inflate를 통해 객체화한 view를 인자로 하여 CustomViewHolder
        * 의 holder를 생성한다*/
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }
    /*이렇게 holder는 item_list의 id들을 가지고 있으므로 arrayList에 값이 추가되었을 때 setText를 통해
    * 화면에 출력할 수 있다*/
    /*이는 생성된 holder에 데이터를 바인딩할 수 있다. 다음 onBindViewHolder가 데이터 바인딩, 출력 과정이다*/
    @Override
    public void onBindViewHolder(@NonNull MainAdapter.CustomViewHolder holder, int position) {
        /*arrayList의 MainData 클래스로부터 tv_date와 tv_content를 받아와 setText(출력)*/
        holder.tv_date.setText(arrayList.get(position).getTv_date());
        holder.tv_content.setText(arrayList.get(position).getTv_content());

        /*holder의 tag 지정*/
        holder.itemView.setTag(position);
        /*holder(뷰)가 눌러졌을 때의 생명 주기 setOnClickListener*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*hoder의 tv_content를 curcontent에 저장, Toast 메세지를 띄움*/
                String curcontent = holder.tv_content.getText().toString();
                Toast.makeText(view.getContext(), curcontent, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(),
                        holder.tv_content.getText().toString() + " 삭제 완료!",
                        Toast.LENGTH_SHORT).show();
                remove(holder.getBindingAdapterPosition());
                return true;
            }
        });
    }

    @Override
    /*전체 데이터 개수를 return 하는 getItemCount*/
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }
    /*arrayList에서 해당 position의 아이템을 삭제하는 remove함수*/
    public void remove(int position){
        try {
            /*해당 position의 아이템을 삭제하고 추가했을 때와 마찬가지로 notifyItemRemoved로
            * arrayList의 갱신을 알려줌*/
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }
    /*CustomViewHolder는 RecyclerView.ViewHolder를 상속받은 클래스로 super()를 통해
    RecyclerViewd.ViewHolder의 생성자 itemView 생성, 이로써 item_list.xml의 id를 참조할 수 있다.*/
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_date;
        protected TextView tv_content;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_date = itemView.findViewById(R.id.tv_date);
            this.tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
