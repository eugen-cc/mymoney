package cc.eugen.mymoney.service.exception;

/**
 * @author Eugen Gross
 * @since 07/18/2019
 **/
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
