/*
 *
 *   Copyright 2016 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.springframework.data.mybatis.repository.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Jarvis Song
 */
public class MybatisQueryNotSupportException extends RuntimeException {
    public MybatisQueryNotSupportException() {
    }

    public MybatisQueryNotSupportException(String message) {
        super(message);
    }

    public MybatisQueryNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisQueryNotSupportException(Throwable cause) {
        super(cause);
    }

    public MybatisQueryNotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T unsupportInstance(Class<T> interfaceClass){
    	if(!interfaceClass.isInterface())throw new MybatisQueryNotSupportException("不支持查询");
    	return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				throw new MybatisQueryNotSupportException("不支持查询");
			}
		});
    }
}
