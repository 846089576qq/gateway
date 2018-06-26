var ThisPrompt;
$(function(){
    ThisPrompt ={
        t:60,     //60s支付提示
        timer:'', //定时器
        url:"",//成功跳转页面
        initFn: function (url) {
            this.render();
            this.url = url;
        },
        render: function () {
            this.timer = null;
            this.confirmBtn  = $("#confirmBtn");//确认按钮
            this.oLeave = $("#leave"); //剩余
            this.oLeaveTxt =$("#leaveTxt"); //剩余0s后文字提示
        },
        dispatchSuccess:function(text1){
            //支付调用成功，则跳转！
            var me=this;
            me.confirmBtn.on("click",function(){
                $(".backdrop").hide();
                location.href=me.url;
            });
            //文字提示
            $("#disStatus").html('付款成功');
            $("#disTxt1").html(text1);
            $(".promptTxt").hide();
            $("#imgState").attr("src",basepath+"source/default/images/success.png");
            $(".backdrop").show();
            $("#confirmBtn").show();
        },
        dispatchFail:function(text1,text2,bool){
            //调用失败，则留在本页
            var me=this;
            this.confirmBtn.on("click",function(){
                $(".backdrop").hide();
                // location.href=me.url;
            });
            $("#disStatus").html('错误信息');
            $("#disTxt1").html(text1);
            $("#disTxt2").html(text2);
            $(".promptTxt").show();
            $("#imgState").attr("src",basepath+"source/default/images/error.png");
            $(".backdrop").show();
            if(bool){
                $("#confirmBtn").show();
            }else{
                $("#confirmBtn").hide();
            }
        },
        starCounter: function (t) {
            //开始倒计时
            var me = this;
            var t = t > 0 ? t : 60;
            me.t = t;//设置倒计时时间
            me.timer=setInterval(function(){
                me.counter();
            },1000);
        },
        counter:function(){
            //计时函数
            var me = this;
            me.t--;
            //刷新时间显示
            me.oLeave.html(me.t);
            if(me.t==0){
                clearInterval(me.timer);
                me.oLeaveTxt.html("请刷新页面重新获取二维码");
            }
        }
    };
    // ThisPrompt.initFn();
    ThisPrompt.initFn(url);

});
