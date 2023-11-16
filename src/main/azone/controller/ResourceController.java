package azone.controller;

import azone.pojo.Resource;
import azone.pojo.User;
import azone.service.CreditService;
import azone.service.ResourceService;
import myssm.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ResourceController {
    private ResourceService resourceService;
    private CreditService creditService;

    public String index( String oper , String keyword , Integer pageNo , HttpServletRequest request ){
        HttpSession session =  request.getSession();

        if( pageNo==null ){
            pageNo = 1;
        }
        if( StringUtil.isNotEmpty(oper) && "search".equals(oper) ){
            System.out.println("进入到search了："+ keyword);
            pageNo = 1 ;
            if(StringUtil.isEmpty(keyword)){
                keyword = "" ;
            }
            session.setAttribute("keyword",keyword);
        }else{
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj!=null){
                keyword = (String)keywordObj ;
            }else{
                keyword = "" ;
            }
        }
        User user = (User)session.getAttribute("curUser");
        //1
        session.setAttribute("pageNo",pageNo);

        List<Resource> resourceList = resourceService.getResourceList( keyword, pageNo, user.getUser_id() );
        session.setAttribute("resourceList", resourceList );

        int resourceCount = resourceService.getResourceCount(keyword);
        //System.out.println("ResourceController の打印; 当前的 resourceCount : " + resourceCount );
        //1——总页数
        int pageCount = (resourceCount+10-1)/10 ;
        //System.out.println("ResourceController の打印；当前的 pageCount : " + pageCount );
        session.setAttribute("pageCount",pageCount);
        //3

        int credit = creditService.getUserCredit(user);
        session.setAttribute("credit",credit);
        //4
        List<Integer> resourceIds = resourceService.getFreeResource(user);
        session.setAttribute("resourceIds",resourceIds);
        return "index";//跳转到了 index的页面
    }

    //    public String index( String oper , String keyword , Integer pageNo , HttpSession session ){
    //        List<Resource> resourceList = resourceService.getResourceList( keyword, pageNo);
    //        session.setAttribute("resourceList", resourceList );
    //        return "index";//跳转到了 index的页面
    //    }

    public String add( String resourceImg, String resourceName, String resourceSize, String publishAuthor,
                       Integer downLoadNum, Integer potentUserNum, String resourceUrl,HttpServletRequest request){

        HttpSession session =  request.getSession();
        Integer status = 0;
        Double resourceSize_wrapper = Double.valueOf(resourceSize);
        Resource resource = new Resource( resourceImg, resourceName, resourceSize_wrapper, publishAuthor, downLoadNum, potentUserNum, resourceUrl );
        resource.setResource_id(status);
        resourceService.addResource(resource);

        //添加完 之后不要忘记添加 给用户 credit+
        User user = (User)session.getAttribute("curUser");
        creditService.addOneCredit(user);
        int credit = creditService.getUserCredit(user);
        session.setAttribute("credit",credit);

        //
        List<Integer> resourceIds = resourceService.getFreeResource(user);
        session.setAttribute("resourceIds",resourceIds);

        return "redirect:resource.do";
    }


}
