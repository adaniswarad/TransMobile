package com.kreatidea.transmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 10/19/2018.
 */
public class Pemakaian {

    @SerializedName("pemakaian_id") private String pemakaianId;
    @SerializedName("username") private String pemohon;
    @SerializedName("tujuan") private String tujuan;
    @SerializedName("keperluan") private String keperluan;
    @SerializedName("jum_penumpang") private String jumPenumpang;
    @SerializedName("tgl_pemakaian") private String tglPemakaian;
    @SerializedName("nopol") private String nopol;
    @SerializedName("km_awal") private String kmAwal;
    @SerializedName("km_akhir") private String kmAkhir;
    @SerializedName("pengemudi") private String pengemudi;
    @SerializedName("status_pemakaian") private String statusPemakaian;

    public String getPemakaianId() {
        return pemakaianId;
    }

    public String getPemohon() {
        return pemohon;
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

    public String getTglPemakaian() {
        return tglPemakaian;
    }

    public String getNopol() {
        return nopol;
    }

    public String getKmAwal() {
        return kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }

    public String getPengemudi() {
        return pengemudi;
    }

    public String getStatusPemakaian() {
        String status_converted = "";
        if (statusPemakaian.equals("0")) {
            status_converted = "on progress";
        } else if (statusPemakaian.equals("1")) {
            status_converted = "finish";
        }
        return status_converted;
    }
}
