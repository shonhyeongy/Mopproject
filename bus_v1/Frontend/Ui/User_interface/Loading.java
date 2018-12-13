package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.sonhyeongi.bus_v1.R;

public class Loading extends Activity{

    public static Activity loading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading = Loading.this;
        Log.d("loading","loading");


        setContentView(R.layout.loading);

        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setBackground(new ShapeDrawable(new OvalShape()));
        iv.setClipToOutline(true);




        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this).load(R.raw.loading3).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageViewTarget);
    }

    public static Activity getloading(){
        return loading;
    }
    public static void setloading(){
        loading = null;
    }



    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        return;
    }










}
