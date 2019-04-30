package com.lc.ssm.service;


import com.lc.ssm.domain.Person;

import java.util.List;

/**
 *
 */
public interface PersonService extends BaseService<Person> {
    public String selectNameByPhone(String phone);
}
