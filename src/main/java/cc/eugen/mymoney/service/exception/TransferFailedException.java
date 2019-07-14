package cc.eugen.mymoney.service.exception;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class TransferFailedException extends RuntimeException {


    public TransferFailedException(String message){
        super(message);
    }
}
