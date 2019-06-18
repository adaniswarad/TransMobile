package com.kreatidea.transmobile.api;

import com.kreatidea.transmobile.model.Respons;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dennis on 10/19/2018.
 */
public interface TransvisionApi {

    @GET("login")
    Call<Respons> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("add_permohonan")
    Call<Respons> addPermohonan(
            @Field("user_id") String userId,
            @Field("tujuan") String tujuan,
            @Field("keperluan") String keperluan,
            @Field("jum_penumpang") String jum_penumpang,
            @Field("tgl_pemakaian") String tglPemakaian,
            @Field("lama_pemakaian") String lamaPemakaian,
            @Field("dasar_pemakaian") String dasarPemakaian
    );

    @GET("get_pemakaian_sekarang")
    Call<Respons> getPemakaianSekarang(@Query("user_id") String userId);

    @GET("get_pemakaian_list")
    Call<Respons> getPemakaianList(@Query("user_id") String userId);

    @GET("get_pemakaian_detail")
    Call<Respons> getPemakaianDetail(@Query("pemakaian_id") String pemakaianId);

    @GET("verifikasi_kode_pemakaian")
    Call<Respons> verifikasiKodePemakaian(
            @Query("pemakaian_id") String pemakaianId,
            @Query("kode_pemakaian") String kodePemakaian
    );

    @GET("is_already_upload_km_awal")
    Call<Respons> isAlreadyUploadKmAwal(@Query("pemakaian_id") String pemakaianId);

    @FormUrlEncoded
    @POST("upload_km_awal")
    Call<Respons> uploadKmAwal(
            @Field("pemakaian_id") String pemakaianId,
            @Field("image") String image,
            @Field("km_awal") String kmAwal
    );

    @FormUrlEncoded
    @POST("upload_km_akhir")
    Call<Respons> uploadKmAkhir(
            @Field("pemakaian_id") String pemakaianId,
            @Field("image") String image,
            @Field("km_akhir") String kmAkhir,
            @Field("catatan") String catatan
    );

    @FormUrlEncoded
    @POST("set_coordinate")
    Call<Respons> setCoordinate(
            @Field("pemakaian_id") String pemakaianId,
            @Field("lat") String lat,
            @Field("lng") String lng
    );
}
