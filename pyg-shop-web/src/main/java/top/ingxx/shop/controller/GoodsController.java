package top.ingxx.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ingxx.manager.service.GoodsService;
import top.ingxx.pojo.TbGoods;
import top.ingxx.pojoGroup.Goods;
import top.ingxx.untils.entity.PageResult;
import top.ingxx.untils.entity.PygResult;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody Goods goods){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.getGoods().setSellerId(sellerId);
        try {
			goodsService.add(goods);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody Goods goods){
	    //判断操作的商品和数据库中商家是否一样  判断操作商品和当前登录用户是否一样
        Goods one = goodsService.findOne(goods.getGoods().getId());
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!one.getGoods().getSellerId().equals(sellerId) || !goods.getGoods().getSellerId().equals(sellerId)){
            return new PygResult(false,"非法操作");
        }
        try {
			goodsService.update(goods);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public PygResult delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new PygResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
        //获取商家ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
	    goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);		
	}
	
}
