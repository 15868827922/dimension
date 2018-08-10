package com.shenjing.dimension.dimension.base.select_picture;

import java.io.Serializable;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5505022632946172494L;
    public String path;
    public String name;
    public long time;
    public String length;

    public Image(String path, String name, long time, String length){
        this.path = path;
        this.name = name;
        this.time = time;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
