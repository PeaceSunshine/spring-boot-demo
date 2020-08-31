package com.mapper;

import com.entity.TUser;
import common.base.IBaseDao;

public interface TUserMapper extends IBaseDao<TUser> {

    TUser selectByIdentification(String username);
}