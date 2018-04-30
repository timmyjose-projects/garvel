package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.cache.api.BinSectionEntry;
import com.tzj.garvel.core.cache.api.CacheKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.api.MainClassEntry;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class RunJob implements Job<RunCommandResult> {
    private final String target;
    private final String[] args;

    public RunJob(final String target, final String[] args) {
        this.target = target;
        this.args = args;
    }

    /**
     * Given the target class (any type other than an inner class, of course):
     * <p>
     * 1. Check that the class is a valid class.
     * 2. Check that there is a default, zero-argument constructor.
     * 3. Check that it has a main method.
     * 4. Invoke the main method with the supplied arguments.
     *
     * @return
     * @throws JobException
     */
    @Override
    public RunCommandResult call() throws JobException {
        final RunCommandResult result = new RunCommandResult();

        final String targetClassName = getClassNameForTarget();
        final Class<?> clazz = checkClassIsValid(targetClassName);
        final Method mainMethod = checkForMainMethod(clazz);
        final Constructor<?> defaultConstructor = getDefaultConstructor(clazz);

        // try to run the target
        runTarget(mainMethod, defaultConstructor);

        result.setRunSuccessful(true);
        return result;
    }

    /**
     * Query the Core Cache to retrieve the actual class specified
     * for the given target.
     * <p>
     * If `none` was specified for the target name, return the `Main-Class`
     * instead.
     *
     * @return
     */
    private String getClassNameForTarget() throws JobException {
        final CacheManagerService cache = CoreModuleLoader.INSTANCE
                .getCacheManager();

        if (target.equalsIgnoreCase("none")) {
            if (cache.containsCacheKey(CacheKey.MAIN_CLASS)) {
                final MainClassEntry mainClassEntry = (MainClassEntry) CoreModuleLoader.INSTANCE
                        .getCacheManager().getEntry(CacheKey.MAIN_CLASS);
                return mainClassEntry.getMainClassPath();
            } else {
                throw new JobException("failed to run the project.\nPlease specify the `main-class` " +
                        "attribute if you don't specify a bin target\n");
            }
        }

        String targetClassName = null;
        final BinSectionEntry targetEntries = (BinSectionEntry) CoreModuleLoader.INSTANCE
                .getCacheManager().getEntry(CacheKey.TARGETS);

        for (final Map.Entry<String, String> entry : targetEntries.getTargets().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(target)) {
                targetClassName = entry.getValue();
                break;
            }
        }

        if (targetClassName == null) {
            throw new JobException(String.format("\"%s\" is not a valid run target.\nPlease specify a valid " +
                    "run target in the Garvel.gl file's bin section.\n", target));
        }

        return targetClassName;
    }

    /**
     * Run the main method of the target with the given arguments.
     *
     * @param mainMethod
     * @param defaultConstructor
     * @throws JobException
     */
    private void runTarget(final Method mainMethod, final Constructor<?> defaultConstructor) throws JobException {
        try {
            final Object targetObject = defaultConstructor.newInstance();
            mainMethod.invoke(targetObject, (Object) args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new JobException(String.format("failed to run the target \"%s\" : %s\n", target, e.getLocalizedMessage()));
        }
    }

    /**
     * For now, we only support targets that have a public zero-argument
     * constructor.
     *
     * @param clazz
     * @return
     * @throws JobException
     */
    private Constructor<?> getDefaultConstructor(final Class<?> clazz) throws JobException {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors != null && constructors.length != 0) {
            for (Constructor<?> cons : constructors) {
                final Class<?>[] typeParams = cons.getParameterTypes();
                if (typeParams == null || typeParams.length == 0) {
                    return cons;
                }
            }
        }

        throw new JobException(String.format("failed to find the public default constructor for target \"%s\".\nPlease check that you have a " +
                "public zero-argument constructor in the target\n", target));
    }

    /**
     * Retrieve the main method for the target.
     *
     * @param clazz
     * @return
     * @throws JobException
     */
    private Method checkForMainMethod(final Class<?> clazz) throws JobException {
        Method mainMethod = null;

        try {
            mainMethod = clazz.getDeclaredMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            throw new JobException(String.format("failed to find `main` method for target \"%s\".\nPlease check that you have a " +
                    "public static void main(String[] args) in the target\n", target));
        }

        // ensure that it is the correct main method
        final int mainMods = mainMethod.getModifiers();
        if (!Modifier.isPublic(mainMods) ||
                !Modifier.isStatic(mainMods) ||
                !mainMethod.getReturnType().equals(Void.TYPE)) {
            throw new JobException(String.format("failed to find `main` method in target \"%s\".\nPlease check that you have a " +
                    "public static void main(String[] args) in the target\n", target));
        }

        return mainMethod;
    }

    /**
     * Check that the class is valid and can be loaded.
     *
     * @param targetClassName
     * @return
     * @throws JobException
     */
    private Class<?> checkClassIsValid(final String targetClassName) throws JobException {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            throw new JobException(String.format("failed to load target class \"%s\" for target \"%s\".\nPlease check the class is valid\n",
                    targetClassName, target));
        }

        return clazz;
    }
}
