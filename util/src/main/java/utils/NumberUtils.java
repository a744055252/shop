package utils;

import java.math.BigDecimal;

public abstract class NumberUtils {

    public static Double format_00(Double d){
        if (d == null) {
            d = 0D;
        }
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
