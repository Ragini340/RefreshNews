package com.kiit.eee.ragini.refreshnews.apicall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by 1807340_RAGINI on 06,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class ImageDownloaderHelper {
    private  void downloadReSizedImgWithRotation(Context ctx, String url, int widthInPx, int heightInPx, int placeHolderImg, int errorImg, int rotateImgInDegree) {
        if (ctx != null &&  (widthInPx != 0 || heightInPx != 0)) {
           Picasso.get().load(url).rotate(rotateImgInDegree).placeholder(placeHolderImg).error(errorImg).resize(widthInPx, heightInPx).into(new PicassTarget());
        }
    }

    private void downloadResizedImgFileWihtoutRotation(Context ctx, File file, int widthInPx, int heightInPx, int placeHolderImg, int errorImg, int rotateImgInDegree){
        if(ctx != null && (widthInPx != 0 || heightInPx != 0 )){
            Picasso.get().load(file)./*.rotate(rotateImgInDegree).*/placeholder(placeHolderImg).error(errorImg).resize(widthInPx, heightInPx).into(new PicassTarget());
        }
    }

    private class PicassTarget implements Target {


        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

}

