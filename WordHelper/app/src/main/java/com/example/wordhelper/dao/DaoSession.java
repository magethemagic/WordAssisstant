package com.example.wordhelper.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.wordhelper.bean.Part;
import com.example.wordhelper.bean.WordBean;

import com.example.wordhelper.dao.PartDao;
import com.example.wordhelper.dao.WordBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig partDaoConfig;
    private final DaoConfig wordBeanDaoConfig;

    private final PartDao partDao;
    private final WordBeanDao wordBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        partDaoConfig = daoConfigMap.get(PartDao.class).clone();
        partDaoConfig.initIdentityScope(type);

        wordBeanDaoConfig = daoConfigMap.get(WordBeanDao.class).clone();
        wordBeanDaoConfig.initIdentityScope(type);

        partDao = new PartDao(partDaoConfig, this);
        wordBeanDao = new WordBeanDao(wordBeanDaoConfig, this);

        registerDao(Part.class, partDao);
        registerDao(WordBean.class, wordBeanDao);
    }
    
    public void clear() {
        partDaoConfig.clearIdentityScope();
        wordBeanDaoConfig.clearIdentityScope();
    }

    public PartDao getPartDao() {
        return partDao;
    }

    public WordBeanDao getWordBeanDao() {
        return wordBeanDao;
    }

}
