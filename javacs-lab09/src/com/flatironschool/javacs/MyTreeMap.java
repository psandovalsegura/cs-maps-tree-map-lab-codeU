/**
 *
 */
package com.flatironschool.javacs;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a Map using a binary search tree.
 *
 * @param <K>
 * @param <V>
 *
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;

	/**
	 * Represents a node in the tree.
	 *
	 */
	protected class Node {
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;

		/**
		 * @param key
		 * @param value
		 * @param left
		 * @param right
		 */
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public boolean isLeaf() {
			if (this.left == null && this.right == null) {
				return true;
			} else {
				return false;
			}
		}

	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsKey(Object target) {
		return findNode(target) != null;
	}

	/**
	 * Returns the entry that contains the target key, or null if there is none.
	 *
	 * @param target
	 */
	private Node findNode(Object target) {
		// some implementations can handle null as a key, but not this one
		if (target == null) {
      throw new NullPointerException();
	  }

		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;

		// the actual search
		Node current = this.root;
		while (k.compareTo(current.getKey()) != 0) {
			if (k.compareTo(current.getKey()) < 0 && current.left != null) {
				current = current.left;
			} else if (k.compareTo(current.getKey()) > 0 && current.right != null) {
				current = current.right;
			} else if (current.right == null || current.left == null) {
				return null;
			}
		}

		return current;
	}

	/**
	 * Compares two keys or two values, handling null correctly.
	 *
	 * @param target
	 * @param obj
	 * @return
	 */
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	@Override
	public boolean containsValue(Object target) {
		Collection<V> allValues = values();
		for (V value: allValues) {
			if (value.equals(target)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if (node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
    addKeys(set, this.root);
		return set;
	}

	/**
	 * Helper method for keySet()
	 */
	public void addKeys(Set<K> set, Node current) {
		if (current.isLeaf()) { //no subtrees
			set.add(current.getKey());
		} else if (current.left != null && current.right != null) { //both left and right subtrees
			addKeys(set, current.left);
			set.add(current.getKey());
			addKeys(set, current.right);
		} else if (current.left != null) { //only left subtree
			addKeys(set, current.left);
			set.add(current.getKey());
		} else { //only right subtree
			set.add(current.getKey());
			addKeys(set, current.right);
		}


	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (root == null) {
			root = new Node(key, value);
			size++;
			return null;
		}
		return putHelper(root, key, value);
	}

	private V putHelper(Node node, K key, V value) {
				//Check if key exists already
				Node optional = findNode(key);
				if (optional != null) {
					V oldValue = optional.value;
					optional.value = value;
					return oldValue;
				}

				//Otherwise, traverse the tree...
        Node current = node;
				Comparable<? super K> k = (Comparable<? super K>) key;
				while (current != null) {
					if (k.compareTo(current.getKey()) < 0) {
						if (current.left == null) {
							current.left = new Node(key, value);
							break;
						}
						current = current.left;
					} else if (k.compareTo(current.getKey()) > 0) {
						if (current.right == null) {
							current.right = new Node(key, value);
							break;
						}
						current = current.right;
					}
				}

				this.size++;
				return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		Deque<Node> stack = new LinkedList<Node>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (node == null) continue;
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MyTreeMap<String, Integer> map = new MyTreeMap<String, Integer>();
		MyTreeMap<String, Integer>.Node node08 = map.makeNode("08", 8);

		MyTreeMap<String, Integer>.Node node03 = map.makeNode("03", 3);
		MyTreeMap<String, Integer>.Node node10 = map.makeNode("10", 10);
		node08.left = node03;
		node08.right = node10;

		MyTreeMap<String, Integer>.Node node01 = map.makeNode("01", 1);
		MyTreeMap<String, Integer>.Node node06 = map.makeNode("06", 6);
		MyTreeMap<String, Integer>.Node node14 = map.makeNode("14", 14);
		node03.left = node01;
		node03.right = node06;
		node10.right = node14;

		MyTreeMap<String, Integer>.Node node04 = map.makeNode("04", 4);
		MyTreeMap<String, Integer>.Node node07 = map.makeNode("07", 7);
		MyTreeMap<String, Integer>.Node node13 = map.makeNode("13", 13);
		node06.left = node04;
		node06.right = node07;
		node14.left = node13;

		map.setTree(node08, 9);

		map.put("05", 5);
	}

	/**
	 * Makes a node.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	/**
	 * Sets the instance variables.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @param node
	 * @param size
	 */
	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	/**
	 * Returns the height of the tree.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @return
	 */
	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if (node == null) {
			return 0;
		}
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}
}
