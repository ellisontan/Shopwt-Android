package top.yokey.shopwt.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;

import top.yokey.shopwt.R;

/**
 * 图片加载器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class BaseImageLoader {

    private Context context;
    private static volatile BaseImageLoader instance;

    public static BaseImageLoader get() {
        if (instance == null) {
            synchronized (BaseImageLoader.class) {
                if (instance == null) {
                    instance = new BaseImageLoader();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {

        this.context = context;

    }

    public Bitmap getSmall(String path) {

        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > 800 || width > 480) {
            int heightRatio = Math.round((float) height / (float) 800);
            int widthRatio = Math.round((float) width / (float) 480);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public Bitmap getLocal(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void display(String url, ImageView imageView) {

        if (BaseApplication.get().isImage()) {
            Glide.with(context).load(url).into(imageView);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).into(imageView);
        }

    }

    public void displayCircle(String url, ImageView imageView) {

        if (BaseApplication.get().isImage()) {
            Glide.with(context).load(url).apply(new RequestOptions().transform(new CircleTransform(context))).into(imageView);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).apply(new RequestOptions().transform(new CircleTransform(context))).into(imageView);
        }

    }

    public void displayRadius(String url, ImageView imageView) {

        if (BaseApplication.get().isImage()) {
            Glide.with(context).load(url).apply(new RequestOptions().transform(new RadiusTransform(context, 2))).into(imageView);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).apply(new RequestOptions().transform(new RadiusTransform(context, 2))).into(imageView);
        }

    }

    public void display(String url, int width, int height, ImageView imageView) {

        if (BaseApplication.get().isImage()) {
            Glide.with(context).load(url).apply(new RequestOptions().override(width, height)).into(imageView);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).apply(new RequestOptions().override(width, height)).into(imageView);
        }

    }

    class CircleTransform extends BitmapTransformation {

        public CircleTransform(Context context) {

        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

    }

    class RadiusTransform extends BitmapTransformation {

        private float radius = 0f;

        public RadiusTransform(Context context) {

            this(context, 4);

        }

        public RadiusTransform(Context context, int dip) {

            this.radius = Resources.getSystem().getDisplayMetrics().density * dip;

        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

    }

}
