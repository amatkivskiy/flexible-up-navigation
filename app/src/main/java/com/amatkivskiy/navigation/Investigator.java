package com.amatkivskiy.navigation;

import android.util.Log;

/**
 * Simple logging tool for adding informative debug logs to code easily.
 * <br><br>
 * Usage: Add <code>Investigator.log(this)</code> (or its variants) to checkpoints.
 *
 * @author Gabor_Keszthelyi
 */
public class Investigator {

    /**
     * The tag used for the logcat messages.
     */
    public static String tag = "Investigator";
    /**
     * Number of the extra stacktrace elements (class + method name) logged from the stacktrace created at the log() call.
     * Zero means no extra method is logged, only the watched one.<p>
     */
    public static int defaultMethodDepth = 0;
    /**
     * Log the thread name or not.
     */
    public static boolean threadNameEnabled = true;
    /**
     * Log level used for logcat.
     */
    public static int logLevel = Log.DEBUG;
    /**
     * Remove the package name from the instance's toString value for easier readability.
     */
    public static boolean removePackageName = true;
    /**
     * When enabled, an extra word ({@link #anonymousClassHighlightWord}) is inserted into anonymous classes' toString values to help notice them more easily.
     * <p>e.g.: <code>FirstFragment$1@1bf1abe3.onClick()</code> --&gt; <code>FirstFragment_INNNER_1@1bf1abe3.onClick()</code>
     */
    public static boolean highlightAnonymousClasses = true;
    public static String anonymousClassHighlightWord = "_ANONYMOUS_";

    private static String patternThreadName = "[%s] ";
    private static String patternInstanceAndMethod = "%s.%s()";
    private static String patternComment = " | %s";
    private static String patternVariableNameAndValue = " | %s = %s";
    private static String messageStopwatchStarted = " | 0 ms (STOPWATCH STARTED)";
    private static String patternElapsedTime = " | %s ms";
    private static String patternStacktraceLine = "\tat %s";
    private static String newLine = "\n";

    private static final int STACKTRACE_INDEX_OF_CALLING_METHOD = 3; // fixed value, need to update only if the 'location' of the stack trace fetching code changes
    private static final String ANONYMOUS_CLASS_TOSTRING_SYMBOL = "$";

    private static boolean isStopWatchGoing;

    /**
     * Logs the calling instance and method name.<p>
     * <b>Example</b>
     * <br>Code:
     * <br><code>Investigator.log(this);</code>
     * <br>Log:
     * <br><code>D/Investigator: MainActivity@788dc5c.onCreate()</code>
     *
     * @param instance the calling object instance
     */
    public static void log(Object instance) {
        doLog(instance, null, false, null, defaultMethodDepth);
    }

    /**
     * Logs the calling instance and method name, and the comment.<p>
     * <b>Example</b>
     * <br>Code:
     * <br><code>Investigator.log(this, "some comment");</code>
     * <br>Log:
     * <br><code>D/Investigator: MainActivity@788dc5c.onCreate() | some comment</code>
     *
     * @param instance the calling object instance
     * @param comment  extra comment message
     */
    public static void log(Object instance, String comment) {
        doLog(instance, comment, false, null, defaultMethodDepth);
    }

    /**
     * Logs the calling instance and method name, and the variable names and values.<p>
     * <b>Example</b>
     * <br>Code:
     * <br><code>Investigator.log(this, "fruit", fruit);</code>
     * <br><code>Investigator.log(this, "fruit", fruit, "color", color);</code>
     * <br>Log:
     * <br><code>D/Investigator: MainActivity@788dc5c.onCreate() | fruit = cherry</code>
     * <br><code>D/Investigator: MainActivity@788dc5c.onCreate() | fruit = cherry | color = red</code>
     *
     * @param instance               the calling object instance
     * @param variableNamesAndValues variable name and value pairs
     */
    public static void log(Object instance, Object... variableNamesAndValues) {
        doLog(instance, null, false, variableNamesAndValues, defaultMethodDepth);
    }

    /**
     * Logs the calling instance and method name, and the stacktrace to the given method depth.<p>
     * <b>Example</b>
     * <br>Code:
     * <br><code>Investigator.log(this, 3);</code>
     * <br>Log:
     * <br><code>D/Investigator: [main] MainActivity@cea8175.stackTrace()<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;at gk.android.investigator.sample.MainActivity.access$500(MainActivity.java:20)<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;at gk.android.investigator.sample.MainActivity$7.onClick(MainActivity.java:87)<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;at android.view.View.performClick(View.java:5204)</code>
     *
     * @param instance    the calling object instance
     * @param methodDepth the number of methods to log from the stacktrace
     */
    public static void log(Object instance, Integer methodDepth) {
        doLog(instance, null, false, null, methodDepth);
    }

