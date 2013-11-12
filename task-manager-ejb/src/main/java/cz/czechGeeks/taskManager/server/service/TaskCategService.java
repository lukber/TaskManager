package cz.czechGeeks.taskManager.server.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;
import cz.czechGeeks.taskManager.server.model.TaskCateg;

/**
 * Podpora pro {@link TaskCateg}
 * 
 * @author lukasb
 * 
 */
@Singleton
public class TaskCategService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	/**
	 * Vrati vsechny dostupne kategorie
	 * 
	 * @return
	 */
	public List<TaskCateg> getAll() {
		return dao.getAll(TaskCateg.class);
	}

	/**
	 * Najde kategorii dle predaneho parametru. Kategorie musi existovat jinak je vyhozena vyjimka.
	 * 
	 * @param id
	 *            identifikator kategorie
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public TaskCateg get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(TaskCateg.class, id);
	}

	/**
	 * Zalozeni nove kategorie. DB omezeni - nazev kategorie musi byt unikatni
	 * 
	 * @param categName
	 *            Nazev nove kategorie
	 * @return Nove zalozena kategorie
	 */
	public TaskCateg insert(String categName) {
		TaskCateg categ = new TaskCateg();
		categ.setName(categName);
		dao.persist(categ);
		return categ;
	}

	/**
	 * Aktualizace kategorie. DB omezeni - nazev kategorie musi byt unikatni
	 * 
	 * @param id
	 *            identifikator upravovane kategorie
	 * @param categName
	 *            novy nazev kategorie
	 * @return upravena kategorie
	 * @throws EntityNotFoundException
	 *             kategorie nebyla nalezena
	 * @throws IllegalStateException
	 *             kategorii neni mozne upravovat {@link TaskCategService#isUpdatable(Long)}
	 */
	public TaskCateg update(Long id, String categName) throws EntityNotFoundException {
		if (!isUpdatable(id)) {
			throw new IllegalStateException("Entitu nelze upravovat");
		}

		TaskCateg categ = dao.findNonNull(TaskCateg.class, id);
		categ.setName(categName);
		dao.merge(categ);
		dao.refresh(categ);
		return categ;
	}

	/**
	 * Odstraneni kategorie
	 * 
	 * @param id
	 * @throws EntityNotFoundException
	 *             kategorie nebyla nalezena
	 * @throws IllegalStateException
	 *             kategorii neni mozne odstranit {@link TaskCategService#isDeleteable(Long)}
	 */
	public void delete(Long id) throws EntityNotFoundException {
		if (!isDeleteable(id)) {
			throw new IllegalStateException("Entitu nelze smazat");
		}

		TaskCateg entity = get(id);
		dao.remove(entity);
	}

	/**
	 * Priznak zda je mozne upravovat kategorii
	 * 
	 * @param id
	 *            kategorie ID
	 * @return TRUE - pokud neexistuji zadne TASKy pro tuto kategorii
	 */
	public boolean isUpdatable(Long id) {
		return !hasTasks(id);
	}

	/**
	 * Priznak zda je mozne odstranit kategorii
	 * 
	 * @param id
	 *            - kategorie ID
	 * @return TRUE - pokud je mozne upravovat
	 */
	public boolean isDeleteable(Long id) {
		return !hasTasks(id);
	}

	/**
	 * Priznak jestli kategorie ma nejake tasky
	 */
	private boolean hasTasks(Long id) {
		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Task> root = query.from(Task.class);

		query.select(builder.count(root));
		query.where(builder.equal(root.get("categId"), id));

		return entityManager.createQuery(query).getSingleResult() > 0;
	}

}
