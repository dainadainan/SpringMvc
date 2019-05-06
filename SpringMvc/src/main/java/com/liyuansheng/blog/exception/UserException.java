package com.liyuansheng.blog.exception;

import com.liyuansheng.blog.enums.ResultEnum;

/**
 * @author 蝎子
 * @date 2017/11/30 下午11:34
 */

public class UserException extends RuntimeException {

    private Integer code;

    public UserException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public UserException(Integer code,String message) {
        super(message);
        this.code = code;
    }
}
