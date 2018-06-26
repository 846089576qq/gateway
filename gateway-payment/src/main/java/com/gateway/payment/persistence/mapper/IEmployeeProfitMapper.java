package com.gateway.payment.persistence.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.EmployeeGatewayEntity;
import com.gateway.payment.entity.EmployeeProfitEntity;
import com.gateway.payment.entity.PersonChannelEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 项目名称:com.zitopay.order.mapper
 * 类名称:EmployeeProfitMapper
 * 描述:代理人分润mapper
 * 创建人:王洪滨
 * 创建时间:2016-10-25 10:35
 */
@Repository
public interface IEmployeeProfitMapper extends Mapper<EmployeeProfitEntity> {

    /**
     * description:根据商户id、通道id查询通道信息以及所属代理信息
     * Author:王洪滨
     * Date:2016/10/25 10:59
     */
	PersonChannelEntity queryPersonChannelInfo(@Param("personId") String personId, @Param("gatewayId") String gatewayId) throws Exception;
    /**
     * description:根据商户的代理递归查询上级所有代理以及通道信息
     * Author:王洪滨
     * Date:2016/10/25 10:59
     */
    List<EmployeeGatewayEntity> queryEmployeeChannelList(@Param("parentId") String parentId, @Param("gatewayId") String gatewayId, @Param("clearform")String clearform) throws Exception;
    
    /**
     * 检查订单是否已经分润
     * @param orderId
     * @return
     */
    int existProfitByOrderId(@Param("orderId") String orderId) throws Exception;
    
    
}
