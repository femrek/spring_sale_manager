package dev.faruk.commoncodebase.dbLogging;

/**
 * This annotation is used to ignore logging for a method. By default, @{link dev.faruk.commoncodebase.aspect.LoggerAspect}
 * logs all the methods in the controllers. If you want to ignore logging for a specific method, you can use this annotation.
 */
public @interface IgnoreDbLog {
}
