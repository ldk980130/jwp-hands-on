package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
		this.beans = new HashSet<>();
		initializeBeans(classes);
    }

	private void initializeBeans(Set<Class<?>> classes) {
		for (Class<?> beanType : classes) {
			inject(beanType);
		}
	}

	private void inject(Class<?> beanType) {
		if (existBeanType(beanType)) {
			return;
		}
		Constructor<?> constructor = beanType.getConstructors()[0];

		if (constructor.getParameterCount() == 0) {
			beans.add(newInstance(constructor, new Object[] {}));
			return;
		}

		Class<?>[] parameterTypes = constructor.getParameterTypes();
		List<Object> parameters = new ArrayList<>();
		findParametersFromBeans(parameterTypes, parameters);
		validateLessParameters(parameterTypes, parameters);
		beans.add(newInstance(constructor, parameters.toArray()));
	}

	private boolean existBeanType(Class<?> beanType) {
		return beans.stream()
			.anyMatch(bean -> beanType.isAssignableFrom(bean.getClass()));
	}

	private Object newInstance(Constructor<?> constructor, Object[] parameters) {
		try {
			return constructor.newInstance(parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void findParametersFromBeans(Class<?>[] parameterTypes, List<Object> parameters) {
		for (Class<?> parameterType : parameterTypes) {
			inject(parameterType);
			parameters.add(getBean(parameterType));
		}
	}

	private void validateLessParameters(Class<?>[] parameterTypes, List<Object> parameters) {
		if (parameterTypes.length != parameters.size()) {
			throw new IllegalArgumentException("컨테이너를 초기화할 수 없습니다.");
		}
	}

	@SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
		return beans.stream()
			.filter(bean -> aClass.isAssignableFrom(bean.getClass()))
			.map(bean -> (T)bean)
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("해당 빈이 없습니다."));
    }
}
