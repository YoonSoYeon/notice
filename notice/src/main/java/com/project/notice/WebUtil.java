package com.project.notice;

import org.springframework.util.ObjectUtils;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtil {
	
	private static final String[] IP_HEADER_CANDIDATES = { 
		"X-Forwarded-For", 
		"Proxy-Client-IP", 
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
		"REMOTE_ADDR"
	};
	
	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : IP_HEADER_CANDIDATES) {
			String ip = request.getHeader(header);
			if (ObjectUtils.isEmpty(ip) == false && "unknown".equalsIgnoreCase(ip) == false) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}