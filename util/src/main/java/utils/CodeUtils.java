package utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代码生成工具
 * @author guanhuan_li
 */
public abstract class CodeUtils {

    /**
     * 生成convert方法(将source设值到target）
     *
     * 注意：如果是内部类需要自己导入一下
     *      ZancunJieguoReq.Proposal.class 生成只会生成convert(Proposal source, Proposal target)
     *      需要手动引入convert(ZancunJieguoReq.Proposal source, ZancunJieguoReq.Proposal target)
     *
     * @param source 源
     * @param target 目标
     * @return java代码
     */
    public static String createJavaCode(Class<?> source, Class<?> target) {
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(source);
        Set<String> class1fieldSet = allFieldsList.stream().map(Field::getName).collect(Collectors.toSet());

        Field[] class2Fields = FieldUtils.getAllFields(target);

        StringBuilder sb = new StringBuilder();

        sb.append("private static void convert(")
                .append(source.getSimpleName())
                .append(" source, ")
                .append(target.getSimpleName())
                .append(" target) { \r\n");

        String blank = "    ";
        String content = Stream.of(class2Fields)
                .filter(field -> class1fieldSet.contains(field.getName()))
                .map(field -> {
                    String get = "get";
                    String name = field.getName();
                    String fileName = StringUtils.toUpperCaseFirstOne(name);

                    if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        // boolean 类型的特殊处理
                        get = "is";
                        String is = "Is";
                        if (fileName.startsWith(is)) {
                            fileName = fileName.replaceFirst(is, "");
                        }
                    }

                    // 拼接代码
                    return blank + "target.set" + fileName + "(source." +
                            get + fileName + "());";
                })
                .collect(Collectors.joining("\r\n"));

        sb.append(content);

        sb.append("\r\n}");

        System.err.println(sb.toString());

        // 没有赋值的字段
        String noUse = Stream.of(class2Fields)
                .filter(field -> !class1fieldSet.contains(field.getName()))
                .map(Field::getName)
                .collect(Collectors.joining(","));

        System.err.println("------------------------------");
        System.err.println("没有赋值的字段");
        System.err.println(noUse);

        return sb.toString();
    }
}
