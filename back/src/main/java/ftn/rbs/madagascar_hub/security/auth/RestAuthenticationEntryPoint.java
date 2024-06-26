package ftn.rbs.madagascar_hub.security.auth;

import java.io.IOException;
import java.util.List;

import ftn.rbs.madagascar_hub.exceptions.NotValidAclException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
    	System.out.println(authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
	
    @ExceptionHandler (value = {AccessDeniedException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 403
        setResponseError(response, HttpServletResponse.SC_FORBIDDEN, String.format("Access Denies: %s", accessDeniedException.getMessage()));
    }
    
    @ExceptionHandler (value = {NotFoundException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, NotFoundException notFoundException) throws IOException {
        // 404
        setResponseError(response, HttpServletResponse.SC_NOT_FOUND, String.format("Not found: %s", notFoundException.getMessage()));
    }

    @ExceptionHandler(NotValidAclException.class)
    protected ResponseEntity<Object> handleNotValidAclException(NotValidAclException e){
        return new ResponseEntity<>("Not valid ACL!", HttpStatus.BAD_REQUEST);
    }
      
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler (value = {HttpMessageNotReadableException.class})
	protected ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return new ResponseEntity<String>("Request body is required.", HttpStatus.BAD_REQUEST);
	}
    

	
	@ExceptionHandler (value = {MethodArgumentNotValidException.class})
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder("Request finished with validation errors: \n");

        for (ObjectError error : errorList ) {
        	sb.append("Field ");
            FieldError fe = (FieldError) error;
            sb.append(fe.getField() + " ");
            sb.append(error.getDefaultMessage()+ "!\n\n");
        }

        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
	}
    
    private void setResponseError(HttpServletResponse response, int errorCode, String errorMessage) throws IOException{
        response.setStatus(errorCode);
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
        response.getWriter().close();
    }
}

