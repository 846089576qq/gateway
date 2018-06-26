/**
 * Copyright [2015-2017]
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
package com.gateway.payment.service.util;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2018年1月17日
 */
public class XmlUtil {

	public static Object xmltoBean(String xml, Class<?> clazz, String encoding) {
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(clazz);
			Unmarshaller um = jaxbContext1.createUnmarshaller();
			Object obj = um.unmarshal(new ByteArrayInputStream(xml.getBytes(encoding)));
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
