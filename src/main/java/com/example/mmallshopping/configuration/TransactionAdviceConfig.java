package com.example.mmallshopping.configuration;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;

/**
 * @author guoyou
 * @date 2019/10/23 10:37
 */
@Configuration
@Aspect
public class TransactionAdviceConfig {

    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.example.mmallshopping.service.*.*(..))";

    @Resource
    private PlatformTransactionManager transactionManager;

    /**
     * 事务通知
     *
     * @return
     */
    @Bean
    public TransactionInterceptor txAdvice() {
        //创建默认的事务查询属性
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        //设置事务的传播行为
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionAttribute.rollbackOn(new Throwable());
        //设置事务隔离级别
        transactionAttribute.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        DefaultTransactionAttribute transactionAttributeReadonly = new DefaultTransactionAttribute();
        transactionAttributeReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionAttributeReadonly.setReadOnly(true);
        //配置事务源
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("add*", transactionAttribute);
        source.addTransactionalMethod("save*", transactionAttribute);
        source.addTransactionalMethod("insert*", transactionAttribute);
        source.addTransactionalMethod("delete*", transactionAttribute);
        source.addTransactionalMethod("update*", transactionAttribute);

        source.addTransactionalMethod("select*", transactionAttributeReadonly);
        source.addTransactionalMethod("get*", transactionAttributeReadonly);
        source.addTransactionalMethod("query*", transactionAttributeReadonly);
        source.addTransactionalMethod("find*", transactionAttributeReadonly);
        source.addTransactionalMethod("list*", transactionAttributeReadonly);
        source.addTransactionalMethod("count*", transactionAttributeReadonly);
        source.addTransactionalMethod("is*", transactionAttributeReadonly);
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 配置切点切面
     *
     * @return
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());

    }
}
