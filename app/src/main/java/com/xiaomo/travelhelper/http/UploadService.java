package com.xiaomo.travelhelper.http;

import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.circle.UploadResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 上传文件
 */
public interface UploadService {

    //String BASE_URL = "http://192.168.1.136:8080/api/file/";
    String BASE_URL = UserConst.BASE_URL + "api/file/";

    @Multipart
    @POST("upload")
    Observable<UploadResult> uploadFile(@Part List<MultipartBody.Part> files);

}


