package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.cache.api.BinSectionEntry;
import com.tzj.garvel.core.cache.api.CacheKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.api.MainClassEntry;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
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
     * 1. Check that the class is a valid class by trying to load it from the
     * JAR file (so the dependency on the build command stays).
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

        // setup
        final String targetClassName = getClassNameForTarget();
        final Class<?> clazz = validateAndLoadTargetClass(targetClassName);
        final Method mainMethod = checkForMainMethod(clazz);

        // try to run the target
        runTarget(mainMethod, args);

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
                throw new JobException("Please specify the `main-class` " +
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
     * @param targetArgs
     * @throws JobException
     */
    private void runTarget(final Method mainMethod, final String[] targetArgs) throws JobException {
        try {
            mainMethod.invoke(null, (Object) args);
        } catch (IllegalAccessException e) {
            throw new JobException(String.format("%s\n", e.getLocalizedMessage()));
        } catch (InvocationTargetException e) {
            // this simply indicates that the target caused an exception. Catch it, log it,
            // and forget about it.
            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Running target \"%s\" threw an exception: %s\n",
                    target, e.getLocalizedMessage());
        } catch (Throwable e) {
            // any other non-invocation errors must simply be logged, and
            // the program must continue
            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Running target \"%s\" threw an error: %s\n",
                    target, e.getLocalizedMessage());
        }
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
    private Class<?> validateAndLoadTargetClass(final String targetClassName) throws JobException {
        final Path jarFilePath = CoreModuleLoader.INSTANCE.getConfigManager().checkProjectJARFileExists();

        if (jarFilePath == null || !jarFilePath.toFile().exists()) {
            throw new JobException("failed to load the project artifact.\nTry running the build command manually " +
                    "before retrying this command.\n");
        }

        Class<?> clazz = null;

        try {
            final String jarFileAbsolutePath = jarFilePath.toFile().getAbsolutePath();
            final URL jarFileUrl = new URL("jar:file:" + jarFileAbsolutePath + "!/");
            final URLClassLoader jarClassLoader = URLClassLoader.newInstance(new URL[]{jarFileUrl});

            clazz = jarClassLoader.loadClass(targetClassName);
        } catch (MalformedURLException e) {
            throw new JobException("failed to load the project artifact.\nTry running the build command manually " +
                    "before retrying this command.\n");
        } catch (ClassNotFoundException e) {
            throw new JobException(String.format("failed to load target class \"%s\" for target \"%s\".\nPlease check the class is valid\n",
                    targetClassName, target));
        }

        return clazz;
    }
}
