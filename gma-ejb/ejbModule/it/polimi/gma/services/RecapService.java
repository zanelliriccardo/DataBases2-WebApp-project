package it.polimi.gma.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.polimi.gma.entities.Answer;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.Questionnaire;
/**
 * Session Bean implementation class InspectionService
 */
@Stateless
public class RecapService {
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * Default constructor. 
     */
    public RecapService() {
        // TODO Auto-generated constructor stub
    }
	public Questionnaire getInformation(String string) throws ParseException {
		// TODO Auto-generated method stub
		Questionnaire q=em.find(Questionnaire.class, (Date)sdf.parse(string));
		return q;
	}
}
