package utils;

import exception.LogicException;

import java.util.Collection;

/**
 * @author liguanhuan
 */
public abstract class Checker {

    public static void isBlank(String str, String msg) {
        if (StringUtils.isBlank(str)) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void notBlank(String str, String msg) {
        if (StringUtils.isNotBlank(str)) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void isTrue(boolean flag, String msg) {
        if (flag) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void isFalse(boolean flag, String msg) {
        if (!flag) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void isNull(Object obj, String msg) {
        if (obj == null) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void notNull(Object obj, String msg) {
        if (obj != null) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void isEmpty(Collection<? extends Object> coll, String msg) {
        if (coll == null || coll.isEmpty()) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static void notEmpty(Collection<? extends Object> coll, String msg) {
        if (coll != null && !coll.isEmpty()) {
            throw LogicException.valueOfUnknow(msg);
        }
    }

    public static boolean isEmpty(Collection<? extends Object> coll) {
        return  (coll == null || coll.isEmpty());
    }

    public static boolean notEmpty(Collection<? extends Object> coll) {
        return (coll != null && !coll.isEmpty());
    }
}
