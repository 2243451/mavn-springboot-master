
package com.lunatic.manager.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lunatic.framework.controller.BaseController;
import com.lunatic.manager.dto.ResultDTO;
import com.lunatic.manager.primary.doman.Device;
import com.lunatic.manager.service.DeviceService;



@Controller
@RequestMapping("/wapi")
public class DeviceController extends BaseController {



	@Autowired
	private DeviceService deviceManageservice;

	
	@RequestMapping(value = "/device/getAllDevice", method = RequestMethod.GET)
	@ResponseBody
	public ResultDTO getAllDevice() {
		try {
			List<Device> deviceList= deviceManageservice.getAllDeviceList();
			return ResultDTO.ok(deviceList);
		} catch (Exception e) {
			return ResultDTO.error(500, "服务器端发生错误");
		}
	}
	
}
