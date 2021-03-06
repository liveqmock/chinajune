package com.ChargePoint.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ChargePoint.Utils.TextUtils;
import com.ChargePoint.bean.PCUser;
import com.ChargePoint.services.PCUserService;

@Controller
@RequestMapping("/f/")
public class Forward {
	
	private static ObjectMapper objectMapper = null;
	
	@RequestMapping(value="Index")
	public String Index(){
		return "forward:/index.jsp";
	}
	
	@RequestMapping(value="Home")
	public String Home(){
		return "pages/home";
	}
	
	@RequestMapping(value="UserManage")
	public String UserManage(){
		return "pages/userManage";
	}
	
	@RequestMapping("CPM")
	public String CPManage(){
		return "pages/CPManage";
	}
	
	@RequestMapping("CPP")
	public String CPparameter(){
		return "pages/CPparameter";
	}
	
	@RequestMapping(value="CPS")
	public String CPS(){
		return "pages/CPstatus";
	}
	
	@RequestMapping(value="Register")
	public String Register(){
		return "pages/register";
	}
	
	@RequestMapping(value="ForgetPassword")
	public String ForgetPW(){
		return "pages/forgetPW";
	}
	
	@RequestMapping(value="ForgetPassword/{step}")
	public ModelAndView ForgetPassword(@PathVariable("step")int step,HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String userName = request.getParameter("userName").trim();
		PCUser user = PCUserService.getPCUser(userName);
		int status = 1;
		switch(step){
		case 2 : status = 2;break;
		case 3 : status = 3;break;
		default : status = 1;
		}
		modelMap.put("user", user);
		modelMap.put("status", status);
		return new ModelAndView("pages/forgetPW", modelMap);
	}

	@RequestMapping(value="ResetPasswordSuccess/{userName}/{code}",method= RequestMethod.GET)
	public ModelAndView RsetPasswordSuccess(@PathVariable("userName")String userName,@PathVariable("code")String code,HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String CODE = "";
		try {
			CODE = URLDecoder.decode(code.trim(),"UTF-8");
			userName = URLDecoder.decode(userName.trim(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		modelMap.put("CODE",CODE);
		modelMap.put("UN",userName);
		return new ModelAndView("forgetPWSuccess", modelMap);
	}
	
	@RequestMapping(value="toUserDetails/{flag}",method= RequestMethod.POST)
	public ModelAndView toUserDetails(@PathVariable("flag")boolean flag,HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		String viewPage = "";
		Map<String, Object> userMaps = null;
		objectMapper = new ObjectMapper();	
		String jsonStr = "";
		try {
			jsonStr = URLDecoder.decode(request.getParameter("jsonStr").trim(),"UTF-8");
			userMaps = objectMapper.readValue(jsonStr,Map.class);
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewPage = flag == true?"userDetails":"muserDetails";
		modelMap.put("jsonData", userMaps);
		return new ModelAndView(viewPage, modelMap);
	}
	
	@RequestMapping(value="toCPLiveDetails",method= RequestMethod.POST)
	public ModelAndView toCPLiveDetails(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		String viewPage = "";
		String id = "";
		id = request.getParameter("CPid");
		modelMap.put("id", id);
		viewPage = "CPLiveDetails";
		return new ModelAndView(viewPage, modelMap);
	}
	
}
