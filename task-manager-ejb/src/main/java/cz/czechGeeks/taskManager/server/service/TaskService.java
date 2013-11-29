package cz.czechGeeks.taskManager.server.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import cz.czechGeeks.taskManager.server.exception.EntityNotPermitedException;
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
	 * @param fromTaskId
	 *            pocatecni ID tasku od ktereho se bude vyhledavat - lze pouzit pro dohledani nove zadanych hodnot
	 * @param categId
	 *            filtr pro omezeni zaznamu pouze na urcite kategorie
	 * @param finishToDate
	 *            filtr pro omezeni zaznamu pouze na ukoly, ktere maji zadan datum dokonceni a tento datum je vetsi nez datum z parametru
	 * @param onlyUnreaded
	 *            TRUE - pouze neprectene zaznamy
	 * @return
	 * @throws IllegalStateException
	 *             loginID = null
	 */
	public List<Task> getAll(Long loginId, Long fromTaskId, Long categId, Boolean mainTasks, Boolean delegatedToMe, Boolean delegatedToOthers, Timestamp finishToDate, Boolean onlyUnreaded) {
		if (loginId == null) {
			throw new IllegalArgumentException("Je potreba zadat alespon jeden parametr");
		}

		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Task> query = builder.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (fromTaskId != null) {
			predicates.add(builder.greaterThan(root.<Long> get("id"), fromTaskId));
		}

		if (categId != null) {
			predicates.add(builder.equal(root.get("categId"), categId));
		}

		if (finishToDate != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.<Timestamp> get("finishToDate"), finishToDate));
		}

		if (onlyUnreaded != null && onlyUnreaded.booleanValue()) {
			predicates.add(builder.equal(root.<Boolean> get("unread"), true));
		}

		if (mainTasks != null && mainTasks.booleanValue()) {
			predicates.add(builder.and(builder.equal(root.get("executorId"), loginId), builder.equal(root.get("inserterId"), loginId)));
		} else if (delegatedToMe != null && delegatedToMe.booleanValue()) {
			predicates.add(builder.and(builder.equal(root.get("executorId"), loginId), builder.notEqual(root.get("inserterId"), loginId)));
		} else if (delegatedToOthers != null && delegatedToOthers.booleanValue()) {
			predicates.add(builder.and(builder.notEqual(root.get("executorId"), loginId), builder.equal(root.get("inserterId"), loginId)));
		} else {
			predicates.add(builder.or(builder.equal(root.get("executorId"), loginId), builder.equal(root.get("inserterId"), loginId)));
		}

		query.select(root);
		query.where(predicates.toArray(new Predicate[0]));
		query.orderBy(builder.asc(root.get("finishToDate")));

		return entityManager.createQuery(query).getResultList();
	}

	/**
	 * Najde ukol dle predaneho parametru. Ukol musi existovat jinak je vyhozena vyjimka.
	 * 
	 * @param id
	 *            identifikator ukolu
	 * @param forLoginId
	 * @return
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 * @throws EntityNotPermitedException
	 */
	public Task get(Long id, Long forLoginId) throws EntityNotFoundException, EntityNotPermitedException {
		if (!isDisplayable(id, forLoginId)) {
			throw new EntityNotPermitedException(Task.class, id);
		}
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
		Task task = new Task();

		task.setCategId(categId);
		task.setExecutorId(executorId);
		task.setInserterId(inserterId);
		task.setName(name);
		task.setDesc(desc);
		task.setFinishToDate(finishToDate);

		task.setInsDate(new Timestamp(new Date().getTime()));
		task.setUpdDate(new Timestamp(new Date().getTime()));

		// Neprectene to bude pokazde pro task zalozeny nekomu jinemu
		task.setUnread(!executorId.equals(inserterId));

		dao.persist(task);
		dao.refresh(task);
		return task;
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

		Task task = dao.findNonNull(Task.class, id);

		if (!task.getExecutorId().equals(executorId)) {
			// predal jsem task na nekoho jineho
			task.setUnread(true);
		}

		task.setCategId(categId);
		task.setExecutorId(executorId);
		task.setName(name);
		task.setDesc(desc);
		task.setFinishToDate(finishToDate);

		task.setUpdDate(new Timestamp(new Date().getTime()));

		dao.merge(task);
		dao.refresh(task);
		return task;
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
	 * Uzavreni ukolu
	 * 
	 * @param id
	 *            ID ukolu
	 * @param loginId
	 *            ID uzivatele ktery ukol uzavira
	 * @return uzavreny ukol
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 * 
	 */
	public Task close(Long id, Long loginId) throws EntityNotFoundException {
		if (!isCloseable(id, loginId)) {
			throw new IllegalStateException("Uzivatel nema prava k uzavreni ukolu");
		}

		Task task = dao.findNonNull(Task.class, id);
		task.setFinishedDate(new Timestamp(new Date().getTime()));
		task.setUpdDate(new Timestamp(new Date().getTime()));

		dao.persist(task);
		dao.refresh(task);
		return task;
	}

	/**
	 * Nastaveni priznaku pro ukol ze byl precteny
	 * 
	 * @param id
	 *            ID ukolu
	 * @return
	 * @throws EntityNotFoundException
	 * @throws IllegalStateException
	 *             Pokud se pokousi oznacit ukol jako precteny ten ktery ho nema zadan
	 */
	public Task markReaded(Long id, Long loginId) throws EntityNotFoundException {
		Task task = dao.findNonNull(Task.class, id);

		if (!loginId.equals(task.getExecutorId())) {
			throw new IllegalStateException("Pouze ten kdo ma ukol splnit muze ukol oznacit jako precteny");
		}
		task.setUnread(false);
		task.setUpdDate(new Timestamp(new Date().getTime()));

		dao.persist(task);
		dao.refresh(task);
		return task;
	}

	/**
	 * Priznak jestli je mozno upravovat ukol
	 * 
	 * @param id
	 *            ID ukolu
	 * @param forLoginId
	 *            ID uzivatele pro ktereho se ma vyhodnotit
	 * @return TRUE - uzivatel muze upravovat ukol pouze pokud ho sam zalozil nebo ho ma vypracovat a zaroven ukol neni uzavren
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public boolean isUpdatable(Long id, Long forLoginId) throws EntityNotFoundException {
		Task entity = dao.findNonNull(Task.class, id);
		return (entity.getInserterId().equals(forLoginId) || entity.getExecutorId().equals(forLoginId)) && entity.getFinishedDate() == null;
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

	/**
	 * Priznak jestli je mozne zobrazit danemu uzivateli
	 * 
	 * @param id
	 *            ID ukolu
	 * @param forLoginId
	 *            ID uzivatele
	 * @return TRUE - uzivatel muze zaznam videt pokud je definovan jako resitel nebo jako zakladatel
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public boolean isDisplayable(Long id, Long forLoginId) throws EntityNotFoundException {
		Task entity = dao.findNonNull(Task.class, id);
		return entity.getInserterId().equals(forLoginId) || entity.getExecutorId().equals(forLoginId);
	}

	/**
	 * Priznak jestli uzivatel muze ukol uzavrit
	 * 
	 * @param id
	 *            ID ukolu
	 * @param forLoginId
	 *            ID uzivatele ktery chce ukol uzavrit
	 * @return TRUE - uzivatel muze zaznam uzavrit pokud jeste neni uzavren a je definovan jako resitel nebo jako zakladatel
	 * @throws EntityNotFoundException
	 *             zaznam nebyl nalezen
	 */
	public boolean isCloseable(Long id, Long forLoginId) throws EntityNotFoundException {
		Task entity = dao.findNonNull(Task.class, id);
		return entity.getFinishedDate() == null && (entity.getInserterId().equals(forLoginId) || entity.getExecutorId().equals(forLoginId));
	}

}
