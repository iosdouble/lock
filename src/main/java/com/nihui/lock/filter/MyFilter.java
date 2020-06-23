package com.nihui.lock.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname MyFilter
 * @Description TODO
 * @Date 2020/6/23 11:48 AM
 * @Created by nihui
 */

public class MyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(httpServletRequest);
        String remoteAddr = httpServletRequest.getHeader("X-Forwarded-For");
        // 如果通过多级反向代理，X-Forwarded-For的值不止一个，而是一串用逗号分隔的IP值，此时取X-Forwarded-For中第一个非unknown的有效IP字符串
        if (isEffective(remoteAddr) && (remoteAddr.indexOf(",") > -1)) {
            String[] array = remoteAddr.split(",");
            for (String element : array) {
                if (isEffective(element)) {
                    remoteAddr = element;
                    break;
                }
            }
        }
        if (!isEffective(remoteAddr)) {
            remoteAddr = httpServletRequest.getHeader("X-Real-IP");
        }
        if (!isEffective(remoteAddr)) {
            remoteAddr = httpServletRequest.getRemoteAddr();
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }


    /**
     * 获取客户端IP地址.<br>
     * 支持多级反向代理
     *
     * @param request
     *            HttpServletRequest
     * @return 客户端真实IP地址
     */
    public static String getRemoteAddr(final HttpServletRequest request) {
        try{
            String remoteAddr = request.getHeader("X-Forwarded-For");
            // 如果通过多级反向代理，X-Forwarded-For的值不止一个，而是一串用逗号分隔的IP值，此时取X-Forwarded-For中第一个非unknown的有效IP字符串
            if (isEffective(remoteAddr) && (remoteAddr.indexOf(",") > -1)) {
                String[] array = remoteAddr.split(",");
                for (String element : array) {
                    if (isEffective(element)) {
                        remoteAddr = element;
                        break;
                    }
                }
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getHeader("X-Real-IP");
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            return remoteAddr;
        }catch(Exception e){
            return "";
        }
    }

    /**
     * 获取客户端源端口
     * @param request
     * @return
     */
    public static Long getRemotePort(final HttpServletRequest request){
        try{
            String port = request.getHeader("remote-port");
            if(StringUtils.isEmpty(port)) {
                try{
                    return Long.parseLong(port);
                }catch(NumberFormatException ex){

                    return 0l;
                }
            }else{
                return 0l;
            }
        }catch(Exception e){

            return 0l;
        }
    }

    /**
     * 远程地址是否有效.
     *
     * @param remoteAddr
     *            远程地址
     * @return true代表远程地址有效，false代表远程地址无效
     */
    private static boolean isEffective(final String remoteAddr) {
        boolean isEffective = false;
        if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))
                && (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {
            isEffective = true;
        }
        return isEffective;
    }

}
