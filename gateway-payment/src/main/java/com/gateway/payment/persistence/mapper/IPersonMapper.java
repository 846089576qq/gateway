package com.gateway.payment.persistence.mapper;

import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.PersonEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 商户mapper
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IPersonMapper extends Mapper<PersonEntity> {

}