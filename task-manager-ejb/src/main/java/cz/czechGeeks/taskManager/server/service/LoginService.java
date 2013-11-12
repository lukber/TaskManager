package cz.czechGeeks.taskManager.server.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Login;

/**
 * Podpora pro {@link Login}
 * 
 * @author lukasb
 * 
 */
@Singleton
public class LoginService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	/**
	 * Vrati vsechny dostupne uzivatele
	 * 
	 * @return
	 */
	public List<Login> getAll() {
		return dao.getAll(Login.class);
	}

	/**
	 * Najde uzivatele dle predaneho parametru. Uzivatel musi existovat jinak je vyhozena vyjimka.
	 * 
	 * @param id
	 *            identifikator hledaneho uzivatele
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public Login get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(Login.class, id);
	}

	/**
	 * Nalezeni ID uzivatele dle predaneho parametru. Uzivatel musi existovat jinak je vyhozena vyjimka.
	 * 
	 * @param userName
	 *            prihlasovaci jmeno uzivatele
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public Long getId(String userName) throws EntityNotFoundException {
		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Login> root = query.from(Login.class);

		query.select(root.<Long> get("id"));
		query.where(builder.equal(root.get("userName"), userName));

		try {
			return entityManager.createQuery(query).getSingleResult();
		} catch (NoResultException e) {
			throw new EntityNotFoundException(Login.class, userName);
		}
	}
}
