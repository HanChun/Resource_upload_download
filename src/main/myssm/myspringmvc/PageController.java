package myssm.myspringmvc;

public class PageController {
    public String page(String page){
        System.out.println("源自PageControllerpage_ page :  "+  page);
        return page;
    }
}
