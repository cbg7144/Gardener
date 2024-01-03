package com.example.myapplication;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment{
    View v;
    // Usage GridAdapter , need to implement
    GridAdapter adapter;
    List<Gallery>  lstGallery;

    // Gallery image download
    File filePath;
    private final int CAMERA_REQUEST_CODE = 40;
    private final int GALLERY_REQUEST_CODE = 50;

    // Permission
    private PermissionSupport permission;


    // openCamera method
    private void openCamera() {
        try {
            String dirPath = getContext().getExternalFilesDir(null).getPath();
            File dir = new File(dirPath);
            if (!dir.exists()) {
                Log.d("MAKE DIR", dir.mkdirs() + "");
            }

            filePath = File.createTempFile("IMG", "jpg", dir);
            Uri photoUri = FileProvider.getUriForFile(getContext(), "com.example.taptest.provider", filePath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // openGallery method
    private void openGallery() {
        Intent newIntent = new Intent();
        newIntent.setType("image/*");
        newIntent.setAction(Intent.ACTION_GET_CONTENT);
        newIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(newIntent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= 23){
            // Use getActivity() instead of this
            permission = new PermissionSupport(getActivity(), getActivity());

            if(!permission.checkPermission()){
                permission.requestPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            // Existing camera permission handling
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission was denied. You can show a toast or alert dialog here.
            }
        }
    }

    public TwoFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_two, container, false);
        GridView gridView = v.findViewById(R.id.gridView);
        adapter = new GridAdapter(v.getContext(), lstGallery);
        gridView.setAdapter(adapter);

        // 카메라 버튼
        FloatingActionButton fab = v.findViewById(R.id.goToCam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });


        FloatingActionButton gal = v.findViewById(R.id.goToGallery);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
                    } else {
                        openGallery();
                    }
                } else {
                    openGallery();
                }
            }
        });

        return v;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(filePath != null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try{
                    InputStream in = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(in, null, options);
                    in.close();
                    in = null;
                } catch (Exception e){
                    e.printStackTrace();
                }

                final int width = options.outWidth;
                final int height = options.outHeight;

                BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                imgOptions.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), imgOptions);

                ExifInterface exif = null;
                try{
                    exif = new ExifInterface(filePath);
                } catch (IOException e){
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                Gallery gallery = new Gallery(bmRotated);
                lstGallery.add(gallery);
                adapter.notifyDataSetChanged();

            }
        }else if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            try{
                if(data.getClipData() != null){
                    for(int index = 0; index < data.getClipData().getItemCount(); index++){
                        Uri uri = data.getClipData().getItemAt(index).getUri();

                        InputStream in = getActivity().getContentResolver().openInputStream(uri);

                        BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                        imgOptions.inSampleSize = 4;

                        Bitmap img = BitmapFactory.decodeStream(in, null, imgOptions);
                        in.close();

                        ExifInterface exif = null;
                        try{
                            exif = new ExifInterface(getPathFromUri(getContext(), uri));
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap bmRotated = rotateBitmap(img, orientation);

                        lstGallery.add(new Gallery(bmRotated));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Uri uri = data.getData();

                    InputStream in = getActivity().getContentResolver().openInputStream(uri);

                    BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                    imgOptions.inSampleSize = 4;

                    Bitmap img = BitmapFactory.decodeStream(in, null, imgOptions);
                    in.close();

                    ExifInterface exif = null;
                    try{
                        exif = new ExifInterface(getPathFromUri(getContext(),uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap bmRotated = rotateBitmap(img, orientation);

                    lstGallery.add(new Gallery(bmRotated));
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstGallery = new ArrayList<>();
        lstGallery.add(new Gallery(R.drawable.za));
        lstGallery.add(new Gallery(R.drawable.zb));
        lstGallery.add(new Gallery(R.drawable.zc));
        lstGallery.add(new Gallery(R.drawable.zd));
        lstGallery.add(new Gallery(R.drawable.ze));
        lstGallery.add(new Gallery(R.drawable.zf));
        lstGallery.add(new Gallery(R.drawable.zg));
        lstGallery.add(new Gallery(R.drawable.zh));
        lstGallery.add(new Gallery(R.drawable.zi));
        lstGallery.add(new Gallery(R.drawable.zj));
        lstGallery.add(new Gallery(R.drawable.zk));
        lstGallery.add(new Gallery(R.drawable.zl));
        lstGallery.add(new Gallery(R.drawable.zm));
        lstGallery.add(new Gallery(R.drawable.zn));
        lstGallery.add(new Gallery(R.drawable.zo));
        lstGallery.add(new Gallery(R.drawable.zp));
        lstGallery.add(new Gallery(R.drawable.zq));
        lstGallery.add(new Gallery(R.drawable.zr));
        lstGallery.add(new Gallery(R.drawable.zs));
        lstGallery.add(new Gallery(R.drawable.zt));
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation){
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case  ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getPathFromUri(final Context context, final Uri uri){

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)){
            // External Storage Provider
            if (isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("Content://downloads/public_downloads"), Long.valueOf(id));
                return  getDatacolumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audi".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDatacolumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())){
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return  getDatacolumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    public static String getDatacolumn(Context context, Uri uri, String selection, String[] selectionArgs){

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()){
                final int index = cursor.getColumnIndexOrThrow(column);
                return  cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
