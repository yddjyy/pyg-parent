app.controller('payController' ,function($scope ,$location,payService){
	
	
	$scope.createNative=function(){
		payService.createNative().success(
			function(response){
				
				//显示订单号和金额
				$scope.money= response.total_fee;
				$scope.out_trade_no=response.out_trade_no;
				
				//生成二维码
				 var qr=new QRious({
					    element:document.getElementById('qrious'),
						size:250,
						value:response.code_url,
						level:'H'
			     });
				 
				 queryPayStatus();//调用查询
				
			}	
		);	
	}
	
	//调用查询
	queryPayStatus=function(){
		payService.queryPayStatus($scope.out_trade_no).success(
			function(response){
				if(response.success){
					location.href="paysuccess.html#?money="+$scope.money+"&out_trade_no="+$scope.out_trade_no;
				}else{
					if(response.message=='二维码超时'){
						//$scope.createNative();//重新生成二维码
						alert("二维码超时");
					}else{
						location.href="payfail.html";
					}
				}				
			}		
		);		
	}
	
	//获取金额
	$scope.getMoney=function(){
		return $location.search()['money'];
	}
	//获取订单编号，回填订单收货地址
	$scope.getOutTraderNO=function(){
		$scope.out_trade_no = $location.search()['out_trade_no'];
	}
	$scope.order={
		receiverAreaName:'',
		receiverMobile:'',
		receiver:''
	}
	$scope.updateOrder=function(){
		$scope.order.receiverAddress=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		$scope.order.id=$scope.out_trade_no;
		payService.updateOrder($scope.order).success(
			function(response){
				if(response.success){
					$(function () {
						$(".btn-change").css("display","inline-block");
						$(".checkout").css("display","none");
					})
				}
			}
		)
	}
	//获取当前用户的地址列表
	$scope.findAddressList=function(){
		payService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				$(function () {
					$(".btn-change").css("display","none");
				})
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}
				}

			}
		);
	}
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;
	}

	//判断某地址对象是不是当前选择的地址
	$scope.isSeletedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}
	}
	//动态添加别名
	$scope.changeAlias=function (alias) {
		console.log(alias);
		$scope.addressEntiy.alias=alias;

	}
	$scope.addressEntiy={
		contact:'',
		address:'',
		mobile:'',
		alias:''
	}
	//添加收货地址
	$scope.addAddress=function () {
		console.log($scope.addressEntiy);
	}
});