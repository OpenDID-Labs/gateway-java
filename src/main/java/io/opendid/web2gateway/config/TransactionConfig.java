package io.opendid.web2gateway.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
*  TransactionConfig
* @author Dong-Jianguo
* @Date: 2024/10/21
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Aspect
@Configuration
public class TransactionConfig {

  private static final int TX_METHOD_TIME_OUT = -1;

  /**
   * the suffix must be Service
   */
  private static final String POITCUT_EXPRESSION =
      "execution(* io.opendid.web2gateway.service..*(..))";

  @Autowired
  private PlatformTransactionManager platformTransactionManager;

  @Bean
  public TransactionInterceptor txadvice() {
    NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
    Map<String, TransactionAttribute> nameMap = new HashMap<>(16);
    //Read-only things, do not update and delete, etc.
    RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
    //Set whether the current transaction is a read-only transaction, true is read-only
    readOnlyRule.setReadOnly(true);
    readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
    //        Rollbackable transaction
    requireRule.setRollbackRules(
        Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
    requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    requireRule.setTimeout(TX_METHOD_TIME_OUT);
    nameMap.put("insert*", requireRule);
    nameMap.put("update*", requireRule);
    nameMap.put("delete*", requireRule);
    //       Batch operation
    nameMap.put("select*", readOnlyRule);
    nameMap.put("count*", readOnlyRule);
    source.setNameMap(nameMap);
    TransactionInterceptor interceptor =new TransactionInterceptor();
    interceptor.setTransactionManager(platformTransactionManager);
    interceptor.setTransactionAttributeSource(source);
    return interceptor;
  }

  @Bean
  public Advisor txAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(POITCUT_EXPRESSION);
    return new DefaultPointcutAdvisor(pointcut, txadvice());
  }
}
