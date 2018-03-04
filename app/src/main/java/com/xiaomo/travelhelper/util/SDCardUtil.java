package com.xiaomo.travelhelper.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.xiaomo.travelhelper.MemoEditActivity;
import com.xiaomo.travelhelper.model.memo.MemoConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDCardUtil {

	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获得文章图片保存目录
	 * @return
	 */
	public static String getPictureDir(){
		String imageCacheUrl = SDCardRoot + "TravelHelper"+ File.separator+"XRichText" + File.separator ;
		File file = new File(imageCacheUrl);
		if(!file.exists())
			file.mkdir();  //如果不存在则创建
		return imageCacheUrl;
	}

	/**
	 * 图片保存到SD卡
	 * @param bitmap
	 * @return
	 */
	public static String saveToSdCard(Bitmap bitmap) {
		String imageUrl = getPictureDir() + getPhotoFileName();
		File file = new File(imageUrl);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 保存到指定路径，笔记中插入图片
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static String saveToSdCard(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("文件保存路径："+ file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	/** 删除文件 **/
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists())
			file.delete(); // 删除文件
	}

	/**
	 * 根据Uri获取图片文件的绝对路径
	 */
	public static String getFilePathByUri(Context context, final Uri uri) {
		if (null == uri) {
			return null;
		}
		final String scheme = uri.getScheme();
		if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
			return uri.getPath();
		}
		if (ContentResolver.SCHEME_CONTENT.equals(scheme)
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			String data = null;
			Cursor cursor = context.getContentResolver().query(uri,
					new String[]{MediaStore.Images.Media.DATA}, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
			return data;
		}
		if (ContentResolver.SCHEME_CONTENT.equals(scheme)
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (DocumentsContract.isDocumentUri(context, uri)) {
				if (isExternalStorageDocument(uri)) {
					// ExternalStorageProvider
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];
					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}
				} else if (isDownloadsDocument(uri)) {
					// DownloadsProvider
					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
							Long.valueOf(id));
					return getDataColumn(context, contentUri, null, null);

				} else if (isMediaDocument(uri)) {
					// MediaProvider
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];
					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}
					final String selection = "_id=?";
					final String[] selectionArgs = new String[]{split[1]};
					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			}
		}
		return null;
	}


	public static Uri getUriForFile(Context context, File file) {
		if (context == null || file == null) {
			throw new NullPointerException();
		}
		Uri uri;
		if (Build.VERSION.SDK_INT >= 24) {
			uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.tdh.rpms.fileprovider", file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}


	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * 启动相机拍照
	 * @param context
	 * @param path  照片保存路径
	 * @return
	 */
	public static Uri startCamera(AppCompatActivity context, String path){

		if(hasSdcard()) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			String fileName = getPhotoFileName() + ".jpg";
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri photoUri = getUriForFile(context,new File(path + fileName));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			context.startActivityForResult(intent, MemoConstants.REQUEST_TAKE_PHOTO);
			return photoUri;
		}
		return null;
	}

	/**
	 * 启动系统图库
	 * @param context
	 * @return
	 */
	public static void startImageMedia(AppCompatActivity context){

		try{
			if(android.os.Build.VERSION.SDK_INT < 19){
				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT) ;
				innerIntent.setType( "image/*");
				innerIntent.addCategory(Intent.CATEGORY_OPENABLE );
				context.startActivityForResult(Intent.createChooser(innerIntent, "TravelHelper") , MemoConstants.REQUEST_TAKE_LOCAL) ;
			} else{
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT) ;
				intent.addCategory(Intent. CATEGORY_OPENABLE);
				intent.setType("image/*") ;
				context.startActivityForResult(intent , MemoConstants.REQUEST_TAKE_LOCAL) ;
			}

		}catch(Exception e){
			try{
				if(android.os.Build.VERSION.SDK_INT >= 19){
					Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT) ;
					innerIntent.setType( "image/*");
					innerIntent.addCategory(Intent.CATEGORY_OPENABLE );
					context.startActivityForResult(Intent.createChooser(innerIntent, "TravelHelper") , MemoConstants.REQUEST_TAKE_LOCAL) ;
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}


	/**
	 * 系统相机路径
	 * @return
	 */
	public static String getSystemCameraPath(){

		return Environment.getExternalStorageDirectory() +
				File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera"+File.separator;

	}

	/**
	 * 根据时间生成照片名称
	 * @return
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return "IMG_" + dateFormat.format(date);
	}

}
