function changeHeight() {
    var width = $(window).width();
    $(".jumbotron").css({
        "height": width * 0.4
    });
}

window.addEventListener('resize', changeHeight);