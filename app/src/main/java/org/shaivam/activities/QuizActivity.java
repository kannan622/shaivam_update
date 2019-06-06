package org.shaivam.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.QuizViewPagerAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Quiz;
import org.shaivam.model.QuizAnswer;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends MainActivity {
  private Toolbar toolbar;
  public static List<Quiz> quizList = new ArrayList<Quiz>();
  private QuizViewPagerAdapter adapter;
  private ViewPager viewPager;
  CoordinatorLayout quiz_main;
  public TextView answer_status, correct_answer;
  public Button submit, next, prev;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);
    toolbar = findViewById(R.id.toolbar);
    quiz_main = findViewById(R.id.quiz_main);
    LogUtils.amplitudeLog(this, "Quiz Screen");
    TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
    Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
    textCustomTitle.setTypeface(customFont);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    correct_answer = findViewById(R.id.correct_answer);
    answer_status = findViewById(R.id.answer_status);
    submit = findViewById(R.id.submit);
    next = findViewById(R.id.next);
    prev = findViewById(R.id.prev);

    viewPager = findViewById(R.id.pager);

    answer_status.setVisibility(View.GONE);
    correct_answer.setVisibility(View.GONE);

    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      public void onPageScrollStateChanged(int state) {

      }

      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      public void onPageSelected(final int position) {
        answer_status.setVisibility(View.GONE);
        correct_answer.setVisibility(View.GONE);

        if (quizList.size() > 0) {
          submit.setVisibility(View.VISIBLE);
          if (position == 0)
            prev.setVisibility(View.GONE);
          else
            prev.setVisibility(View.VISIBLE);

          if (position == quizList.size() - 1)
            next.setVisibility(View.GONE);
          else
            next.setVisibility(View.VISIBLE);

        }
      }
    });
    if (quizList.size() == 1) {
      prev.setVisibility(View.GONE);
      next.setVisibility(View.GONE);
    } else if(quizList.size() > 0){
      prev.setVisibility(View.VISIBLE);
      next.setVisibility(View.VISIBLE);
    }
    prev.setVisibility(View.GONE);

    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Quiz model = quizList.get(viewPager.getCurrentItem());
        boolean isSelect = false;
        int selected_position = 0;

        for (int i = 0; i < quizList.get(viewPager.getCurrentItem()).getQuizAnswer().size(); i++) {
          if (quizList.get(viewPager.getCurrentItem()).getQuizAnswer().get(i).isSelected()) {
            isSelect = true;
            selected_position = i;
          }
        }

        if (isSelect) {
          if (quizList.get(viewPager.getCurrentItem()).getQuizAnswer().get(selected_position).getAnswer().equalsIgnoreCase(model.getCorrectanswer())) {
            answer_status.setVisibility(View.VISIBLE);
            answer_status.setBackgroundColor(getResources().getColor(R.color.correct_color_light));
            answer_status.setText(AppConfig.getTextString(QuizActivity.this, AppConfig.correct));
            answer_status.setTextColor(getResources().getColor(R.color.correct_color_dark));
            correct_answer.setVisibility(View.GONE);
          } else {
            answer_status.setVisibility(View.VISIBLE);
            answer_status.setBackgroundColor(getResources().getColor(R.color.wrong_color_light));
            answer_status.setText(AppConfig.getTextString(QuizActivity.this, AppConfig.wrong));
            answer_status.setTextColor(getResources().getColor(R.color.wrong_color_dark));
            correct_answer.setVisibility(View.VISIBLE);
            correct_answer.setText("Correct: " + model.getCorrectanswer());
          }
        } else {
          correct_answer.setVisibility(View.GONE);
          answer_status.setVisibility(View.GONE);
          Toast.makeText(QuizActivity.this, AppConfig.getTextString(QuizActivity.this, AppConfig.select_answer),
              Toast.LENGTH_SHORT).show();
        }
      }
    });

    prev.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int tab = viewPager.getCurrentItem();
        if (tab > 0) {
          tab--;
          viewPager.setCurrentItem(tab);
        } else if (tab == 0) {
          viewPager.setCurrentItem(tab);
        }
      }
    });

    // Images right navigatin
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int tab = viewPager.getCurrentItem();
        tab++;
        viewPager.setCurrentItem(tab);
      }
    });
    if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
      AppConfig.showProgDialiog(this);
      MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_QUIZ,
          null,
          mapCallBack, com.android.volley.Request.Method.POST);
    } else {
      snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
      submit.setVisibility(View.GONE);
      next.setVisibility(View.GONE);
      prev.setVisibility(View.GONE);
    }
  }

  VolleyCallback mapCallBack = new VolleyCallback() {
    @Override
    public void Success(int stauscode, String response) {
      try {
        JSONObject jObj = new JSONObject(response);
        if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
          quizList.clear();
          List<Quiz> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), Quiz[].class));
          for (int j = 0; j < data.size(); j++) {
            String[] answer = data.get(j).getAnswer().split("\\*");
            List<QuizAnswer> quizAnswers = new ArrayList<>();
            for (int i = 0; i < answer.length; i++) {
              quizAnswers.add(new QuizAnswer(false, answer[i]));
            }
            data.get(j).setQuizAnswer(quizAnswers);
            quizList.add(data.get(j));
          }

          submit.setVisibility(View.VISIBLE);
          prev.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          adapter = new QuizViewPagerAdapter(QuizActivity.this, quizList);
          viewPager.setOffscreenPageLimit(3);
          viewPager.setAdapter(adapter);
          // displaying selected image first
          viewPager.setCurrentItem(0);
          adapter.notifyDataSetChanged();

        } else {
          snackBar(jObj.getString("Message"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        snackBar(AppConfig.getTextString(QuizActivity.this, AppConfig.went_wrong));
      }
    }

    @Override
    public void Failure(int stauscode, String errorResponse) {
      snackBar(errorResponse);
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    AppConfig.customeLanguage(this);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

}
