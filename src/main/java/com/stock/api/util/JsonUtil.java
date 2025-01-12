package com.stock.api.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public final class JsonUtil {
   private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
   private static final ObjectMapper MAPPER = new ObjectMapper();
   private static final ObjectMapper MAPPER_VIEW = new ObjectMapper();

   private JsonUtil() {
   }

   public static ObjectMapper createObjectMapper() {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(Include.NON_NULL);
      objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper;
   }

   public static <T> String safeObjectToJson(T t) {
      try {
         return objectToJson(t);
      } catch (Exception var2) {
         return "";
      }
   }

   public static <K, V> Map<K, V> objectToMap(Object object, Class<K> keyType, Class<V> valueType) {
      JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
      return (Map)MAPPER.convertValue(object, type);
   }

   public static <T> String objectToJson(T t) {
      StringWriter writer = new StringWriter();

      try {
         MAPPER.writeValue(writer, t);
      } catch (Exception var3) {
         return null;
      }

      return writer.toString();
   }

   public static <T> String objectToJsonWithView(Object obj, Class<T> jsonViewClazz) {
      try {
         return MAPPER_VIEW.writerWithView(jsonViewClazz).writeValueAsString(obj);
      } catch (Exception var3) {
         throw new IllegalStateException("Parse object to JSON failed", var3);
      }
   }

   public static <T> T jsonToObject(String jsonString, Class<T> valueType) {
      try {
         return MAPPER.readValue(jsonString, valueType);
      } catch (Exception var3) {
         throw new IllegalStateException("Parse JSON to object failed", var3);
      }
   }

   public static <T> T safeJsonToObject(String jsonString, Class<T> valueType) {
      try {
         return MAPPER.readValue(jsonString, valueType);
      } catch (Exception var3) {
         return null;
      }
   }

   public static <T> List<T> jsonToList(String jsonString, Class<T> valueType) {
      CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, valueType);

      try {
         return (List)MAPPER.readValue(jsonString, type);
      } catch (IOException var4) {
         return Op.list();
      }
   }

   public static <K, V> Map<K, V> jsonToMap(String jsonString, Class<K> keyType, Class<V> valueType) {
      MapType type = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);

      try {
         return (Map)MAPPER.readValue(jsonString, type);
      } catch (IOException var5) {
         throw new IllegalStateException("Parse object to Map failed", var5);
      }
   }

   public static <T> T mapToObject(Map map, Class<T> toValueType) {
      return MAPPER.convertValue(map, toValueType);
   }

   public static <T> T objectToObject(Object map, Class<T> toValueType) {
      return MAPPER.convertValue(map, toValueType);
   }

   public static <T> List<T> listToObject(List list, Class<T> toValueType) {
      JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, toValueType);
      return (List)MAPPER.convertValue(list, type);
   }

   public static <T> List<T> toObjects(List<Map> rows, Class<T> toValueType) {
      if (Op.empty(rows)) {
         return Op.list();
      } else {
         JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, toValueType);
         return (List)MAPPER.convertValue(rows, type);
      }
   }

   public static ObjectMapper getMapper() {
      return MAPPER;
   }

   public static JsonNode readTreeWithView(String content) {
      try {
         return MAPPER_VIEW.readTree(content);
      } catch (Exception var2) {
         throw new IllegalStateException("Parse string to JsonNode failed", var2);
      }
   }

   public static JsonNode readTree(File file) throws IOException {
      return MAPPER.readTree(file);
   }

   public static JsonNode readTree(String json) throws JsonProcessingException {
      return MAPPER.readTree(json);
   }

   static {
      MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      MAPPER_VIEW.disable(new MapperFeature[]{MapperFeature.DEFAULT_VIEW_INCLUSION});
      MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
      MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
   }
}
