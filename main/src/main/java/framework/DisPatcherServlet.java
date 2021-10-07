package framework;

import framework.dispatcher.GetDispatcher;
import framework.dispatcher.PostDispatcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Map;

@WebServlet("/")
public class DisPatcherServlet extends HttpServlet {
    private Map<String, GetDispatcher> getDispatcherMap=null;
    private Map<String, PostDispatcher> postDispatcherMap=null;
    @Override
    public void init(){
        System.out.println("开始初始化，通过反射获取Index类中的方法与参数");
        try {
            Class<?> controllerIndex=Class.forName("controller.Index");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }




}
