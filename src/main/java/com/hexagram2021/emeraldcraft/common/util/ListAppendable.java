package com.hexagram2021.emeraldcraft.common.util;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ListAppendable<E> {
	List<E> append(E entry);
	List<E> appendAll(Iterator<E> entries);
	List<E> appendAll(Iterable<E> entries);
}
