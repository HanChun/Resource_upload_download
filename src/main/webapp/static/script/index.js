function addCart(resource_id){
    console.log("！！！这是Js的输出resource_id : " + resource_id);
    window.location.href='cart.do?operate=addCart&resource_id='+resource_id;
}

function downLoadFile(button){
    console.log("！！！这是Js的输出resourceName : " + resourceName);
    var resourceName = button.getAttribute('data-resource-name');
    window.location.href='download.do?operate=downLoadFile&fileNamePath='+resourceName;
}

function page(pageNo){
    window.location.href='resource.do?pageNo='+pageNo;
}





