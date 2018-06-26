package com.gateway.common.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.zitopay.datagram.ResponseInfo;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 信息提示值枚举类，默认情况下枚举是单实例类型,范序列化单例会被破坏</br> dubbo反序列化时自定义变量会全部为null,故不利用此作为接口方法返回值，接口方法返回值建议全部使用Map
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2016年12月19日
 */
public enum ResponseInfoEnum implements ResponseInfo {

	/* 调用成功提示,唯一 */
	调用成功("0x0000", "调用成功"),

	/* 全局提示,2x00开头 */
	调用失败("2x0001", "调用失败"),
	参数为空("2x0002", "参数不能为空,系统调用失败!"),
	参数有误("2x0003", "参数有误,系统调用失败!"), 
	验签失败("2x0004", "签名验证失败"), 
	二维码生成失败("2x0005","二维码生成失败"), 
	预检查失败("2x0006", "支付预检查失败，请联系管理员"), 
	签名被篡改("2x0007", "签名被篡改"), 
	签名KEY获取失败("2x0008", "签名KEY获取失败"), 
	回调异常("2x0009","第三方回调异常,请检查!"), 
	参数组装失败("2x0010", "参数组装失败,系统调用失败!"), 
	内容为空("2x0011", "内容为空"), 
	系统异常("2x0012", "系统异常"), 
	转换异常("2x0013", "转换异常"), 
	缺少参数("2x0014", "缺少参数"), 
	第三方异常("2x0015", "第三方返回异常"), 
	字符集错误("2x0016", "字符集错误"), 
	报文异常("2x0014", "报文有误,请检查报文"), 
	服务处理异常("2x0015","服务处理异常,请联系服务方"),
	支付回调失败("2x0016","支付回调处理失败"),
	提现回调失败("2x0017","提现回调处理失败"),
	退款回调失败("2x0018","退款回调处理失败"),
	退款成功("3x0001","退款处理成功"),
	退款失败("3x0002","退款处理失败"),
	退款查询失败("3x0003","退款查询失败"),

	/* 订单提示,2x10开头 */
	订单不存在("2x1000", "支付订单不存在!"), 
	订单状态失败("2x1001", "支付订单状态不匹配!"), 
	订单未支付("2x1002", "订单未支付"), 
	订单数不一致("2x1003", "订单数目不一致"),
	条形码无效("2x1004","条形码无效"),
	等待用户输入密码("2x1005","等待用户输入密码"),
	订单已存在("2x1006","订单已经存在"),
	退款订单不存在("2x1007","退款订单不存在"),
	退款订单已存在("2x1008","退款订单已存在"), 

	/* 商户应用提示,2x11开头 */
	商户应用不存在("2x1100", "商户应用不存在!"), 
	商户应用被禁用("2x1101", "商户应用被禁用"),

	/* 商户提示,2x12开头 */
	商户不存在("2x1200", "商户不存在!"), 
	商户已禁用("2x1201", "商户已经被禁用!"),
	商户公钥不存在("2x1202","商户公钥不存在"),   
	获取商户公钥异常("2x1203","商户公钥获取异常!"),
	
	/* 通道提示,2x13开头 */
	通道不存在("2x1300", "通道不存在!"), 
	通道已禁用("2x1301", "通道已禁用!"), 
	商户通道被禁用("2x1302", "商户通道被禁用!"), 
	通道数量不一致("2x1303", "商户通道数目不一致!"), 
	商户通道不存在("2x1304","商户通道不存在!"),
	
	/* 商户认证提示,2x14开头 */
	商户认证记录不存在("2x1400","商户认证记录未找到"),
	商户认证信息不存在("2x1401","商户认证信息不存在,请查看商户认证状态"),
	;

	/**
	 * @param msg
	 * @param code
	 * @param success
	 */
	private ResponseInfoEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;

	private String msg;

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static final String KEY_MSG = "msg", KEY_CODE = "code", KEY_DESCRIPTION = "description",KEY_CONTENT="content";

	public Map<String, Object> getMap(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map != null && !map.isEmpty())
			resultMap.putAll(map);
		resultMap.put(KEY_MSG, getMsg());
		resultMap.put(KEY_CODE, this.getCode());
		return resultMap;
	}
	

	public Map<String, Object> getMap(String description, Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map != null && !map.isEmpty())
			resultMap.putAll(map);
		resultMap.put(KEY_MSG, getMsg());
		resultMap.put(KEY_CODE, this.getCode());
		resultMap.put(KEY_DESCRIPTION, description);
		return resultMap;
	}

	public Map<String, Object> getMap(String description) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(KEY_MSG, getMsg());
		resultMap.put(KEY_CODE, this.getCode());
		resultMap.put(KEY_DESCRIPTION, description);
		return resultMap;
	}

	public Map<String, Object> getMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(KEY_MSG, getMsg());
		resultMap.put(KEY_CODE, this.getCode());
		return resultMap;
	}

	/**
	 * 判断接口返回值内容一致性
	 * 
	 * @param map
	 * @return
	 */
	public boolean equals(Map<String, Object> map) {
		if (null == map || map.isEmpty())
			return false;
		if (this.getCode().equals(map.get(KEY_CODE)))
			return true;
		return false;
	}

	/**
	 * 返回json 格式
	 * 
	 * @return
	 */
	public String toJson() {
		return BeanUtils.bean2JSON(this.getMap());
	}

	/**
	 * 返回json 格式
	 * 
	 * @return
	 */
	public String toJson(Map<String, Object> map) {
		return BeanUtils.bean2JSON(this.getMap(map));
	}

	public static ResponseInfoEnum valueOf(Map<String, Object> map) {
		for (ResponseInfoEnum info : ResponseInfoEnum.values()) {
			if (info.getCode().equals(map.get(KEY_CODE))) {
				String msg = StringUtils.substringBefore(map.get(KEY_MSG).toString(), " 描述：");
				if(null != map.get(KEY_DESCRIPTION)) {
					info.setMsg(msg + " 描述：" + map.get(KEY_DESCRIPTION));
				}else {
					info.setMsg(msg);
				}
				return info;
			}
		}
		return null;
	}
}
