package com.lc.ssm.service;

import com.lc.ssm.domain.User;

import java.util.List;

public interface UserService extends BaseService<User> {
    public void longTx();
    public void save(User u);

    public List<User> selectPage(int offset, int len);
}
