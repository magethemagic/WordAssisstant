package com.example.wordhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2016/10/24.
 */
@Entity
public class Part {
    @Id
    private Long id;
    private Long wordId;
    private String part;
    private String means;

    @Generated(hash = 1811726784)
    public Part(Long id, Long wordId, String part, String means) {
        this.id = id;
        this.wordId = wordId;
        this.part = part;
        this.means = means;
    }

    @Generated(hash = 130301790)
    public Part() {
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", part='" + part + '\'' +
                ", means='" + means + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public Long getWordId() {
        return this.wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getMeans() {
        return this.means;
    }

    public void setMeans(String means) {
        this.means = means;
    }
}