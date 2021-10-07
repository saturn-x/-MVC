package framework;

import annotation.GetPath;
import framework.dispatcher.AbstractDispatcher;
import framework.dispatcher.GetDispatcher;
import framework.dispatcher.PostDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import framework.ViewEngine;
@WebServlet("/")
public class DisPatcherServlet extends HttpServlet {
    public Map<String, GetDispatcher> getDispatcherMap=new HashMap<>();
    private Map<String, PostDispatcher> postDispatcherMap=new HashMap<>();
    private ViewEngine viewEngine;
    @Override
    public void init(){
        System.out.println("开始初始化，通过反射获取Index类中的方法与参数");
        try {
            Class<?> controllerIndex=Class.forName("controller.Index");
            Object controllerInstance=controllerIndex.getConstructor().newInstance();
            Method[] methods=controllerIndex.getDeclaredMethods();
            for (Method m:methods ) {
                //找出GetPath注释的
                if(m.getAnnotation(GetPath.class)!=null){
                    //将方法中的参数名拿出来 并且放入String[]数组中，例如 {arg0 arg1 arg2 ````}
                    String[] parameterNames = Arrays.stream(m.getParameters()).map(p -> p.getName())
                            .toArray(String[]::new);
                    String path=m.getAnnotation(GetPath.class).value();
                    //将get的路径，new GetDispatcher（控制层实例，方法名，参数名，函数的返回值）
                    this.getDispatcherMap.put(path, new GetDispatcher(controllerInstance, m, parameterNames,
                            m.getParameterTypes()));
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        process(req, resp, this.getDispatcherMap);
    }
    private void process(HttpServletRequest req, HttpServletResponse resp,
                         Map<String, ? extends AbstractDispatcher> dispatcherMap) throws IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        AbstractDispatcher dispatcher = dispatcherMap.get(path);
        if (dispatcher == null) {
            resp.sendError(404);
            return;
        }
        ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req, resp);
        } catch (ReflectiveOperationException e) {
            throw new ServletException(e);
        }
        if (mv == null) {
            return;
        }
        if (mv.view.startsWith("redirect:")) {
            resp.sendRedirect(mv.view.substring(9));
            return;
        }
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();

    }




}
