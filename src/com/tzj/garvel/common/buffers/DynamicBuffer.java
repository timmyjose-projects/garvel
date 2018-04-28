package com.tzj.garvel.common.buffers;

import com.tzj.garvel.core.net.api.exception.buffers.BufferException;

/**
 * A generic growable and shrinkable dynamic array.
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class DynamicBuffer<T> {
    private T[] buffer;
    private int len;
    private int cap;

    public DynamicBuffer() {
        this.len = 0;
        this.cap = 1;
        this.buffer = (T[]) new Object[this.cap];
    }

    /**
     * Get the element at the specified index.
     *
     * @param idx
     * @return
     */
    public T get(final int idx) {
        if (idx < 0 || idx >= len) {
            throw new BufferException(String.format("invalid index for get: %d\n", idx));
        }

        return buffer[idx];
    }

    /**
     * Replace the element at the specified index.
     *
     * @param idx
     * @param val
     */
    public void set(final int idx, final T val) {
        if (idx < 0 || idx >= len) {
            throw new BufferException(String.format("invalid index for set: %d\n", idx));
        }

        buffer[idx] = val;
    }

    /**
     * Remove and return the element at the specified index.
     *
     * @param idx
     */
    public T remove(final int idx) {
        if (idx < 0 || idx >= len) {
            throw new BufferException(String.format("invalid index for remove: %d\n", idx));
        }

        final T ret = buffer[idx];
        for (int i = idx; i < len - 1; i++) {
            buffer[i] = buffer[i + 1];
        }

        len--;

        if (len < cap / 2) {
            T[] newBuffer = (T[]) new Object[cap / 2];
            for (int i = 0; i < len; i++) {
                newBuffer[i] = buffer[i];
            }

            cap /= 2;
            buffer = newBuffer;
        }

        return ret;
    }

    /**
     * Add the given element at the end of the buffer,
     * growing the buffer if need be.
     *
     * @param val
     */
    public void pushBack(final T val) {
        if (len == cap) {
            T[] newBuffer = (T[]) new Object[cap * 2];
            for (int i = 0; i < len; i++) {
                newBuffer[i] = buffer[i];
            }

            cap *= 2;
            buffer = newBuffer;
        }

        buffer[len++] = val;
    }

    /**
     * Pop and return the element at the end of the array,
     * shrinking the array if need be.
     *
     * @return
     */
    public T popBack() {
        final T ret = buffer[len - 1];
        len--;

        if (len < cap / 2) {
            T[] newBuffer = (T[]) new Object[cap / 2];
            for (int i = 0; i < len; i++) {
                newBuffer[i] = buffer[i];
            }

            cap /= 2;
            buffer = newBuffer;
        }

        return ret;
    }

    /**
     * Add the given element at the beginning of the buffer,
     * growing the buffer if need be.
     *
     * @param val
     */
    public void pushFront(final T val) {
        if (len == cap) {
            T[] newBuffer = (T[]) new Object[cap * 2];
            for (int i = 0; i < len; i++) {
                newBuffer[i] = buffer[i];
            }

            cap *= 2;
            buffer = newBuffer;
        }

        for (int i = len; i > 0; i--) {
            buffer[i] = buffer[i - 1];
        }

        buffer[0] = val;
        len++;
    }

    /**
     * Pop and return the element at the beginning of the array,
     * shrinking the array if need be.
     *
     * @return
     */
    public T popFront() {
        final T ret = buffer[0];

        for (int i = 0; i < len - 1; i++) {
            buffer[i] = buffer[i + 1];
        }

        len--;

        if (len < cap / 2) {
            T[] newBuffer = (T[]) new Object[cap / 2];
            for (int i = 0; i < len; i++) {
                newBuffer[i] = buffer[i];
            }

            cap /= 2;
            buffer = newBuffer;
        }

        return ret;
    }
}
