/*
 * @(#)com.tocel.framework.controller
 * @(#)BaseController.java	2018年2月28日
 * 
 * Copyright © 2001-2012, All rights reserved.
 * Tocel Information Technology ( Group ) Co., Ltd.
 */
package com.lunatic.framework.controller;

import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * <P>Description: TODO</P>
 * @ClassName: BaseController
 * @author Asdpboy Yan  2018年2月28日 下午6:24:34
 * @see
 */
public class BaseController {

	protected final Logger logger = LogManager.getLogger(getClass());
	

	protected boolean isNull(Object obj){
		if (obj == null) {
			return true;
		}
		if ((obj instanceof List)) {
			return ((List) obj).size() == 0;
		}
		if ((obj instanceof String)) {
			return ((String) obj).trim().equals("");
		}
		return false;
	}
}
