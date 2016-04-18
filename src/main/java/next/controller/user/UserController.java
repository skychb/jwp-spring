package next.controller.user;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class UserController {
	private UserDao userDao = UserDao.getInstance();

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String userId, String password, HttpSession session, Model model) {
		User user = userDao.findByUserId(userId);
		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}
		if (user.matchPassword(password)) {
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
			return "redirect:/";
		} else {
			throw new IllegalStateException("비밀번호가 틀립니다.");
		}
	}

	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String loginForm() {
		return "/user/login";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String form(Model model) {
		model.addAttribute("user", new User());
		return "/user/form";
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(Model model) throws SQLException {
		model.addAttribute("users", userDao.findAll());
		return "/user/list";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model, String userId) {
		User user = userDao.findByUserId(userId);
		model.addAttribute("user", user);
		return "/user/profile";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(String userId, String password, String name, String mail) {
		User user = new User(userId, password, name, mail);
		userDao.insert(user);
		return "redirect:/";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String updateForm(String userId, Model model, HttpSession session) {
		User user = userDao.findByUserId(userId);
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		model.addAttribute("user", user);
		return "/user/updateForm";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String userId, String password, String name, String mail, HttpSession session) {
		User user = new User(userId, password, name, mail);
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		userDao.update(user);
		return "redirect:/";
	}

}
