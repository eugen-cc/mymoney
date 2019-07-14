package cc.eugen.mymoney.model.dao.api;

import cc.eugen.mymoney.model.entity.Account;

import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface AccountDAO {

    Optional<Account> findById(Long accountId);

    Optional<List<Account>> findAll();

    Account save(Account account);
}
