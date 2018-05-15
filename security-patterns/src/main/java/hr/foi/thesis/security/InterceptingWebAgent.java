package hr.foi.thesis.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class InterceptingWebAgent extends HandlerInterceptorAdapter {

    @Autowired
    private SecureBaseAction secureBaseAction;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        secureBaseAction.request(new RequestContext<>(request, response));
        return true;
    }
}
