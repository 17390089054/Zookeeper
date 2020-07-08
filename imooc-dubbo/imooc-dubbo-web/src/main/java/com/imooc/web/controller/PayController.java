package com.imooc.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.common.utils.IMoocJSONResult;
import com.imooc.curator.untils.ZKCurator;
import com.imooc.item.pojo.Items;
import com.imooc.item.service.ItemsService;
import com.imooc.order.pojo.Orders;
import com.imooc.order.service.OrdersService;
import com.imooc.web.service.CulsterService;

/**
 * @Description: 订购商品controller
 */
@Controller
public class PayController {
	
	@Autowired
	private CulsterService buyService;
	
	@Autowired
	private ItemsService itemService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private ZKCurator zkCurator;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/item")
	@ResponseBody
	public IMoocJSONResult getItemById(String id) {
		Items item = itemService.getItem(id);
		
		return IMoocJSONResult.ok(item);
	}
	
	@RequestMapping("/order")
	@ResponseBody
	public IMoocJSONResult getOrderById(String id) {
		Orders order=orderService.getOrder(id);
		return IMoocJSONResult.ok(order);
	}
	
	
	@GetMapping("/buy")
	@ResponseBody
	public IMoocJSONResult doGetlogin(String itemId) {
		
		if (StringUtils.isNotBlank(itemId)) {
			buyService.displayBuy(itemId);
		} else {
			return IMoocJSONResult.errorMsg("商品id不能为空");
		}
		
		return IMoocJSONResult.ok();
	}
	
	/**
	 * 模拟数据不一致的情况
	 * @param itemId
	 * @return
	 */
	@GetMapping("/buy2")
	@ResponseBody
	public IMoocJSONResult buy2(String itemId) {
		boolean result=buyService.displayBuy(itemId);
		return IMoocJSONResult.ok(result?"订单创建成功....":"订单创建失败");
	}
	
	/**
	 * 判断zk是否连接
	 * @return
	 */
	@GetMapping("/isZKAlive")
	@ResponseBody
	public IMoocJSONResult isZKAlive() {
		boolean isAlive = zkCurator.isZKAlive();
		String result=isAlive?"连接":"断开";
		return IMoocJSONResult.ok(result);
	}
	
	
	
}
