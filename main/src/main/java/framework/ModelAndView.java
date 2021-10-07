package framework;

import base.User;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    String view;//需要渲染额路径
    Map<String,Object> model=null;


    public ModelAndView(String view, String user, Object object) {
        this.view=view;
        this.model = new HashMap<>();
        this.model.put(user, object);

    }

    public ModelAndView(String view) {
        this.view=view;
    }
}
