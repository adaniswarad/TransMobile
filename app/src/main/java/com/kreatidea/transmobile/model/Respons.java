package com.kreatidea.transmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dennis on 10/19/2018.
 */
public class Respons {

    @SerializedName("respons")
    private String respons;

    @SerializedName("users")
    private List<User> users;

    @SerializedName("permohonan")
    private List<Permohonan> permohonan;

    @SerializedName("permohonan_detail")
    private Permohonan permohonanDetail;

    @SerializedName("pemakaian")
    private List<Pemakaian> pemakaian;

    @SerializedName("pemakaian_detail")
    private Pemakaian pemakaianDetail;

    public String getRespons() {
        return respons;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Permohonan> getPermohonan() {
        return permohonan;
    }

    public Permohonan getPermohonanDetail() {
        return permohonanDetail;
    }

    public List<Pemakaian> getPemakaian() {
        return pemakaian;
    }

    public Pemakaian getPemakaianDetail() {
        return pemakaianDetail;
    }
}
