package com.cover.core;

import com.cover.annotation.BanReentry;
import com.cover.model.ParamMap;
import com.cover.annotation.Param;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cover.util.CoverConstants.SEPARATOR;


/**
 * Created by zhaoqing on 2017/10/13.
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReentryAspect {

    private InitializerReturnType returnTypes;

    private ReentryHandler reentryHandler;

    @PostConstruct
    public void init() {
        if (reentryHandler == null) {
            throw new RuntimeException("No injection related implementations are implemented: " + ReentryHandler.class);
        }
        if (returnTypes == null) {
            throw new RuntimeException("No injection related initializer instances: " + InitializerReturnType.class);
        }
    }

    @Around("@annotation(com.cover.annotation.BanReentry)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Method method = getMethodFromJoinPoint(joinPoint);
            String clzName = joinPoint.getTarget().getClass().getName();
            String reentryKey = clzName + SEPARATOR + method.getName();
            Class<?> returnType = method.getReturnType();
            BanReentry banReentry = method.getAnnotation(BanReentry.class);

            StringBuffer keys = new StringBuffer(banReentry.prefix());
            keys.append(banReentry.key().length() == 0 ? reentryKey : banReentry.key());
            List<ParamMap> paramMaps = getParamsFromMethod(method, joinPoint.getArgs());
            for (ParamMap param : paramMaps) {
                keys.append(SEPARATOR).append(param.getValue());
            }

            // normal request
            if (reentryHandler.isReentry(keys.toString(), banReentry.sec())) {
                return joinPoint.proceed(joinPoint.getArgs());
            }

            // reentry request
            if (returnType.getName().equals("void")) {
                return null;
            } else {
                Map<Class, Object> types = returnTypes.getInstanceOfReturnTypes();
                Object o = types.get(returnType);
                if (o != null) {
                    return o;
                } else {
                    throw new RuntimeException("No Such Instance Exception : " + returnType);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<ParamMap> getParamsFromMethod(Method method, Object[] args) {
        List<ParamMap> list = new ArrayList<>();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = discoverer.getParameterNames(method);
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : method.getParameterAnnotations()[i]) {
                if (annotation instanceof Param) {
                    ParamMap param = new ParamMap();
                    param.setName(paramNames[i]);
                    param.setValue(args[i]);
                    //param.setParam(true);
                    list.add(param);
                    break;
                }
            }
        }
        return list;
    }

    private static Method getMethodFromJoinPoint(JoinPoint point) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        if (method.getDeclaringClass().isInterface()) {
            method = point.getTarget().getClass().getDeclaredMethod(point.getSignature().getName(), method
                    .getParameterTypes());
        }
        return method;
    }

    public InitializerReturnType getReturnTypes() {
        return returnTypes;
    }

    public void setReturnTypes(InitializerReturnType returnTypes) {
        this.returnTypes = returnTypes;
    }

    public ReentryHandler getReentryHandler() {
        return reentryHandler;
    }

    public void setReentryHandler(ReentryHandler reentryHandler) {
        this.reentryHandler = reentryHandler;
    }
}
