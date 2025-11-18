package org.pwss.data_structure;

/**
 * A thread-safe, fixed-size ring buffer implementation.
 *
 * @param <T> The type of elements held in this collection.
 */
public final class RingBuffer<T> {
    private final Object[] buffer;
    private int writeIndex = 0;
    private int readIndex = 0;
    private int size = 0;

    /**
     * Constructs a new ring buffer with the specified capacity.
     *
     * @param capacity The maximum number of elements that can be stored in this
     *                 buffer.
     */
    public RingBuffer(int capacity) {
        buffer = new Object[capacity];
    }

    /**
     * Adds an item to the end of the buffer. If the buffer is full, this method
     * will overwrite the oldest element with the new one.
     *
     * @param item The item to add to the buffer.
     */
    public void add(T item) {
        buffer[writeIndex] = item;
        writeIndex = (writeIndex + 1) % buffer.length;

        if (size == buffer.length) {
            // Buffer is full, move read pointer forward.
            readIndex = (readIndex + 1) % buffer.length;
        } else {
            size++;
        }
    }

    /**
     * Retrieves the item at the specified index from this buffer. The first element
     * in
     * the buffer has an index of zero.
     *
     * @param index The index of the item to retrieve.
     * @return The item at the specified position in this buffer.
     * @throws IndexOutOfBoundsException If the specified index is greater than or
     *                                   equal to the size of this
     *                                   buffer.
     */
    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        int realIndex = (readIndex + index) % buffer.length;
        return (T) buffer[realIndex];
    }

    /**
     * Returns the number of elements in this buffer.
     *
     * @return The current number of elements in this buffer.
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether this buffer is empty or not.
     *
     * @return True if this buffer contains no elements; false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }
}