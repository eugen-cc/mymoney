package cc.eugen.mymoney.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Getter
@Builder
public class GenericResponse<T> implements Serializable {

    private T object;
    private int statusCode;
    private String error;
}
