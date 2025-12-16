package com.example.somethingfortesting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int imageArray[] = {
            R.drawable.download,
            R.drawable.download1,
            R.drawable.download3
    };

    int headingArray[] = {
            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide
    };

    int descriptionArray[] = {
            R.string.description,
            R.string.description,
            R.string.description
    };

    @Override
    public int getCount() {
        return headingArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliding_layout, container, false);

        // find views from sliding_layout.xml
    ImageView slideImage = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
         TextView slideDescription = view.findViewById(R.id.slide_description);

        // set data
        slideImage.setImageResource(imageArray[position]);
        slideHeading.setText(headingArray[position]);
        slideDescription.setText(descriptionArray[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

