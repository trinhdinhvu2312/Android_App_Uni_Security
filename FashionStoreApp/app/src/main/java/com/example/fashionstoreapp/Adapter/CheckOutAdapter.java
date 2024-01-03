package com.example.fashionstoreapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionstoreapp.Model.Cart;
import com.example.fashionstoreapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder>{
    List<Cart> listCart;
    Context context;

    public CheckOutAdapter(List<Cart> listCart, Context context) {
        this.listCart = listCart;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckOutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_checkout_item, parent, false);
        return new CheckOutAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutAdapter.ViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        Glide.with(holder.itemView.getContext())
                .load(cart.getProduct().getProductImage().get(0).getUrl_Image())
                .into(holder.ivProductImage);

        holder.tvProductName.setText(cart.getProduct().getProduct_Name());
        holder.tvProductPrice.setText(en.format(cart.getProduct().getPrice()));
        holder.tvCount.setText(cart.getCount()+"");
        holder.tvTotalPrice.setText(en.format(cart.getCount() * cart.getProduct().getPrice()));
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvCount, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
