package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.EventDetailActivity;
import org.shaivam.model.Events;
import org.shaivam.utils.AppConfig;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
  private List<Events> mDataset;
  private Context context;
  private LayoutInflater inflater;
  private static int currentPosition = -1;
  public static Events staticEvents = new Events();

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView events_date, events_month, events_text, events_week, events_location;
    LinearLayout card_events;
    ImageView events_arrow;

    public ViewHolder(View v) {
      super(v);

      events_date = v.findViewById(R.id.events_date);
      events_month = v.findViewById(R.id.events_month);
      events_text = v.findViewById(R.id.events_text);
      events_week = v.findViewById(R.id.events_week);
      events_location = v.findViewById(R.id.events_location);
      card_events = v.findViewById(R.id.card_events);
      events_arrow = v.findViewById(R.id.events_arrow);
    }
  }

  public EventsAdapter(Context context, List<Events> eventsModelList) {
    this.context = context;
    mDataset = eventsModelList;
    inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    currentPosition = -1;
  }

  @Override
  public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
    View rootView = inflater.inflate(R.layout.adapter_events, parent, false);
    return new EventsAdapter.ViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(final EventsAdapter.ViewHolder holder, final int position) {
    if (mDataset.size() > 0) {
      final Events eventsModel = mDataset.get(position);
      Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);

      holder.events_date.setTypeface(tf);
      holder.events_month.setTypeface(tf);
      holder.events_text.setTypeface(tf);
      holder.events_week.setTypeface(tf);

      String[] dateParts;

      if (eventsModel.getFestivalSdate() != null && !eventsModel.getFestivalSdate().equalsIgnoreCase(""))
        dateParts = eventsModel.getFestivalSdate().split("-");
      else if (eventsModel.getCurrent() != null && !eventsModel.getCurrent().equalsIgnoreCase(""))
        dateParts = eventsModel.getCurrent().split("-");
      else
        dateParts = eventsModel.getCreatedDate().split("-");

      String day = dateParts[2];
      String month = dateParts[1];
      String year = dateParts[0];

      holder.events_date.setText(day.trim());

          /*  Calendar cal=Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            int monthnum=Integer.parseInt(month);
            cal.set(Calendar.MONTH,monthnum);
            String month_name = month_date.format(cal.getTime());
*/
      holder.events_month.setText(theMonth(Integer.parseInt(month) - 1));
      holder.events_text.setText(eventsModel.getNameOffestival().trim());
      holder.events_week.setText(getNameOfDay(Integer.parseInt(year), (Integer.parseInt(month) - 1), Integer.parseInt(day)).trim());
      holder.events_location.setText(eventsModel.getFestivalDistrict());

      if (position == currentPosition) {
        holder.card_events.setBackgroundResource(R.color.colorPrimary);
        holder.events_date.setTextColor(context.getResources().getColor(R.color.bg_white));
        holder.events_month.setTextColor(context.getResources().getColor(R.color.bg_white));
        holder.events_text.setTextColor(context.getResources().getColor(R.color.bg_white));
        holder.events_week.setTextColor(context.getResources().getColor(R.color.Yellow));
        holder.events_location.setTextColor(context.getResources().getColor(R.color.Yellow));
        holder.events_location.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_events_location_yellow),
            null, null, null);
      } else {
        holder.card_events.setBackgroundResource(R.color.bg_white);
        holder.events_date.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.events_month.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.events_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.events_week.setTextColor(context.getResources().getColor(R.color.text_grey));
        holder.events_location.setTextColor(context.getResources().getColor(R.color.text_grey));
        holder.events_location.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_events_location),
            null, null, null);
      }
      holder.card_events.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (currentPosition >= 0) {
            int prev = currentPosition;
            notifyItemChanged(prev);
          }
          if (currentPosition == holder.getPosition()) {
            currentPosition = holder.getPosition();
            notifyItemChanged(currentPosition);
            try {
              Intent intent = new Intent(context, EventDetailActivity.class);
              staticEvents = eventsModel;
              context.startActivity(intent);
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            currentPosition = holder.getPosition();
            notifyItemChanged(currentPosition);
            try {
              Intent intent = new Intent(context, EventDetailActivity.class);
              staticEvents = eventsModel;
              context.startActivity(intent);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      });
    }

  }

  public static String theMonth(int month) {
    String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
        "AUG", "SEP", "OCT", "NOV", "DEC"};
    return monthNames[month];
  }

  public String getNameOfDay(int year, int month, int dayOfYear) {

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfYear);
    String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);

    return days[dayIndex - 1];
  }

  public int getItemCount() {

    if (mDataset != null)
      return mDataset.size();
    else
      return 0;
  }
}