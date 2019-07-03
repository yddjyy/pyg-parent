app.service('payService',function($http){
	//本地支付

	this.createNative=function(){
		return $http.get('pay/createNative.do');
	}
	
	//查询支付状态
	this.queryPayStatus=function(out_trade_no){
		return $http.get('pay/queryPayStatus.do?out_trade_no='+out_trade_no);
	}
	//更新订单信息(回填地址信息)
	this.updateOrder=function(order){
		console.log(order);
		return $http.post('seckillOrder/updateOrder.do',order);
	}
	//获取当前登录账号的收货地址
	this.findAddressList=function(){
		return $http.get('address/findListByLoginUser.do');
	}
});