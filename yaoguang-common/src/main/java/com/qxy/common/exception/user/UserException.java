package com.qxy.common.exception.user;

import com.qxy.common.exception.base.BaseException;

import java.io.Serial;

/**
 * 用户信息异常类
 */
public class UserException extends BaseException
{
    @Serial
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
