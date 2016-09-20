package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.player.Mail;
import com.pengpeng.stargame.model.player.MailInfo;
import com.pengpeng.stargame.model.player.MailPlus;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29上午11:32
 */
@Component()
public class RspMailFactory extends RspFactory {

    public MailPlusVO[] mailList(List<MailPlusVO> list) {
        MailPlusVO[] mailPlusVOs = new MailPlusVO[list.size()];
        list.toArray(mailPlusVOs);
        return mailPlusVOs;
    }


}
