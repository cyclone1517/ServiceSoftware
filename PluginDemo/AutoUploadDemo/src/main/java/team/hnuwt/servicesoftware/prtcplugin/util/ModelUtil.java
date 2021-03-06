package team.hnuwt.servicesoftware.prtcplugin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 利用反射将数据存入到实体类中
 */
public class ModelUtil {

    private static Logger logger = LoggerFactory.getLogger(ModelUtil.class);

    public static void setFieldValue(Object model, Map<String, Object> valueMap)
    {
        Class<?> cls = model.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields)
        {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMethod(methods, fieldSetName))
                {
                    continue;
                }
                Method fieldSetMethod = cls.getMethod(fieldSetName, field.getType());
                String fieldKeyName = field.getName();
                Object value = valueMap.get(fieldKeyName);
                if (value == null)
                {
                    continue;
                }
                String fieldType = field.getType().getSimpleName();
                if ("int".equals(fieldType))
                {
                    fieldSetMethod.invoke(model, (int) ((long) value));
                } else
                {
                    fieldSetMethod.invoke(model, value);
                }
            } catch (Exception e) {
                logger.error("", e);
                continue;
            }
        }
    }

    private static boolean checkSetMethod(Method[] methods, String fieldSetMetethod)
    {
        for (Method method : methods)
        {
            if (fieldSetMetethod.equals(method.getName()))
            {
                return true;
            }
        }
        return false;
    }

    private static String parSetName(String name)
    {
        if (name == null || "".equals(name))
        {
            return null;
        }
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
