package pessoa.service.restinterface;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pessoa.service.exception.CamposInvalidosException;
import pessoa.service.exception.OperacaoNaoPermitidaException;
import pessoa.service.exception.RegistroNaoEncontradoException;

public abstract class AbstractController<L, T> {

	@ResponseBody
    @ExceptionHandler(value = {RegistroNaoEncontradoException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String retornaRegistroNaoEncontrado(Exception ex) {
        return ex.toString();
    }
	
	@ResponseBody
    @ExceptionHandler(value = {OperacaoNaoPermitidaException.class})
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public String retornaOperacaoNaoPermitida(Exception ex) {
        return ex.toString();
    }
    
    @ResponseBody
    @ExceptionHandler(value = {CamposInvalidosException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String retornaRequestInvalido(Exception ex) {
        return ex.toString();
    }
    
    @ResponseBody
    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String retornaErroInesperado(Exception ex) {
        return ex.toString();
    }
    
    protected abstract L adicionaListaLinksPermitidos(L lista);
    
    protected abstract T adicionaLinksPermitidos(T item);
    
}
