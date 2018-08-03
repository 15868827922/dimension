package com.ghnor.imagecompressor.spec.decoration;

import android.graphics.Bitmap;

/**
 * Created by ghnor on 2017/9/9.
 * ghnor.me@gmail.com
 */

public interface Decoration {
    Bitmap onDraw(Bitmap bitmap);
}
