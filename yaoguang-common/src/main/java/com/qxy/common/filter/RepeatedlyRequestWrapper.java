package com.qxy.common.filter;

import com.qxy.common.constant.Constants;
import com.qxy.common.utils.http.HttpHelper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 构建可重复读取inputStream的request
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] body;

    public RepeatedlyRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException
    {
        super(request);
        request.setCharacterEncoding(Constants.UTF8);
        response.setCharacterEncoding(Constants.UTF8);

        body = HttpHelper.getBodyString(request).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream()
        {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public int available() {
                return body.length;
            }

            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener)
            {

            }
        };
    }
}
