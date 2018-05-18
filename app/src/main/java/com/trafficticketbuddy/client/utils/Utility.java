package com.trafficticketbuddy.client.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;

import com.trafficticketbuddy.client.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class Utility {
	

	

	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }


	public static String readFileFromSDCard(String path, String fileName){

		File file = new File(path,fileName);
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		}
		catch (IOException e) {
			return null;
		}
		return text.toString();
	}
	 

    public static int dpToPx(Context context, int size) {
	  Resources r = context.getResources();
	  float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());
	  return (int) px;
    }

    public static Bitmap rotateBitmap(int rotation, Bitmap bmp) {
	  if (rotation != 0) {
		Matrix matrix = new Matrix();
		matrix.postRotate(rotation);
		bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	  }
	  return bmp;
    }


    public static File saveImage(Bitmap myBitmap, File cacheDir) {

	  File file = new File(cacheDir, "ShareNPay_" + new Date().getTime() + ".jpg");
	  if (file.exists()) file.delete();
	  try {
		FileOutputStream out = new FileOutputStream(file);
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		out.flush();
		out.close();

	  } catch (Exception e) {
		e.printStackTrace();
	  }
	  return file;
    }

    public static Bitmap scaledDownBitmap(String imagePath, float dh, float dw) {
	  BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	  bmpFactoryOptions.inJustDecodeBounds = true;

	  BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);

	  int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / dh);
	  int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / dw);

	  if (heightRatio > 1 && widthRatio > 1) {
		if (heightRatio > widthRatio) {
		    bmpFactoryOptions.inSampleSize = heightRatio;
		} else {
		    bmpFactoryOptions.inSampleSize = widthRatio;
		}
	  }
	  bmpFactoryOptions.inJustDecodeBounds = false;

	  return BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);
    }

    public static File getApplicationCacheDir(Context context) {
	  File cacheDir = new File(Environment.getExternalStorageDirectory()
		    .getAbsolutePath()
		    + File.separator
		    + context.getString(R.string.app_name)
		    + "_cache"
		    + File.separator);
	  if (!cacheDir.exists()) cacheDir.mkdirs();

	  return cacheDir;
    }

    public static byte[] getByteArrayImage(String imagePath){
	  Bitmap bm = BitmapFactory.decodeFile(imagePath);
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
	  byte[] b = baos.toByteArray();
	  return b;
    }

	public static int getRotationFromMediaStore(Context context, Uri imageUri) {
		String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
		Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
		if (cursor == null) return 0;

		cursor.moveToFirst();

		int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
		return cursor.getInt(orientationColumnIndex);
	}

	public static int getCameraPhotoOrientation(String imageFilePath) {
		int rotate = 0;
		try {

			ExifInterface exif;

			exif = new ExifInterface(imageFilePath);
			String exifOrientation = exif
					.getAttribute(ExifInterface.TAG_ORIENTATION);
			Log.d("exifOrientation", exifOrientation);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			Log.d("HOM", "orientation :" + orientation);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rotate;
	}


	public static String getMonth(int val){
		String month = "";
		switch (val){
			case 1:
				month = "JAN";
				break;
			case 2:
				month = "FEB";
				break;
			case 3:
				month = "MAR";
				break;
			case 4:
				month = "APR";
				break;
			case 5:
				month = "MAY";
				break;
			case 6:
				month = "JUN";
				break;
			case 7:
				month = "JUL";
				break;
			case 8:
				month = "AUG";
				break;
			case 9:
				month = "SEP";
				break;
			case 10:
				month = "OCT";
				break;
			case 11:
				month = "NOV";
				break;
			case 12:
				month = "DEC";
				break;

		}
		return  month;
	}

	public static String  getYearFormated(String year){
		return year.substring(2,4);
	}
	public static String  getDayFormated(int val){
		if(val ==1){
			return "1st";
		}else if(val ==2){
			return "2nd";
		}else if(val ==3){
			return "3rd";
		}else {
			return val+"th";
		}

	}

}
