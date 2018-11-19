
package com.lunatic.manager.dto;


import com.tocel.framework.constant.WebStatusEnum;
import com.tocel.framework.model.BaseObject;

/**
 * <P>Description: TODO</P>
 * @ClassName: ResultDTO
 * @author Asdpboy Yan  2018年3月26日 下午6:53:49
 * @see
 */
public class ResultDTO extends BaseObject {
	private static final long serialVersionUID = 1L;

	public static final Integer CODE = WebStatusEnum.SUCCESS.getCode();

	private Integer code;

	private String msg;

	private Object result;

	public ResultDTO() {

	}

	public ResultDTO(Integer code) {
		this.code = code;
	}

	public ResultDTO(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResultDTO(Integer code, String msg, Object result) {
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public ResultDTO(Integer code, Object result) {
		this.code = code;
		this.result = result;
	}

	public ResultDTO(Object result) {
		this.result = result;
	}

	public static ResultDTO error() {
		return error(WebStatusEnum.SERVER_EXCEPTION.getCode(), "未知异常，请联系管理员");
	}

	public static ResultDTO error(String msg) {
		return error(WebStatusEnum.SERVER_EXCEPTION.getCode(), msg);
	}

	public static ResultDTO error(Integer code, String msg) {
		return new ResultDTO(code, msg);
	}

	public static ResultDTO error(Integer code, String msg, Object object) {
		return new ResultDTO(code, msg, object);
	}

	public static ResultDTO error(Integer code, Object object) {
		return new ResultDTO(code, object);
	}

	public static ResultDTO ok(String msg) {
		return new ResultDTO(CODE, msg);
	}

	public static ResultDTO ok(Object result) {
		return new ResultDTO(CODE, result);
	}

	public static ResultDTO ok(String msg, Object result) {
		return new ResultDTO(CODE, result);
	}

	public static ResultDTO ok() {
		return new ResultDTO(CODE);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
