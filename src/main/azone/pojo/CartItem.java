package azone.pojo;

import java.math.BigDecimal;

// 购物车 物品 详情表
public class CartItem {
    private Integer cartItem_id;
    private Resource resource_id;
    private Integer downLoadNum;//buyCount
    private User user_id;
    private Double totalItemFileSize;

    public CartItem(){};

    public CartItem(Integer cartItem_id, Integer downLoadNum){
        this.cartItem_id = cartItem_id ;
        this.downLoadNum = downLoadNum ;
    }

    public CartItem(Resource resource_id, Integer downLoadNum, User user_id) {
        this.resource_id = resource_id;
        this.downLoadNum = downLoadNum;
        this.user_id = user_id;
    }

    public CartItem(Integer cartItem_id) {
        this.cartItem_id = cartItem_id;
    }

    public Integer getCartItem_id() {
        return cartItem_id;
    }

    public void setCartItem_id(Integer cartItem_id) {
        this.cartItem_id = cartItem_id;
    }


    public Resource getResource_id() {
        return resource_id;
    }


    public void setResource_id(Resource resource_id) {
        this.resource_id = resource_id;
    }


    public Integer getDownLoadNum() {
        return downLoadNum;
    }


    public void setDownLoadNum(Integer downLoadNum) {
        this.downLoadNum = downLoadNum;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Double getTotalFileSize(){
        BigDecimal bigDecimalFileSize = new BigDecimal(getResource_id().getResourceSize() +"");
        BigDecimal bigDecimalDownLoadNum = new BigDecimal(downLoadNum + "");
        BigDecimal bigDecimalTotalItemFileSize = bigDecimalDownLoadNum.multiply(bigDecimalFileSize );
        totalItemFileSize = bigDecimalTotalItemFileSize.doubleValue();
        return totalItemFileSize;
    }
}
