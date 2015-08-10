package com.hasgeek.zalebi.model;

/**
 * Created by heisenberg on 10/08/15.
 */
public class ScannedData {
    private String puk;
    private String key;

    public ScannedData(String puk, String key) {
        this.puk = puk;
        this.key = key;
    }

    public static ScannedData parse(String rawContent) throws UnknownBadgeException {
        if(rawContent == null || rawContent.length() !=16 ){
            throw new UnknownBadgeException();
        }
        String puk = rawContent.substring(0, 8);
        String key = rawContent.substring(8);
        return new ScannedData(puk, key);
    }

    public static class UnknownBadgeException extends Exception{
        public UnknownBadgeException() {
        }
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
