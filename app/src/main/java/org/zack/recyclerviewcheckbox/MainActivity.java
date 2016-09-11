package org.zack.recyclerviewcheckbox;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private CheckBox selectAll;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private boolean []flag;//把flag数组定义为全局变量
    private List<String> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectAll = (CheckBox) findViewById(R.id.id_select_all);
        recyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        flag = new boolean[100];//初始化flag
        content = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            content.add("CheckBox" + i);
        }
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        //设置外面CheckBox的选中监听器，把它的选中状态赋值给其他的所有CheckBox,然后更新RecyclerView的Adapter
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (int i = 0; i < 100; i++) {
                    flag[i] = b;
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }



    private class MyAdapter extends RecyclerView.Adapter {

//        private List<String> content;
//        private boolean[] flag = new boolean[100];

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_recyclerview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.checkBox.setText(content.get(position));
            myViewHolder.checkBox.setOnCheckedChangeListener(null);
            myViewHolder.checkBox.setChecked(flag[position]);
            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    flag[position] = b;
                }
            });
            //设置监听器，当按钮被点击是，删除它所在的item
            myViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    content.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, content.size());//对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
                }
            });


        }

        @Override
        public int getItemCount() {
//            content = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                content.add("CheckBox" + i);
//            }
            return content.size();
        }
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private Button button;//定义删除按钮

        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.id_check_box);
            button = (Button) itemView.findViewById(R.id.id_delete);
        }
    }
}
