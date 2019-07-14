package cc.eugen.mymoney.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Getter
@Setter
@Entity
@ToString
@NamedQueries(
        @NamedQuery(
                name = Exchange.FIND_ALL,
                query = Exchange.FIND_ALL_QUERY
        )
)
public class Exchange extends AbstractEntity {

    public static final String FIND_ALL = "Exchange.retrieveAll";
    static final String FIND_ALL_QUERY = "SELECT e FROM Exchange e";

    @Column
    private BigDecimal amount;

    @Column
    private Currency external;

    @Column
    private Currency internal;

    @Column
    private BigDecimal rate;

    @Column
    private BigDecimal fee;

    @Transient
    private BigDecimal value;
}
