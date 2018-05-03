package com.xiaomo.travelhelper.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.WindowManager;

import net.bither.util.CompressTools;
import net.bither.util.FileUtil;

import java.io.File;


public class UIUtils {
    /**
     * dip-->px
     */
    public static int dip2Px(Context context,int dip) {

        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();

    }






    /**
     * 压缩图片
     * @param context
     * @param oldFile
     * @param targetPath
     */
    public static void compressFile(Context context, File oldFile, final String targetPath, final CompressListener compressListener){

        CompressTools.newBuilder(context).setMaxWidth(1080) // 默认最大宽度为720
                .setMaxHeight(1920) // 默认最大高度为960
                .setQuality(60) // 默认压缩质量为60,60足够清晰
                //.setKeepResolution(true)//设置是否保持原图分辨率，则设置的最大宽高就无效了。不需要设置最大宽高了。设置也不会报错了，该参数默认false
                .setBitmapFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setFileName(oldFile.getName()).setDestinationDirectoryPath(targetPath).build()
                .compressToFile(oldFile, new CompressTools.OnCompressListener()
                {
                    @Override
                    public void onStart() {
                        if(compressListener != null){
                            compressListener.onStart();
                        }
                    }
                    @Override
                    public void onFail(String error) {
                        if(compressListener != null){
                            compressListener.onFail(error);
                        }
                    }
                    @Override
                    public void onSuccess(File file) {
                        if(compressListener != null){
                            compressListener.onSuccess(file,targetPath);
                        }
                    }
                });
    }

    /**
     * 压缩图片
     * @param context
     * @param oldFile
     * @param targetPath
     */
    public static void compressBitmap(Context context, File oldFile, final String targetPath, final CompressListener compressListener){

        CompressTools.newBuilder(context).setMaxWidth(1080) // 默认最大宽度为720
                .setMaxHeight(1920) // 默认最大高度为960
                .setQuality(60) // 默认压缩质量为60,60足够清晰
                .setBitmapFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setFileName(oldFile.getName()).setDestinationDirectoryPath(targetPath).build()
                .compressToBitmap(oldFile, new CompressTools.OnCompressBitmapListener()
                {
                    @Override
                    public void onStart() {
                        if(compressListener != null){
                            compressListener.onStart();
                        }
                    }
                    @Override
                    public void onFail(String error) {
                        if(compressListener != null){
                            compressListener.onFail(error);
                        }
                    }
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if(compressListener != null){
                            compressListener.onSuccess(bitmap,targetPath);
                        }
                    }
                });
    }


    public interface CompressListener{

        void onStart();

        void onSuccess(Object o,String path);

        void onFail(String error);

    }



}
