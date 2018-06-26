$(function(){

    //向下预览
    $("#arrow").on("click",function () {
        var char_top = $("#character").offset().top - $("#header").outerHeight();
        scroll(char_top)
    });

    //回到顶部.
    $("#backTop").on("click",function () {
        scroll(0);
    });

    //滑动预览
    function scroll(scrollTop,speed) {
        var speed = speed > 0 ? speed :500;
        $("html,body").stop().animate({scrollTop:scrollTop},speed);
    }

});