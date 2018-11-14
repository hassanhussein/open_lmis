package org.openlmis.core.exception;


import lombok.Getter;
import org.openlmis.core.dto.MessageResponseDTO;

public class CustomDataException extends RuntimeException {

    private MessageResponseDTO responseDTO;
    @Getter
    private String message;
    public CustomDataException() {
        super();
    }
    public CustomDataException(String s) {
        super(s);
        this.message = s;
    }
    public CustomDataException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public CustomDataException(Throwable throwable) {
        super(throwable);
    }
    @Override
    public String toString(){
        return message;
    }
   /* public  MessageResponseDTO getResponseDTO(){
        return responseDTO;
    }
*/
}
