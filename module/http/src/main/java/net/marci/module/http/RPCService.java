package net.marci.module.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.marci.lib.utils.DataSerializer;
import net.marci.module.http.dto.RPCRequest;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RPCService {

  @Autowired
  private ApplicationContext applicationContext;

  public Object processRequest(RPCRequest request, List<Object> argumentsHolder) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
    final String service = request.getService();
    final String component = request.getComponent();

    Object springComponent = applicationContext.getBean(request.getComponent());
    Class<?> springComponentType = AopUtils.getTargetClass(springComponent);
    BeanInfo beanInfo = Introspector.getBeanInfo(springComponentType);
    MethodDescriptor[] methods = beanInfo.getMethodDescriptors();
    MethodDescriptor methodDescriptor = null;
    for (MethodDescriptor method : methods) {
      if (method.getMethod().getName().equals(request.getService())) {
        methodDescriptor = method;
      }
    }

    final String nullMethodDescriptorMessage = MessageFormat.format("No method `{0}` in class {1}", service, component);
    Assert.notNull(methodDescriptor, nullMethodDescriptorMessage);
    Parameter[] methodArguments = methodDescriptor.getMethod().getParameters();

    final Map<String, JsonNode> params = request.getParameters();
    int argumentIdx = argumentsHolder.size();
    for (JsonNode jsonNode : params.values()) {
      Class<?> argType = methodArguments[argumentIdx].getType();
      if (List.class.isAssignableFrom(argType)) {
        ParameterizedType pType = (ParameterizedType) methodArguments[argumentIdx].getParameterizedType();
        Class<?> actualType = (Class<?>) pType.getActualTypeArguments()[0];
        List<?> values = DataSerializer.JSON.convertTreeToListObject(jsonNode, actualType);
        argumentsHolder.add(values);
      } else {
        Object value = DataSerializer.JSON.convertTreeToObject(jsonNode, argType);
        argumentsHolder.add(value);
      }

      argumentIdx++;
    }

    Object[] args = argumentsHolder.toArray();
    return methodDescriptor.getMethod().invoke(springComponent, args);
  }
}