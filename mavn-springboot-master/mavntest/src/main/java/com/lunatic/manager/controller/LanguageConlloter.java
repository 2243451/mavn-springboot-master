package com.lunatic.manager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lunatic.framework.utils.I18nMessageUtil;
import com.lunatic.manager.dto.ResultDTO;

@Controller
@RequestMapping("/wapi")
public class LanguageConlloter {
	
	@Autowired
	private I18nMessageUtil messageSourceUtil;
	
	
	@RequestMapping(value ="/Language", method = RequestMethod.GET)
	@ResponseBody
	public ResultDTO testLanguage() {
	String welcome = messageSourceUtil.getMessage("welcome");
	System.out.println(welcome);
	Object object=(Object)welcome;
	return ResultDTO.ok(object);
	}
}
