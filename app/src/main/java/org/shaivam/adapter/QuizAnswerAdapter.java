package org.shaivam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.QuizActivity;
import org.shaivam.model.Quiz;
import org.shaivam.model.QuizAnswer;
import org.shaivam.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private int main_position;

    public class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton answer_text_checkbox;
        ConstraintLayout answer_main;
        LinearLayout answer_text_linear;
        ImageView answer_text_image;
        TextView answer_text;

        public ViewHolder(View v) {
            super(v);

            answer_text_checkbox = v.findViewById(R.id.answer_text_checkbox);
            answer_main = v.findViewById(R.id.answer_main);
            answer_text_linear = v.findViewById(R.id.answer_text_linear);
            answer_text_image = v.findViewById(R.id.answer_text_image);
            answer_text = v.findViewById(R.id.answer_text);
        }
    }

    private void resetData() {
        for (int i = 0; i < QuizActivity.quizList.get(main_position).getQuizAnswer().size(); i++) {
            QuizActivity.quizList.get(main_position).getQuizAnswer().get(i).setSelected(false);
        }
    }

    QuizAnswerAdapter(Context context, int position) {
        this.main_position = position;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @NonNull @Override
    public QuizAnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_quiz_answer, parent, false);
        return new QuizAnswerAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final QuizAnswerAdapter.ViewHolder holder, final int position) {
        if (QuizActivity.quizList.get(main_position).getQuizAnswer().size() > 0) {
            final QuizAnswer quiz = QuizActivity.quizList.get(main_position).getQuizAnswer().get(position);
            holder.answer_text_checkbox.setText(quiz.getAnswer().trim());
            holder.answer_text.setText(quiz.getAnswer().trim());
            holder.answer_text_checkbox.setChecked(quiz.isSelected());
            holder.answer_text_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetData();
                    QuizActivity.quizList.get(main_position).getQuizAnswer().get(position).setSelected(true);
                    notifyDataSetChanged();
                }
            });

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {

        if (QuizActivity.quizList.get(main_position) != null)
            return QuizActivity.quizList.get(main_position).getQuizAnswer().size();
        else
            return 0;
    }
}