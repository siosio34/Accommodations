package com.youngje.tgwing.accommodations.Activity;

import android.graphics.Bitmap;

/**
 * Created by SEGU on 2016-12-01.
 */

public class Country{
    public Country(String nameOfCountry, Bitmap imageOfCountry, String codeOfCountry){
        setNameOfCountry(nameOfCountry);
        setImageOfCountry(imageOfCountry);
        setCodeOfCountry(codeOfCountry);
    }
    private String nameOfCountry;
    private String codeOfCountry;
    private Bitmap imageOfCountry;

    public String getCodeOfCountry() {
        return codeOfCountry;
    }

    public void setCodeOfCountry(String codeOfCountry) {
        this.codeOfCountry = codeOfCountry;
    }
    public Bitmap getImageOfCountry() {
        return imageOfCountry;
    }

    public void setImageOfCountry(Bitmap imageOfCountry) {
        this.imageOfCountry = imageOfCountry;
    }

    public String getNameOfCountry() {
        return nameOfCountry;
    }

    public void setNameOfCountry(String nameOfCountry) {
        this.nameOfCountry = nameOfCountry;
    }
}