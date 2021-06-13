package com.demo;

import static org.junit.Assert.assertEquals;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Description: Controller测试类
 * @author 应文灿
 * @date 2021年5月19日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SMPApplication.class)
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestController {
	
	/**context*/
	@Autowired
	private WebApplicationContext context;
	
    /**mockMvc*/
    private MockMvc mockMvc;
    
    @BeforeEach
    public void initMokcMvc(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
	
	/**
	 * @param URL
	 * @throws Exception
	 */
	@Order(1)
    @ParameterizedTest
	@ValueSource(strings = {"/select","/insert","/delete","/update"})
	public void testHtmlResponse(String URL) throws Exception {
		MvcResult mvcResult = mockMvc
                .perform(
                		MockMvcRequestBuilders.get(URL)
                		)
                .andReturn();
		int status = mvcResult.getResponse().getStatus();
		
	    assertEquals("请求错误", 200, status);
	    assertEquals("URL错误","http://localhost"+URL,mvcResult.getRequest().getRequestURL().toString());
	}
	
	/**
	 * @throws Exception
	 */
	@Order(2)
	@Test
	public void testInsertSame() throws Exception {
		MvcResult mvcResult1 = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/insert/1")
            			.param("name", "应文灿")
            			.param("date","2018-09-01")
            			.param("sid","180001")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int status1 = mvcResult1.getResponse().getStatus();
		String responseHtml1 = mvcResult1.getModelAndView().getViewName();
		Object msg1 = mvcResult1.getModelAndView().getModelMap().getAttribute("success");
		
	    assertEquals("请求错误", 200, status1);
	    assertEquals("返回结果不一致", "successful.html", responseHtml1);
	    assertEquals("信息输出不一致", "成功插入一条记录！", msg1);
	    
	    MvcResult mvcResultT = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/insert/1")
            			.param("name", "应文灿")
            			.param("date","2018-09-01")
            			.param("sid","180001")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int statusT = mvcResultT.getResponse().getStatus();
		String responseHtmlT = mvcResultT.getModelAndView().getViewName();
		Object msgT = mvcResultT.getModelAndView().getModelMap().getAttribute("SQL");
		
	    assertEquals("请求错误", 200, statusT);
	    assertEquals("返回结果不一致", "insert.html", responseHtmlT);
	    assertEquals("信息输出不一致", "学生姓名重复，请重新输入！", msgT);
	    
	}
	
	/**
	 * Description: 参数化测试，由userGenerator提供参数流
	 * @param name
	 * @param date
	 * @param sid
	 * @param phone
	 * @param type
	 * @param tag
	 * @throws Exception
	 */
	@Order(3)
	@ParameterizedTest(name = "[{index}] user with name: {0} and date: {1} and sid: {2} and phone: {3} and type: {4} and tag: {5}")
	@MethodSource("userGenerator")
	public void testInsert(String name, String date, String sid, String phone, String type, String tag) throws Exception {
		MvcResult mvcResult = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/insert/1")
            			.param("name", name)
            			.param("date",date)
            			.param("sid",sid)
            			.param("phone",phone)
            			.param("type",type)
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int status = mvcResult.getResponse().getStatus();
		String responseHtml = mvcResult.getModelAndView().getViewName();
		Object SQLmsg = mvcResult.getModelAndView().getModelMap().getAttribute("SQL");
		
	    assertEquals("请求错误", 200, status);
	    assertEquals("URL错误","insert.html",responseHtml);
	    if (tag.equals("date")) {
	    	assertEquals("信息输出不一致", "日期输入错误，请重新输入！", SQLmsg);
	    }
	    else if (tag.equals("sid")) {
	    	assertEquals("信息输出不一致", "学号输入错误，请重新输入！", SQLmsg);
	    }
	    else if (tag.equals("phone")) {
	    	assertEquals("信息输出不一致", "电话号码错误，请重新输入！", SQLmsg);
	    }   
	    
	}
	
	
	/**
	 * @throws Exception
	 */
	@Order(4)
	@Test
	public void testUpdate() throws Exception {
		MvcResult mvcResult1 = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/update/1")
            			.param("name", "应文灿")
            			.param("date","2018-09-01")
            			.param("sid","180005")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int status1 = mvcResult1.getResponse().getStatus();
		String responseHtml1 = mvcResult1.getModelAndView().getViewName();
		Object msg1 = mvcResult1.getModelAndView().getModelMap().getAttribute("success");
		
	    assertEquals("请求错误", 200, status1);
	    assertEquals("返回结果不一致", "successful.html", responseHtml1);
	    assertEquals("信息输出不一致", "成功更新一条记录！", msg1);
	    
	    MvcResult mvcResultT = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/update/1")
            			.param("name", "Mike")
            			.param("date","2018-09-01")
            			.param("sid","180001")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int statusT = mvcResultT.getResponse().getStatus();
		String responseHtmlT = mvcResultT.getModelAndView().getViewName();
		Object msgT = mvcResultT.getModelAndView().getModelMap().getAttribute("fail");
		
	    assertEquals("请求错误", 200, statusT);
	    assertEquals("返回结果不一致", "fail.html", responseHtmlT);
	    assertEquals("信息输出不一致", "更新记录失败！", msgT);
	    
	}
	
	/**
	 * @throws Exception
	 */
	@Order(5)
	@Test
	public void testDelete() throws Exception {
		MvcResult mvcResult1 = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/delete/1")
            			.param("name", "Mike")
            			.param("date","2018-09-01")
            			.param("sid","180005")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int status1 = mvcResult1.getResponse().getStatus();
		String responseHtml1 = mvcResult1.getModelAndView().getViewName();
		Object msg1 = mvcResult1.getModelAndView().getModelMap().getAttribute("fail");
		
	    assertEquals("请求错误", 200, status1);
	    assertEquals("返回结果不一致", "fail.html", responseHtml1);
	    assertEquals("信息输出不一致", "删除记录失败！", msg1);
	    
	    MvcResult mvcResultT = mockMvc
                .perform(
                		MockMvcRequestBuilders.get("/delete/1")
            			.param("name", "应文灿")
            			.param("date","2018-09-01")
            			.param("sid","180001")
            			.param("phone","13000000000")
            			.param("type","统招全日制")
                		)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
		int statusT = mvcResultT.getResponse().getStatus();
		String responseHtmlT = mvcResultT.getModelAndView().getViewName();
		Object msgT = mvcResultT.getModelAndView().getModelMap().getAttribute("success");
		
	    assertEquals("请求错误", 200, statusT);
	    assertEquals("返回结果不一致", "successful.html", responseHtmlT);
	    assertEquals("信息输出不一致", "成功删除一条记录！", msgT);
	    
	}
	
	/**
	 * Description: 参数生成器，以流的形式注入参数化测试
	 * @return
	 */
	static Stream<Arguments> userGenerator(){
	    return Stream.of(
	    		Arguments.of("Mike1","1900-01-01","180001","13000000000","统招全日制","date"),
	    		Arguments.of("Mike2","2018-09-02","1802","13000000000","统招全日制","sid"),
	    		Arguments.of("Mike3","2018-09-03","180003","1300000","统招全日制","phone")
	    		);
	}

}
