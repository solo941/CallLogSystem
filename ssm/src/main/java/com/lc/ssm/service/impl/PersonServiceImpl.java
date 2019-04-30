package com.lc.ssm.service.impl;


import com.lc.ssm.dao.BaseDao;
import com.lc.ssm.dao.impl.PersonDaoImpl;
import com.lc.ssm.domain.Person;
import com.lc.ssm.service.PersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("personService")
public class PersonServiceImpl extends BaseServiceImpl<Person> implements PersonService {

    @Resource(name="personDao")
    public void setDao(BaseDao<Person> dao) {
        super.setDao(dao);
    }

    public String selectNameByPhone(String phone){
        return ((PersonDaoImpl)getDao()).selectNameByPhone(phone) ;
    }
}