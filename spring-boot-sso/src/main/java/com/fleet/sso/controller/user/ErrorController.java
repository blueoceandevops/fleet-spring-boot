package com.fleet.sso.controller.user;
// package com.fleet.consumer.user.controller;
//
// import java.util.Map;
//
// import javax.servlet.http.HttpServletRequest;
//
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.context.request.RequestAttributes;
// import org.springframework.web.context.request.ServletRequestAttributes;
// import org.springframework.web.servlet.ModelAndView;
//
// public class ErrorController {
//
// @RequestMapping(value = ERROR_PATH, produces = "text/html")
// public ModelAndView errorHtml(HttpServletRequest request) {
// Map<String, Object> map = getAttributes(request, false);
// ModelAndView mav = null;
//
// Integer status = (Integer) map.get("status");
// if (status == 404) {
// mav = new ModelAndView("pages/404", map);
// } else if (status == 403) {
// mav = new ModelAndView("pages/403", map);
// } else if (status == 500) {
// mav = new ModelAndView("pages/500", map);
// } else {
// mav = new ModelAndView("greeting", map);
// }
//
// return mav;
// }
//
// private Map<String, Object> getAttributes(HttpServletRequest request, boolean
// includeStackTrace) {
// RequestAttributes requestAttributes = new ServletRequestAttributes(request);
// Map<String, Object> map =
// this.errorAttributes.getErrorAttributes(requestAttributes,
// includeStackTrace);
// String URL = request.getRequestURL().toString();
// map.put("URL", URL);
// logger.debug("AppErrorController.method [error info]: status-" +
// map.get("status") + ", request url-" + URL);
// return map;
// }
// }
