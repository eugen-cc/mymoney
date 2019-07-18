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
 * @since 07/17/2019
 **/
@Getter
@Setter
@Builder
@ToString
public class AccountDTO implements Serializable {

    private long id;
    @NonNull
    private double balance;
    @NonNull
    private Currency currency;
}
