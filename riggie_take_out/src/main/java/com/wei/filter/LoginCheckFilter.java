package com.wei.filter;

import com.alibaba.druid.support.json.JSONWriter;
import com.alibaba.fastjson2.JSON;
import com.wei.common.BaseContext;
import com.wei.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * @Description: check user login or not
 * @author: Wei Liang
 * @date: 2023年02月28日 12:22 PM
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI=request.getRequestURI();
        log.info("拦截到: {}",requestURI);

        //不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要处理,放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            long id = Thread.currentThread().getId();
            log.info("线程id为, {}",id);
            filterChain.doFilter(request,response);
            return;
        }

        //4.如果需要处理,在判断是否登入,如果登入放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登入,用户id为: {}",request.getSession().getAttribute("employee"));

            Long empId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4.2.如果需要处理,在判断是否登入,如果登入放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登入,用户id为: {}",request.getSession().getAttribute("user"));

            Long UserId=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(UserId);

            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登入");

        //5.需要filter的地方,且未登入.通过输出流的方式向客户端相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配,检查本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
