package com.saiy.services;

import com.saiy.mapper.UsersMapper;
import com.saiy.models.Users;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by rjora on 2016/12/30 0030.
 */
@Service
public class TestService {

    @Resource
    private UsersMapper usersMapper;

    public int count() {
        return usersMapper.countByExample(null);
    }

    public Users getUser(int userid){
        return usersMapper.selectByPrimaryKey(userid);
    }
}
