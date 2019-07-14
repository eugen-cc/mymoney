package cc.eugen.mymoney.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@NamedQueries(
        @NamedQuery(
                name = Transaction.FIND_ALL,
                query = Transaction.FIND_ALL_QUERY
        )
)
@Getter
@Setter
@Entity
public class Transaction extends AbstractEntity {

    public static final String FIND_ALL = "Transaction.retrieveAll";
    static final String FIND_ALL_QUERY = "SELECT t FROM Transaction t";

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private Boolean completed;

}
