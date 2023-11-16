document.addEventListener("DOMContentLoaded", function() {
    var downloadButton = document.getElementById("downloadButton");
    var userCredit = parseInt(document.getElementById("credits").textContent); // 使用 ".credits" 类名选择用户信用值
    var totalResourceCount = parseInt(document.getElementById("total-resource-count").textContent);

    downloadButton.addEventListener("click", function() {

            if ( userCredit >= totalResourceCount ) {
                // 触发下载操作，例如页面跳转到下载页面
                window.location.href ='order.do?operate=checkout';
            } else {
                alert( "Credit is not sufficient. Please upload resources to earn credits, or reduce your download volume." );
            }
        }
    );
});

function editCart(cartItem_id,downLoadNum){
    window.location.href='cart.do?operate=editCart&cartItem_id='+cartItem_id+'&downLoadNum='+downLoadNum;
}

function deleteCartItem(cartItem_id){
    window.location.href='cart.do?operate=deleteCartItem&cartItem_id='+cartItem_id;
}

