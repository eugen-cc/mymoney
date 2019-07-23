package cc.eugen.mymoney.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/


@NamedQueries(
    @NamedQuery(
            name = Account.FIND_ALL,
            query = Account.FIND_ALL_QUERY
    )
)
@Getter
@Setter
@Entity
@ToString
@Cacheable(false)
public class Account extends AbstractEntity {

    public static final String FIND_ALL = "Account.retrieveAll";
    static final String FIND_ALL_QUERY = "SELECT a FROM Account a";

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal overdraft;

}