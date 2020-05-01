$(".year,.month, #archive-toggle").click(function(){
    $(this).next("ul").slideToggle();
    var selector = $(this).find("img.archive-selector");
    if(selector.attr("src") === "/images/right.png"){
        selector.attr("src", "/images/down.png");
    }
    else{
        selector.attr("src", "/images/right.png");
    }
});

$("#archive-toggle").click(function(){$("#archive-content").removeClass("show-desktop")});

$("#next-post").click(function(){
    var currScroll = $(window).scrollTop() + 5;

    let body = $('html, body');
    if(currScroll < $( ".blog-post:nth-child(2)" ).offset().top){
        body.animate({scrollTop: $( ".blog-post:nth-child(2)" ).offset().top},300);

    }
    else if(currScroll < $( ".blog-post:nth-child(3)" ).offset().top){
        body.animate({scrollTop: $( ".blog-post:nth-child(3)" ).offset().top},300);
    }
    else {
        body.animate({scrollTop: $("#footer").offset().top - window.innerHeight },300);
    }
});

$("#prev-post").click(function(){
    var currScroll = $(window).scrollTop() + 5;

    if(currScroll >= $( ".blog-post:nth-child(3)" ).offset().top){
        $('html, body').animate({scrollTop: $( ".blog-post:nth-child(2)" ).offset().top},300);

    }
    else if(currScroll >= $( ".blog-post:nth-child(2)" ).offset().top){
        $('html, body').animate({scrollTop: $( ".blog-post:nth-child(1)" ).offset().top},300);
    }
});

$("#top-of-page").click(function(){
    $('html, body').animate({scrollTop: 0}, 300);
});

function pullDivDown(){
    $("#nav-col").css("height", window.innerHeight);
    $("#pull-down").css('margin-top', $("#nav-col").height() - $("#pull-down").height() - $("#pull-down").prev().height());
}

$(function() {
    var timer_id;

    $(window).resize(function() {
        clearTimeout(timer_id);
        timer_id = setTimeout(function() {pullDivDown()}, 300);
    });
});

function goToBlogPage(pageNum){
    window.location.href = '/blog/' + pageNum;
}

$(window).load(setTimeout(function() {pullDivDown()}, 100));
