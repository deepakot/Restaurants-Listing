package com.deepak.restaurantslisting.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepak.restaurantslisting.Activities.MainActivity;
import com.deepak.restaurantslisting.Constants.AppConstants;
import com.deepak.restaurantslisting.Models.RestaurantData;
import com.deepak.restaurantslisting.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Sharma on 6/1/2016.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    private ArrayList<RestaurantData> restData;
    private Context context;
    public MyOnclickListener myOnclickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView restName, offers, distance;
        public ImageView like,restImage;
        public RelativeLayout movie_list;
        public Button category;
        public MyViewHolder(View view) {
            super(view);
            restName = (TextView) view.findViewById(R.id.restName);
            offers= (TextView) view.findViewById(R.id.offers);
            distance = (TextView) view.findViewById(R.id.distance);
            category= (Button) view.findViewById(R.id.category);
            like = (ImageView) view.findViewById(R.id.like);
            restImage = (ImageView) view.findViewById(R.id.restImage);
            movie_list = (RelativeLayout) view.findViewById(R.id.movie_list);
        }
    }


    public RestaurantAdapter(ArrayList<RestaurantData> restData, Context ctx) {
        this.restData = restData;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rest_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RestaurantData restaurant = restData.get(position);
        final int p=position;
        holder.restName.setText(restaurant.getRestname());
        holder.offers.setText(restaurant.getNoOfOffers()+ AppConstants.Offers);
        holder.distance.setText(restaurant.getDistance()+ AppConstants.Meter+restaurant.getLocality());
        Picasso.with(context)
                .load(restaurant.getRestImage())
                .placeholder(R.drawable.logo)
                .resize(150,46)
                .into(holder.restImage);
        holder.like.setImageResource(R.drawable.heart_off);
        if(!restaurant.isLiked())
        {
            holder.like.setImageResource(R.drawable.heart_off);
        }
        else
        {
            holder.like.setImageResource(R.drawable.heart_on);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!restData.get(p).isLiked())
                {
                    restData.get(p).setLiked(true);
                    holder.like.setImageResource(R.drawable.heart_on);
                }
                else
                {
                    restData.get(p).setLiked(false);
                    holder.like.setImageResource(R.drawable.heart_off);
                }
            }
        });
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnclickListener = ((MainActivity)context);
                myOnclickListener.openCategory(p);
            }
        });
    }
    public interface MyOnclickListener {
        public void openCategory(int position);
    }
    @Override
    public int getItemCount() {
        return restData.size();
    }
    public void setFilter(ArrayList<RestaurantData> countryModels){
        restData = new ArrayList<>();
        restData.addAll(countryModels);
        notifyDataSetChanged();
    }
}
