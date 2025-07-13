//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.sh1rocu.emeraldcraft.util.forge;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class DistExecutor {
    private static final Logger LOGGER = LogManager.getLogger();

    private DistExecutor() {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static <T> T callWhenOn(Supplier<Callable<T>> toRun) {
        return unsafeCallWhenOn(toRun);
    }

    public static <T> T unsafeCallWhenOn(Supplier<Callable<T>> toRun) {
        try {
            return (toRun.get()).call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T safeCallWhenOn(Supplier<SafeCallable<T>> toRun) {
        validateSafeReferent(toRun);
        Objects.requireNonNull(toRun);
        return callWhenOn(toRun::get);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static void runWhenOn(Supplier<Runnable> toRun) {
        unsafeRunWhenOn(toRun);
    }

    public static void unsafeRunWhenOn(Supplier<Runnable> toRun) {
        toRun.get().run();

    }

    public static void safeRunWhenOn(Supplier<SafeRunnable> toRun) {
        validateSafeReferent(toRun);
        toRun.get().run();

    }

    /**
     * @deprecated
     */
    @Deprecated
    public static <T> T runForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return unsafeRunForDist(clientTarget, serverTarget);
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> {
                return clientTarget.get().get();
            }
            case SERVER -> {
                return serverTarget.get().get();
            }
            default -> throw new IllegalArgumentException("UNSIDED?");
        }
    }

    public static <T> T safeRunForDist(Supplier<SafeSupplier<T>> clientTarget, Supplier<SafeSupplier<T>> serverTarget) {
        validateSafeReferent(clientTarget);
        validateSafeReferent(serverTarget);
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> {
                return clientTarget.get().get();
            }
            case SERVER -> {
                return serverTarget.get().get();
            }
            default -> throw new IllegalArgumentException("UNSIDED?");
        }
    }

    private static void validateSafeReferent(Supplier<? extends SafeReferent> safeReferentSupplier) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            SafeReferent setter;
            try {
                setter = safeReferentSupplier.get();
            } catch (Exception var6) {
                return;
            }

            for (Class<?> cl = setter.getClass(); cl != null; cl = cl.getSuperclass()) {
                try {
                    Method m = cl.getDeclaredMethod("writeReplace");
                    m.setAccessible(true);
                    Object replacement = m.invoke(setter);
                    if (!(replacement instanceof SerializedLambda)) {
                        break;
                    }

                    SerializedLambda l = (SerializedLambda) replacement;
                    if (Objects.equals(l.getCapturingClass(), l.getImplClass())) {
                        LOGGER.fatal("Detected unsafe referent usage, please view the code at {}", Thread.currentThread().getStackTrace()[3]);
                        throw new RuntimeException("Unsafe Referent usage found in safe referent method");
                    }
                } catch (NoSuchMethodException ignored) {
                } catch (InvocationTargetException | IllegalAccessException var8) {
                    break;
                }
            }

        }
    }

    public interface SafeCallable<T> extends SafeReferent, Callable<T>, Serializable {
    }

    public interface SafeReferent {
    }

    public interface SafeRunnable extends SafeReferent, Runnable, Serializable {
    }

    public interface SafeSupplier<T> extends SafeReferent, Supplier<T>, Serializable {
    }
}
