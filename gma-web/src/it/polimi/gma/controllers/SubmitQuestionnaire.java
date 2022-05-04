package it.polimi.gma.controllers;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.gma.entities.Answer;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.Question;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.enums.Sex;
import it.polimi.gma.exceptions.OffensiveWordException;
import it.polimi.gma.services.ConsumerHomeService;
import it.polimi.gma.services.QuestionnaireCompilerService;
import it.polimi.gma.webutils.AuthenticationChecker;

@WebServlet
public class SubmitQuestionnaire extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.gma.services.QuestionnaireCompilerService")
	private QuestionnaireCompilerService qcs;
	
	@EJB(name = "it.polimi.gma.services.ConsumerHomeService")
	private ConsumerHomeService chs;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(true || request.getParameter("cancel") == "1") {
			HttpSession session = request.getSession();
			try {
				qcs.cancelQuestionnaire((Consumer)session.getAttribute("user"), 
						(LocalDateTime)session.getAttribute("loginTime"), 
						(Questionnaire)session.getAttribute("questionnaire"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		response.sendRedirect(request.getContextPath() + "/consumerHome"); 
		}
		else {
			throw new ServletException("Invalid request params for cancel (GET)");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.CONSUMER)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
    	
		Questionnaire q = (Questionnaire)request.getSession().getAttribute("questionnaire");
		
		Consumer c = (Consumer)request.getSession().getAttribute("user");
		
		if(qcs.couldSubmit(c)) { // if the user has not submitted any other questionnaires for today
			List<Question> questions = (List<Question>)request.getSession().getAttribute("questions");
			
			Set<Answer> answers = new HashSet<Answer>();
			
			for(String paramName : request.getParameterMap().keySet()) {
				if(paramName.length() > 8 && paramName.substring(0, 8).equals("question")) {
					Answer a = new Answer();
					a.setUser(c);
					a.setQuestionnaire(q);
					a.setValue(request.getParameter(paramName));
					int id = Integer.valueOf(paramName.substring(8));
					q.getAnswers().add(a);
					addQuestionToAnswer(a, questions, id);
					answers.add(a);
				}
			}
					
			if(request.getParameter("age") != null && request.getParameter("age").equals("true")) {
				try {
					int age = c.getAge();
					Answer a = new Answer();
					a.setUser(c);
					a.setQuestionnaire(q);
					a.setValue(String.valueOf(age));
					q.getAnswers().add(a);
					addQuestionToAnswer(a, questions, 1); // 1 is the id of the question of the age
					answers.add(a);
				} catch (Exception e) {
					// do nothing
				}
			}
			if(request.getParameter("sex") != null && request.getParameter("sex").equals("true")) {
				Sex sex = c.getSex();
				if (sex != null) {
					Answer a = new Answer();
					a.setUser(c);
					a.setQuestionnaire(q);
					a.setValue(String.valueOf(sex));
					q.getAnswers().add(a);
					addQuestionToAnswer(a, questions, 2); // 2 is the id of the question of the sex
					answers.add(a);
				}
			}
			
			if(request.getParameter("expertise") != null) {
				String expertise = request.getParameter("expertise");
				Answer a = new Answer();
				a.setUser(c);
				a.setQuestionnaire(q);
				a.setValue(expertise);
				q.getAnswers().add(a);
				addQuestionToAnswer(a, questions, 3); // 3 is the id of the question of the expertise
				answers.add(a);
			}
			try {
				qcs.persistAnswers(answers);
				request.getSession().setAttribute("canSubmit", false);
			} catch (OffensiveWordException e) {
				((Consumer)request.getSession().getAttribute("user")).setBanned(true);
				qcs.banUser(((Consumer)request.getSession().getAttribute("user")).getUsername());
			}
		}
		response.sendRedirect(request.getContextPath() + "/consumerHome"); 	
	}
	
	private void addQuestionToAnswer(Answer a, List<Question> questions, int questionId) throws ServletException {
		List<Integer> ids = new ArrayList<Integer>();
		for(Question elem : questions) {
			ids.add(elem.getIdQuestion());
			if (elem.getIdQuestion() == questionId) {
				a.setQuestion(elem);
				elem.getAnswers().add(a);
				return;
			}
		}
		throw new ServletException("Did not find any question (size: " + String.valueOf(questions.size()) + ", IDs: "+ids.toString()+").");
	}
}
