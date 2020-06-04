package com.haivera.processcenter.common.util;

import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

	private static Map<String, String> code2MsgMap = new HashMap<String, String>();

	/********** 返回码定义 **********/
	public static final String SUCCESS_CODE = "000000";
	public static final String SUCCESS_MSG = "成功";

	public static final String FINISH_CODE = "000001";
	public static final String FINISH_MSG = "流程结束";

	public static final String FAILED_CODE = "999999";
	public static final String FAILED_MSG = "由于系统原因导致异常，请稍后重试。如有问题，请联系管理员。";

	public static final String METHOD_ARGS_NOTVALID = "999901";
	public static final String METHOD_ARGS_NOTVALID_MSG = "参数校验异常";

	public static final String PERMISSION = "999902";
	public static final String PERMISSION_MSG = "权限异常，您没有权限";
	
	public static final String USER_NOT_LOGIN_CODE = "444";
	public static final String USER_NOT_LOGIN_MSG = "用户未登录，请先登录。";

	public static final String REQUIRE_PARAM_IS_NULL_CODE = "301";
	public static final String REQUIRE_PARAM_IS_NULL_MSG = "您输入的参数有误，请检查！";

	public static final String CAPTCHA_IS_WRONG_CODE = "302";
	public static final String CAPTCHA_IS_WRONG_MSG = "您输入的图形验证码有误，请重新输入！";
	
	public static final String USER_IS_EXIST_CODE = "303";
	public static final String USER_IS_EXIST_MSG = "您输入的用户名已存在，请重新输入！";

	private static final String KEY_RESPONSE_CODE = "rtnCode";
	private static final String KEY_RESPONSE_MSG = "rtnMsg";
	private static final String KEY_RESPONSE_DATA = "data";
	
	public static final String NO_PAGE = "1";//1表示不分页
	
	static {
		code2MsgMap.put(SUCCESS_CODE, SUCCESS_MSG);
		code2MsgMap.put(FAILED_CODE, FAILED_MSG);
	}

	/**
	 * 根据code获取message
	 * 
	 * @param code
	 * @return
	 */
	public static String getMsgByCode(String code) {
		return code2MsgMap.get(code);
	}

	/**
	 * 将ResponseInfo转换为ModelMap。
	 * 
	 * <pre>
	 * 用于将ResponseInfo转换为ESA支持的返回对象
	 * </pre>
	 * 
	 * @param responseInfo
	 * @return
	 */
	public static ModelMap toModelMap(ResponseInfo<?> responseInfo) {
		ModelMap map = new ModelMap();
		map.put(KEY_RESPONSE_CODE, responseInfo.getRtnCode());
		map.put(KEY_RESPONSE_MSG, responseInfo.getRtnMsg());
		map.put(KEY_RESPONSE_DATA, responseInfo.getData());
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static ResponseInfo getError(String msg){
		ResponseInfo<?> response = new ResponseInfo();
		response.setRtnCode(FAILED_CODE);
		response.setRtnMsg(msg);
		return response;
	}

}
