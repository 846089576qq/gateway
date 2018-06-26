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
package com.gateway.common.constants.param;

/**
 * 查询常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class QueryParamConstant extends BaseParamConstant {
	
	/**
	 * order_list 订单集合
	 */
	public static final String param_order_list = "order_list";
	
	/**
	 * order_list 订单总数
	 */
	public static final String param_order_num = "order_num";

	/**
	 * order_id 融智付订单ID(zitopay_order.order_id)
	 */
	public static final String param_order_id = "order_id";

	/**
	 * start_date 订单开始时间
	 */
	public static final String param_startDate = "start_date";

	/**
	 * end_date 订单结束时间
	 */
	public static final String param_endDate = "end_date";

	/**
	 * page_no 页码
	 */
	public static final String param_pageNo = "page_no";

	/**
	 * page_size 页容量
	 */
	public static final String param_pageSize = "page_size";

	/**
	 * order_title 订单标题(zitopay_order.ordertitle)
	 */
	public static final String param_orderTitle = "order_title";

	/**
	 * pay_time 支付时间(yyyy-MM-dd HH:mm:ss,zitopay_order.paysucctime)
	 */
	public static final String param_payTime = "pay_time";

	/**
	 * mer_order_id 商户订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order.orderidinf)
	 */
	public static final String param_merchantOrderId = "mer_order_id";

	/**
	 * order_time 下单时间(yyyyMMddHHmmssSSS,zitopay_order.posttime)
	 */
	public static final String param_orderTime = "order_time";

	/**
	 * order_amt 订单金额(##.##,单位:元,zitopay_order.totalprice)
	 */
	public static final String param_orderAmount = "order_amt";

	/**
	 * amount 实际到账金额(##.##,单位:元,zitopay_order.amount)
	 */
	public static final String param_actualAmount = "act_amt";

	/**
	 * fee 手续费金额(##.##,单位:元,zitopay_order.ratemmoney)
	 */
	public static final String param_fee = "fee";

	/**
	 * pay_no 支付流水号(zitopay_order.payno)
	 */
	public static final String param_payNo = "pay_no";

	/**
	 * top_pay_no 上游支付渠道(交易)流水号(支付成功后上游返回,zitopay_order.toppayno)
	 */
	public static final String param_topPayNo = "top_pay_no";

	/**
	 * state 订单状态(zitopay_order.state)
	 */
	public static final String param_state = "state";
	
	/**
	 * terminal_no 订单状态(zitopay_order.zongduanno)
	 */
	public static final String param_terminal_no = "terminal_no";

	/**
	 * count 数据总数量
	 */
	public static final String param_count = "count";

}
