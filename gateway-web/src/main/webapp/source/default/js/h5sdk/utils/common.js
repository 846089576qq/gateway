(function(){
    //使用rem布局
    function w() {
        var r = document.documentElement;
        var a = r.getBoundingClientRect().width;
        if (a > 750 ){
            a = 750;
        }
        rem = a / 7.5;
        r.style.fontSize = rem + "px"
    }
    var t;
    w();
    window.addEventListener("resize", function() {
        clearTimeout(t);
        t = setTimeout(w, 300)
    }, false);
})();



