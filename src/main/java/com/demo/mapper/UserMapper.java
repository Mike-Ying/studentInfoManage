package com.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.demo.bean.UserBean;

/**
 * Description: Mybatis Mapper 接口 执行mapper/UserMapper.xml
 * @author 应文灿
 * @update 2021年5月6日
 */
@Mapper
public interface UserMapper {

	public List<UserBean> getUser();

	public int insertUser(UserBean user);

	public int updateUser(UserBean user);

	public int deleteUser(String name);

	public int clearUser();

}
