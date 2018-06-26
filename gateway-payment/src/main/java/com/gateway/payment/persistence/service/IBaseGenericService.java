package com.gateway.payment.persistence.service;

import java.util.List;

public interface IBaseGenericService<T> {

	/**
	 * 根据id查询数据
	 * 
	 * @param id
	 * @return
	 */
	public T queryById(Object id);

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	public List<T> queryAll();

	/**
	 * 根据条件查询一条数据，如果有多条数据会抛出异常
	 * 
	 * @param record
	 * @return
	 */
	public T queryOne(T record);

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param record
	 * @return
	 */
	public List<T> queryListByWhere(T record);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param rows
	 * @param record
	 * @return
	 *//*
	public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record);*/

	/**
	 * 新增数据，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer save(T record);

	/**
	 * 新增数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer saveSelective(T record);

	/**
	 * 修改数据，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer update(T record);

	/**
	 * 修改数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer updateSelective(T record);

	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 * @return
	 */
	public Integer deleteById(Object id);

	/**
	 * 批量删除
	 * @param clazz
	 * @param property
	 * @param values
	 * @return
	 */
	public Integer deleteByIds(Class<T> clazz, String property, List<Object> values);

	/**
	 * 根据条件做删除
	 * 
	 * @param record
	 * @return
	 */
	public Integer deleteByWhere(T record);

}
