package cc.eugen.mymoney.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Getter
@Setter
@Builder
@ToString
public class TransferDTO implements Serializable {

    @NonNull
    private long sender;
    @NonNull
    private long receiver;
    @NonNull
    private double amount;
}
