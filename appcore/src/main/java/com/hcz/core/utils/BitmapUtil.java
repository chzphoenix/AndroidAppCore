package com.hcz.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    /**
     * 缩小图片
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap image, int width, int height) {
        Matrix matrix = new Matrix();
        float zoomW = (float) width / image.getWidth();
        float zoomH = (float) height / image.getHeight();
        float zoom = Math.min(zoomH, zoomW);
        zoom = zoom > 1 ? 1 : zoom;
        matrix.setScale(zoom, zoom);
        Bitmap output = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        if (image != output) {
            image.recycle();
        }
        return output;
    }

    /**
     * 质量压缩图片
     *
     * @param image
     * @param size  压缩的大小，注意未必会达到这个大小
     * @return
     */
    public static Bitmap compressImage(Bitmap image, long size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.size() > size && options > 0) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 获取本地照片的方向
     *
     * @param imgFilePath
     * @return digree 角度
     */
    public static int getImgOrientation(String imgFilePath) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        return digree;
    }

    /**
     * 保存bitmap到指定路径
     *
     * @param path
     * @param bitmap
     * @return
     */
    public static boolean saveBitmap(String path, Bitmap bitmap) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

}