package com.inthree.WH.API;

import com.google.gson.JsonArray;
import com.inthree.WH.model.GRNResponse;
import com.inthree.WH.model.GrnRequest;
import com.inthree.WH.model.Login;
import com.inthree.WH.model.LoginResponse;
import com.inthree.WH.model.PoOrderRequest;
import com.inthree.WH.model.PoOrderResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

   // public static String BASE_URL;
   public String BoonBox_URL ="http://dev.in3access.in";

    @POST("/bb_po_user.php/")
    Call <LoginResponse> get_authincation(@Body Login login) ;

    @POST("/bb_grn_details.php")
    Call <GRNResponse> get_grn_details(@Body GrnRequest grnRequest) ;

    @POST("/bb_po_details.php")
    Call <PoOrderResponse> get_po_orderdetails(@Body PoOrderRequest poOrderRequest) ;

}
