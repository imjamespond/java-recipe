package com.pengpeng.admin.stargame.action;

import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.model.player.MailConstant;
import com.pengpeng.stargame.rpc.MailRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.BaseRewardVO;
import com.pengpeng.stargame.vo.role.MailAttachmentVO;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailReq;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/bulletin")
public class BulletinAction {

    @Autowired
    private MailRpcRemote mailRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Autowired
    private HibernateTemplate template;

    /**
     * list
     */
    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("player/bulletinViewList");
    }

    /**
     * ajax list
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Page<Bulletin> list(@RequestParam(value = "fid", defaultValue = "") String fid, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MailPlusVO[] mailPlusVOs = null;
        int uid = Integer.valueOf(request.getParameter("uid"));
        if(uid == 0){
            mailPlusVOs = mailRpcRemote.getBulletin(null, null);
        }else{
            String pid = playerRpcRemote.getPid(null,uid);
            Session session = new Session(pid,null);
            mailPlusVOs = mailRpcRemote.getMailList(session,null);
        }
        List<Bulletin> list = new ArrayList<Bulletin>();
        for (MailPlusVO vo : mailPlusVOs) {
            Bulletin bulletin = new Bulletin();
            bulletin.id = vo.getId();
            bulletin.title = vo.getTitle();
            bulletin.content = vo.getContent();
            if (null != vo.getAttachmentVOList()) {
                StringBuilder attachment = new StringBuilder();
                for (BaseRewardVO avo : vo.getAttachmentVOList()) {
                    attachment.append("类型:");
                    attachment.append(BaseRewardConstant.toString(avo.getType()));
                    attachment.append("&nbsp&nbsp物品:");
                    attachment.append(avo.getItemName());
                    attachment.append("&nbsp&nbsp数量:");
                    attachment.append(avo.getNum());
                    attachment.append(",</br>");
                }
                bulletin.attachment = attachment.toString();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            bulletin.date = sdf.format(vo.getCreateDate());

            list.add(bulletin);
        }
        Page<Bulletin> page = new Page<Bulletin>();

        page.setRows(list);
        return page;
    }

    @RequestMapping(value = "/sendMail", method = {RequestMethod.POST})
    public
    @ResponseBody
    ResResult sendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {

        MailReq req = new MailReq();
        req.setAttachments(request.getParameter("attachment"));
        req.setTitle(request.getParameter("title"));
        req.setContent(request.getParameter("content"));
        req.setType(MailConstant.TYPE_BULLETIN);
        mailRpcRemote.send(null, req);
        return ResResult.newOk();
    }

    @RequestMapping(value = "/remove", method = {RequestMethod.POST})
    public
    @ResponseBody
    ResResult remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MailReq req = new MailReq();
        req.setMailId(Long.valueOf(request.getParameter("id")));
        req.setType(MailConstant.TYPE_BULLETIN);
        mailRpcRemote.remove(null, req);
        return ResResult.newOk();
    }

    @RequestMapping(value = "/loadTypes", method = {RequestMethod.GET})
    public
    @ResponseBody
    Map<String,String> loadTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Field[] fields = BaseRewardConstant.class.getDeclaredFields();
        Map<String,String> map = new HashMap<String,String>();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAccessible()) {
                map.put(f.getName(),String.valueOf( f.getInt(null)));
        }
        }
        return map;
    }

    private class Bulletin {
        public Long id;
        public String title;
        public String content;
        public String attachment;
        public String date;

        public Bulletin() {
        }
    }

}


