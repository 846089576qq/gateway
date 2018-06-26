package com.gateway.channel.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gateway.common.algorithm.MD5;
import com.gateway.common.properties.ChannelProperties;
import com.gateway.common.properties.EnvProperties;
import com.zitopay.foundation.common.env.DigestEnvironment;
import com.zitopay.foundation.common.exception.ServiceException;

public class BaseChannelServiceImpl {

//    @Autowired
//    protected ChannelProperties channelProperties;

    @Autowired
    protected EnvProperties envProperties;
    
    @Inject
    protected DigestEnvironment environment;

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T XMLStringToBean(String xml, Class<T> bean) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(bean);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String beanToXml(Object obj) throws ServiceException {
        String result = null;

        JAXBContext context;
        try {
            context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null) {
            return null;
        }
        try {
            Object obj = beanClass.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
            return (T) obj;
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        String charset = params.get("_input_charset");
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    
    
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param encode
     *            是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, String charset) {

        // params = paraFilter(params);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            try {
                value = URLEncoder.encode(value, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * RSA私钥加密
     *
     * @param srcSignPacket
     * @param privateKey
     * @return
     * @throws ServiceException
     */
    public static String rsaSign(String srcSignPacket, String privateKey) throws ServiceException {
        try {
            byte[] bytesKey = (new BASE64Decoder()).decodeBuffer(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytesKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = null;
            signature = Signature.getInstance("MD5WithRSA");
            signature.initSign(priKey);
            signature.update(srcSignPacket.getBytes("GBK"));
            String sign = (new BASE64Encoder()).encodeBuffer(signature.sign());
            return sign;
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (InvalidKeyException e) {
            throw new ServiceException(e);
        } catch (SignatureException e) {
            throw new ServiceException(e);
        } catch (InvalidKeySpecException e) {
            throw new ServiceException(e);
        }
    }

    public static String assemblyGETParam(Map<String, String> map) {
        StringBuffer sbf = new StringBuffer("");
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == null || "".equals(entry.getValue())) {// null为跳出本次循环
                continue;
            }
            sbf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String info = "";
        if (sbf.length() > 0) {
            info = sbf.substring(0, sbf.length() - 1);
        }
        return info;
    }

    public static String assemblyGETParam(JSONObject jsonObj) {
        return assemblyGETParam((Map) jsonObj);
    }


    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if ("sign".equals(key)) {
                continue;
            }
            if (value == null || "".equals(value)) {
                continue;
            }
            if (i == keys.size() - 1) {
                sb.append(key).append("=").append(value);
            } else {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        return sb.toString();
    }

    public static String createLinkStringMd5(Map<String, String> params, String key) {
        String sign = createLinkString(params) + "&key=" + key;
        String su = MD5.encode(sign).toUpperCase();// 转大写
        return su;
    }

    /*xml转Bean*/
    public static <T> T xmlToBean(String xmlStr, Class<T> cls) throws ServiceException {
        try {
            Class clazz = cls;
            //创建对象
            T obj = (T) clazz.newInstance();
            SAXReader reader = new SAXReader();

            InputStream ins = new ByteArrayInputStream(xmlStr.getBytes());
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();

            @SuppressWarnings("unchecked")
            List<Element> list = root.elements();
            for (Element e : list) {
                Field privateField = getPrivateField(e.getName(), cls);
                if (privateField == null) {
                    throw new NoSuchFieldException();
                }
                privateField.setAccessible(true);
                /*判断类型*/
                String type = privateField.getGenericType().toString();
                if (type.equals("class java.lang.String")) {
                    privateField.set(obj, e.getText());
                } else if (type.equals("class java.lang.Boolean")) {
                    privateField.set(obj, Boolean.parseBoolean(e.getText()));
                } else if (type.equals("class java.lang.Long")) {
                    privateField.set(obj, Long.parseLong(e.getText()));
                } else if (type.equals("class java.lang.Integer")) {
                    privateField.set(obj, Integer.parseInt(e.getText()));
                } else if (type.equals("class java.lang.Double")) {
                    privateField.set(obj,Double.parseDouble(e.getText()));
                } else if (type.equals("class java.lang.Float")) {
                    privateField.set(obj,Float.parseFloat(e.getText()));
                }
            }
            ins.close();
            return obj;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /*拿到反射父类私有属性*/
    private static Field getPrivateField(String name, Class cls) {
        Field declaredField = null;
        try {
            declaredField = cls.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {

            if (cls.getSuperclass() == null) {
                return declaredField;
            } else {
                declaredField = getPrivateField(name, cls.getSuperclass());
            }
        }
        return declaredField;
    }


    public static Map<String, String> xmlToMap(String xml) throws ServiceException {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        try {
            InputStream ins = new ByteArrayInputStream(xml.getBytes());
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();

            @SuppressWarnings("unchecked")
            List<Element> list = root.elements();
            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            ins.close();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return map;
    }


    /**
     * json大写转小写
     *
     * @param jSONArray1 jSONArray1
     * @return JSONObject
     */
    public static net.sf.json.JSONObject transToLowerObject(net.sf.json.JSONObject jSONArray1) {
        net.sf.json.JSONObject jSONArray2 = new net.sf.json.JSONObject();
        Iterator it = jSONArray1.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object object = jSONArray1.get(key);
            if (object.getClass().toString().endsWith("String")) {
                jSONArray2.accumulate(key.toLowerCase(), object);
            } else if (object.getClass().toString().endsWith("JSONObject")) {
                jSONArray2.accumulate(key.toLowerCase(), transToLowerObject((net.sf.json.JSONObject) object));
            } else if (object.getClass().toString().endsWith("JSONArray")) {
                jSONArray2.accumulate(key.toLowerCase(), transToArray(jSONArray1.getJSONArray(key)));
            }
        }
        return jSONArray2;
    }

    /**
     * jsonArray转jsonArray
     *
     * @param jSONArray1 jSONArray1
     * @return JSONArray
     */
    public static JSONArray transToArray(JSONArray jSONArray1) {
        JSONArray jSONArray2 = new JSONArray();
        for (int i = 0; i < jSONArray1.size(); i++) {
            Object jArray = jSONArray1.getJSONObject(i);
            if (jArray.getClass().toString().endsWith("JSONObject")) {
                jSONArray2.add(transToLowerObject((net.sf.json.JSONObject) jArray));
            } else if (jArray.getClass().toString().endsWith("JSONArray")) {
                jSONArray2.add(transToArray((JSONArray) jArray));
            }
        }
        return jSONArray2;
    }

    /**
   	 * 除去数组中的空值和签名参数
   	 * 
   	 * @param sArray
   	 *            签名参数组
   	 * @return 去掉空值与签名参数后的新签名参数组
   	 */
   	public static Map<String, String> paraFilter(Map<String, String> sArray) {

   		Map<String, String> result = new HashMap<String, String>();

   		if (sArray == null || sArray.size() <= 0) {
   			return result;
   		}

   		for (String key : sArray.keySet()) {
   			String value = sArray.get(key);
   			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
   				continue;
   			}
   			// try {
   			// value = URLEncoder.encode(value,charset);
   			// } catch (UnsupportedEncodingException e) {
   			// e.printStackTrace();
   			// }
   			result.put(key, value);
   		}

   		return result;
   	}

    /**
     *
     *
     * 方法描述: json字符串转对象<br>
     * @return T <br>
     * 作者： 徐诚 <br>
     * 创建时间： 2016-5-20 下午05:34:57
     */
    public static final <T> T stringToObject(String result, Class<T> clazz) {
        try{
            T a = JSON.parseObject(result,clazz);
            return a;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
