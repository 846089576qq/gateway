package com.gateway.payment.persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.PersonApplicationEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 商户应用mapper
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IPersonApplicationMapper extends Mapper<PersonApplicationEntity> {

}