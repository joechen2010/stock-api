package com.stock.api.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IntegerConverter extends JsonDeserializer<Integer> {
   private static final Logger logger = LoggerFactory.getLogger(IntegerConverter.class);

   public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      String value = p.getValueAsString();
      return convert(value);
   }

   public static int convert(String value) {
      try {
         return !org.apache.commons.lang3.math.NumberUtils.isNumber(value) && "null".equals(value) ? -100 : org.apache.commons.lang3.math.NumberUtils.toInt(value, -100);
      } catch (Exception var2) {
         logger.error("Fail to deserialize {}. ", value, var2);
         return -100;
      }
   }

   public Integer getNullValue(DeserializationContext ctxt) throws JsonMappingException {
      return -100;
   }
}
