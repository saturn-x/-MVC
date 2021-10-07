import framework.DisPatcherServlet;

public class Test_DisPatcherServlet {
    public static void main(String[] args) {
        System.out.println("hello world");
        DisPatcherServlet test=new DisPatcherServlet();
        test.init();
        for (String s:test.getDispatcherMap.keySet()){
            System.out.println("路径:"+s);
            System.out.println("实例对象:"+test.getDispatcherMap.get(s));
        }
    }
}
