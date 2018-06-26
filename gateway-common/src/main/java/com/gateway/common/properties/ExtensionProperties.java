/**
 * classpath:prop * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.common.properties;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.zitopay.foundation.common.env.DigestEnvironment;

/**
 * 扩展属性文件
 * 
 * @author caishuangxi
 *
 */
@Component
public class ExtensionProperties {

	@Inject
	private DigestEnvironment environment;

	// public WechatProperty getWechatProperty() {
	// return new WechatProperty();
	// }
	//
	// public class WechatProperty {
	//
	// /**
	// * 融拓智付 WECHAT_RTZF_APPID
	// */
	// public final String WECHAT_RTZF_APPID = environment.getProperty("WECHAT_RTZF_APPID");
	//
	// /**
	// * 融拓智付 WECHAT_RTZF_APPSECRET
	// */
	// public final String WECHAT_RTZF_APPSECRET = environment.getProperty("WECHAT_RTZF_APPSECRET");
	//
	//
	// }


	/**
	 * @return 融智付 银行卡列表
	 */
	public String getBankList() {
		return environment.getProperty("bank.list");
	}

}
