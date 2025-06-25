package com.hexagram2021.emeraldcraft.common.util;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ListAppendable<E> {
	List<E> emeraldcraft$append(E entry);
	List<E> emeraldcraft$appendAll(Iterator<E> entries);
	List<E> emeraldcraft$appendAll(Iterable<E> entries);
}
