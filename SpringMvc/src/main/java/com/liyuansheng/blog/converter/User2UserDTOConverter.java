package com.liyuansheng.blog.converter;

import org.springframework.beans.BeanUtils;

import com.liyuansheng.blog.dto.UserDTO;
import com.liyuansheng.blog.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蝎子
 * @date 2017/12/1 下午5:13
 */

public class User2UserDTOConverter {

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }

    public static List<UserDTO> convert(List<User> userList) {
        //lambda表达式，需要 jdk1.8支持
//        return userList.stream().map(e ->
//            convert(e)
//        ).collect(Collectors.toList());
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        for(int i=0;i<userList.size();i++) {
            UserDTO userDTO = new UserDTO();
            User user = userList.get(i);
            BeanUtils.copyProperties(user,userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
