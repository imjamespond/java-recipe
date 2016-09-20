package com.test.qianxun.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qianxun.model.job.HitJob;
import com.qianxun.service.JobService;
import com.test.qianxun.model.Comment;
import com.test.qianxun.model.Reply;
import com.test.qianxun.model.Session;
import com.test.qianxun.model.Topic;
import com.test.qianxun.service.CommentService;
import com.test.qianxun.service.ReplyService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.TopicService;
import com.test.qianxun.util.MimeUtils;

@Controller
@RequestMapping(value = "/topic")
public class TopicController {
	@Autowired
	private TopicService topicService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private JobService jobService;
	@Value("#{properties['upload.folder']}")
	private String uploadFolder;
	@Value("#{properties['main.http']}")
	private String mainUrl;
	private int limit = 20;
	private String blankRegex = "^\\s*$";
	
	@RequestMapping("/index")
	public String index(Model model) {
		int total1 = topicService.countByStype(1);
		int total2 = topicService.countByStype(2);
		int total3 = topicService.countByStype(3);
		model.addAttribute("total1", total1);
		model.addAttribute("total2", total2);
		model.addAttribute("total3", total3);
		return "topic/index";
	}

	/**分类标题列表
	 * @param stype
	 * @param model
	 * @return
	 */
	@RequestMapping("/{stype}")
	public String list(@PathVariable int stype, Model model) {
		return list(stype, 1, model);
	}
	@RequestMapping("/{stype}/{number}")
	public String list(@PathVariable int stype, @PathVariable int number,
			Model model) {
		Page page = new Page(number, limit);
		List<Topic> topicList = topicService.listByStype(stype, page);
		List<Topic> topList = topicService.listTop(stype, 5);
		model.addAttribute("topicList", topicList);
		model.addAttribute("topList", topList);
		model.addAttribute("stype", stype);
		model.addAttribute("page", page);
		return "topic/list";
	}

	/**回复跟贴列表
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/show/{id}")
	public String show(@PathVariable long id, Model model) {
		return show(id, 1, model);
	}
	@RequestMapping("/show/{id}/{number}")
	public String show(@PathVariable long id, @PathVariable int number,
			Model model) {
		//贴子标题
		Topic topic = topicService.get(id);
		boolean isAuthor = false;
		Session session = sessionService.get();
		if(session != null){
			long uid = Long.parseLong(sessionService.getUid());
			if(uid == topic.getUid()){
				isAuthor = true;
			}
		}
		
		//TODO 增加hit rate
		if(!isAuthor){
			jobService.produce(new HitJob(id));
		}
		
		Page page = new Page(number, limit);
		//回复列表
		List<Reply> replyList = replyService.listByTid(id, page);
		for (Reply reply : replyList) {
			//跟帖列表
			List<Comment> commentList = commentService.listByRid(reply.getId());
			reply.setCommentList(commentList);
		}
		model.addAttribute("mainUrl", mainUrl);
		model.addAttribute("page", page);
		model.addAttribute("topic", topic);
		model.addAttribute("replyList", replyList);
		return "topic/show";
	}
	
	/**增加分类
	 * @param stype
	 * @param model
	 * @return
	 */
	@RequestMapping("/create/{stype}")
	public String create( @PathVariable int stype, Model model) {
		model.addAttribute("stype", stype);
		return "topic/create";
	}
	
	/**保存话题
	 * @param topic
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(Topic topic) throws UnsupportedEncodingException{
		Session session = sessionService.get();
		topic.setTitle( topic.getTitle().replaceAll("\\<.*?\\>", ""));
		String title = topic.getTitle();	
		String content = topic.getContent();
		if(title == null || title.isEmpty() || Pattern.matches(blankRegex, title)){
			return "1";
		}else if(title.length() > 50){
			return "2";
		}else if(content == null || content.isEmpty() || Pattern.matches(blankRegex, content)){
			return "3";
		}else{
			long uid = Long.parseLong(sessionService.getUid());
			String username = session.getUsername();
			topic.setUid(uid);
			topic.setTop(0);
			topic.setUsername(username);
			topic.setLastReply(username);
			topic.setCreateTime(System.currentTimeMillis());
			topic.setReplyTime(System.currentTimeMillis());
			long id = topicService.save(topic);
			return "/topic/show/" + id;
		}
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestParam("upfile") MultipartFile multipartFile){
		String type = multipartFile.getContentType();
		type = MimeUtils.getImageType(type);
		String url = "";
		String state = "上传失败";
		if (type != null) {
			long time = System.currentTimeMillis();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			int year = calendar.get(Calendar.YEAR);
			File path = new File(this.uploadFolder + "/topic/" + year);
			String title = time + "." + type;
			if (!path.exists()) {
				path.mkdirs();
			}
			try {
				File file = new File(path, title);
				multipartFile.transferTo(file);
				
				url = "/upload/topic/" + year + "/" + title;
				state = "SUCCESS";
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		String content = "<script>parent.UM.getEditor('content').getWidgetCallback('image')('"
				+ url + "','" + state + "')</script>";

		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("text", "html",
				Charset.forName("utf-8"));
		headers.setContentType(mediaType);
		return new ResponseEntity<String>(content, headers, HttpStatus.OK);
	}
}