package azone.pojo;

public class OrderItem {
    private Integer orderItem_id;
    private Resource resource_id;//M:1
    private Integer downLoadNum;
    private Order order_id;// 隶属于哪一个订单; 多对一 M:1
    public OrderItem(){}

    public Resource getResource_id() {
        return resource_id;
    }

    public void setResource_id(Resource resource_id) {
        this.resource_id = resource_id;
    }

    public Order getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Order order_id) {
        this.order_id = order_id;
    }

    public OrderItem(Integer orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    public Integer getOrderItem_id() {
        return orderItem_id;
    }

    public void setOrderItem_id(Integer orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    public Integer getDownLoadNum() {
        return downLoadNum;
    }

    public void setDownLoadNum(Integer downLoadNum) {
        this.downLoadNum = downLoadNum;
    }


}
