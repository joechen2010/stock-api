package com.stock.api.util;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.util.Assert;

public class Ov {
   private static final Logger logger = LoggerFactory.getLogger(Op.class);
   private final Object bean;
   private BeanWrapper beanWrapper;
   private final List<Boolean> executors = Lists.newArrayList();
   private boolean ignoreCaseWithStringCompare = false;

   private Ov(Object bean) {
      this.bean = bean;
      this.beanWrapper = new BeanWrapperImpl(bean);
   }

   public static Ov of(Object bean) {
      Assert.notNull(bean, "The bean object must not be null");
      return new Ov(bean);
   }

   public Ov ignoreCase() {
      this.ignoreCaseWithStringCompare = true;
      return this;
   }

   public Ov ignoreCase(boolean ignore) {
      this.ignoreCaseWithStringCompare = ignore;
      return this;
   }

   public Ov is(String property, String value) {
      String strValue = this.getStringValue(property);
      this.executors.add(Op.eq(this.ignoreCaseWithStringCompare, strValue, value));
      return this;
   }

   public Ov is(String property, String value, boolean ignoreCase) {
      String strValue = this.getStringValue(property);
      this.executors.add(Op.eq(ignoreCase, strValue, value));
      return this;
   }

   public Ov is(String propertyName, Object value) {
      Object propertyValue = this.getValue(propertyName);
      this.executors.add(Objects.equals(value, propertyValue));
      return this;
   }

   public Ov isTrue(String propertyName) {
      this.executors.add(this.getBooleanValue(propertyName));
      return this;
   }

   public Ov isFalse(String propertyName) {
      this.executors.add(!this.getBooleanValue(propertyName));
      return this;
   }

   public Ov notEq(String propertyName, String value) {
      String strValue = this.getStringValue(propertyName);
      this.executors.add(Op.notEq(strValue, value));
      return this;
   }

   public Ov in(String propertyName, String... values) {
      String strValue = this.getStringValue(propertyName);
      this.executors.add(Op.withIn(strValue, values));
      return this;
   }

   public Ov in(String propertyName, Object... values) {
      Object objValue = this.getValue(propertyName);
      boolean flag = Arrays.stream(values).anyMatch((value) -> {
         return Op.has(objValue, value);
      });
      this.executors.add(flag);
      return this;
   }

   public Ov has(String... propertyNames) {
      Arrays.stream(propertyNames).forEach(this::has);
      return this;
   }

   public Ov has(String propertyName) {
      Object value = this.getValue(propertyName);
      this.executors.add(Op.notEmpty(value));
      return this;
   }

   public Ov contain(String propertyName, String value) {
      Object curValue = this.getValue(propertyName);
      this.executors.add(Op.has(curValue, value));
      return this;
   }

   public Ov between(String propertyName, int start, int end) {
      int value = DataTypeUtils.toInt(this.getValue(propertyName));
      this.executors.add(value >= start && value <= end);
      return this;
   }

   public Ov between(String propertyName, long start, long end) {
      int value = DataTypeUtils.toInt(this.getValue(propertyName));
      this.executors.add((long)value >= start && (long)value <= end);
      return this;
   }

   public Ov len(String propertyName, int min, int max) {
      Object value = this.getValue(propertyName);
      boolean flag = Op.range(value, min, max);
      this.executors.add(flag);
      return this;
   }

   public Ov size(String propertyName, int size) {
      Object value = this.getValue(propertyName);
      boolean flag = false;
      if (Op.type().isCollection(value)) {
         flag = ((Collection) value).size() == size;
      }

      if (Op.type().isArray(value)) {
         flag = ArrayUtils.getLength(value) == size;
      }

      this.executors.add(flag);
      return this;
   }

   public boolean ok() {
      return this.executors.stream().allMatch((b) -> {
         return b;
      });
   }

   public boolean fail() {
      return this.executors.stream().anyMatch((b) -> {
         return !b;
      });
   }

   public boolean allTrue() {
      return this.executors.stream().allMatch((b) -> {
         return b;
      });
   }

   public boolean anyTrue() {
      return this.executors.stream().anyMatch((b) -> {
         return b;
      });
   }

   public boolean anyFalse() {
      return this.executors.stream().anyMatch((b) -> {
         return !b;
      });
   }

   public int toInt(String propertyNameOrKey) {
      Object value = this.getValue(propertyNameOrKey);
      return DataTypeUtils.toInt(value, 0);
   }

   public long toLong(String propertyNameOrKey) {
      Object value = this.getValue(propertyNameOrKey);
      return DataTypeUtils.toLong(value, 0L);
   }

   public String toStr(String propertyNameOrKey) {
      Object value = this.getValue(propertyNameOrKey);
      return Objects.toString(value, (String)null);
   }

   protected Object getValue(String property) {
      try {
         return this.beanWrapper.getPropertyValue(property);
      } catch (InvalidPropertyException var4) {
         String msg = String.format("Failed to get property %s from bean:%s", property, this.bean);
         logger.debug(msg, var4.getMessage());
         return null;
      }
   }

   protected String getStringValue(String property) {
      return (String)this.getValue(property);
   }

   protected boolean getBooleanValue(String property) {
      return DataTypeUtils.toBoolean(this.getValue(property), false);
   }
}
