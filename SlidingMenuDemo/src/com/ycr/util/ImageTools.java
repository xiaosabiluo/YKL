package com.ycr.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageTools {
//Drawable-->Bitmap
public static Bitmap drawableToBitmap(Drawable drawable){ 
int width = drawable.getIntrinsicWidth(); 
int height = drawable.getIntrinsicHeight(); 
Bitmap bitmap = Bitmap.createBitmap(width, height, 
drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 
: Bitmap.Config.RGB_565); 
Canvas canvas = new Canvas(bitmap); 
drawable.setBounds(0,0,width,height); 
drawable.draw(canvas); 
return bitmap; 

}
//Bitmap--> Drawable
public static Drawable BitmapConvertToDrawale(Bitmap bitmap) {
// Bitmap bitmap = new Bitmap();
@SuppressWarnings("deprecation")
Drawable drawable = new BitmapDrawable(bitmap);
return drawable;
}
// Bitmap-->Drawable
/*
* public static Bitmap DrawableConvertToBitmap(long id) { Resources
* res=getResources();
* 
* Bitmap bmp=BitmapFactory.decodeResource(,id);
* 
* return bmp; }
*/
// Bitmap-->Byte
public static byte[] Bitmap2Bytes(Bitmap bm) {
ByteArrayOutputStream baos = new ByteArrayOutputStream();
bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
return baos.toByteArray();
}
// Byte-->Bitmap
public static Bitmap Bytes2Bimap(byte[] b) {
if (b.length != 0) {
return BitmapFactory.decodeByteArray(b, 0, b.length);
}
else {
return null;
}
}
//改变图片的颜色
public static Bitmap invert(Bitmap src) {
Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
src.getConfig());
//A表示透明度,R,G,B是RGB调色
int A, R, G, B;
int pixelColor;
int height = src.getHeight();
int width = src.getWidth();
for (int y = 0; y < height; y++) {
for (int x = 0; x < width; x++) {
pixelColor = src.getPixel(x, y);
A = Color.alpha(pixelColor);
R = 255 - Color.red(pixelColor);
G = 255 - Color.green(pixelColor);
B = 255 - Color.blue(pixelColor);
output.setPixel(x, y, Color.argb(A, R, G, B));
}
}
return output;
}

public static byte[] getImage(String urlpath) throws Exception {
URL url = new URL(urlpath);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
conn.setConnectTimeout(6 * 1000);
// 别超过10秒。
if(conn.getResponseCode()==200){
InputStream inputStream=conn.getInputStream();
return readStream(inputStream);
}
return null;
}

/**
* 读取数据 
* 输入流
* 
* */
public static byte[] readStream(InputStream inStream) throws Exception {
ByteArrayOutputStream outstream=new ByteArrayOutputStream();
byte[] buffer=new byte[1024];
int len=-1;
while((len=inStream.read(buffer)) !=-1){
outstream.write(buffer, 0, len);
}
outstream.close();
inStream.close();

return outstream.toByteArray();
}
}