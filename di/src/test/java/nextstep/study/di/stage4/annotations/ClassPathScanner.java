package nextstep.study.di.stage4.annotations;

import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getComponentClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(Component.class)
            .stream()
            .filter(type -> !type.isAnnotation())
            .collect(Collectors.toSet());
    }
}
