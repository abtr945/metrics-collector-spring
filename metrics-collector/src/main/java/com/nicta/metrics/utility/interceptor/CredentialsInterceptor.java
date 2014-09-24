package com.nicta.metrics.utility.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nicta.metrics.service.aws.AwsCredentialsService;
import com.nicta.metrics.service.aws.AwsEndpointService;

public class CredentialsInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	AwsCredentialsService awsCredentialsService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	/** 
	 * Intercept all requests and check whether both AWS Credentials and Region are set;
	 * if not, redirect to AWS Settings page.
	 */
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
		// If either AWS Credentials or AWS Region not set, redirect to AWS Settings page
        if (awsCredentialsService.isCredentialsSet() && awsEndpointService.isRegionSet()) {
            return true;
        } else {
            response.sendRedirect(request.getContextPath() + "/aws/credentials");
            return false;
        }
    }
	
}
