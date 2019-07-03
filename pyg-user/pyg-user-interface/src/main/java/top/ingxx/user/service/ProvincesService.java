package top.ingxx.user.service;

import top.ingxx.pojo.TbProvinces;
import top.ingxx.untils.entity.PageResult;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ProvincesService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbProvinces> findAll();


	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);


	/**
	 * 增加
	*/
	public void add(TbProvinces tbProvinces);


	/**
	 * 修改
	 */
	public void update(TbProvinces tbProvinces);


	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbProvinces findOne(Long id);


	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbProvinces tbProvinces, int pageNum, int pageSize);
	
	public List<TbProvinces> findListByProvinceidId(String provinceid);
	
}
