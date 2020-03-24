package com.inthree.WH.API;

import com.google.gson.JsonArray;
import com.inthree.WH.model.CheckDuplicateRequest;
import com.inthree.WH.model.CheckDuplicateResponse;
import com.inthree.WH.model.CreateGrnResponse;
import com.inthree.WH.model.GRNDetailsResponse;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
import com.inthree.WH.model.Login;
import com.inthree.WH.model.LoginResponse;
import com.inthree.WH.model.PoOrderRequest;
import com.inthree.WH.model.PoOrderResponse;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

   // public static String BASE_URL;
   public String BoonBox_URL ="http://dev.in3access.in";
   public int Login_user_id = 0;

    @POST("/bb_po_user.php/")
    Call <LoginResponse> get_authincation(@Body Login login) ;

    @POST("/bb_grn_details.php")
    Call <GRNResponse> get_grn_details(@Body GrnRequest grnRequest) ;

//    @POST("/bb_grn_details.php")
//    Call <GRNDetailsResponse> get_grn_details(@Body GrnRequest grnRequest) ;

    @POST("/bb_po_details.php")
    Call <PoOrderResponse> get_po_orderdetails(@Body PoOrderRequest poOrderRequest) ;

    @POST("/bb_product_duplication_check.php")
    Call <CheckDuplicateResponse> check_duplicate_ids(@Body CheckDuplicateRequest duplicateModel) ;


//    @POST("/cron/boonbox_api_create_grn.php")
//    Call <CreateGrnResponse> create_grn(@Body GRNResponse.GRN_details grnResponse) ;

    @Multipart
    @POST("/cron/boonbox_api_create_grn.php")
    Call <CreateGrnResponse> create_grn( @Part("json_string") RequestBody  grnResponse, @Part  MultipartBody.Part file) ;

    @Multipart
    @POST("test/array.php")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );

   @Multipart
   @POST("/api/fileupload")
   Call<ResponseBody> postFile(@Part MultipartBody.Part file, @Part("description") RequestBody description);





}
