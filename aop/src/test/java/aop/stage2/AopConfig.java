package aop.stage2;

import java.util.Arrays;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import aop.Transactional;
import aop.stage1.TransactionAdvice;

@Configuration
public class AopConfig {

    @Bean
    public ProxyPostProcessor proxyPostProcessor(PlatformTransactionManager platformTransactionManager) {
        return new ProxyPostProcessor(platformTransactionManager);
    }

    static class ProxyPostProcessor implements BeanPostProcessor {

        private final PlatformTransactionManager platformTransactionManager;

        ProxyPostProcessor(PlatformTransactionManager platformTransactionManager) {
            this.platformTransactionManager = platformTransactionManager;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (hasTransactionAnnotation(bean)) {
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setTarget(bean);
                proxyFactory.addAdvice(new TransactionAdvice(platformTransactionManager));
                return proxyFactory.getProxy();
            }
            return bean;
        }

        private boolean hasTransactionAnnotation(Object bean) {
            return Arrays.stream(bean.getClass().getMethods())
                .anyMatch(method -> method.isAnnotationPresent(Transactional.class)) ||
                bean.getClass().isAnnotationPresent(Transactional.class);
        }
    }
}
