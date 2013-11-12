package cz.czechGeeks.taskManager.server.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;

/**
 * Podpora pro {@link Task}
 * 
 * @author lukasb
 * 
 */
@Singleton
public class TaskService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	/**
	 * Vrati vsechny ukoly pro daneho uzivatele.
	 * 
	 * @param loginId
	 *            ID uzivatele
	 * @param categId
	 *            filtr pro omezeni zaznamu pouze na urcite kategorie
	 * @param finishToDate
	 *            filtr pro omezeni zaznamu pouze na ukoly, ktere maji zadan datum dokonceni a tento datum je vetsi nez datum z parametru
	 * @return
	 * @throws IllegalStateException
	 *             loginID = null
	 */
	public List<Task> getAll(Long loginId, Long categId, Timestamp finishToDate) {
		if (loginId == null) {
			throw new IllegalArgumentException("Je potreba zadat alespon jeden parametr");
		}

		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Task> query = builder.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.or(builder.equal(root.get("executorId"), loginId), builder.equal(root.get("inserterId"), loginId)));

		if (categId != null) {
			predicates.add(builder.equal(root.get("categId"), categId));
		}
		if (finishToDate != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.<Timestamp> get("finishToDate"), finishToDate));
		}

		query.select(root);
		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

	/**
	 * Najde ukol dle predaneho parametru. Ukol musi existovat jinak je vyhozena vyjimka.
	 * 
	 * @param id
	 *            identifikator ukolu
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public Task get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(Task.class, id);
	}

	/**
	 * Vlozeni noveho ukolu.
	 * 
	 * @param categId
	 *            ID kategorie
	 * @param executorId
	 *            ID uzivatele ktery ma ukol vypracovat
	 * @param inserterId
	 *            ID uzivatele ktery zaznam zalozil
	 * @param name
	 *            Nazev ukolu
	 * @param desc
	 *            Popis ukolu
	 * @param finishToDate
	 *            Datum a cas do kdy ma byt ukol udelan
	 * @return nove zalozeny ukol
	 */
	public Task insert(Long categId, Long executorId, Long inserterId, String name, String desc, Timestamp finishToDate) {
		Task categ = new Task();

		categ.setCategId(categId);
		categ.setExecutorId(executorId);
		categ.setInserterId(inserterId);
		categ.setName(name);
		categ.setDesc(desc);
		categ.setFinishToDate(finishToDate);

		dao.persist(categ);
		return categ;
	}

	/**
	 * Uprava ukolu
	 * 
	 * @param id
	 *            ID ukolu ktery ma byt upraven
	 * @param loginId
	 *            ID uzivatele ktery aktualizaci provadi
	 * @param categId
	 *            ID kategorie
	 * @param executorId
	 *            ID uzivatele ktery ma ukol vypracovat
	 * @param inserterId
	 *            ID uzivatele ktery zaznam zalozil
	 * @param name
	 *            Nazev ukolu
	 * @param desc
	 *            Popis ukolu
	 * @param finishToDate
	 *            Datum a cas do kdy ma byt ukol udelan
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public Task update(Long id, Long loginId, Long categId, Long executorId, String name, String desc, Timestamp finishToDate) throws EntityNotFoundException {
		if (!isUpdatable(id, loginId)) {
			throw new IllegalStateException("Entitu nelze upravovat");
		}

		Task categ = dao.findNonNull(Task.class, id);

		categ.setCategId(categId);
		categ.setExecutorId(executorId);
		categ.setName(name);
		categ.setDesc(desc);
		categ.setFinishToDate(finishToDate);

		dao.merge(categ);
		dao.refresh(categ);
		return categ;
	}

	/**
	 * Odstraneni ukolu
	 * 
	 * @param id
	 *            ID ukolu
	 * @param loginId
	 *            ID uzivatele ktery odstraneni provadi
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public void delete(Long id, Long loginId) throws EntityNotFoundException {
		if (!isDeleteable(id, loginId)) {
			throw new IllegalStateException("Entitu nelze smazat");
		}

		Task entity = dao.findNonNull(Task.class, id);
		dao.remove(entity);
	}

	/**
	 * Priznak jestli je mozno upravovat ukol
	 * 
	 * @param id
	 *            ID ukolu
	 * @param forLoginId
	 *            ID uzivatele pro ktereho se ma vyhodnotit
	 * @return TRUE - uzivatel muze upravovat ukol pouze pokud ho sam zalozil
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public boolean isUpdatable(Long id, Long forLoginId) throws EntityNotFoundException {
		Task entity = dao.findNonNull(Task.class, id);
		return entity.getInserterId().equals(forLoginId);
	}

	/**
	 * Priznak jestli je mozno smazat ukol
	 * 
	 * @param id
	 *            ID ukolu
	 * @param forLoginId
	 *            ID uzivatele pro ktereho se ma vyhodnotit
	 * @return TRUE - uzivatel muze smazat ukol pouze pokud ho sam zalozil
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public boolean isDeleteable(Long id, Long forLoginId) throws EntityNotFoundException {
		Task entity = dao.findNonNull(Task.class, id);
		return entity.getInserterId().equals(forLoginId);
	}

}
