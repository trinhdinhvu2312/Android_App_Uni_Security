package com.example.fashionstoreapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fashionstoreapp.Fragment.AllOrderFragment;
import com.example.fashionstoreapp.Fragment.PayOnDeliveryFragment;
import com.example.fashionstoreapp.Fragment.PayWithZalopayFragment;

public class OrderFragmentAdapter extends FragmentStateAdapter {


    public OrderFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new AllOrderFragment();
        else if (position == 1) {
            return new PayOnDeliveryFragment();
        }
        else
            return new PayWithZalopayFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
