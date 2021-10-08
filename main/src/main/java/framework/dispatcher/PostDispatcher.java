package framework.dispatcher;


import com.fasterxml.jackson.databind.ObjectMapper;
import framework.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class PostDispatcher extends AbstractDispatcher {

    final Object instance;
    final Method method;
    final Class<?>[] parameterClasses;
    final ObjectMapper objectMapper;

    public PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses, ObjectMapper objectMapper) {
        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
        this.objectMapper = objectMapper;
    }

    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ReflectiveOperationException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else {
                BufferedReader reader = request.getReader();
                arguments[i] = this.objectMapper.readValue(reader, parameterClass);
            }
        }
        System.out.println("打印invoke的参数列表");
        for (Object i:arguments
             ) {
            System.out.println(i);
        }
        return (ModelAndView) this.method.invoke(instance, arguments);
    }

    public Method getMethod() {
        return this.method;
    }
}
