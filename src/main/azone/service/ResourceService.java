package azone.service;

import azone.pojo.Resource;
import azone.pojo.User;

import java.util.List;

public interface ResourceService {
    List<Resource> getResourceList(String keyword, Integer pageNo, Integer user_id);
    Resource getResource(Integer resource_id);
    void addResource(Resource resource);
    public int getResourceCount(String keyword);

    public List<Integer> getFreeResource( User user );
}
