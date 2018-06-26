package com.gateway.channel.service.ysf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gateway.channel.entity.ysf.pay.YsfPayInDto;
import com.gateway.channel.entity.ysf.query.YsfQueryInDto;
import com.gateway.channel.entity.ysf.tansfer.YsfTranserInDto;
import com.gateway.channel.service.BaseChannelServiceImpl;
import com.gateway.channel.utils.ysf.CxfCommunication;
import com.gateway.channel.utils.ysf.DESC;
import com.gateway.channel.utils.ysf.HttpRequest;
import com.gateway.channel.utils.ysf.OrderQueryService;
import com.gateway.channel.utils.ysf.TradeQueryService;
import com.gateway.channel.utils.ysf.Verify;
import com.gateway.channel.utils.ysf.XMLFactory;
import com.zitopay.foundation.common.exception.ServiceException;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class YsfChannelServiceImpl extends BaseChannelServiceImpl implements IYsfChannelService {

	private static final Logger logger = LoggerFactory.getLogger(YsfChannelServiceImpl.class);

	@Override
	public String pay(YsfPayInDto merchantForm, String key) throws ServiceException {
		// body部分
		String bodyXml = "<body>" +
				"<MerBillNo>" + merchantForm.getMerBillNo() + "</MerBillNo>" +
				"<Lang>" + merchantForm.getLang() + "</Lang>" +
				"<Amount>" + merchantForm.getAmount() + "</Amount>" +
				"<Date>" + merchantForm.getDate() + "</Date>" +
				"<CurrencyType>" + merchantForm.getCurrencyType() + "</CurrencyType>" +
				"<GatewayType>" + merchantForm.getGatewayType() + "</GatewayType>" +
				"<Merchanturl>" + merchantForm.getMerchantUrl() + "</Merchanturl>" +
				"<FailUrl><![CDATA[" + merchantForm.getFailUrl() + "]]></FailUrl>" +
				"<Attach><![CDATA[" + merchantForm.getAttach() + "]]></Attach>" +
				"<OrderEncodeType>" + merchantForm.getOrderEncodeType() + "</OrderEncodeType>" +
				"<RetEncodeType>" + merchantForm.getRetEncodeType() + "</RetEncodeType>" +
				"<RetType>" + merchantForm.getRettype() + "</RetType>" +
				"<ServerUrl><![CDATA[" + merchantForm.getServerUrl() + "]]></ServerUrl>" +
				"<BillEXP>" + merchantForm.getBillExp() + "</BillEXP>" +
				"<GoodsName>" + merchantForm.getGoodsName() + "</GoodsName>" +
				"<IsCredit>" + merchantForm.getIsCredit() + "</IsCredit>" +
				"<BankCode>" + merchantForm.getBankcode() + "</BankCode>" +
				"<ProductType>" + merchantForm.getProductType() + "</ProductType>" +
				"</body>";
		// MD5签名
		String sign = DigestUtils.md5Hex(Verify.getBytes(bodyXml + merchantForm.getMerCode() + key, "UTF-8"));
		// xml
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String xml = "<Ips>" +
				"<GateWayReq>" +
				"<head>" +
				"<Version>" + merchantForm.getVersion() + "</Version>" +
				"<MerCode>" + merchantForm.getMerCode() + "</MerCode>" +
				"<MerName>" + merchantForm.getMerName() + "</MerName>" +
				"<Account>" + merchantForm.getMerAcccode() + "</Account>" +
				"<MsgId>" + "msg" + date + "</MsgId >" +
				"<ReqDate>" + date + "</ReqDate >" +
				"<Signature>" + sign + "</Signature>" +
				"</head>" +
				bodyXml +
				"</GateWayReq>" +
				"</Ips>";
		logger.info(">>>>> 订单支付 请求信息: {}", xml);
		String url = "http://gateway.dikeww.top/";
		try {
			if("210061".equals(merchantForm.getMerCode())) {
				url = "http://gateway.ansijjw.top/";
			}else if("210915".equals(merchantForm.getMerCode())) {
				url = "http://gateway.wrrrwl.top/";
			}
			url += "payment/form?pGateWayReq=" + URLEncoder.encode(xml, "UTF-8") + "&serverUrl=" + environment.getProperty("YSF_NET_URL") + "gateway/payment.do";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static void main(String[] args) {
		String text = "<body><MerBillNo>2018041215064708775</MerBillNo><CurrencyType>156</CurrencyType><Amount>0.01</Amount><Date>20180412</Date><Status>Y</Status><Msg><![CDATA[支付成功！]]></Msg><Attach><![CDATA[null]]></Attach><IpsBillNo>BO2018041215070056687</IpsBillNo><IpsTradeNo>2018041215070014920</IpsTradeNo><RetEncodeType>17</RetEncodeType><BankBillNo>710013988000</BankBillNo><ResultType>0</ResultType><IpsBillTime>20180412150741</IpsBillTime></body>";
		logger.info(DigestUtils.md5Hex(Verify.getBytes(text + "207972" + "Pn334LDWTMPSToS3dxWdC35Hw4zOenrSqXgxFgUhBx4XyFUHDT9rXP85OevyyxW5qtXCXwBgHsy6bPYX6fJDTDAKGkflSAKDBxw0wbyalTQKfBjWPPNx4H4Bojb0kUbl", "UTF-8")));
	}

	/**
	 * 验签
	 *
	 * @param xml
	 * @return
	 */
	public Map<String, String> checkNotify(String xml, String merCode, String directStr, String ipsRsaPub) {
		String result = "true";
		if (xml == null){
			result = "false";
		}
		String OldSign = getSign(xml); // 返回签名
		String text = getBodyXml(xml); // body
		logger.info("MD5验签，验签文：" + text + "\n待比较签名值:" + OldSign);
		String retEncodeType =  getRetEncodeType(xml); //加密方式
		logger.info("加密方式 ：" + retEncodeType);
		if (OldSign == null || retEncodeType == null) {
			result = "false";
		}
		
		// 根据验签方式进行验签
		if (retEncodeType.equals("16")){
			if(!Verify.verifyMD5withRSA(ipsRsaPub, text + merCode, OldSign)) {
				result = "false";
			}
		} else if (retEncodeType.equals("17")){
			result = DigestUtils.md5Hex(Verify.getBytes(text + merCode + directStr, "UTF-8"));
			if(OldSign.equals(result)) {
				result = "true";
			}
		} else {
			result = "false";
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", getStatus(xml));
		resultMap.put("merBillNo", getMerBillNo(xml));
		resultMap.put("rspCode", getRspCode(xml));
		resultMap.put("success", result);
		resultMap.put("ipsTradeNo", getIpsTradeNo(xml));
		resultMap.put("bankBillNo", getBankBillNo(xml));
		return resultMap;
	}

	/**
	 * 获取报文中<Signature></Signature>部分
	 * @param xml
	 * @return
	 */
	public String getSign(String xml) {
		int s_index = xml.indexOf("<Signature>");
		int e_index = xml.indexOf("</Signature>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}

	/**
	 * 获取body部分
	 * @param xml
	 * @return
	 */
	public String getBodyXml(String xml) {
		int s_index = xml.indexOf("<body>");
		int e_index = xml.indexOf("</body>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index, e_index + 7);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspCode></RspCode>部分
	 * @param xml
	 * @return
	 */
	public String getRspCode(String xml) {
		int s_index = xml.indexOf("<RspCode>");
		int e_index = xml.indexOf("</RspCode>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 9, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspMsg></RspMsg>部分
	 * @param xml
	 * @return
	 */
	public String getRspMsg(String xml) {
		int s_index = xml.indexOf("<RspMsg>");
		int e_index = xml.indexOf("</RspMsg>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 8, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<ErrorMsg></ErrorMsg>部分
	 * @param xml
	 * @return
	 */
	public String getErrorMsg(String xml) {
		int s_index = xml.indexOf("<ErrorMsg>");
		int e_index = xml.indexOf("</ErrorMsg>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 10, e_index);
		}
		return sign;
	}

	/**
	 * 获取报文中<BatchErrorMsg></BatchErrorMsg>部分
	 * @param xml
	 * @return
	 */
	public String getBatchErrorMsg(String xml) {
		int s_index = xml.indexOf("<BatchErrorMsg>");
		int e_index = xml.indexOf("</BatchErrorMsg>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 15, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspCode></RspCode>部分
	 * @param xml
	 * @return
	 */
	public String getBatchBillno(String xml) {
		int s_index = xml.indexOf("<BatchBillno>");
		int e_index = xml.indexOf("</BatchBillno>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 13, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspCode></RspCode>部分
	 * @param xml
	 * @return
	 */
	public String getBatchStatus(String xml) {
		int s_index = xml.indexOf("<BatchStatus>");
		int e_index = xml.indexOf("</BatchStatus>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 13, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<TrdAmt></TrdAmt>部分
	 * @param xml
	 * @return
	 */
	public String getTrdAmt(String xml) {
		int s_index = xml.indexOf("<TrdAmt>");
		int e_index = xml.indexOf("</TrdAmt>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 8, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspCode></RspCode>部分
	 * @param xml
	 * @return
	 */
	public String getRspCodeSmall(String xml) {
		int s_index = xml.indexOf("<rspCode>");
		int e_index = xml.indexOf("</rspCode>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 9, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RspCode></RspCode>部分
	 * @param xml
	 * @return
	 */
	public String getIpsTradeNo(String xml) {
		int s_index = xml.indexOf("<IpsTradeNo>");
		int e_index = xml.indexOf("</IpsTradeNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 12, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<BankBillNo></BankBillNo>部分
	 * @param xml
	 * @return
	 */
	public String getBankBillNo(String xml) {
		int s_index = xml.indexOf("<BankBillNo>");
		int e_index = xml.indexOf("</BankBillNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 12, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<MerBillNo></MerBillNo>部分
	 * @param xml
	 * @return
	 */
	@Override
	public String getMerBillNo(String xml) {
		int s_index = xml.indexOf("<MerBillNo>");
		int e_index = xml.indexOf("</MerBillNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<Status></Status>部分
	 * @param xml
	 * @return
	 */
	public String getStatus(String xml) {
		int s_index = xml.indexOf("<Status>");
		int e_index = xml.indexOf("</Status>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 8, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<RetEncodeType></RetEncodeType>部分
	 * @param xml
	 * @return
	 */
	public String getRetEncodeType(String xml) {
		int s_index = xml.indexOf("<RetEncodeType>");
		int e_index = xml.indexOf("</RetEncodeType>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 15, e_index);
		}
		return sign;
	}


	@Override
	public Map<String, String> query(YsfQueryInDto ysfQueryInDto, String key) {
		//获取参数
		String merCode = ysfQueryInDto.getMerCode();
		// body
		String bodyXml = "<body>" +
				"<MerBillNo>" + ysfQueryInDto.getMerBillNo() + "</MerBillNo>" +
				"<Date>" + ysfQueryInDto.getDate() + "</Date>" +
				"<Amount>" + ysfQueryInDto.getAmount() + "</Amount>" +
				"</body>";
		// MD5签名
		String sign = DigestUtils.md5Hex(Verify.getBytes(bodyXml + merCode + key, "UTF-8"));
		// xml
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String xml = "<Ips>" + "<OrderQueryReq>" +
				"<head>" +
				"<Version>v1.0.0</Version>" +
				"<MerCode>" + merCode + "</MerCode >" +
				"<MerName>" + ysfQueryInDto.getMerName() + "</MerName>" +
				"<Account>" + ysfQueryInDto.getAccCode() + "</Account>" +
				"<MsgId>" + "msg" + date + "</MsgId>" +
				"<ReqDate>" + date + "</ReqDate>" +
				"<Signature>" + sign + "</Signature>" +
				"</head>" +
				bodyXml +
				"</OrderQueryReq>" + "</Ips>";
		logger.info(">>>>> 商户订单号查询订单信息 请求报文: " + xml);
		OrderQueryService orderQueryService = (OrderQueryService)CxfCommunication.getWsPort(environment.getProperty("YSF_NET_URL") + "services/order", OrderQueryService.class);
		//调用ws接口  获取响应报文
		String resultXml = orderQueryService.getOrderByMerBillNo(xml);
		logger.info(">>>>> response xml: " + resultXml);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("rspCode", getRspCode(resultXml));
		resultMap.put("status", getStatus(resultXml));
		resultMap.put("IpsBillNo", getIpsBillNo(resultXml));
		return resultMap;
	}
	
	/**
	 * 验签
	 * @param xml
	 * @return
	 */
	public boolean checkSign(String merCode, String directStr, String xml) {

		if (xml == null){
			return false;
		}
		String OldSign = getSign(xml); // 返回签名
		String text = getBodyXml(xml); // body
		logger.info("MD5验签，验签文：" + text + "\n待比较签名值:" + OldSign);
		String	result = DigestUtils
				.md5Hex(Verify.getBytes(text + merCode + directStr,
						"UTF-8"));
		if (OldSign == null || result == null || !OldSign.equals(result)) {
			return false;
		}
		return true;
	}


	@Override
	public Map<String, String> transfer(YsfTranserInDto in, String key, String deskey, String desiv) throws ServiceException {
		String body = "<body><merBillNo>" + in.getMerBillNo() + "</merBillNo><transferType>" + in.getTransferType()+ "</transferType><merAcctNo>" + in.getMerAcctNo() + "</merAcctNo><customerCode>" + in.getCustomerCode() + "</customerCode><transferAmount>" + in.getTransferAmount() + "</transferAmount><collectionItemName>" + in.getCollectionItemName() + "</collectionItemName><remark></remark></body>";
		
		// 请求 xml
		String supplierAddXml = XMLFactory.createTransXML(body, key);
		logger.info("请求 xml组装  " + supplierAddXml);
		
		//3des加密  请求
		// https post
		String createDesXML = XMLFactory.createDesXML(supplierAddXml, in.getMerCode(), deskey, desiv);
		String fpmsReturn = null;
		try {
			fpmsReturn =  HttpRequest.sendPost(environment.getProperty("YSF_API_URL") + "action/trade/transfer.do", "ipsRequest=" + createDesXML);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("加密的 xml=" + fpmsReturn);
		String decrypt3DES = DESC.decrypt3DES(getP3DesXmlPara(fpmsReturn), deskey, desiv);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("rspCode", getRspCodeSmall(fpmsReturn));
		resultMap.put("ipsBillNo", getIpsBillNoSmall(decrypt3DES));
		resultMap.put("tradeId", getTradeId(decrypt3DES));
		resultMap.put("ipsFee", getIpsFee(decrypt3DES));
		resultMap.put("tradeState", getTradeState(decrypt3DES));
		return resultMap;
	}
	
	/**
	 * 获取报文中<BankBillNo></BankBillNo>部分
	 * @param xml
	 * @return
	 */
	public String getP3DesXmlPara(String xml) {
		int s_index = xml.indexOf("<p3DesXmlPara>");
		int e_index = xml.indexOf("</p3DesXmlPara>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 14, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<ipsBillNo></ipsBillNo>部分
	 * @param xml
	 * @return
	 */
	public String getIpsBillNo(String xml) {
		int s_index = xml.indexOf("<IpsBillNo>");
		int e_index = xml.indexOf("</IpsBillNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<BatchNo></BatchNo>部分
	 * @param xml
	 * @return
	 */
	public String getBatchNo(String xml) {
		int s_index = xml.indexOf("<BatchNo>");
		int e_index = xml.indexOf("</BatchNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 9, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<ipsBillNo></ipsBillNo>部分
	 * @param xml
	 * @return
	 */
	public String getIpsBillNoSmall(String xml) {
		int s_index = xml.indexOf("<ipsBillNo>");
		int e_index = xml.indexOf("</ipsBillNo>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<tradeId></tradeId>部分
	 * @param xml
	 * @return
	 */
	public String getTradeId(String xml) {
		int s_index = xml.indexOf("<tradeId>");
		int e_index = xml.indexOf("</tradeId>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 9, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<ipsFee></ipsFee>部分
	 * @param xml
	 * @return
	 */
	public String getIpsFee(String xml) {
		int s_index = xml.indexOf("<ipsFee>");
		int e_index = xml.indexOf("</ipsFee>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 8, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<tradeState></tradeState>部分
	 * @param xml
	 * @return
	 */
	public String getTradeState(String xml) {
		int s_index = xml.indexOf("<tradeState>");
		int e_index = xml.indexOf("</tradeState>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 12, e_index);
		}
		return sign;
	}
	
	@Override
	public Map<String, String> issued(Map<String, String> params,String key, String deskey, String iv) {
		// 获取参数
		String merCode = params.get("MerCode");
		//（<Detail>为重复节点,可多次出现）
	    String detail=
	           "<MerBillNo>"+ params.get("MerBillno")+"</MerBillNo>" +    
	           "<AccountName><![CDATA["+ params.get("AccountName")+"]]></AccountName>" +   
	           "<AccountNumber>"+ params.get("AccountNumber")+"</AccountNumber>" +    
	           "<BankName><![CDATA["+ params.get("BankName")+"]]></BankName>" +    
	           "<BranchBankName><![CDATA["+ params.get("BranchBankName")+"]]></BranchBankName>" +    
	           "<BankCity><![CDATA["+ params.get("BankCity")+"]]></BankCity>" +    
	           "<BankProvince><![CDATA["+ params.get("BankProvince")+"]]></BankProvince>" +    
	           "<BillAmount>"+ params.get("BillAmount")+"</BillAmount>" +    
	           "<IdCard>"+ params.get("IdCard")+"</IdCard>" +    
	           "<MobilePhone>"+ params.get("MobilePhone")+"</MobilePhone>"
	           + "<Remark><![CDATA[" + "测试" + "]]></Remark>";
	    String detailDes=null;
	    try {
	    	//对Detail内容用3des加密
			 detailDes=DESC.encrypt3DES(detail, deskey, iv);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    
	    String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    
	    // body前部分
	 	String bodyXmlh=	"<Body>" + 
	 	    "<BizId>1</BizId>" +  
	 	    "<ChannelId>3</ChannelId>" +  
	 	    "<Currency>156</Currency>" +   
	 	    "<Date>"+ date+"</Date>" +    
	 	    "<Attach><![CDATA[备注]]></Attach>" +    
	 	   "<IssuedDetails>" +  "<Detail>";
	    
	 	//body尾部
	    String bodyXmlf= "</Detail>"+"</IssuedDetails>" + "</Body>" ;
		
	    //拼接完整<Body></Body>
	    String bodyXml=bodyXmlh+detailDes+bodyXmlf;
	    logger.info("bodyXml:"+bodyXml);
	    
        // 利用body+数字证书做MD5签名
	    String sign = DigestUtils.md5Hex(Verify.getBytes(bodyXml  + key,"UTF-8"));
		logger.info("sign:"+sign);
		
		// 拼接发送ips完整的xml报文
		String xml =
		"<Req>"+ 
		  "<Head>" + 
		    "<Version>v1.0.0</Version>" +  
		    "<MerCode>" + merCode + "</MerCode>" +  
		    "<MerName>" + params.get("MerName") + "</MerName>" +  
		    "<Account>" + params.get("Account") + "</Account>" +  
		    "<MsgId>"+date+"</MsgId>" +  
		    "<ReqDate>" + date + "</ReqDate>" +  
		    "<Signature>"+ sign + "</Signature>" + 
	  "</Head>"+
		bodyXml +
	"</Req>";
		
		logger.info(">>>>> 委托付款 请求报文: " + xml);
		//拼接webservice  url地址
		String wsUrlSdl=environment.getProperty("YSF_DF_API_URL") +"issued?wsdl";
		
		//代理方式调用webservice
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsUrlSdl);
        Object[] result=null;
		try {
			result = client.invoke("issued", xml);//调用webservice
			//调用ws接口  获取响应报文
			logger.info("返回报文---------------------------------------");
			logger.info("非客户端模式"+(String) result[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String resultXml=(String) result[0];
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("RspCode", getRspCode(resultXml));
		resultMap.put("RspMsg", getRspMsg(resultXml));
		resultMap.put("ErrorMsg", getErrorMsg(resultXml));
		resultMap.put("BatchErrorMsg", getBatchErrorMsg(resultXml));
		resultMap.put("BatchBillno", getBatchBillno(resultXml));
		resultMap.put("BatchStatus", getBatchStatus(resultXml));
		return resultMap;
	}
	
	@Override
	public Map<String, String> issuedSearch(Map<String, String> params,String directStr) {
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //获取参数
        String merCode = params.get("merCode");
        // body （<Param>为重复节点）
        String bodyXml =
                "<body>" +
                        "<BatchNo>" + params.get("batchNo") + "</BatchNo>" +
                        "<MerBillNo>" + params.get("MerBillNo") + "</MerBillNo>" +
                        /*"<Date>" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "</Date>" +*/
                        "</body>";

        // MD5签名
        String sign = DigestUtils
                .md5Hex(Verify.getBytes(bodyXml + merCode + directStr,
                        "UTF-8"));
        // 发送给ipsxml
        String xml = "<Ips>" +
                "<IssuedTradeReq>" +
                "<head>" +
                "<Version>v1.0.0</Version>" +
                "<MerCode>" + merCode + "</MerCode>" +
                "<MerName>" + params.get("merName") + "</MerName>" +
                "<Account>" + params.get("accCode") + "</Account>" +
                "<MsgId>" + "msg" + date + "</MsgId>" +
                "<ReqDate>" + date + "</ReqDate>" +
                "<Signature>" + sign + "</Signature>" +
                "</head>" +
                bodyXml +
                "</IssuedTradeReq>" +
                "</Ips>";
        logger.info(">>>>> 委托付款查询 请求报文: " + xml);
        String qwsUrlSdl = environment.getProperty("YSF_DF_QUERY_API_URL") + "trade?wsdl";
        logger.info("qwsUrlSdl:" + qwsUrlSdl);

        //获取webservice service
        TradeQueryService query = (TradeQueryService) CxfCommunication.getWsPort(qwsUrlSdl, TradeQueryService.class);
        //调用ws接口  获取响应报文
        String resultXml = null;
        try {
            resultXml = query.getIssuedByBillNo(xml);
            //调用ws接口  获取响应报文
            logger.info("返回报文---------------------------------------");
            logger.info("客户端模式" + (String) resultXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(">>>>> response xml: " + resultXml);
        
        Map<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(resultXml)) {
        	resultMap.put("RspCode", getRspCode(resultXml));
        	resultMap.put("RspMsg", getRspMsg(resultXml));
        	resultMap.put("ErrorMsg", getErrorMsg(resultXml));
    		resultMap.put("IpsBillno", getIpsBillNo(resultXml));
    		resultMap.put("BatchNo", getBatchNo(resultXml));
    		resultMap.put("TrdAmt", getTrdAmt(resultXml));
    		resultMap.put("TrdInCash", getTrdInCash(resultXml));
    		resultMap.put("TrdOutCash", getTrdOutCash(resultXml));
    		resultMap.put("OrdStatus", getOrdStatus(resultXml));
    		resultMap.put("TrdStatus", getTrdStatus(resultXml));
    		resultMap.put("TrdDoTime", getTrdDoTime(resultXml));
        }
		return resultMap;
	}
	
	

	@Override
	public Map<String, String> issuedReturnSearch(Map<String, String> params,String directStr) {
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //获取参数
        String merCode = params.get("merCode");
     // body （<Param>为重复节点）
        String bodyXml =
                "<body>" +
                        "<BatchNo>" + params.get("batchNo") + "</BatchNo>" +
                        "<MerBillNo>" + params.get("MerBillNo") + "</MerBillNo>" +
                        /*"<AccountName>" + params.get("AccountName") + "</AccountName>" +
                        "<AccountNumber>" + params.get("AccountNumber") + "</AccountNumber>" +*/
                        "<StartTime>" + params.get("date") + "</StartTime>" +
                        "<EndTime>" + params.get("date") + "</EndTime>" +
                        /*"<Page>" + params.get("Page") + "</Page>" +
                        "<PageSize>" + params.get("PageSize") + "</PageSize>" +*/
                        "</body>";

        // MD5签名
        String sign = DigestUtils
                .md5Hex(Verify.getBytes(bodyXml + merCode + directStr,
                        "UTF-8"));
        // 发送给ipsxml
        String xml = "<Ips>" +
                "<IssuedTradeReq>" +
                "<head>" +
                "<Version>v1.0.0</Version>" +
                "<MerCode>" + merCode + "</MerCode>" +
                "<MerName>" + params.get("merName") + "</MerName>" +
                "<Account>" + params.get("accCode") + "</Account>" +
                "<MsgId>" + "msg" + date + "</MsgId>" +
                "<ReqDate>" + date + "</ReqDate>" +
                "<Signature>" + sign + "</Signature>" +
                "</head>" +
                bodyXml +
                "</IssuedTradeReq>" +
                "</Ips>";
        logger.info(">>>>> 委托付款查询 请求报文: " + xml);
        String qwsUrlSdl = environment.getProperty("YSF_DF_QUERY_API_URL") + "trade?wsdl";
        logger.info("qwsUrlSdl:" + qwsUrlSdl);

        //获取webservice service
        TradeQueryService query = (TradeQueryService) CxfCommunication.getWsPort(qwsUrlSdl, TradeQueryService.class);
        //调用ws接口  获取响应报文
        String resultXml = null;
        try {
            resultXml = query.getIssuedRetrunInfo(xml);
            //调用ws接口  获取响应报文
            logger.info("返回报文---------------------------------------");
            logger.info("客户端模式" + (String) resultXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(">>>>> response xml: " + resultXml);
        
        Map<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(resultXml)) {
        	resultMap.put("RspCode", getRspCode(resultXml));
        	resultMap.put("RspMsg", getRspMsg(resultXml));
        	resultMap.put("ErrorMsg", getErrorMsg(resultXml));
    		resultMap.put("IpsBillno", getIpsBillNo(resultXml));
    		resultMap.put("BatchNo", getBatchNo(resultXml));
    		resultMap.put("TrdAmt", getTrdAmt(resultXml));
    		resultMap.put("TrdInCash", getTrdInCash(resultXml));
    		resultMap.put("TrdOutCash", getTrdOutCash(resultXml));
    		resultMap.put("TrdStatus", getTrdStatus(resultXml));
    		resultMap.put("TrdDoTime", getTrdDoTime(resultXml));
        }
		return resultMap;
	}
	
	@Override
	public Map<String, String> issuedTradeSearch(Map<String, String> params,String directStr) {
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //获取参数
        String merCode = params.get("merCode");
     // body （<Param>为重复节点）
        String bodyXml =
                "<body>" +
                        "<BatchNo>" + params.get("batchNo") + "</BatchNo>" +
                        "<MerBillNo>" + params.get("MerBillNo") + "</MerBillNo>" +
                        /*"<AccountName>" + params.get("AccountName") + "</AccountName>" +
                        "<AccountNumber>" + params.get("AccountNumber") + "</AccountNumber>" +*/
                        "<StartTime>" + params.get("date") + "</StartTime>" +
                        "<EndTime>" + params.get("date") + "</EndTime>" +
                        /*"<Page>" + params.get("Page") + "</Page>" +
                        "<PageSize>" + params.get("PageSize") + "</PageSize>" +*/
                        "</body>";

        // MD5签名
        String sign = DigestUtils
                .md5Hex(Verify.getBytes(bodyXml + merCode + directStr,
                        "UTF-8"));
        // 发送给ipsxml
        String xml = "<Ips>" +
                "<IssuedTradeReq>" +
                "<head>" +
                "<Version>v1.0.0</Version>" +
                "<MerCode>" + merCode + "</MerCode>" +
                "<MerName>" + params.get("merName") + "</MerName>" +
                "<Account>" + params.get("accCode") + "</Account>" +
                "<MsgId>" + "msg" + date + "</MsgId>" +
                "<ReqDate>" + date + "</ReqDate>" +
                "<Signature>" + sign + "</Signature>" +
                "</head>" +
                bodyXml +
                "</IssuedTradeReq>" +
                "</Ips>";
        logger.info(">>>>> 委托付款查询 请求报文: " + xml);
        String qwsUrlSdl = environment.getProperty("YSF_DF_QUERY_API_URL") + "trade?wsdl";
        logger.info("qwsUrlSdl:" + qwsUrlSdl);

        //获取webservice service
        TradeQueryService query = (TradeQueryService) CxfCommunication.getWsPort(qwsUrlSdl, TradeQueryService.class);
        //调用ws接口  获取响应报文
        String resultXml = null;
        try {
            resultXml = query.getTradeList(xml);
            //调用ws接口  获取响应报文
            logger.info("返回报文---------------------------------------");
            logger.info("客户端模式" + (String) resultXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(">>>>> response xml: " + resultXml);
        
        Map<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(resultXml)) {
        	resultMap.put("RspCode", getRspCode(resultXml));
        	resultMap.put("RspMsg", getRspMsg(resultXml));
        	resultMap.put("ErrorMsg", getErrorMsg(resultXml));
    		resultMap.put("IpsBillno", getIpsBillNo(resultXml));
    		resultMap.put("BatchNo", getBatchNo(resultXml));
    		resultMap.put("TrdAmt", getTrdAmt(resultXml));
    		resultMap.put("TrdInCash", getTrdInCash(resultXml));
    		resultMap.put("TrdOutCash", getTrdOutCash(resultXml));
    		resultMap.put("OrdStatus", getOrdStatus(resultXml));
    		resultMap.put("TrdStatus", getTrdStatus(resultXml));
    		resultMap.put("TrdDoTime", getTrdDoTime(resultXml));
        }
		return resultMap;
	}
	
	/**
	 * 获取报文中<TrdDoTime></TrdDoTime>部分
	 * @param xml
	 * @return
	 */
	public String getTrdDoTime(String xml) {
		int s_index = xml.indexOf("<TrdDoTime>");
		int e_index = xml.indexOf("</TrdDoTime>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<TrdStatus></TrdStatus>部分
	 * @param xml
	 * @return
	 */
	public String getTrdStatus(String xml) {
		int s_index = xml.indexOf("<TrdStatus>");
		int e_index = xml.indexOf("</TrdStatus>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<OrdStatus></OrdStatus>部分
	 * @param xml
	 * @return
	 */
	public String getOrdStatus(String xml) {
		int s_index = xml.indexOf("<OrdStatus>");
		int e_index = xml.indexOf("</OrdStatus>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<TrdInCash></TrdInCash>部分
	 * @param xml
	 * @return
	 */
	public String getTrdInCash(String xml) {
		int s_index = xml.indexOf("<TrdInCash>");
		int e_index = xml.indexOf("</TrdInCash>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 11, e_index);
		}
		return sign;
	}
	
	/**
	 * 获取报文中<TrdOutCash></TrdOutCash>部分
	 * @param xml
	 * @return
	 */
	public String getTrdOutCash(String xml) {
		int s_index = xml.indexOf("<TrdOutCash>");
		int e_index = xml.indexOf("</TrdOutCash>");
		String sign = null;
		if (s_index > 0) {
			sign = xml.substring(s_index + 12, e_index);
		}
		return sign;
	}


}