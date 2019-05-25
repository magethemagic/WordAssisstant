package com.example.wordhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

import com.example.wordhelper.dao.DaoSession;
import com.example.wordhelper.dao.PartDao;
import com.example.wordhelper.dao.WordBeanDao;


@Entity
public class WordBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String en_sympol;
    private String am_symbol;
    private String en_mp3;
    private String am_mp3;
    private String groupName;
    @Transient
    private boolean checked;
    private boolean done;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDone() {
        return done;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @ToMany(referencedJoinProperty = "wordId")
    private List<Part> parts;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2056944827)
    private transient WordBeanDao myDao;



    @Generated(hash = 477143817)
    public WordBean(Long id, String name, String en_sympol, String am_symbol,
            String en_mp3, String am_mp3, String groupName, boolean done) {
        this.id = id;
        this.name = name;
        this.en_sympol = en_sympol;
        this.am_symbol = am_symbol;
        this.en_mp3 = en_mp3;
        this.am_mp3 = am_mp3;
        this.groupName = groupName;
        this.done = done;
    }

    public WordBean(Long id, String name, String en_sympol, String am_symbol, String en_mp3, String am_mp3, String groupName, List<Part> parts) {
        this.id = id;
        this.name = name;
        this.en_sympol = en_sympol;
        this.am_symbol = am_symbol;
        this.en_mp3 = en_mp3;
        this.am_mp3 = am_mp3;
        this.groupName = groupName;
        this.parts = parts;
    }

    @Generated(hash = 1596526216)
    public WordBean() {
    }


    
    @Override
    public String toString() {
        return "WordBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", en_sympol='" + en_sympol + '\'' +
                ", am_symbol='" + am_symbol + '\'' +
                ", en_mp3='" + en_mp3 + '\'' +
                ", am_mp3='" + am_mp3 + '\'' +
                ", groupName='" + groupName + '\'' +
                ", checked=" + checked +
                ", parts=" + parts +
                '}';
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return this.name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getEn_sympol() {
        return this.en_sympol;
    }



    public void setEn_sympol(String en_sympol) {
        this.en_sympol = en_sympol;
    }



    public String getAm_symbol() {
        return this.am_symbol;
    }



    public void setAm_symbol(String am_symbol) {
        this.am_symbol = am_symbol;
    }



    public String getEn_mp3() {
        return this.en_mp3;
    }



    public void setEn_mp3(String en_mp3) {
        this.en_mp3 = en_mp3;
    }



    public String getAm_mp3() {
        return this.am_mp3;
    }



    public void setAm_mp3(String am_mp3) {
        this.am_mp3 = am_mp3;
    }



    public String getGroupName() {
        return this.groupName;
    }



    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public boolean getDone() {
        return this.done;
    }



    public void setDone(boolean done) {
        this.done = done;
    }



    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1228913115)
    public List<Part> getParts() {
        if (parts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PartDao targetDao = daoSession.getPartDao();
            List<Part> partsNew = targetDao._queryWordBean_Parts(id);
            synchronized (this) {
                if (parts == null) {
                    parts = partsNew;
                }
            }
        }
        return parts;
    }



    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 352649916)
    public synchronized void resetParts() {
        parts = null;
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 954740466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWordBeanDao() : null;
    }

}

