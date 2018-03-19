package com.vgaw.nrfconnect.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * @author caojin
 * @date 2016/8/15
 */
public class Utils {
    public static String nullToEmpty(String raw) {
        return raw == null ? "" : raw;
    }

    public static String nullTo(String raw, String after) {
        return raw == null ? after : raw;
    }

    public static String emptyTo(String raw, String after){
        return "".equals(raw) ? after : raw;
    }

    /**
     * 保留两位小数（去尾法）
     * @return
     */
    public static String getDot(float raw, int limit){
        return String.format("%." + limit + "f", raw);
    }

    public static String formatTime(int seconds){
        int minute = seconds / 60;
        int second = seconds % 60;
        return addZero(minute) + ":" + addZero(second);
    }

    private static String addZero(int raw){
        return raw > 9 ? String.valueOf(raw) : ("0" + raw);
    }


    public static String getTime(String format) {
        return String.valueOf(new SimpleDateFormat(format).format(new Date()));
    }
     public static String getTime(String format,long date) {
        return String.valueOf(new SimpleDateFormat(format).format(new Date(date)));
    }
    public static String getTimeData(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)));
        return sd;
    }

    public static String getISO8601Time(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static boolean checkGpsCoordinate(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public static String getMD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将"秒"转化为"时分秒"格式
     * @return
     */
    public static String getHHMMSS(int seconds){
        int h = seconds / 60 / 60;
        int m = (seconds % (60 * 60)) / 60;
        int s = seconds - h * 60 * 60 - m * 60;
        return addZero(h) + ":" + addZero(m) + ":" + addZero(s);
    }

    public static String calLength(int length){
        if (length < 1000){
            return length + "m";
        }else {
            return getDot(((float) length / 1000), 2) + "km";
        }
    }

    public static float getFoot(float raw) {
        return raw * 3.280839895f;
    }

    /**
     * @param raw m/s
     * @param type 0: MPH; 1: km/h; 2: m/s
     * @return
     */
    public static float getSpeed(float raw, int type) {
        switch (type) {
            case 0:
                return raw * 2.23693629f;
            case 1:
                return raw * 3.6f;
            default:
                return raw;
        }
    }

    public static Long getTrafficNow() {
        Long rx = TrafficStats.getUidRxBytes(android.os.Process.myUid());
        Long tx = TrafficStats.getUidTxBytes(android.os.Process.myUid());
        if (rx == TrafficStats.UNSUPPORTED || tx == TrafficStats.UNSUPPORTED) {
            return null;
        }
        return rx + tx;
    }

    public static String formatTraffic(Long raw) {
        if (raw == null) {
            return "UNSUPPORTED";
        }
        if (raw < 1024 * 1024) {
            return (raw / 1024) + "KB";
        }
        if (raw < 1024 * 1024 * 1024) {
            return (raw / 1024 / 1024) + "MB";
        }
        return (raw / 1024 / 1024 / 1024) + "GB";
    }

    public static boolean isPasswordSimple(String raw) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z._~!? -]{6,18}$";
        return !Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean isPhoneValid(String raw) {
        String pattern = "^1[0-9]{10}$";
        return Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean hex(char raw) {
        return raw > 47 && raw < 58 ||
                raw > 64 && raw < 71 ||
                raw > 96 && raw < 103;
    }

    /**
     * 判断是否全是数字
     * @param str
     * @return
     */
    public static boolean isNumeric (String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static Bitmap compressPic(Resources res, int drawableId, int referenceHeight){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, drawableId, bmOptions);

        float ratio = referenceHeight / (float)bmOptions.outHeight;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, (int) (bmOptions.outWidth * ratio), referenceHeight);
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, drawableId, bmOptions);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }

    public static String getPath(Context context, Intent data) {
        String path = null;
        Uri uri = data.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, uri)) {
            String wholeId = DocumentsContract.getDocumentId(uri);
            if (isExternalStorageDocument(uri)) {
                String[] split = wholeId.split(":");
                String type = split[0];
                if ("primary".equals(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    // Environment.getExternalStorageDirectory(): /storage/emulated/0
                }
                // TODO: 2015-12-22 handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // content://com.android.providers.downloads.documents/document/1
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(wholeId));
                path = getPath(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // content://com.android.providers.media.documents/document/image%3A11
                String id = wholeId.split(":")[1];
                path = getPath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.Media._ID + "=?",
                        new String[]{id});
            }
        } else if ("content".equals(uri.getScheme())) {
            // action_pick && api<19
            // content://media/external/images/media/12
            if (isGooglePhotosUri(uri)) {
                path = uri.getLastPathSegment();
            }
            path = getPath(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // action_pick && api>=19
            // file:///storage/emulated/0/...
            path = uri.getPath();
        }
        return path;
    }

    private static String getPath(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        Cursor cursor = null;
        try{
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        }finally{
        }
        return path;
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getAppVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    public static boolean anotherDay(int savedDay) {
        //获得保存的天数，如果没有记录就赋值为-1表示第一次执行
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return (savedDay == -1 || savedDay != curDay);
    }

    public static boolean usernameValid(String raw) {
        String pattern = "^([a-zA-Z\\u4e00-\\u9fa5]{1}[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,13}[a-zA-Z0-9\\u4e00-\\u9fa5]{1})|([\\u4e00-\\u9fa5]{2})$";
        return Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    public static boolean containChinese(String str, boolean all) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                if (!all) {
                    return true;
                }
            } else if (all) {
                return false;
            }
        }
        return all;
    }
}
