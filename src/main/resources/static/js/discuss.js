function like(btn, entityType, entityId){

    $.post(
        CONTEXT_PATH+"/like",
        {"entityType":entityType,"entityId":entityId},
        function(data){
            data = $.parseJSON(data);
            if(data.code==0){
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'己贊':'贊');
            }else{
                alert(data.msg);
            }
        }
    );
}