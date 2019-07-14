package cc.eugen.mymoney.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Getter
@Setter
@ToString
public class Transfer implements Serializable {

    private long sender;
    private long receiver;
    private double amount;
}
