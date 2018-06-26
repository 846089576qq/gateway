//=======倒计时==========
(function () {
    var timer;
    var m = n = 30; //设定时间，单位：分
    var s = 60; //60s
    var flag = false;
    //页面显示数字
    function setHtml(m,s){
        var min = m;
        var sec = s;
        //console.log(s);
        document.getElementById('m').innerHTML = format(min);
        document.getElementById('s').innerHTML = format(sec);
        invoke();
    }
    //格式化数字
    function format(min){
        if(min<10 &&min>=0){
            min = "0" + min;
        }
        return min;
    }
    //运算倒计时
    function calculateTime(){
        s = s-1;
        if(!flag){
            flag = true;
            m = m - 1;
        }
        setHtml(m,s);
        if(s==0){
            m = m -1;
            s = 60;
        }
    }
    //启动倒计时
    function start(){
        document.getElementById('m').innerHTML = format(n);
        document.getElementById('s').innerHTML = format(0);
        clearInterval(timer);
        if(m>0){
            timer = setInterval(function(){
                calculateTime();
            },1000);
        }
    }
    //倒时时结束执行
    function invoke() {
        if(m == 0 && s == 0) {
            clearInterval(timer);
            alert("支付订单失效，请重新下单！");
            //window.location='abc.html?a=10';//倒计时结束跳转到abc.html?a=10
        }
    }
    //开始倒时计
    start();
})();


//fastclick.js 处理移动端 click 事件 300 毫秒延迟
window.addEventListener('load', function() {
    FastClick.attach(document.body);
}, false);