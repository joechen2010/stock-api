package com.stock.api.util;

import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import java.util.Collection;
import java.util.List;

public class OpType {
   public boolean isArray(Object source) {
      return source != null && source.getClass().isArray();
   }

   public boolean isCollection(Object source) {
      return source instanceof Collection;
   }

   public String simpleName(Object source) {
      return source != null ? source.getClass().getSimpleName() : null;
   }

   public List<?> toList(Object array) {
      List<?> results = Lists.newArrayList();
      if (array instanceof int[]) {
         results = Ints.asList((int[])array);
      } else if (array instanceof long[]) {
         results = Longs.asList((long[])array);
      } else if (array instanceof short[]) {
         results = Shorts.asList((short[])array);
      } else if (array instanceof byte[]) {
         results = Bytes.asList((byte[])array);
      } else if (array instanceof double[]) {
         results = Doubles.asList((double[])array);
      } else if (array instanceof float[]) {
         results = Floats.asList((float[])array);
      } else if (array instanceof char[]) {
         results = Chars.asList((char[])array);
      } else if (array instanceof boolean[]) {
         results = Booleans.asList((boolean[])array);
      } else if (array.getClass().isArray()) {
         results = Lists.newArrayList((Object[])array);
      }

      return (List)results;
   }
}
