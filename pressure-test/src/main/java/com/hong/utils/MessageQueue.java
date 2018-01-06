package com.hong.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessageQueue<T> {
  private Queue<T> queue = new LinkedList<T>();

  public synchronized void put(T data) {
    queue.offer(data);
  }

  public synchronized List<T> get(int number) {
    List<T> results = new ArrayList<T>();
    for (int i = 0; i < number; ++i) {
      results.add(queue.poll());
    }
    return results;
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public int size() {
    return queue.size();
  }
}
