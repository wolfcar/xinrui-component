package com.xinrui.component.idempotent.aspect;


import com.xinrui.component.idempotent.biz.RedisLockBiz;
import com.xinrui.component.idempotent.dto.IdempotentParam;
import com.xinrui.component.idempotent.exception.IdempotentConfigError;
import com.xinrui.component.idempotent.exception.IdempotentExecuteException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author jerry
 */
@Aspect
public class DistributedIdempotentAspect extends AbstractIdempotentAspectSupport {

    @Value("${spring.application.name:default}")
    private String applicationName;
    private RedisLockBiz redisLockBiz;

    public DistributedIdempotentAspect(RedisLockBiz redisLockBiz) {
        this.redisLockBiz = redisLockBiz;
    }


    @Around(value = "@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (!StringUtils.hasText(idempotent.key())) {
            throw new RuntimeException("幂等异常：" + method.getName() + ",注解@Idempotent配置 key不能为空");
        }
        String prefix = new StringJoiner(":").add(applicationName)
                .add(joinPoint.getTarget().getClass().getSimpleName())
                .add(method.getName()).toString();

        String key = parseSpelKey(idempotent, args, method, prefix);
        String idempotentKey = prefix + key;
        String lockKey = prefix + key + "_lock";

        IdempotentParam idempotentParam = new IdempotentParam().setIdempotentKey(idempotentKey)
                .setApplicationName(applicationName)
                .setIdempotentKey(idempotentKey)
                .setLockKey(lockKey)
                .setLockWaitTime(idempotent.lockWaitTime())
                .setLockExpireTime(idempotent.lockExpireTime())
                .setFirstLevelExpireTime(idempotent.firstLevelExpireTime())
                .setSecondLevelExpireTime(idempotent.secondLevelExpireTime())
                .setResultType(((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType())
                .setIdempotentHandler(idempotent.idempotentHandler())
                .setIdempotentHandlerClass(idempotent.idempotentHandlerClass())
                .setRepeat(idempotent.repeat());

        return redisLockBiz.lockAfterIdempotent(idempotentParam, () -> {
            try {
                return joinPoint.proceed();
            } catch (IdempotentExecuteException ex) {
                return this.handleIdempotentException(joinPoint, idempotent, ex);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });


    }

    private String parseSpelKey(Idempotent idempotent, Object[] args, Method method, String prefix) {
        if (!idempotent.key().startsWith("$")) {
            String message = "配置错误：" + prefix + "方法上，注解@Idempotent配置错误,key不能为空，且未SPEL表达式";
            throw new IdempotentConfigError("E-100", message);
        }
        return parseKey(idempotent.key(), method, args);
    }


    /**
     * 获取幂等的key, 支持SPEL表达式
     *
     * @param key
     * @param method
     * @param args
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = nameDiscoverer.getParameterNames(method);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        try {
            return parser.parseExpression(key).getValue(context, String.class);
        } catch (SpelEvaluationException e) {
            throw new RuntimeException("SPEL表达式解析错误", e);
        }
    }

}
