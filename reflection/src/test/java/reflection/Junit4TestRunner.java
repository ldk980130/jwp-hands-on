package reflection;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Junit4Test junit4Test = new Junit4Test();

        for (Method method : methods) {
            Optional<MyTest> myTest = Optional.ofNullable(method.getDeclaredAnnotation(MyTest.class));
            if (myTest.isPresent()) {
                method.invoke(junit4Test);
            }
        }
    }
}
