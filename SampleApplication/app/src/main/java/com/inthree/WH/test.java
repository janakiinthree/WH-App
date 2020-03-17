package com.inthree.WH;

import android.net.Uri;

import okhttp3.OkHttpClient;

//public class test {
//
//    private void mulipleFileUploadFile(Uri[] fileUri) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        OkHttpClient clientWith30sTimeout = okHttpClient.newBuilder()
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(API_URL_BASE)
//                .addConverterFactory(new MultiPartConverter())
//                .client(clientWith30sTimeout)
//                .build();
//
//        WebAPIService service = retrofit.create(WebAPIService.class); //here is the interface which you have created for the call service
//        Map<String, okhttp3.RequestBody> maps = new HashMap<>();
//
//        if (fileUri!=null && fileUri.length>0) {
//            for (int i = 0; i < fileUri.length; i++) {
//                String filePath = getRealPathFromUri(fileUri[i]);
//                File file1 = new File(filePath);
//
//                if (filePath != null && filePath.length() > 0) {
//                    if (file1.exists()) {
//                        okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), file1);
//                        String filename = "imagePath" + i; //key for upload file like : imagePath0
//                        maps.put(filename + "\"; filename=\"" + file1.getName(), requestFile);
//                    }
//                }
//            }
//        }
//
//        String descriptionString = " string request";//
//        //hear is the your json request
//        Call<String> call = service.postFile(maps, descriptionString);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call,
//                                   Response<String> response) {
//                Log.i(LOG_TAG, "success");
//                Log.d("body==>", response.body().toString() + "");
//                Log.d("isSuccessful==>", response.isSuccessful() + "");
//                Log.d("message==>", response.message() + "");
//                Log.d("raw==>", response.raw().toString() + "");
//                Log.d("raw().networkResponse()", response.raw().networkResponse().toString() + "");
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e(LOG_TAG, t.getMessage());
//            }
//        });
//    }
//
//    public String getRealPathFromUri(final Uri uri) { // function for file path from uri,
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(mContext, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(mContext, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(mContext, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//
//            return getDataColumn(mContext, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//}
