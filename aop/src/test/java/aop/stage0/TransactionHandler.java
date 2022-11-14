package aop.stage0;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import aop.DataAccessException;
import aop.Transactional;

public class TransactionHandler <T> implements InvocationHandler {

    private final PlatformTransactionManager transactionManager;

    private final T target;
    private final Map<String, Method> transactionalMethods = new HashMap<>();

    public TransactionHandler(T target, PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;

        for (Method method : target.getClass().getMethods()) {
            putMethodIfTransactionalAnnotationPresent(method);
        }
    }

    private void putMethodIfTransactionalAnnotationPresent(Method method) {
        if (method.isAnnotationPresent(Transactional.class)) {
            transactionalMethods.put(method.getName(), method);
        }
    }

    /**
     * @Transactional 어노테이션이 존재하는 메서드만 트랜잭션 기능을 적용하도록 만들어보자.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (transactionalMethods.containsKey(methodName)) {
            return applyTransaction(args, transactionalMethods.get(methodName));
        }
        return method.invoke(target, args);
    }

    private Object applyTransaction(Object[] args, Method targetMethod) {

        Object result;
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            result = targetMethod.invoke(target, args);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new DataAccessException(e);
        }
        transactionManager.commit(transactionStatus);
        return result;
    }
}
