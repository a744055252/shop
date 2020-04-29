package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T decodeJson(String json, Class<T> pojoClass) throws Exception {
        try {
            return objectMapper.readValue(json, pojoClass);
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static <T> T decodeJson(InputStream is, Class<T> pojoClass) throws Exception {
        try {
            return objectMapper.readValue(is, pojoClass);
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static <T> T decodeJson(String json, TypeReference<T> ref) {
        try {
            return objectMapper.readValue(json, ref);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> T decodeJson(String json, Type type) {
        try {
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            return objectMapper.readValue(json, javaType);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> T decodeJson(String json, JavaType javaType) {
        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String encodeJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String formatJson(String content) {
        StringBuffer sb = new StringBuffer();
        int index = 0;

        for(int count = 0; index < content.length(); ++index) {
            char ch = content.charAt(index);
            int i;
            if (ch != '{' && ch != '[') {
                if (ch != '}' && ch != ']') {
                    if (ch == ',') {
                        sb.append(ch);
                        sb.append('\n');

                        for(i = 0; i < count; ++i) {
                            sb.append('\t');
                        }
                    } else {
                        sb.append(ch);
                    }
                } else {
                    sb.append('\n');
                    --count;

                    for(i = 0; i < count; ++i) {
                        sb.append('\t');
                    }

                    sb.append(ch);
                }
            } else {
                sb.append(ch);
                sb.append('\n');
                ++count;

                for(i = 0; i < count; ++i) {
                    sb.append('\t');
                }
            }
        }

        return sb.toString();
    }

    public static String compactJson(String content) {
        String regEx = "[\t\n]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.replaceAll("").trim();
    }

    static {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setConfig(objectMapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }
}
