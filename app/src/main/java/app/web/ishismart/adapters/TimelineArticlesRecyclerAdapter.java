package app.web.ishismart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.web.ishismart.R;
import app.web.ishismart.models.MorningTea;

public class TimelineArticlesRecyclerAdapter extends RecyclerView.Adapter<TimelineArticlesRecyclerAdapter.ViewHolder> {

    private static String TAG = "Timeline TeaRecycler Adapter : ";
    private List<MorningTea> morningTeaList;

    public TimelineArticlesRecyclerAdapter(List<MorningTea> morningTeaList) {
        this.morningTeaList = morningTeaList;
    }

    @NonNull
    @Override
    public TimelineArticlesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_timeline_layout, parent, false);
        return new TimelineArticlesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineArticlesRecyclerAdapter.ViewHolder holder, int position) {
        holder.time_ago.setText(String.valueOf(morningTeaList.get(position).getPost_date().getTimestamp().toDate()));
        holder.post_title.setText(morningTeaList.get(position).getMessage_title());
    }

    @Override
    public int getItemCount() {
        return morningTeaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time_ago, post_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time_ago = itemView.findViewById(R.id.time_ago);
            post_title = itemView.findViewById(R.id.post_title);
        }
    }
}
