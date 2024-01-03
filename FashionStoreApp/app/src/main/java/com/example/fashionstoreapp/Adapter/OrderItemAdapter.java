package com.example.fashionstoreapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionstoreapp.Model.Order_Item;
import com.example.fashionstoreapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    List<Order_Item> order_items;
    Context context;

    public OrderItemAdapter(List<Order_Item> order_items, Context context) {
        this.order_items = order_items;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_item, parent, false);
        return new OrderItemAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        Order_Item order_item = order_items.get(position);
        Glide.with(holder.itemView.getContext())
                .load(order_item.getProduct().getProductImage().get(0).getUrl_Image())
                .into(holder.ivProductImage);
        holder.tvProductName.setText(order_item.getProduct().getProduct_Name());
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        holder.tvPrice.setText(en.format(order_item.getProduct().getPrice()));
        holder.tvUnits.setText(order_item.getCount()+"");
        holder.tvTotalPrice.setText(en.format(order_item.getProduct().getPrice()*order_item.getCount()));
    }

    @Override
    public int getItemCount() {
        return  order_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvUnits, tvPrice, tvTotalPrice;
        ImageView ivProductImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
