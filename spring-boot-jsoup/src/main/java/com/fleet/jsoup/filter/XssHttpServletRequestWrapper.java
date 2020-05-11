package com.fleet.jsoup.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fleet.jsoup.util.JsoupUtil.clean;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest orgRequest = null;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(orgRequest.getInputStream()));
        String line = br.readLine();
        String result = "";
        if (line != null) {
            result += clean(line);
        }
        return new WrapperServletInputStream(new ByteArrayInputStream(result.getBytes()));
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (StringUtils.isNotEmpty(header)) {
            header = clean(header);
        }
        return header;
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        if (StringUtils.isNotEmpty(parameter)) {
            parameter = clean(parameter);
        }
        return parameter;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = clean(values[i]);
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = clean(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    private class WrapperServletInputStream extends ServletInputStream {
        private InputStream stream;

        public WrapperServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}
