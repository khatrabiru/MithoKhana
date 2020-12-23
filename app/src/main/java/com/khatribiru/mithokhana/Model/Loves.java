package com.khatribiru.mithokhana.Model;

public class Loves {

    private String id;
    private String LovedUserId;

    public Loves() {
    }

    public Loves(String id, String lovedUserId) {
        this.id = id;
        LovedUserId = lovedUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLovedUserId() {
        return LovedUserId;
    }

    public void setLovedUserId(String lovedUserId) {
        LovedUserId = lovedUserId;
    }
}
