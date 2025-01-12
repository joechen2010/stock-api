package com.stock.api.util;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class DataTypeUtils {
   private DataTypeUtils() {
   }

   public static boolean isInteger(Object value) {
      boolean flag = false;
      if (value instanceof String) {
         flag = Ints.tryParse((String)value) != null;
      }

      if (value instanceof Integer) {
         flag = true;
      }

      return flag;
   }

   public static int toInt(Object number) {
      return toInt(number, 0);
   }

   public static int toInt(Object number, int defaultValue) {
      int value = defaultValue;
      if (number instanceof String) {
         value = org.apache.commons.lang3.math.NumberUtils.toInt(getStringValue((String)number), defaultValue);
      } else if (number instanceof Integer) {
         value = (Integer)number;
      } else if (number instanceof Long) {
         value = ((Long)number).intValue();
      } else if (number instanceof Double) {
         value = ((Double)number).intValue();
      } else if (number instanceof Float) {
         value = ((Float)number).intValue();
      }

      return value;
   }

   public static String getStringValue(String number) {
      return StringUtils.trimToEmpty(number);
   }

   public static long toLong(Object number, long defaultValue) {
      long value = defaultValue;
      if (number instanceof String) {
         value = org.apache.commons.lang3.math.NumberUtils.toLong(getStringValue((String)number), defaultValue);
      } else if (number instanceof Integer) {
         value = ((Integer)number).longValue();
      } else if (number instanceof Long) {
         value = (Long)number;
      } else if (number instanceof Date) {
         value = ((Date)number).getTime();
      }

      return value;
   }

   public static Long toLongOrNull(String value) {
      if (Op.empty((CharSequence)value)) {
         return null;
      } else {
         try {
            return Long.parseLong(value);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static double toDouble(Object number, double defaultValue) {
      double value = defaultValue;
      if (number instanceof String) {
         value = org.apache.commons.lang3.math.NumberUtils.toDouble(getStringValue((String)number), defaultValue);
      } else if (number instanceof Integer) {
         value = ((Integer)number).doubleValue();
      } else if (number instanceof Long) {
         value = ((Long)number).doubleValue();
      } else if (number instanceof Double) {
         value = (Double)number;
      } else if (number instanceof Float) {
         value = ((Float)number).doubleValue();
      }

      return value;
   }

   public static float toFloat(Object number, float defaultValue) {
      float value = defaultValue;
      if (number instanceof String) {
         value = org.apache.commons.lang3.math.NumberUtils.toFloat(getStringValue((String)number), defaultValue);
      } else if (number instanceof Integer) {
         value = ((Integer)number).floatValue();
      } else if (number instanceof Long) {
         value = ((Long)number).floatValue();
      } else if (number instanceof Double) {
         value = ((Double)number).floatValue();
      } else if (number instanceof Float) {
         value = (Float)number;
      }

      return value;
   }

   public static boolean toBoolean(Object v, boolean defaultValue) {
      boolean value = defaultValue;
      if (v instanceof String) {
         Boolean converted = BooleanUtils.toBooleanObject(getStringValue((String)v));
         value = converted != null ? converted : defaultValue;
      } else if (v instanceof Boolean) {
         value = (Boolean)v;
      }

      return value;
   }

   public static String toBooleanString(Object v) {
      Object value = v instanceof String ? BooleanUtils.toBooleanObject(getStringValue((String)v)) : v;
      return Objects.toString(value, (String)null);
   }

   public static boolean isTrue(Object v) {
      return toBoolean(v, false);
   }

   public static boolean isFalse(Object v) {
      return !toBoolean(v, true);
   }

   public static int toSize(Collection<?> values) {
      return values == null ? 0 : values.size();
   }

   public static int toSize(Map<?, ?> values) {
      return values == null ? 0 : values.size();
   }

   public static int toRealSize(Collection<?> values) {
      return values == null ? 0 : (int)values.stream().filter(Objects::nonNull).count();
   }

   public static boolean toBoolean(Map<String, ?> map, String key) {
      return map != null && toBoolean(map.get(key), false);
   }

   public static String toDate(long timestamp) {
      return DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(timestamp);
   }

   public static Long parseDate(String source) {
      try {
         Date date = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse(source);
         return date.getTime();
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean isRawType(Object object) {
      return object instanceof String || object instanceof Integer || object instanceof Long || object instanceof Double || object instanceof Short || object instanceof Byte || object instanceof Float || object instanceof Boolean || object instanceof Character || object instanceof Enum;
   }

   public static boolean isBooleanStr(Object value) {
      if (value == null) {
         return false;
      } else {
         String v = getStringValue(value.toString());
         return Boolean.TRUE.toString().equalsIgnoreCase(v) || Boolean.FALSE.toString().equalsIgnoreCase(v);
      }
   }

   public static BigDecimal toBigDecimalValue(Serializable value) {
      BigDecimal result = null;
      if (value instanceof BigDecimal) {
         result = (BigDecimal)value;
      } else if (value instanceof String) {
         result = new BigDecimal((String)value);
      } else if (value instanceof BigInteger) {
         result = new BigDecimal((BigInteger)value);
      } else if (value instanceof Integer) {
         result = new BigDecimal((Integer)value);
      } else if (value instanceof Long) {
         result = new BigDecimal((Long)value);
      } else if (value instanceof Float) {
         result = BigDecimal.valueOf((double)(Float)value);
      } else if (value instanceof Short) {
         result = new BigDecimal((Short)value);
      } else if (value instanceof Double) {
         result = BigDecimal.valueOf((Double)value);
      } else if (value instanceof Byte) {
         result = new BigDecimal((Byte)value);
      } else if (value instanceof Number) {
         result = BigDecimal.valueOf(((Number)value).doubleValue());
      }

      Preconditions.checkArgument(result != null, "Can not convert value %s to number", new Object[]{value});
      return result;
   }
}
