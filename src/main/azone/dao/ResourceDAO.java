package azone.dao;
import azone.pojo.Order;
import azone.pojo.Resource;
import azone.pojo.User;

import java.util.List;

public interface ResourceDAO {
    public List<Resource> getResourceList(String keyword, Integer pageNo, Integer user_id);
    List<Resource> getResourceList_Size( Integer minResourceSize,Integer maxResourceSize,Integer pageNo);
    List<Resource> getResourceList_keyWord( String keyword , Integer pageNo);
    Resource getResource(Integer resource_id);
    void addResource( Resource resource);//添加订单
    public int getResourceCount(String keyword) ;

    public List<Resource> getFreeResource(User user );
}
