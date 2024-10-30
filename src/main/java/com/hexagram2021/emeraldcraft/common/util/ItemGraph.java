package com.hexagram2021.emeraldcraft.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

public class ItemGraph {
	private final Map<Item, Node> ALL_NODES = Maps.newIdentityHashMap();

	public void addEdge(Item from, Item to) {
		if(from != to) {
			this.getOrCreateNode(from).addEdge(this.getOrCreateNode(to));
		}
	}

	private Node getOrCreateNode(Item item) {
		if(ALL_NODES.containsKey(item)) {
			return ALL_NODES.get(item);
		}
		Node ret = new Node(item);
		ALL_NODES.put(item, ret);
		return ret;
	}

	public void visit(Item item) {
		if(ALL_NODES.containsKey(item)) {
			Node start = ALL_NODES.get(item);
			if(!start.isVisited()) {
				Queue<Node> queue = Queues.newArrayDeque();
				start.setVisited();
				queue.add(start);
				while (!queue.isEmpty()) {
					Node node = queue.remove();
					node.visitChildren(child -> {
						if (!child.isVisited()) {
							child.setVisited();
							queue.add(child);
						}
					});
				}
				return;
			}
			return;
		}
		Node ret = new Node(item);
		ret.setVisited();
		ALL_NODES.put(item, ret);
	}

	public Optional<Integer> degreeIfNotVisited(Item item) {
		if(ALL_NODES.containsKey(item)) {
			Node node = ALL_NODES.get(item);
			if(node.isVisited()) {
				return Optional.empty();
			}
			return Optional.of(node.fromEdges.size());
		}
		return Optional.of(0);
	}

	private static class Node {
		private final Item item;
		private final List<Node> fromEdges = Lists.newArrayList();
		private final List<Node> edges = Lists.newArrayList();
		private boolean visited = false;

		public Node(Item item) {
			this.item = item;
		}

		public Item getItem() {
			return this.item;
		}

		public void visitChildren(Consumer<Node> consumer) {
			this.edges.forEach(consumer);
		}

		public void addEdge(Node node) {
			if(!this.edges.contains(node)) {
				this.edges.add(node);
				node.fromEdges.add(this);
			}
		}

		public void setVisited() {
			this.visited = true;
		}
		public boolean isVisited() {
			return this.visited;
		}
	}
}
