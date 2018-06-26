var basepath=null;
var url=null;
var qrcode = {
			isError : false,
			pollingOrder : null,
	        encryptData:null,
            dataInit :function(_basepath,_url){
            	basepath=_basepath;
            	url=_url;
            },
			//初始化
			initEvent:function(){
	            $("#weixin_code_div1_id").slideDown(700,function(){
	                $("#weixin_code_div2_id").show(500);
	            });
	            //点击确定弹窗隐藏
	            $("#feedback button").on("click",function(){
	                $("#feedback").addClass("hide");
	                if(!isError){
	                    window.location.href=url;
	                }
	            });
			},
			//创建二维码
            createQrCodeMain:function (){
            	encryptData=$("#encryptData").val();
                $.post(basepath+"payment/json",encryptData,function(_data){
                    if(_data.code=="0x0000"){//如果调用成功
                    	qrcode.sendencrypt(_data);
                    }else{
                        isError = true;
                        //alert("错误信息:"+data.msg);
                        qrcode.alertView(0,_data.msg);
                    }
                }, 'json');
            },
            //开启订单轮询
            orderEach:function (){
                $.post(basepath+"query/json",encryptData,function(encryptiondata){
                	$.post(basepath+"cashier/decrypt",encryptiondata,
                			function(data){
		                		if(data.code=="0x0000" && 1 == data.data.state){//如果调用成功
		                        	qrcode.clearOrderEach();//停止订单轮询定时器
		                            isError = false;
		                            qrcode.alertView(1,"恭喜!支付成功!");
		                        }else if(data.code!="0x0000" || 2 != data.data.state){
		                        	isError = true;
		                        	qrcode.clearOrderEach();//停止订单轮询定时器
		                        	qrcode.alertView(-4,data.msg);
		                        }	
                     		}
                	 , 'json');
                }, 'json');
            },
            //生成二维码
            createCode:function (str){
                createerweima(str,"qr_code",298,298);//生成二维码(二维码字符串)[二维码字符串,二维码放入的div的id,宽,高]
            },
            //停止订单轮询定时器
            clearOrderEach:function (){
                clearInterval(qrcode.pollingOrder);//停止订单定时器
            },
            //弹窗设定
            alertView:function (state,msg) {
                if(1 == state){
                    ThisPrompt.dispatchSuccess("扫码调用通道成功");
                }else if(-4==state){
                    ThisPrompt.dispatchFail("",msg,true);
                }else{
                    ThisPrompt.dispatchFail("加载调用二维码失败",msg,true);
                };
            },
            sendencrypt:function(encryptiondata){
            	 $.post(basepath+"cashier/decrypt",encryptiondata,
            			function(data){
            		 		 if(data.code == "0x0000"){
            		 			qrcode.createCode(data.data.url);//生成二维码
                     			qrcode.pollingOrder=setInterval("qrcode.orderEach()",3000);
            		 		}else{
            		 		qrcode.alertView(0,"二维码解密失败");
            		 		}	
                 		}
            	 , 'json');
            },
            //进入通道
            createPass:function (){
            	encryptData=$("#encryptData").val();
                $.post(basepath+"payment/json",encryptData,function(_data){
                    if(_data.code=="0x0000"){//如果调用成功
                    	qrcode.sendPassencrypt(_data);
                    }else{
                        //alert("错误信息:"+data.msg);
                        qrcode.alertView(-4,_data.msg);
                    }
                }, 'json');
            },
            //解析数据
             sendPassencrypt:function(encryptiondata){
           	 $.post(basepath+"cashier/decrypt",encryptiondata,
         			function(data){
         		 		 if(data.code == "0x0000"){
         		 		 location.href=data.data.url;
         		 		}else{
         		 		qrcode.alertView(0,"网址解析失败");
         		 		}	
              		}
         	 , 'json');
         }
	}