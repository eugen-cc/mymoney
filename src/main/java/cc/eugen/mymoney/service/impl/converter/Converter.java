package cc.eugen.mymoney.service.impl.converter;

import com.google.gson.Gson;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class Converter {

    private final Gson gson = new Gson();

    public <T> String convert(T t) {
        return gson.toJson(t);
    }

    public <T> T convert(String s, Class<T> tClass) {
        return gson.fromJson(s, tClass );
    }

}
