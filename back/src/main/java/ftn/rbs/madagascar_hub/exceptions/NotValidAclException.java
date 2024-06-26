package ftn.rbs.madagascar_hub.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not valid ACL!")
public class NotValidAclException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6212178527151597983L;
}

