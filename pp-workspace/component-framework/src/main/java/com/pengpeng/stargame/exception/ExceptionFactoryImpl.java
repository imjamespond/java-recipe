package com.pengpeng.stargame.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.pengpeng.stargame.IMessageResource;

import java.util.Locale;

@Component
public class ExceptionFactoryImpl implements IExceptionFactory {
	@Autowired
	private MessageSource message;

	
	public void throwException(String key ,Object[] value) throws GameException {
        try{
    		String msg = message.getMessage(key, value, Locale.CHINA);
            throw new GameException(msg);
        }catch(NoSuchMessageException e){
            throw new GameException(key);
        }
	}

	public void throwResourceExhaustedException(String key, Object[] value) throws ResourceExhaustedException {
        try{
            String msg = message.getMessage(key, value, Locale.CHINA);
		    throw new ResourceExhaustedException(msg);
        }catch(NoSuchMessageException e){
            throw new ResourceExhaustedException(key);
        }
    }
    @Override
    public void throwRuleException(String key) throws RuleException {
        try{
        String msg = message.getMessage(key, null, Locale.CHINA);
        throw new RuleException(msg);
        }catch (NoSuchMessageException e){
            throw new RuleException(key);
        }
    }

    @Override
    public void throwAlertException(String key) throws AlertException {
        try{
        String msg = message.getMessage(key, null, Locale.CHINA);
        throw new AlertException(msg);
        }catch (NoSuchMessageException e){
            throw  new AlertException(key);
        }
    }

	@Override
	public void throwCanNotFoundException(String key) throws CanNotFoundException {
        try{
        String msg = message.getMessage(key, null, Locale.CHINA);
		throw new CanNotFoundException(msg);
        }catch (NoSuchMessageException e){
            throw new CanNotFoundException(key);
        }
	}


    @Override
    public void throwRuleException(String key,Object[] value) throws RuleException {
        try{
        String msg = message.getMessage(key, value, Locale.CHINA);
        throw new RuleException(msg);
        }catch (NoSuchMessageException e){
            throw new RuleException(key);
        }
    }

    @Override
    public void throwAlertException(String key,Object[] value) throws AlertException {
        try{
        String msg = message.getMessage(key, value, Locale.CHINA);
        throw new AlertException(msg);
        }catch (NoSuchMessageException e){
            throw new AlertException(key);
        }
    }

    @Override
    public void throwCanNotFoundException(String key,Object[] value) throws CanNotFoundException {
        try{
        String msg = message.getMessage(key, value, Locale.CHINA);
        throw new CanNotFoundException(msg);
        }catch (NoSuchMessageException e){
            throw new CanNotFoundException(key);
        }
    }

    @Override
    public void throwTxtException(String msg) throws AlertException {
        throw new AlertException(msg);
    }
}
