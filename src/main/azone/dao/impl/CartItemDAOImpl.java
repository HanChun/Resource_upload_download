package azone.dao.impl;

import azone.dao.CartItemDAO;
import azone.pojo.CartItem;
import azone.pojo.User;
import myssm.basedao.BaseDAO;

import java.util.List;

public class CartItemDAOImpl extends BaseDAO<CartItem> implements CartItemDAO {
    @Override
    public void addCartItem(CartItem cartItem) {
        //System.out.println("1_ :"+ cartItem.getResource_id().getResource_id() +";2: "+cartItem.getDownLoadNum() +"; 3:"+cartItem.getUser_id().getUser_id());
        executeUpdate("insert into t_cart_item values((select max(\"cartItem_id\")+1 FROM t_cart_item),?,?,?)",
                cartItem.getResource_id().getResource_id(),cartItem.getDownLoadNum(),cartItem.getUser_id().getUser_id());
    }

    /**
     * To preserve the original case of your SQL language when using psql, you have a couple of options:
     *
     * Use Double Quotes: If you want to preserve the original case of identifiers (table names, column names, etc.),
     * you can enclose them in double quotes. Here's an example:
     * @param cartItem
     */

    @Override
    public void updateCartItem(CartItem cartItem) {
        executeUpdate("update t_cart_item set \"downLoadNum\" = ? where \"cartItem_id\"=?", cartItem.getDownLoadNum(), cartItem.getCartItem_id());
    }

    @Override
    public List<CartItem> getCartItemList(User user) {
        return executeQuery("select * from t_cart_item where user_id = ?",user.getUser_id() );
    }

    @Override
    public void delCartItem(Integer cartItem_id) {
        super.executeUpdate("delete from t_cart_item where \"cartItem_id\" = ?", cartItem_id);
    }


}











