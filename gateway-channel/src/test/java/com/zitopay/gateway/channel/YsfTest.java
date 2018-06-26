package com.zitopay.gateway.channel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gateway.channel.entity.ysf.query.YsfQueryInDto;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.zitopay.gateway.BaseTest;

public class YsfTest extends BaseTest {

	@Autowired
	private IYsfChannelService ysfChannelService;

	@Test
	public void test() {
		// 商户号
		String mch_id = "207972";
		// 商户名称
		String mch_name = "上海地氪网络科技有限公司";
		// 商户号名称
		String mch_account = "2079720013";
		// 秘钥
		String key = "Pn334LDWTMPSToS3dxWdC35Hw4zOenrSqXgxFgUhBx4XyFUHDT9rXP85OevyyxW5qtXCXwBgHsy6bPYX6fJDTDAKGkflSAKDBxw0wbyalTQKfBjWPPNx4H4Bojb0kUbl";
		String order_no = "2018042313100662737";// 订单号
		YsfQueryInDto in = new YsfQueryInDto();
		in.setMerCode(mch_id);
		in.setMerName(mch_name);
		in.setAccCode(mch_account);
		in.setMerBillNo(order_no);
		in.setDate("20180423");
		in.setAmount("0.01");
		ysfChannelService.query(in, key);
	}

}
