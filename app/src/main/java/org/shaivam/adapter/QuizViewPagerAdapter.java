package org.shaivam.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.shaivam.R;
import org.shaivam.activities.QuizActivity;
import org.shaivam.model.Quiz;
import org.shaivam.model.QuizAnswer;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class QuizViewPagerAdapter extends PagerAdapter {

    private List<Quiz> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;
    QuizAnswerAdapter quizAnswerAdapter;

    // constructor
    public QuizViewPagerAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        mDataset = quizList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
    }

    @Override
    public int getCount() {
        return this.mDataset.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView question_text, question_count;
        RecyclerView recycle_quiz_list;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.adapter_quiz, container,
                false);
        question_text = viewLayout.findViewById(R.id.question_text);
        question_count = viewLayout.findViewById(R.id.question_count);
        recycle_quiz_list = viewLayout.findViewById(R.id.recycle_quiz_list);
        if (mDataset.size() > 0) {
            final Quiz quiz = mDataset.get(position);

            question_text.setText(quiz.getQuestion().trim());
            question_count.setText(String.format("%02d", position + 1)+"/"+ String.format("%02d",mDataset.size()));


            for (int j = 0; j < QuizActivity.quizList.size(); j++) {
                for (int i = 0; i < QuizActivity.quizList.get(j).getQuizAnswer().size(); i++) {
                    QuizActivity. quizList.get(j).getQuizAnswer().get(i).setSelected(false);
                }
            }

            quizAnswerAdapter = new QuizAnswerAdapter(context, position);
            recycle_quiz_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recycle_quiz_list.setAdapter(quizAnswerAdapter);
            recycle_quiz_list.setHasFixedSize(false);
            quizAnswerAdapter.notifyDataSetChanged();
        }
        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}