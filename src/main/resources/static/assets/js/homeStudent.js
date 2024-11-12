/*
Download button made by ZeroX
*/
$(".btn").click(function(){  /*When user click the button*/
    $("span").fadeOut(); /*Fadeout the download span*/
    $(".btn").animate({  
      width: "50px", /*Animate the width*/
    },function(){
      $(".btn").animate({
        top: "40%", 
      },function(){
        $(".bar").css("display","block"); 
        $(".btn").html('<i class="fa fa-ellipsis-h" aria-hidden="true"></i>');
        $(".p").animate({width:"100%"},2000,function(){
          $(".btn").html('<i class="fa fa-check" aria-hidden="true"></i>');
          $(".bar").fadeOut();
          $(".btn").animate({top:"50%"},function(){
            $(".btn").animate({width:"150px"},function(){
              $(".btn").html("<span>Download</span>");
              $(".p").css("width","0%");
            })
          });
        });
      })
    });
  });