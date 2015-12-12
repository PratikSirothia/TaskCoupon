package com.task.couponduniyatask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.task.couponduniyatask.R;
import com.task.couponduniyatask.model.RestaurantPojo;
import com.task.couponduniyatask.utility.Utils;

import java.util.List;

/**
 * Created by getnow on 11/12/15.
 */
public class HomeAdapter extends
        RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final String TAG = "HomeAdapter";
    private static Context context;
    private List<RestaurantPojo> lstRestaurantPojoList;

    public HomeAdapter(Context context, List<RestaurantPojo> lstRestaurantPojoList) {
        this.context = context;
        this.lstRestaurantPojoList = lstRestaurantPojoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.home_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RestaurantPojo restaurantPojo = lstRestaurantPojoList.get(position);
        viewHolder.titleTextView.setText(restaurantPojo.getOutletName());
        viewHolder.categoriesTextView.setText(Html.fromHtml(restaurantPojo.getCateGories()));
        viewHolder.areaTextView.setText(restaurantPojo.getDistance() + " m " + restaurantPojo.getNeighbourhoodName());

        if (!Utils.isNullString(restaurantPojo.getNumCoupons())) {
            if (restaurantPojo.getNumCoupons().equals("1")) {
                viewHolder.offerTextView.setText(restaurantPojo.getNumCoupons() + " Offer");
            } else {
                viewHolder.offerTextView.setText(restaurantPojo.getNumCoupons() + " Offers");
            }

            viewHolder.offerTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.offerTextView.setVisibility(View.GONE);
        }

        Glide.with(context).load(restaurantPojo.getLogoURL()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(viewHolder.logoImageView);
    }

    @Override
    public int getItemCount() {
        return lstRestaurantPojoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView offerTextView;
        private TextView categoriesTextView;
        private TextView areaTextView;
        private ImageView logoImageView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView
                    .findViewById(R.id.home_title);
            offerTextView = (TextView) itemView.findViewById(R.id.home_offer);
            categoriesTextView = (TextView) itemView
                    .findViewById(R.id.home_cate_name);
            areaTextView = (TextView) itemView
                    .findViewById(R.id.home_area_name);
            logoImageView = (ImageView) itemView
                    .findViewById(R.id.home_logo);
        }

    }
}
