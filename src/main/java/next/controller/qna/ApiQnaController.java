package next.controller.qna;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;
import next.model.User;
import next.service.QnaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import core.jdbc.DataAccessException;

@RestController
@RequestMapping("/api/questions")
public class ApiQnaController {
	
	private QuestionDao questionDao = QuestionDao.getInstance();
	private AnswerDao answerDao = AnswerDao.getInstance();
	private QnaService qnaService = QnaService.getInstance();
	
	@RequestMapping(value="/{questionId}", method=RequestMethod.DELETE)
	public Result deleteQuestion(HttpSession session, @PathVariable long questionId) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return Result.fail("Login is required");
		}
		
	
			qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(session));
			return Result.ok();
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Question> list() throws Exception {
		return questionDao.findAll();
	}
	
	@RequestMapping(value = "/{questionId}/answers", method = RequestMethod.POST)
	public Map<String, Object> addAnswer(HttpSession session, @PathVariable long questionId, String contents) throws Exception {
    	Map<String, Object> values = Maps.newHashMap();
		if (!UserSessionUtils.isLogined(session)) {
			values.put("result", Result.fail("Login is required"));
			return values;
		}
    	
    	User loginUser = UserSessionUtils.getUserFromSession(session);
    	Answer answer = new Answer(loginUser.getUserId(), contents, questionId);
    	Answer savedAnswer = answerDao.insert(answer);
		questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
		
		values.put("answer", savedAnswer);
		values.put("result", Result.ok());
		return values;
	}
	
	@RequestMapping(value = "/{questionId}/answers/{answerId}", method = RequestMethod.DELETE)
	public Result deleteAnswer(HttpSession session, @PathVariable long answerId) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return Result.fail("Login is required");
		}
		
		Answer answer = answerDao.findById(answerId);
		
		
		try {
			answerDao.delete(answerId);
			return Result.ok();
		} catch (DataAccessException e) {
			return Result.fail(e.getMessage());
		}
	}
}