package com.pengpeng.admin.stargame.manager;

import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-12-6
 * Time: 上午10:12
 */
public interface IFamilyAssistantManager {
    void election() throws NotFoundBeanException, BeanAreadyException;
}
