package next.controller.qna;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

@Controller
@RequestMapping("/qna")
public class QuestionController {
	QuestionDao qd = QuestionDao.getInstance();
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show (long questionId){
		ModelAndView mav = new ModelAndView("qna/show");
		mav.addObject("question", qd.findById(questionId));
		return mav;
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView createForm (){
		ModelAndView mav = new ModelAndView("qna/form");
		return mav;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(String title, String contents, HttpSession session){
		User user = UserSessionUtils.getUserFromSession(session);
		Question question = new Question(user.getUserId(), title, contents);
		qd.insert(question);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpSession session, long questionId){
		Question question = qd.findById(questionId);
		ModelAndView mav = new ModelAndView("qna/update");
		return mav.addObject("question", question);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpSession session, long questionId, String title, String contents){
		User user = UserSessionUtils.getUserFromSession(session);
		Question question = new Question(questionId, user.getUserId(), title, contents, new Date(), 0);
		qd.update(question);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteQuestion(HttpSession session, long questionId){
		qd.delete(questionId);
		return "redirect:/";
	}
	
}
