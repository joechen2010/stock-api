package com.stock.api.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DoubleConverter extends JsonDeserializer<Double> {
   private static final Logger logger = LoggerFactory.getLogger(DoubleConverter.class);

   public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      String value = p.getValueAsString();
      return convert(value);
   }

   public Double getNullValue(DeserializationContext ctxt) throws JsonMappingException {
      return -100.0D;
   }

   public static double convert(String value) {
      try {
         return !Op.empty((CharSequence)value) && !org.apache.commons.lang3.math.NumberUtils.isNumber(value) && "null".equals(value) ? -1000.0D : org.apache.commons.lang3.math.NumberUtils.toDouble(value, -1000.0D);
      } catch (Exception var2) {
         logger.error("Fail to deserialize {}. ", value, var2);
         return -1000.0D;
      }
   }
}
