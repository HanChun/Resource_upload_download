package azone.pojo;

import java.util.Date;
import java.util.List;

public class Order {// orderBean
    private Integer order_id;
    private String orderNo;
    private Date orderDate;
    private User user_id;// 在sql里面 取出来的是 user_id
    private Double orderFileSize;

    private Integer totalResourceCount ;
    private Integer orderStatus;

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Integer getTotalResourceCount() {
        return totalResourceCount;
    }

    // 一个订单里面 有多个 订单详情；
    private List<OrderItem> oderItemList;// 1：N

    public Order(){};
    public Order(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    public Double getOrderFileSize() {
        return orderFileSize;
    }

    public void setOrderFileSize(Double orderFileSize) {
        this.orderFileSize = orderFileSize;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOderItemList() {
        return oderItemList;
    }

    public void setOderItemList(List<OrderItem> oderItemList) {
        this.oderItemList = oderItemList;
    }
}
