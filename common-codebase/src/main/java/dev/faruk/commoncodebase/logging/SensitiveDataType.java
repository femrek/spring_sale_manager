package dev.faruk.commoncodebase.logging;

/**
 * An interface for implementing sensitive data types. Implement this interface to model classes including sensitive
 * fields like passwords.
 */
public interface SensitiveDataType {
    String toVisualString();
}
