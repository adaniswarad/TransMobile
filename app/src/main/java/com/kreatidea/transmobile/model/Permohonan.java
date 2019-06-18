package com.kreatidea.transmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 10/20/2018.
 */
public class Permohonan {

    @SerializedName("permohonan_id")
    private String permohonanId;

    @SerializedName("tujuan")
    private String tujuan;

    @SerializedName("keperluan")
    private String keperluan;

    @SerializedName("jum_penumpang")
    private String jumPenumpang;

    @SerializedName("created_on")
    private String tglPengajuan;

    @SerializedName("tgl_pemakaian")
    private String tglPemakaian;

    @SerializedName("status_permohonan")
    private String statusPermohonan;

    public String getPermohonanId() {
        return permohonanId;
    }

    public String getTujuan() {
        return tujuan;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public String getJumPenumpang() {
        return jumPenumpang;
    }

    public String getTglPengajuan() {
        return tglPengajuan;
    }

    public String getTglPemakaian() {
        return tglPemakaian;
    }

    public String getStatusPermohonan() {
        String status_converted = "";
        if (statusPermohonan.equals("0")) {
            status_converted = "pending";
        } else if (statusPermohonan.equals("1")) {
            status_converted = "accepted";
        } else {
            status_converted = "rejected";
        }
        return status_converted;
    }
}
