package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiSongsDetailActivity;
import org.shaivam.activities.ThirumuraiSongsListActivity;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;

import java.util.List;

public class SearchTirumuraiAdapter extends RecyclerView.Adapter<SearchTirumuraiAdapter.ViewHolder> {

    private List<Thirumurai_songs> thirumurais;
    private List<String> filterdTypes;
    private Context context;
    String search_text;

    public SearchTirumuraiAdapter(Context context, String search_text, List<Thirumurai_songs> thirumurais, List<String> filterdTypes) {
        this.context = context;
        this.thirumurais = thirumurais;
        this.filterdTypes = filterdTypes;
        this.search_text = search_text;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_tirumurai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.thalam))) {
            holder.searchkey.setText(thirumurais.get(position).getThalam());
            holder.searchtype.setText(filterdTypes.get(position));
        } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.pann))) {
            holder.searchkey.setText(thirumurais.get(position).getPann() + " - " +
                    thirumurais.get(position).getThirumuraiId());
            holder.searchtype.setText(filterdTypes.get(position));
        } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.nadu))) {
            holder.searchkey.setText(thirumurais.get(position).getCountry());
            holder.searchtype.setText(filterdTypes.get(position));
        } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.aruliyavar))) {
            holder.searchkey.setText(thirumurais.get(position).getAuthor());
            holder.searchtype.setText(filterdTypes.get(position));
        } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.paadal))) {
            holder.searchkey.setText(thirumurais.get(position).getTitle());
            holder.searchtype.setText(filterdTypes.get(position));
        } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.paadal_varigal))) {
            holder.searchkey.setText(thirumurais.get(position).getSong().replace("\\n", "\n").trim());
            holder.searchtype.setText(filterdTypes.get(position));
        }

        holder.search_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.thalam))) {
                    Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                    intent.putExtra("thalam", thirumurais.get(position).getThalam());
                    context.startActivity(intent);
                } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.pann))) {
                    Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                    intent.putExtra("thirumurai_id", thirumurais.get(position).getThirumuraiId());
                    intent.putExtra("pann", thirumurais.get(position).getPann());
                    context.startActivity(intent);
                } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.nadu))) {
                    Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                    intent.putExtra("country", thirumurais.get(position).getCountry());
                    context.startActivity(intent);
                } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.aruliyavar))) {
                    Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                    intent.putExtra("author", thirumurais.get(position).getAuthor());
                    context.startActivity(intent);
                } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.paadal))) {
                    Intent intent = new Intent(context, ThirumuraiSongsDetailActivity.class);
                    intent.putExtra("title", thirumurais.get(position).getTitle());
                    context.startActivity(intent);
                } else if (filterdTypes.get(position).equalsIgnoreCase(AppConfig.getTextString(context, AppConfig.paadal_varigal))) {
                    Intent intent = new Intent(context, ThirumuraiSongsDetailActivity.class);
                    intent.putExtra("title", thirumurais.get(position).getTitle());
                    intent.putExtra("search_item", thirumurais.get(position).getSong());
                    context.startActivity(intent);
                }
            }
        });
        highlightString(holder.searchkey, search_text);
    }

    private void highlightString(TextView textView, String input) {
//Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(textView.getText());
//Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span : backgroundSpans) {
            spannableString.removeSpan(span);
        }

//Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword > 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + input.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }

//Set the final text on TextView
        textView.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return thirumurais.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView searchkey, searchtype;
        CardView search_cardview;

        ViewHolder(View itemView) {
            super(itemView);

            searchkey = itemView.findViewById(R.id.searchkey);
            searchtype = itemView.findViewById(R.id.searchtype);
            search_cardview = itemView.findViewById(R.id.search_cardview);

        }
    }

    public void filterList(String search_text, List<Thirumurai_songs> filterdNames, List<String> filterdTypes) {
        this.search_text = search_text;
        this.thirumurais = filterdNames;
        this.filterdTypes = filterdTypes;
        notifyDataSetChanged();
    }
}