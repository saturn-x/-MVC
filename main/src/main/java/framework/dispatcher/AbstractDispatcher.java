package framework.dispatcher;

import framework.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

abstract class AbstractDispatcher {

    public abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ReflectiveOperationException;
}
