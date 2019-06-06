package org.shaivam.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.shaivam.R;
import org.shaivam.adapter.EventsAdapter;
import org.shaivam.model.Events;
import org.shaivam.utils.AppConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends MainActivity {
  private Toolbar toolbar;
  @BindView(R.id.time_header_text)
  TextView time_header_text;
  @BindView(R.id.title_header_text)
  TextView title_header_text;

  @BindView(R.id.start_date_text)
  TextView start_date_text;
  @BindView(R.id.end_date_text)
  TextView end_date_text;
  @BindView(R.id.description_text)
  TextView description_text;
  @BindView(R.id.time_text)
  TextView time_text;
  @BindView(R.id.place_text)
  TextView place_text;
  @BindView(R.id.presenter_text)
  TextView presenter_text;
  @BindView(R.id.posted_by_text)
  TextView posted_by_text;

  @BindView(R.id.start_date_direction)
  TextView start_date_direction;
  @BindView(R.id.end_date_direction)
  TextView end_date_direction;
  @BindView(R.id.description_direction)
  TextView description_direction;
  @BindView(R.id.time_direction)
  TextView time_direction;
  @BindView(R.id.place_direction)
  TextView place_direction;
  @BindView(R.id.presenter_direction)
  TextView presenter_direction;
  @BindView(R.id.posted_by_direction)
  TextView posted_by_direction;

  @BindView(R.id.event_image)
  ImageView event_image;

  @BindView(R.id.event_detail_main)
  CoordinatorLayout event_detail_main;

  @BindView(R.id.audiio_vertical_view)
  View audiio_vertical_view;

  @BindView(R.id.video_vertical_view)
  View video_vertical_view;

  @BindView(R.id.audiio_by_linear)
  LinearLayout audiio_by_linear;

  @BindView(R.id.video_by_linear)
  LinearLayout video_by_linear;

  @BindView(R.id.audiio_by_direction)
  TextView audiio_by_direction;

  @BindView(R.id.video_by_direction)
  TextView video_by_direction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);
    ButterKnife.bind(this);
    toolbar = findViewById(R.id.toolbar);
    TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
    Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
    textCustomTitle.setTypeface(customFont);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setFont();
    setData();

  }

  public void setFont() {
    Typeface tf = Typeface.createFromAsset(getAssets(), AppConfig.FONT_KAVIVANAR);

    time_header_text.setTypeface(tf);
    title_header_text.setTypeface(tf);

    start_date_text.setTypeface(tf);
    end_date_text.setTypeface(tf);
    description_text.setTypeface(tf);
    time_text.setTypeface(tf);
    place_text.setTypeface(tf);
    presenter_text.setTypeface(tf);
    posted_by_text.setTypeface(tf);

    start_date_direction.setTypeface(tf);
    end_date_direction.setTypeface(tf);
    description_direction.setTypeface(tf);
    time_direction.setTypeface(tf);
    place_direction.setTypeface(tf);
    presenter_direction.setTypeface(tf);
    posted_by_direction.setTypeface(tf);
  }

  @Override
  protected void onResume() {
    super.onResume();
    AppConfig.customeLanguage(this);
  }

  private void setData() {
    final Events events = EventsAdapter.staticEvents;
    title_header_text.setText(events.getNameOffestival());
    if (events.getFestivalSdate() != null && !events.getFestivalSdate().equalsIgnoreCase(""))
      start_date_direction.setText(events.getFestivalSdate());
    else
      start_date_direction.setText(events.getCurrent());
    if (events.getFestivalEdate() != null && !events.getFestivalEdate().equalsIgnoreCase(""))
      end_date_direction.setText(events.getFestivalEdate());
    else
      end_date_direction.setText(events.getCurrent());
    description_direction.setText(Html.fromHtml(events.getFestivalDescription()));
//    time_direction.setText(events.get());
    if (!events.getFestivalLocation().equalsIgnoreCase("") && !events.getFestivalDistrict().equalsIgnoreCase(""))
      place_direction.setText(events.getFestivalLocation().trim() + ", " + events.getFestivalDistrict());
    else if (!events.getFestivalLocation().equalsIgnoreCase(""))
      place_direction.setText(events.getFestivalLocation());
    else
      place_direction.setText(events.getFestivalDistrict());

    presenter_direction.setText(events.getFestivalContactname());
    posted_by_direction.setText(events.getFestival_eventcategory());

    if (events.getFestivalImage() != null && !events.getFestivalImage().equalsIgnoreCase("")) {
      final String[] imageUrl = events.getFestivalImage().split("\\*");
      if (imageUrl != null && imageUrl.length > 0) {
        event_image.setVisibility(View.VISIBLE);
        Glide.with(this)
            .load(AppConfig.URL_EVENT_IMAGE + imageUrl[0])
            .into(event_image);
        event_image.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            Intent intent = new Intent(EventDetailActivity.this, WebViewActivity.class);
            intent.putExtra("url", AppConfig.URL_EVENT_IMAGE + imageUrl[0]);
            startActivity(intent);
          }
        });
      } else
        event_image.setVisibility(View.GONE);

    } else {
      event_image.setVisibility(View.GONE);
    }

    if (events.getAudioUrl() != null && !events.getAudioUrl().equalsIgnoreCase("")) {
      audiio_by_linear.setVisibility(View.VISIBLE);
      audiio_vertical_view.setVisibility(View.VISIBLE);
      audiio_by_direction.setText(events.getAudioUrl());
      audiio_by_direction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(EventDetailActivity.this, WebViewActivity.class);
          intent.putExtra("url", events.getAudioUrl());
          startActivity(intent);
        }
      });
    } else {
      audiio_by_linear.setVisibility(View.GONE);
      audiio_vertical_view.setVisibility(View.GONE);
    }

    if (events.getVideoUrl() != null && !events.getVideoUrl().equalsIgnoreCase("")) {
      video_by_linear.setVisibility(View.VISIBLE);
      video_vertical_view.setVisibility(View.VISIBLE);
      video_by_direction.setText(events.getVideoUrl());
      video_by_direction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(EventDetailActivity.this, WebViewActivity.class);
          intent.putExtra("url", events.getVideoUrl());
          startActivity(intent);
        }
      });
    } else {
      video_by_linear.setVisibility(View.GONE);
      video_vertical_view.setVisibility(View.GONE);
    }

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}
