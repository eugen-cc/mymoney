package cc.eugen.mymoney.model.dto;

import cc.eugen.mymoney.model.entity.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Eugen Gross
 * @since 07/18/2019
 **/
@Getter
@Setter
@ToString
@Builder
public class TransactionDTO implements Serializable {

    private long id;
    @NonNull
    private long sender;
    @NonNull
    private long receiver;
    @NonNull
    private double amount;
    @NonNull
    private Currency currency;
    private boolean completed;

}
