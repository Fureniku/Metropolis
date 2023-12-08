package com.fureniku.metropolis.utils;

import com.fureniku.metropolis.Metropolis;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class Debug {

    //All the registered mods and their package names
    private static HashMap<String, String> _registeredMods = new HashMap();

    /**
     * Register a mod with the logger. After calling this in your mods constructor, you can use the static functions in this class directly.
     * This maps your mod to its package name, so the printed log can tell which mod it came from with no need to pass that information or create an instance/derived class.
     * Naming convention comes from being used to debugging/logging in Unity. Sorry for the PascalCase!
     * @param name The name of your mod, as you'd like it to be displayed
     */
    public static void registerMod(String name) {
        String pkg = getPackageName();
        Metropolis.LOGGER.atInfo().log("[Metropolis] registering mod " + name + " with logger under package name " + pkg);
        _registeredMods.put(pkg, name);
    }

    /**
     * Print a standard info-level message. Good for informing end users of whats happening during startup etc
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void Log(String msg, Object... params) {
        Metropolis.LOGGER.atInfo().log(String.format(getModFomCall(getCallPackage()) + msg, params));
    }

    /**
     * Print a DebugLog message, only client-side.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogClient(Level level, String msg, Object... params) {
        if (level.isClientSide) {
            LogDebug(msg, params);
        }
    }

    /**
     * Print a DebugLog message, only server-side.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogServer(Level level, String msg, Object... params) {
        if (!level.isClientSide) {
            LogDebug(msg, params);
        }
    }

    /**
     * Print a DebugLog message, and prefix whether the message came from the client or server
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogSided(Level level, String msg, Object... params) {
        LogDebug((level.isClientSide ? "[CLIENT] " : "[SERVER] ") + msg, params);
    }

    /**
     * Print debug messages. Mainly used for development - shouldn't even show in a release build.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogDebug(String msg, Object... params) {
        Metropolis.LOGGER.atDebug().log(String.format(getModFomCall(getCallPackage()) + msg, params));
    }

    /**
     * Print debug messages. Mainly used for development - shouldn't even show in a release build.
     * Also prints the class and line number, so you can go find that pesky message you forgot to remove.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogDebugVerbose(String msg, Object... params) {
        Metropolis.LOGGER.atDebug().log(String.format(getModFomCall(getCallPackage()) + msg, params));
        Metropolis.LOGGER.atDebug().log(printClassLine());
    }

    /**
     * Print a warning message. If something's not going well, but not broken, send it here.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogWarning(String msg, Object... params) {
        Metropolis.LOGGER.atWarn().log(String.format(getModFomCall(getCallPackage()) + msg, params));
        Metropolis.LOGGER.atDebug().log(printClassLine());
    }

    /**
     * Print a warning message. If something's not going well, but not broken, send it here.
     * Also prints the class and line number, so you can go find where it broke.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogWarningVerbose(String msg, Object... params) {
        Metropolis.LOGGER.atWarn().log(String.format(getModFomCall(getCallPackage()) + msg, params));
        Metropolis.LOGGER.atDebug().log(printClassLine());
    }

    /**
     * Print an error-level message. This should be used for important stuff that's breaking!
     * Also prints the class and line number, so you can go find where it broke.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogError(String msg, Object... params) {
        Metropolis.LOGGER.atError().log(String.format(getModFomCall(getCallPackage()) + msg, params));
        Metropolis.LOGGER.atDebug().log(printClassLine());
    }

    /**
     * Print an error-level message for the biggest problems. This sends the message to online ops (or the player for single-player worlds), as well as the console.
     * Also prints a stacktrace to fully debug what happened.
     * @param msg The message you want to send
     * @param params vararg for parameters
     */
    public static void LogCritical(String msg, Object... params) {
        Metropolis.LOGGER.atError().log(String.format(getModFomCall(getCallPackage()) + msg, params));
        new Exception().printStackTrace(System.out);
        //TODO send a message to local player / all online ops
    }

    //Find which mod sent the message
    private static String getModFomCall(String callPkg) {
        for (HashMap.Entry<String, String> entry : _registeredMods.entrySet()) {
            if (callPkg.contains(entry.getKey())) {
                return "[" + entry.getValue() + "] ";
            }
        }
        return "[UNKNOWN MOD: " + callPkg + "] ";
    }

    //Get the class which the message came from
    private static String printClassLine() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[3].toString();
    }

    //Get the package that called the message, so we can find which mod it was
    private static String getCallPackage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length >= 4) {
            return stackTrace[3].getClassName();
        }
        return "default";
    }

    //Get the top-level package name of the mod (com.example.package) - this is what we register with and check for later.
    private static String getPackageName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length >= 4) {
            String callingClass = stackTrace[3].getClassName();
            int lastDotIndex = -1;
            for (int i = 0; i < 3; i++) {
                lastDotIndex = callingClass.indexOf('.', lastDotIndex + 1);

                if (lastDotIndex == -1) {
                    return callingClass;
                }
            }

            return callingClass.substring(0, lastDotIndex);
        }
        return "default";
    }
}
