package azone.pojo;
import java.util.Map;
import java.util.Set;

public class Cart {
    private Map<Integer,CartItem> cartItemMap; // 购物车 中 购物车项 的集合：这个map集合中的 key 是resource的resource_id
    private Double totalFileSize;       // 购物车 的 文件总大小 total
    private Integer totalCount;       // 购物车  中 购物项 的 数量
    private Integer totalResourceCount ;// 购物车中 Resource 的总数量 而不是
    public Cart(){}
    public void setCartItemMap( Map<Integer, CartItem> cartItemMap )  {
        this.cartItemMap = cartItemMap;
    }
    public Map<Integer, CartItem> getCartItemMap() {
        return cartItemMap;
    }
    public Double getTotalFileSize() {
        totalFileSize = 0.0;
        if( cartItemMap!=null && cartItemMap.size()>0 ){
            Set<Map.Entry<Integer,CartItem>> entries = cartItemMap.entrySet();
            for( Map.Entry<Integer,CartItem> cartItemEntry : entries){
                CartItem cartItem = cartItemEntry.getValue();
                totalFileSize = totalFileSize + cartItem.getResource_id().getResourceSize() * cartItem.getDownLoadNum();
            }
        }
        return totalFileSize;
    }
    public Integer getTotalCount() {//
        totalCount = 0 ;
        if( cartItemMap != null && cartItemMap.size()>0 ){
           totalCount = cartItemMap.size();
        }
        return totalCount;
    }
    public Integer getTotalResourceCount(){
        totalResourceCount = 0;
        if( cartItemMap != null && cartItemMap.size()>0 ){
             for( CartItem cartItem: cartItemMap.values() ){
                    totalResourceCount = totalResourceCount + cartItem.getDownLoadNum() ;
             };
        }
        return totalResourceCount;
    }

}








