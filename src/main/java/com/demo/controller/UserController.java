package com.demo.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.bean.UserBean;
import com.demo.mapper.UserMapper;

/**
 * Description: Controller层
 * @author 应文灿
 * @date 2021年5月14日
 */
@Controller
public class UserController {

	// 自动注入mapper接口
	@Autowired
	private UserMapper mapper;

	/**
	 * select
	 * Description:
	 * @param map
	 * @return
	 */
	@GetMapping(value={"/select","/"})
	public String select(HashMap<String, Object> map) {
		// 调用mapper接口，返回用户列表
		List<UserBean> users = mapper.getUser();
		map.put("users", users);
		return "info.html";
	}
	
	/**
	 * insert
	 * Description:
	 * @return
	 */
	@GetMapping("/insert")
	public String insert() {
		return "insert.html";
	}
	
	/**
	 * insert
	 * Description: 插入功能逻辑及页面跳转
	 * @param map
	 * @param request
	 * @return
	 */
	@GetMapping("/insert/1")
	public String insert(HashMap<String, Object> map, HttpServletRequest request) {
			//捕获中文字符带来的编码模式异常
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			/* 获取HashMap存储的参数 */
			String name = request.getParameter("name");
			String date = request.getParameter("date");
			String sid = request.getParameter("sid");
			String phone = request.getParameter("phone");
			String type = request.getParameter("type");
			/* 获取HashMap存储的参数结束 */
			
			//调用工具方法，将字符串型日期转化为sql.Date型日期
			Date sqlDate = strToDate(date);
			
			UserBean user = new UserBean(name,sqlDate,sid,phone,type);
			
			int val = 0;
			try {
				val = mapper.insertUser(user);
				// 在这个位置捕获数据库异常
			}catch (Exception e2) {
				map.clear();
				// 根据异常情况分类，向用户反馈提示信息
				if (e2.getMessage().indexOf("错误: 对于可变字符类型来说，值太长了(15)")!=-1)
					map.put("SQL", "学生姓名超过十个字符，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 对于可变字符类型来说，值太长了(10)")!=-1)
					map.put("SQL", "学生学号超过十个字符，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 对于可变字符类型来说，值太长了(20)")!=-1)
					map.put("SQL", "电话号码超过二十个字符，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 重复键违反唯一约束")!=-1)
					map.put("SQL","学生姓名重复，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 关系 \"user_test\" 的新列违反了检查约束 \"phone_check\"")!=-1)
					map.put("SQL","电话号码错误，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 关系 \"user_test\" 的新列违反了检查约束 \"date_check\"")!=-1)
					map.put("SQL","日期输入错误，请重新输入！");
				else if (e2.getMessage().indexOf("错误: 关系 \"user_test\" 的新列违反了检查约束 \"sid_check\"")!=-1)
					map.put("SQL","学号输入错误，请重新输入！");
				else
					map.put("SQL", e2);
				return "insert.html";
			}
			if (val==1) {
				map.put("success", "成功插入一条记录！");
				return "successful.html";
			}
			else {
				map.put("fail", "插入记录失败！");
				return "fail.html";
			}
	}
	
	
	/**
	 * update
	 * Description:
	 * @return
	 */
	@GetMapping("/update")
	public String update() {
		return "update.html";
	}
	

	
	/**
	 * update
	 * Description:
	 * @param map
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value = "/update/1")
	public String update(HashMap<String, Object> map, HttpServletRequest request) throws UnsupportedEncodingException {
		map.clear();
		/* 获取HashMap存储的参数 */
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String date = request.getParameter("date");
		String sid = request.getParameter("sid");
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
		/* 获取HashMap存储的参数 */
		
		//调用工具方法，将字符串型日期转化为sql.Date型日期
		Date sqlDate = strToDate(date);
		
		UserBean user = new UserBean(name,sqlDate,sid,phone,type);
		
		int val = mapper.updateUser(user);
		if (val==1) {
			map.put("success", "成功更新一条记录！");
			return "successful.html";
		}
		else {
			map.put("fail", "更新记录失败！");
			return "fail.html";
		}
		
	}
	
	/**
	 * delete
	 * Description:
	 * @return
	 */
	@GetMapping("/delete")
	public String delete() {
		return "delete.html";
	}
	
	/**
	 * delete
	 * Description:
	 * @param map
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/delete/1")
	public String delete(HashMap<String, Object> map, HttpServletRequest request) throws UnsupportedEncodingException {
		map.clear();
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		map.put("name", name);
		int val = mapper.deleteUser(name);
		
		if (val==1) {
			map.put("success", "成功删除一条记录！");
			return "successful.html";
		}
		else {
			map.put("fail", "删除记录失败！");
			return "fail.html";
		}
		
	}
	
	
	/**
	 * strToDate
	 * Description: 将字符串型日期转化为sql.Date型日期
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {  
        String str = strDate;
        // 新建日期样式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        java.util.Date d = null;  
        try {  
            d = format.parse(str);
        } catch (Exception e) { 
            e.printStackTrace();  
        }  
        Date date = new Date(d.getTime());
        return date;  
    }  
}
