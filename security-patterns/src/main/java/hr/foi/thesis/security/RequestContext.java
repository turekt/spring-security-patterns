package hr.foi.thesis.security;

import org.springframework.ui.ModelMap;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext<T> {

    private T data;
    private ModelMap modelMap;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Subject subject;

    public RequestContext(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.modelMap = new ModelMap();
    }

    public RequestContext(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        this.modelMap = modelMap;
        this.request = request;
        this.response = response;
    }

    public RequestContext(T data, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        this.data = data;
        this.modelMap = modelMap;
        this.request = request;
        this.response = response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ModelMap getModelMap() {
        return modelMap;
    }

    public void setModelMap(ModelMap modelMap) {
        this.modelMap = modelMap;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
