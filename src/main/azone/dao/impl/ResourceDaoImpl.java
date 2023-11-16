package azone.dao.impl;

import azone.pojo.User;
import myssm.basedao.BaseDAO;
import azone.dao.ResourceDAO;
import azone.pojo.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourceDaoImpl extends BaseDAO<Resource> implements ResourceDAO {

    @Override
    public List<Resource> getResourceList(String keyword, Integer pageNo, Integer user_id) {
        System.out.println("ResourceDaoImpl ===> keyword : "+ keyword +" ; " + " pageNo : "+ pageNo +" ; 页首的元素的index : "+ (pageNo-1)*10 + "; user_id : " + user_id  );
        return super.executeQuery(
                "select * from t_downLoadResource where status = 0 \n" +
                "and \"publishAuthor\" IN \n" +
                "(\n" +
                "\tselect \n" +
                "\tt_user.uname\n" +
                "\tfrom\n" +
                "\t(\n" +
                "\t select user_id \n" +
                "\t from t_user_degree\n" +
                "\t where degree_id = (select degree_id from t_user_degree where user_id = ?)\n" +
                "\t) as a\n" +
                "\tleft join t_user\n" +
                "\ton a.user_id = t_user.user_id\n" +
                ")\n" +
                "or \"resourceName\" like ? limit 10 offset ?",user_id,"%"+ keyword +"%",(pageNo-1)*10);
         //return super.executeQuery("select * from t_downLoadResource where status = 0 or \"resourceName\" like ? limit 10 offset ?","%"+keyword+"%",(pageNo-1)*10);
    }
    @Override
    public List<Resource> getResourceList_Size(Integer minResourceSize, Integer maxResourceSize, Integer pageNo) {
        return null;
    }

    @Override
    public List<Resource> getResourceList_keyWord(String keyword, Integer pageNo) {
        return null;
    }

    @Override
    public Resource getResource( Integer resource_id) {
        return load("select * from t_downLoadResource where resource_id = ?",resource_id);
    }

    @Override
    public void addResource(Resource resource) {
        int resource_id = super.executeUpdate("insert into t_downloadresource values((select max(\"resource_id\")+1 FROM t_downloadresource),?,?,?,?,?,?,?)",
                resource.getResourceImg(),resource.getResourceName(),resource.getResourceSize(),resource.getPublishAuthor(),
                resource.getDownLoadNum(),resource.getPotentUserNum(),resource.getResourceUrl());
        resource.setResource_id(resource_id);
    }
    @Override
    public int getResourceCount(String keyword) {
        return ((Long)super.executeComplexQuery("select count(*) from t_downloadresource where \"resourceName\" like ?","%"+keyword+"%")[0]).intValue();
    }

   public List<Resource> getFreeResource(User user){
        String sql = "select distinct resource_id\n" +
                "from t_order_item\n" +
                "left join t_order\n" +
                "on t_order_item.order_id = t_order.order_id\n" +
                "where t_order.user_id = ?\n" +
                "union\n" +
                "select resource_id FROM t_downloadresource \n" +
                "WHERE \"publishAuthor\" = ?;";

       return super.executeQuery( sql, user.getUser_id(), user.getUname() );
   }

/*    public static void main(String[] args) {

        User user = new User();
        user.setUser_id(123); // 设置用户ID
        user.setUname("Lina");

        ResourceDaoImpl resourceDao = new ResourceDaoImpl();
        List<Integer> resourceIds = new ArrayList<>();
        List<Resource> resources = resourceDao.getFreeResource( user);

        for (Resource resource : resources) {
            int resourceId = resource.getResource_id();
            resourceIds.add(resourceId);
        }
    }*/
}


















