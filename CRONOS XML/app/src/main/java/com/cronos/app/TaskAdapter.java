package com.cronos.app;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskActionListener {
        void onCheckedChanged(TaskItem task, boolean completed);
        void onDelete(TaskItem task);
    }

    private List<TaskItem> tasks;
    private final OnTaskActionListener listener;

    public TaskAdapter(List<TaskItem> tasks, OnTaskActionListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    public void updateTasks(List<TaskItem> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem task = tasks.get(position);
        holder.tvTaskTitle.setText(task.getTitle());
        holder.tvTaskDescription.setText(task.getDescription());
        holder.tvTaskCategory.setText(task.getCategory());
        holder.tvTaskMeta.setText("📅 " + fechaCorta(task.getDueDate())
                + (task.getTag().isEmpty() ? "" : "   #" + task.getTag()));

        // color segun la prioridad
        holder.tvTaskPriority.setText("• " + task.getPriority());
        int color;
        if ("Alta".equals(task.getPriority())) color = 0xFFE5484D;
        else if ("Media".equals(task.getPriority())) color = 0xFFF59E0B;
        else color = 0xFF18B981;
        holder.tvTaskPriority.setTextColor(color);

        holder.cbTaskDone.setOnCheckedChangeListener(null);
        holder.cbTaskDone.setChecked(task.isCompleted());
        aplicarEstiloCompletado(holder.tvTaskTitle, task.isCompleted());

        holder.cbTaskDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            aplicarEstiloCompletado(holder.tvTaskTitle, isChecked);
            listener.onCheckedChanged(task, isChecked);
        });

        holder.btnDeleteTask.setOnClickListener(view -> listener.onDelete(task));
    }

    // pasa 11/09/2026 a 11 sept
    private String fechaCorta(String fecha) {
        String[] meses = {"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sept", "oct", "nov", "dic"};
        try {
            String[] partes = fecha.trim().split(" ")[0].split("/");
            int mes = Integer.parseInt(partes[1]);
            return Integer.parseInt(partes[0]) + " " + meses[mes - 1];
        } catch (Exception e) {
            return fecha;
        }
    }

    private void aplicarEstiloCompletado(TextView textView, boolean completed) {
        if (completed) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbTaskDone;
        TextView tvTaskTitle;
        TextView tvTaskDescription;
        TextView tvTaskCategory;
        TextView tvTaskMeta;
        TextView tvTaskPriority;
        ImageButton btnDeleteTask;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTaskDone = itemView.findViewById(R.id.cbTaskDone);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            tvTaskCategory = itemView.findViewById(R.id.tvTaskCategory);
            tvTaskMeta = itemView.findViewById(R.id.tvTaskMeta);
            tvTaskPriority = itemView.findViewById(R.id.tvTaskPriority);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}
