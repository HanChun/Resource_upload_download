package azone.service.impl;

import azone.dao.ResourceDAO;
import azone.dao.impl.ResourceDaoImpl;
import azone.pojo.Resource;
import azone.pojo.User;
import azone.service.ResourceService;

import java.util.ArrayList;
import java.util.List;

public class ResourceServiceImpl implements ResourceService {
    private ResourceDAO resourceDAO;
    @Override
    public List<Resource> getResourceList(String keyword, Integer pageNo, Integer user_id) {
        return resourceDAO.getResourceList(keyword, pageNo, user_id);
    }
    @Override
    public Resource getResource( Integer resource_id ) {
        return resourceDAO.getResource(resource_id);
    }
    @Override
    public void addResource( Resource resource ) {
        resourceDAO.addResource(resource);
    }
    @Override
    public int getResourceCount( String keyword ){
        return resourceDAO.getResourceCount(keyword);
    }
    @Override
    public List<Integer> getFreeResource( User user ){
        ResourceDaoImpl resourceDao = new ResourceDaoImpl();
        List<Integer> resourceIds = new ArrayList<>();
        List<Resource> resources = resourceDao.getFreeResource( user);

        for (Resource resource : resources) {
            int resourceId = resource.getResource_id();
            resourceIds.add(resourceId);
        }
        return resourceIds;
    }

}