    /**
     * Starts an internal stopwatch and the consequent log calls will print the time elapsed since this call.
     * Calling it multiple times restarts the stopwatch.<p>
     * <b>Example</b>
     * <br>Code:
     * <br><code>Investigator.startStopWatch(this);</code>
     * <br><code>...</code>
     * <br><code>Investigator.log(this);</code>
     * <br>Log:
     * <br><code>D/Investigator: MainActivity@788dc5c.onCreate() | 0 ms (STOPWATCH STARTED)</code>
     * <br><code>D/Investigator: NetworkController@788dc5c.onJobFinished() | 126 ms</code>
     *
     * @param instance the calling object instance
     */
    public static void startStopWatch(Object instance) {
        StopWatch.startStopWatch();
        isStopWatchGoing = true;
        doLog(instance, null, true, null, defaultMethodDepth);
    }

    /**
     * Stop logging times (started by {@link Investigator#startStopWatch(Object)} from this point on.
     */
    public static void stopLoggingTimes() {
        isStopWatchGoing = false;
    }

    private static void doLog(Object instance, String comment, boolean hasStopWatchJustStarted, Object[] variableNamesAndValues, Integer methodDepth) {
        StackTraceElement[] stackTrace = getStackTrace();
        StringBuilder msg = new StringBuilder();
        if (threadNameEnabled) {
            msg.append(threadName());
        }
        msg.append(instanceAndMethodName(instance, stackTrace));
        if (comment != null) {
            msg.append(commentMessage(comment));
        }
        if (variableNamesAndValues != null) {
            msg.append(variablesMessage(variableNamesAndValues));
        }
        if (hasStopWatchJustStarted) {
            msg.append(messageStopwatchStarted);
        }
        if (isStopWatchGoing && !hasStopWatchJustStarted) {
            msg.append(timeElapsedMessage());
        }
        if (methodDepth > 0) {
            msg.append(extraStackTraceLines(stackTrace, methodDepth));
        }
        logText(msg);
    }

    private static StackTraceElement[] getStackTrace() {
        // new Exception().getStackTrace() is faster than Thread.currentThread().getStackTrace(), see http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6375302
        return new Exception().getStackTrace();
    }

    private static String threadName() {
        return String.format(patternThreadName, Thread.currentThread().getName());
    }

    private static String instanceAndMethodName(Object instance, StackTraceElement[] stackTrace) {
        String methodName = stackTrace[STACKTRACE_INDEX_OF_CALLING_METHOD].getMethodName();
        String instanceName = instance.toString();
        if (removePackageName) {
            instanceName = removePackageName(instanceName);
        }
        instanceName = checkAndHighlightAnonymousClass(instanceName);
        return String.format(patternInstanceAndMethod, instanceName, methodName);
    }

    // VisibleForTesting
    static String removePackageName(String instanceName) {
        int lastDotIndex = instanceName.lastIndexOf(".");
        if (lastDotIndex < 0 || lastDotIndex == instanceName.length() - 1) {
            return instanceName;
        } else {
            return instanceName.substring(lastDotIndex + 1);
        }
    }

    // VisibleForTesting
    static String checkAndHighlightAnonymousClass(String instanceName) {
        if (!highlightAnonymousClasses) {
            return instanceName;
        }
        int symbolIndex = instanceName.indexOf(ANONYMOUS_CLASS_TOSTRING_SYMBOL);
        boolean hasSymbolPlusDigit = symbolIndex > 0 && instanceName.length() > symbolIndex + 1 && Character.isDigit(instanceName.charAt(symbolIndex + 1));
        if (hasSymbolPlusDigit) {
            return new StringBuilder(instanceName).deleteCharAt(symbolIndex).insert(symbolIndex, anonymousClassHighlightWord).toString();
        } else {
            return instanceName;
        }
    }

    private static String commentMessage(String comment) {
        return String.format(patternComment, comment);
    }

    private static StringBuilder variablesMessage(Object... variableNamesAndValues) {
        try {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < variableNamesAndValues.length; i++) {
                Object variableName = variableNamesAndValues[i];
                Object variableValue = variableNamesAndValues[++i]; // Will fail on odd number of params deliberately
                String variableMessage = String.format(patternVariableNameAndValue, variableName, variableValue);
                result.append(variableMessage);
            }
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missed to add variable names and values in pairs? There has to be an even number of the 'variableNamesAndValues' varargs parameters).", e);
        }
    }

    private static String timeElapsedMessage() {
        return String.format(patternElapsedTime, StopWatch.getElapsedTimeInMillis());
    }

    private static StringBuilder extraStackTraceLines(StackTraceElement[] stackTrace, Integer methodDepth) {
        StringBuilder extraLines = new StringBuilder();
        for (int i = STACKTRACE_INDEX_OF_CALLING_METHOD + 1;
             i <= STACKTRACE_INDEX_OF_CALLING_METHOD + methodDepth && i < stackTrace.length;
             i++) {
            extraLines.append(newLine).append(stackTraceLine(stackTrace[i]));
        }
        return extraLines;
    }

    private static String stackTraceLine(StackTraceElement stackTraceElement) {
        return String.format(patternStacktraceLine, stackTraceElement.toString());
    }

    private static void logText(StringBuilder message) {
        Log.println(logLevel, tag, message.toString());
    }

    static class StopWatch {

        private static long startTimeInMillis;

        static void startStopWatch() {
            startTimeInMillis = System.currentTimeMillis();
        }

        static long getElapsedTimeInMillis() {
            return System.currentTimeMillis() - startTimeInMillis;
        }
    }
}












