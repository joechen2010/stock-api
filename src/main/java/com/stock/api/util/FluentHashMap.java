package com.stock.api.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class FluentHashMap<K, V> extends LinkedHashMap<K, V> {
   public FluentHashMap() {
   }

   public FluentHashMap(int initialCapacity) {
      super(initialCapacity);
   }

   public FluentHashMap(Map<? extends K, ? extends V> m) {
      super(m);
   }

   public FluentHashMap<K, V> set(K key, V value) {
      this.put(key, value);
      return this;
   }

   public static <K, V> FluentHashMap<K, V> map(K key, V value) {
      return (new FluentHashMap()).set(key, value);
   }
}
