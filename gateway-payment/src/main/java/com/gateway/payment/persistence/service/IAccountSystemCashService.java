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
package com.gateway.payment.persistence.service;


import com.gateway.payment.entity.AccountSystemCashEntity;

/**
 * 提现的接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IAccountSystemCashService{
    /**
     * 获取提现记录
     * @param personId
     * @param gatewayId
     * @param orderNo
     * @return
     */
    public AccountSystemCashEntity getZitopayAccountSystemCash(String personId, String gatewayId, String orderNo);

    /**
     * 更新提现流水信息
     *
     * @param drawAmount 提现金额
     * @param drawFee 手续费
     * @param settleDate 日期
     * @param orderNo 流水号
     * @param state 状态
     * @param note
     * @return
     */
    public int updateOrder(String drawAmount,String drawFee,String settleDate,String orderNo,String state,String note);

}
