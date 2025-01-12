package com.stock.api.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class Op {
   public static final String DEFAULT_SEPARATOR = ",";
   private static OpType typeChecker = new OpType();

   private Op() {
   }

   public static boolean anyEmpty(Object... objects) {
      return Arrays.stream(objects).anyMatch(Op::empty);
   }

   public static boolean allEmpty(Object... objects) {
      return Arrays.stream(objects).allMatch(Op::empty);
   }

   public static String str(Map<String, ?> map) {
      return StringUtils.join(new Map[]{map});
   }

   public static String str(Throwable e) {
      if (e == null) {
         return null;
      } else {
         return e.getMessage() != null && e.getCause() == null ? e.getMessage() : getMessage(e);
      }
   }

   public static String getMessage(final Throwable th) {
      if (th == null) {
         return "";
      } else {
         String msg = getBuiltinMessage(th);
         if (notEmpty((CharSequence)msg)) {
            return msg;
         } else {
            String[] stacks = ExceptionUtils.getStackFrames(th);
            StringBuilder message = new StringBuilder(100);
            String[] var4 = stacks;
            int var5 = stacks.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String stack = var4[var6];
               message.append(stack);
               if (stack.contains(".java:")) {
                  break;
               }
            }

            return message.toString();
         }
      }
   }

   private static String getBuiltinMessage(Throwable th) {
      Throwable root = ExceptionUtils.getRootCause(th);
      boolean includeValidRootMessage = root != null && notEmpty((CharSequence)root.getMessage()) && notEq(root.toString(), root.getMessage());
      boolean invalidMessage = root != null && eq(root.toString(), th.getMessage());
      String msg = invalidMessage ? "" : th.getMessage();
      if (empty((CharSequence)msg) && includeValidRootMessage) {
         msg = root.getMessage();
      }

      return msg;
   }

   public static String str(final Iterable<?> values) {
      return StringUtils.join(values, ",");
   }

   public static String str(Iterable<?> values, String separator) {
      return StringUtils.join(values, separator);
   }

   public static String str(Object... objects) {
      return StringUtils.join(objects, ",");
   }

   public static String str(Object object) {
      return Objects.toString(object, (String)null);
   }

   public static String str(String format, Object... arguments) {
      int placeHolderCount = StringUtils.countMatches(format, "{}");
      if (placeHolderCount != 0 && arguments != null && !empty(arguments)) {
         String[] snippets = format.split("\\{}");
         StringBuilder sb = new StringBuilder(format.length() * 2);

         for(int i = 0; i < snippets.length; ++i) {
            sb.append(snippets[i]);
            if (i < ArrayUtils.getLength(arguments) && i < placeHolderCount) {
               Object toStr = arguments[i] instanceof Throwable ? str((Throwable)arguments[i]) : arguments[i];
               sb.append(toStr);
            }
         }

         return sb.toString();
      } else {
         return format;
      }
   }

   public static String format(long createTimeFrom) {
      return DateFormatUtils.format(createTimeFrom, "yyyyMMdd");
   }

   public static <T> String join(Collection<T> containers, Function<? super T, String> mapper) {
      return join(containers, mapper, ",");
   }

   public static <T> String join(Collection<T> containers, Function<? super T, String> mapper, String separator) {
      return containers != null && mapper != null ? (String)containers.stream().map(mapper).filter(Op::notEmpty).collect(Collectors.joining(separator)) : "";
   }

   public static long now() {
      return System.currentTimeMillis();
   }

   public static <T, K, V> Map<K, V> map(Collection<T> beans, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
      BiConsumer<Map<K, V>, T> accumulator = (map, element) -> {
         map.put(keyMapper.apply(element), valueMapper.apply(element));
      };
      return (Map)(Optional.ofNullable(beans).orElseGet(ArrayList::new)).stream().collect(Collector.of(HashMap::new, accumulator, (a, b) -> {
         return b;
      }));
   }

   public static <T, K, V> Map<K, V> map(T[] array, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
      if (empty(array)) {
         return Maps.newHashMap();
      } else {
         BiConsumer<Map<K, V>, T> accumulator = (map, element) -> {
            map.put(keyMapper.apply(element), valueMapper.apply(element));
         };
         return (Map)Arrays.stream(array).collect(Collector.of(HashMap::new, accumulator, (a, b) -> {
            return b;
         }));
      }
   }

   public static boolean eq(Object source, Object target1, Object target2) {
      return eq(source, target1) && eq(source, target2);
   }

   public static int toRange(int value, int min, int max) {
      if (range(value, min, max)) {
         return value;
      } else {
         int newValue = Math.max(value, min);
         newValue = Math.min(newValue, max);
         return newValue;
      }
   }

   public static <T> T of(T first, T optional) {
      return first != null ? first : optional;
   }

   public static <T> T of(T first, Supplier<T> optional) {
      return first != null ? first : eval(optional);
   }

   public static String of(String first, String optional) {
      return StringUtils.isNotBlank(first) ? first : optional;
   }

   public static String of(String value) {
      return StringUtils.isNotBlank(value) ? value : null;
   }

   public static String of(String first, Supplier<String> optional) {
      return StringUtils.isNotBlank(first) ? first : (String)eval(optional);
   }

   public static <T> List<T> of(T value) {
      return empty(value) ? Lists.newArrayList() : Lists.newArrayList(value);
   }

   public static <T> List<T> of(Collection<T> source) {
      return empty(source) ? Lists.newArrayList() : Lists.newArrayList(source);
   }

   public static <T> List<T> of(List<T> source) {
      return empty(source) ? Lists.newArrayList() : Lists.newArrayList(source);
   }

   public static <T> void ifPresent(T value, Consumer<? super T> consumer) {
      if (notEmpty(value)) {
         consumer.accept(value);
      }

   }

   public static <T> List<T> list() {
      return Lists.newArrayList();
   }

   public static <T> List<T> list(T... values) {
      return (List)(values == null ? Lists.newArrayList() : (List)Arrays.stream(values).filter(Objects::nonNull).collect(Collectors.toList()));
   }

   public static <T> List<T> list(T newItem, Collection<T> collection) {
      List<T> newCollection = of(newItem);
      if (notEmpty(newCollection)) {
         newCollection.addAll(collection);
      }

      return newCollection;
   }

   public static <K, V, T> List<T> list(Map<K, V> map, Function<Entry<K, V>, T> mapper) {
      return map == null ? list() : (List)map.entrySet().stream().filter(Objects::nonNull).map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
   }

   public static <T> void add(Collection<T> collections, T value) {
      if (collections != null && value != null) {
         collections.add(value);
      }

   }

   public static <T> void add(boolean expression, List<T> collections, T value) {
      if (expression && collections != null && value != null) {
         collections.add(value);
      }

   }

   public static <K, V> Map<K, V> of(Map<K, V> source) {
      return (Map)(empty((Object)source) ? map() : new FluentHashMap(source));
   }

   public static <K, V> Map<K, V> map() {
      return new FluentHashMap(0);
   }

   public static <K, V> Map<K, V> map(K k, V v) {
      return FluentHashMap.map(k, v);
   }

   public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
      return FluentHashMap.map(k1, v1).set(k2, v2);
   }

   public static void when(boolean expression, Runnable thenExecute) {
      execute(expression, thenExecute);
   }

   public static <T> T eval(Supplier<T> methodChain, T defaultValue) {
      try {
         return methodChain.get();
      } catch (NullPointerException var3) {
         return defaultValue;
      }
   }

   public static <T> T eval(Supplier<T> methodChain) {
      return eval(methodChain, null);
   }

   public static <R, T> R call(T bean, Function<T, R> call) {
      return Optional.ofNullable(bean).map(call).orElse(null);
   }

   public static <R, T, A> R call(T bean, Function<T, A> call1, Function<A, R> calll2) {
      return Optional.ofNullable(bean).map(call1).map(calll2).orElse(null);
   }

   public static void onNotEq(String left, String right, Runnable callback) {
      boolean flag = notEq(left, right);
      execute(flag, callback);
   }

   public static boolean notEq(String left, String right) {
      return empty((CharSequence)left) ? notEmpty((CharSequence)right) : !StringUtils.equals(left, right);
   }

   public static boolean notEq(Object source, String target) {
      return target == null ? source != null : !StringUtils.equals(Objects.toString(source, (String)null), target);
   }

   public static boolean notEq(Object source, Object target) {
      return !Objects.equals(source, target);
   }

   public static boolean notEq(Object source, String target1, String target2) {
      return notEq(source, target1) && notEq(source, target2);
   }

   public static boolean notEq(String left, String right, boolean when) {
      return when && notEq(left, right);
   }

   public static boolean notEqAny(String source, String... targets) {
      return Arrays.stream(targets).noneMatch((t) -> {
         return eq(source, t);
      });
   }

   public static boolean notEqAny(Object source, String... values) {
      return notEmpty((Object[])values) && Arrays.stream(values).noneMatch((t) -> {
         return eq(source, t);
      });
   }

   public static boolean eq(String left, String right) {
      return StringUtils.equals(StringUtils.trimToEmpty(left), StringUtils.trimToEmpty(right));
   }

   public static boolean isTrue(Object value) {
      return DataTypeUtils.toBoolean(value, false);
   }

   public static boolean isFalse(Object value) {
      return value != null && !DataTypeUtils.toBoolean(value, true);
   }

   public static void onEq(String left, String right, Runnable callback) {
      boolean flag = eq(left, right);
      execute(flag, callback);
   }

   public static boolean eq(boolean ignoreCase, String left, String right) {
      return ignoreCase ? StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(left), StringUtils.trimToEmpty(right)) : StringUtils.equals(StringUtils.trimToEmpty(left), StringUtils.trimToEmpty(right));
   }

   public static boolean eq(Object left, Object right) {
      return Objects.equals(left, right);
   }

   public static boolean eq(Object left, String right) {
      return StringUtils.equals(Objects.toString(left, (String)null), right);
   }

   public static boolean eq(Long left, String right) {
      return notEmpty(left, right) && left == DataTypeUtils.toLong(right, left + 1L);
   }

   public static <E> boolean eq(List<E> left, List<E> right) {
      if (!empty(left) && !empty(right)) {
         return size(left) == size(right) && left.containsAll(right) && right.containsAll(left);
      } else {
         return size(left) == size(right);
      }
   }

   public static boolean eqAny(Object source, Object... targets) {
      return Arrays.stream(targets).anyMatch((a) -> {
         return eq(source, a);
      });
   }

   public static boolean eqAll(String left1, String right1, String left2, String right2) {
      return StringUtils.equals(left1, right1) && StringUtils.equals(left2, right2);
   }

   public static boolean ends(String str, String suffix) {
      return StringUtils.endsWith(str, suffix);
   }

   public static boolean empty(CharSequence source) {
      return StringUtils.isBlank(source);
   }

   public static boolean empty(Object source) {
      if (source == null) {
         return true;
      } else {
         boolean flag = false;
         if (source instanceof String) {
            flag = StringUtils.isBlank((String)source);
         } else if (type().isCollection(source)) {
            flag = ((Collection)source).isEmpty();
         } else if (source instanceof Map) {
            flag = ((Map)source).isEmpty();
         } else if (type().isArray(source)) {
            flag = ArrayUtils.getLength(source) == 0;
         }

         return flag;
      }
   }

   public static boolean empty(Object... values) {
      return values == null || Arrays.stream(values).allMatch(Op::empty);
   }

   public static <E> boolean empty(Collection<E> source) {
      return source == null || source.isEmpty();
   }

   public static boolean empty(Supplier<? extends Object> methodChain) {
      Object value = eval(methodChain, null);
      return value == null || empty(value);
   }

   public static boolean notEmpty(CharSequence source) {
      return StringUtils.isNotBlank(source);
   }

   public static boolean notEmpty(Object source) {
      return source != null && !empty(source);
   }

   public static boolean notEmpty(Object... values) {
      return values != null && Arrays.stream(values).anyMatch(Op::notEmpty);
   }

   public static <E> boolean notEmpty(Collection<E> source) {
      return source != null && !source.isEmpty();
   }

   public static <K, V> boolean notEmpty(Map<K, V> source) {
      return source != null && !source.isEmpty();
   }

   public static boolean withIn(String cs, String... searchChars) {
      return StringUtils.containsAny(cs, searchChars);
   }

   public static boolean withIn(Object cs, String... searchChars) {
      return StringUtils.containsAny(Objects.toString(cs), searchChars);
   }

   public static boolean withIn(Object cs, Object... values) {
      return values != null && Arrays.asList(values).contains(cs);
   }

   public static boolean any(Boolean... expressions) {
      return expressions != null && Arrays.stream(expressions).anyMatch((a) -> {
         return DataTypeUtils.toBoolean(a, false);
      });
   }

   public static boolean all(Boolean... expressions) {
      return expressions != null && Arrays.stream(expressions).allMatch((a) -> {
         return DataTypeUtils.toBoolean(a, false);
      });
   }

   public static boolean gt(Number left, Number right) {
      return !anyEmpty(left, right) && BigDecimal.valueOf(left.doubleValue()).compareTo(BigDecimal.valueOf(right.doubleValue())) > 0;
   }

   public static boolean gte(Number left, Number right) {
      return notEmpty(left, right) && BigDecimal.valueOf(left.doubleValue()).compareTo(BigDecimal.valueOf(right.doubleValue())) >= 0;
   }

   public static boolean positive(Object... values) {
      return values != null && Arrays.stream(values).allMatch((v) -> {
         boolean b;
         if (v instanceof Number) {
            b = gte((Number)v, 0);
         } else if (v instanceof Boolean) {
            b = isTrue(v);
         } else if (type().isArray(v)) {
            b = ArrayUtils.getLength(v) > 0;
         } else {
            b = notEmpty(v);
         }

         return b;
      });
   }

   public static boolean has(Object container, Object value) {
      boolean flag = false;
      if (container instanceof String) {
         flag = StringUtils.contains((CharSequence)container, (String)value);
      } else if (type().isCollection(container)) {
         flag = ((Collection)container).contains(value);
      } else if (type().isArray(container)) {
         flag = type().toList(container).stream().anyMatch((v) -> {
            return Objects.equals(v, value);
         });
      } else if (value != null) {
         flag = container == value;
      }

      return flag;
   }

   public static <T> boolean hasAny(Collection<T> container, Collection<T> values) {
      return values == null ? has(container, (Object)null) : values.stream().anyMatch((v) -> {
         return has(container, v);
      });
   }

   public static <T> boolean hasAny(Object container, Collection<T> values) {
      return values == null ? has(container, (Object)null) : values.stream().anyMatch((v) -> {
         return has(container, v);
      });
   }

   public static boolean hasAny(Object container, Object... values) {
      if (values == null) {
         return has(container, (Object)null);
      } else {
         Predicate<Object> predicate = container == null ? Objects::isNull : (v) -> {
            return has(container, v);
         };
         return Arrays.stream(values).anyMatch(predicate);
      }
   }

   public static boolean hasAll(Object container, Object... values) {
      return values == null ? has(container, (Object)null) : Arrays.stream(values).allMatch((v) -> {
         return has(container, v);
      });
   }

   public static <T> boolean belong(Collection<T> container, Object... values) {
      return notEmpty(container) && container.stream().allMatch((v) -> {
         return has(values, v);
      });
   }

   public static <T> boolean belong(Collection<T> container, Collection<T> values) {
      return notEmpty(container) && container.stream().allMatch((v) -> {
         return has(values, v);
      });
   }

   public static boolean notRange(Object value, int min, int max) {
      return !range(value, min, max);
   }

   public static boolean range(Object value, int min, int max) {
      boolean flag = false;
      if (value instanceof String) {
         int length = StringUtils.length((String)value);
         flag = length <= max && length >= min;
      } else {
         long lvalue;
         if (value instanceof Long) {
            lvalue = DataTypeUtils.toLong(value, 0L);
            flag = lvalue <= (long)max && lvalue >= (long)min;
         } else if (value instanceof Integer) {
            lvalue = (long)DataTypeUtils.toInt(value, 0);
            flag = lvalue <= (long)max && lvalue >= (long)min;
         }
      }

      return flag;
   }

   private static void execute(boolean flag, Runnable exec) {
      if (flag) {
         exec.run();
      }

   }

   public static <T> int size(Collection<T> collections) {
      return notEmpty(collections) ? collections.size() : 0;
   }

   public static int size(Object[] values) {
      return notEmpty(values) ? values.length : 0;
   }

   public static <K, V> int size(Map<K, V> map) {
      return notEmpty(map) ? map.size() : 0;
   }

   public static <T> boolean size(Collection<T> collections, int targetSize) {
      return size(collections) == targetSize;
   }

   public static <T> Stream<T> stream(Collection<T> list) {
      return (Optional.ofNullable(list).orElse(Lists.newArrayList())).stream();
   }

   public static <T> Stream<T> stream(T[] array) {
      return empty(array) ? stream(Lists.newArrayList()) : Arrays.stream(array);
   }

   public static <K, V> Stream<Entry<K, V>> stream(Map<K, V> sources) {
      return ((Map)Optional.ofNullable(sources).orElse(Maps.newHashMap())).entrySet().stream();
   }

   public static <T> List<T> filter(Collection<T> list, Predicate<? super T> predicate) {
      return (List)stream(list).filter(predicate).collect(Collectors.toList());
   }

   public static <K, V> Map<K, V> filter(Map<K, V> sources, Predicate<Entry<K, V>> predicate) {
      Supplier<Map<K, V>> result = getMapSupplier(sources);
      return (Map)stream(sources).filter((a) -> {
         return a.getKey() != null;
      }).filter(predicate).filter(Objects::nonNull).collect(result, (m, v) -> {
         m.put(v.getKey(), v.getValue());
      }, Map::putAll);
   }

   public static <V, K> Supplier<Map<K, V>> getMapSupplier(Map<?, ?> sources) {
      Supplier<Map<K, V>> result = HashMap::new;
      if (sources instanceof LinkedHashMap) {
         result = LinkedHashMap::new;
      }

      if (sources instanceof TreeMap) {
         result = TreeMap::new;
      }

      if (sources instanceof ConcurrentMap) {
         result = ConcurrentHashMap::new;
      }

      return result;
   }

   public static <T> void each(Collection<T> collection, Consumer<? super T> consumer) {
      if (notEmpty(collection)) {
         collection.stream().filter(Objects::nonNull).forEach(consumer);
      }

   }

   public static <T> void each(T[] array, Consumer<? super T> consumer) {
      if (notEmpty(array)) {
         Arrays.stream(array).filter(Objects::nonNull).forEach(consumer);
      }

   }

   public static <K, V> void each(Map<K, V> sources, BiConsumer<? super K, ? super V> consumer) {
      if (notEmpty(sources)) {
         sources.forEach(consumer);
      }

   }

   public static <T> void each(Supplier<List<T>> methodChain, Consumer<? super T> consumer) {
      List<T> t = (List)eval(methodChain, null);
      if (notEmpty(t)) {
         each(t, (Consumer)consumer);
      }

   }

   public static <K, T> List<K> map(Collection<T> collection, Function<T, K> mapper) {
      return empty(collection) ? list() : (List)collection.stream().filter(Objects::nonNull).map(mapper).filter(Op::notEmpty).collect(Collectors.toList());
   }

   public static <K, T> List<K> map(T[] array, Function<T, K> mapper) {
      return empty(array) ? list() : (List)Arrays.stream(array).filter(Objects::nonNull).map(mapper).filter(Op::notEmpty).collect(Collectors.toList());
   }

   public static <K, T> List<K> map(Collection<T> list, Function<T, K> mapper, boolean unique) {
      return !unique ? map(list, mapper) : (List)stream(list).filter(Objects::nonNull).map(mapper).filter(Op::notEmpty).distinct().collect(Collectors.toList());
   }

   public static <T> void filterThen(List<T> list, Predicate<? super T> predicate, Consumer<T> forEach) {
      stream(list).filter(predicate).forEach(forEach);
   }

   public static <T> boolean any(Collection<T> containers, Predicate<? super T> predicate) {
      return first(containers, predicate) != null;
   }

   public static <T> boolean any(T[] containers, Predicate<? super T> predicate) {
      return first(containers, predicate) != null;
   }

   public static <T> boolean noOne(List<T> list, Predicate<? super T> predicate) {
      return first(list, predicate) == null;
   }

   public static <T> T first(Collection<T> source) {
      return empty(source) ? null : source.iterator().next();
   }

   public static <T> T first(Collection<T> list, Predicate<? super T> predicate) {
      return stream(list).filter(predicate).findFirst().orElse(null);
   }

   public static <T> T first(T[] sources) {
      return empty(sources) ? null : sources[0];
   }

   public static <T> T first(T[] sources, Predicate<? super T> predicate) {
      return sources == null ? null : Arrays.stream(sources).filter(predicate).findFirst().orElse(null);
   }

   @SafeVarargs
   public static <T> T firstNotEmpty(T... sources) {
      return first(sources, Op::notEmpty);
   }

   public static <T> T firstNotEmpty(Collection<T> sources) {
      return first(sources, Op::notEmpty);
   }

   public static <T> T last(T[] array) {
      return array != null && array.length != 0 ? array[array.length - 1] : null;
   }

   public static <T> T last(Collection<T> source) {
      return empty(source) ? null : Iterables.getLast(source);
   }

   public static OpType type() {
      return typeChecker;
   }

   public static Ov validator(Object bean) {
      return Ov.of(bean);
   }
}
