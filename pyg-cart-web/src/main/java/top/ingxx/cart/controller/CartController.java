package top.ingxx.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.ingxx.cart.service.CartService;
import top.ingxx.pojoGroup.Cart;
import top.ingxx.untils.entity.PygResult;
import top.ingxx.untils.until.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout=60000)
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {
		//当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人：" + username);

		String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if (cartListString == null || cartListString.equals("")) {
			cartListString = "[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

		if (username.equals("anonymousUser")) {//如果未登录
			//从cookie中提取购物车
			System.out.println("从cookie中提取购物车");

			return cartList_cookie;

		} else {//如果已登录
			//获取redis购物车
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
			if (cartList_cookie.size() > 0) {//判断当本地购物车中存在数据
				//得到合并后的购物车
				List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
				//将合并后的购物车存入redis
				cartService.saveCartListToRedis(username, cartList);
				//本地购物车清除
				CookieUtil.deleteCookie(request, response, "cartList");
				System.out.println("执行了合并购物车的逻辑");
				return cartList;
			}
			return cartList_redis;
		}

	}

	@RequestMapping(value = "/addGoodsToCartList",method = RequestMethod.GET)
	@CrossOrigin
	public PygResult addGoodsToCartList(Long itemId, Integer num) {
		System.out.println("显示传入的数据：--"+itemId+"-------"+num);
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");//可以访问的域(当此方法不需要操作cookie)
		//response.setHeader("Access-Control-Allow-Credentials", "true");//如果操作cookie，必须加上这句话

		//当前登录人账号
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人：" + name);


		try {
			//提取购物车
			List<Cart> cartList = findCartList();
			//调用服务方法操作购物车
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			for(Cart cart:cartList){
				System.out.println("商品-----"+cartList.size()+cart.getSellerId()+":"+cart.getSellerName()+""+cart.getOrderItemList().size());
			}
			if (name.equals("anonymousUser")) {//如果未登录
				//将新的购物车存入cookie
				String cartListString = JSON.toJSONString(cartList);
				CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
				System.out.println("向cookie存储购物车");

			} else {//如果登录
				cartService.saveCartListToRedis(name, cartList);
			}
			System.out.println("存入购物车成功！===================================");
			return new PygResult(true, "存入购物车成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "存入购物车失败");
		}


	}


}