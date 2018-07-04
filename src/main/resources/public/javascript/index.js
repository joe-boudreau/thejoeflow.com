function changeHeight() {
    var width = $(window).width();
    $(".jumbotron").css({
        "height": width * 0.4
    });

    var minHeight = Math.max(width * 0.2, 200)
    $(".carousel-inner").css({
        "height": minHeight
    });
}

window.addEventListener('resize', changeHeight);