package com.e.hp.sgniin.HelperClasses;

public class phonehelper {

    int image;
    String title;
 /*   GradientDrawable color;*/

    public phonehelper(int image, String title) {
        this.image = image;
        this.title = title;
 /*       this.color = color;*/
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }


 /*   public Drawable getgradient() {
        return color;
    }*/


}
