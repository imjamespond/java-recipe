package com.pengpeng.stargame.exception;

public interface IExceptionFactory {
	public void throwException(String key, Object[] value) throws GameException;
	public void throwResourceExhaustedException(String key, Object[] value) throws ResourceExhaustedException;

    public void throwRuleException(String key)throws RuleException;
    public void throwAlertException(String key) throws AlertException;
    public void throwCanNotFoundException(String key) throws CanNotFoundException;

    public void throwRuleException(String key,Object[] value) throws RuleException;
    public void throwAlertException(String key,Object[] value) throws AlertException;
    public void throwCanNotFoundException(String key,Object[] value) throws CanNotFoundException;

    void throwTxtException(String value)throws AlertException;;
}
