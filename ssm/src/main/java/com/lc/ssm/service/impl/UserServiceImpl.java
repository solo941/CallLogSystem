package com.lc.ssm.service.impl;

import com.lc.ssm.dao.BaseDao;
import com.lc.ssm.domain.Item;
import com.lc.ssm.domain.Order;
import com.lc.ssm.domain.User;
import com.lc.ssm.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Resource(name = "userDao")
    public void setDao(BaseDao<User> dao) {
        super.setDao(dao);
    }
    @Resource(name = "itemDao")
    private BaseDao<Item> itemDao;

    public void longTx() {
        Item i = new Item();
        i.setItemName("ttt");
        Order o = new Order();
        o.setId(2);
        itemDao.insert(i);

        this.delete(1);
    }
    public void save(User u){
        if (u.getId() != null){
            this.update(u);
        }
        else {
            this.insert(u);
        }
    }

    /**
     * 分页查询
     * @param offset
     * @param len
     * @return
     */
    public List<User> selectPage(int offset, int len) {
        return getDao().selectPage(offset,len);
    }
}
