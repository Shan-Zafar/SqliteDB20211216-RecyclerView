package haqnawaz.org.sqlitedb20211216;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.MyViewHolder> {
    public AdapterListener onClickListener;
    ArrayList<StudentModel> Students;

    public myRecyclerViewAdapter(ArrayList<StudentModel> students) {
        this.Students = students;
    }

    public void setOnItemClickListener(AdapterListener listener)
    {
        this.onClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nametv;
        TextView agetv;
        TextView statustv;
        Button delBtn;
        Button updateBtn;
        StudentModel data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.name);
            agetv = itemView.findViewById(R.id.age);
            statustv = itemView.findViewById(R.id.status);
            delBtn = itemView.findViewById(R.id.delbtn);
            updateBtn = itemView.findViewById(R.id.updatebtn);
        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View singleitem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singleitem, parent, false);
        return new MyViewHolder(singleitem);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.data=Students.get(position);
        holder.nametv.setText(holder.data.getName());
        holder.agetv.setText(String.valueOf(holder.data.getAge()));
        if (holder.data.isActive())
            holder.statustv.setText("Active");
        else
            holder.statustv.setText("Inactive");

        holder.delBtn.setOnClickListener(view -> onClickListener.onRecyclerViewItemClicked(position, view.getId()));

        holder.updateBtn.setOnClickListener(view -> onClickListener.onRecyclerViewItemClicked(position, view.getId()));


    }

    @Override
    public int getItemCount() {
        return Students.size();
    }

    public void deleteRecord(int position)
    {
        Students.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,Students.size());
    }

    public void updateStudent(int id, String name, int age, boolean status)
    {
        StudentModel student = Students.get(id);
        student.setName(name);
        student.setAge(age);
        student.setActive(status);
        notifyItemChanged(id);
    }

}
