package com.lunatic.manager.constant;

/**
 * <P>Description: 接口状态码及对应的错误信息枚举类</P>
 * @ClassName: WebStatusEnum
 * @author Asdpboy Yan  2018年4月9日 下午2:15:37
 * @see
 */
public enum WebStatusEnum {

	/**
	 * 定义接口返回状态码
	 *
	 */

	SUCCESS(0, ""), PAGE_NOT_FOUND(404, "page not found!"), PARAM_ERROR(400, "param error!"), SERVER_EXCEPTION(500,
			"server exception!"), PERMISSION_DENIED(403, "Permission denied"), //没有权限
	FAILED(7000, "failed"), NOT_ANY_PARAMS(7001, "Not any params"), //没有任何数据
	NOT_SOME_PARAMS(7002, "Data defect"), //数据有缺陷
	LOGIN_FAILED(3021, "The employee has already logged in"), //用户已经登录
	PLEASE_SELECT_BRANCH(3023, "Please select branch"),//请选择分厂
	EXIST_DATA(3024, "Exist same data"),//存在相同的数据
	IDSEAT_DEVICE_BINDED(7003, "have binded");//数据已经被引用

	/**
	 * 系统码
	 */
	private Integer code;

	/**
	 * 描述
	 */
	private String desc;

	private WebStatusEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
