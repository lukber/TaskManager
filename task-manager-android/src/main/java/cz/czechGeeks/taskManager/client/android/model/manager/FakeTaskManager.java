package cz.czechGeeks.taskManager.client.android.model.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.czechGeeks.taskManager.client.android.model.TaskModel;

/**
 * Servis pro testovaci generovana data
 * 
 * @author lukasb
 * 
 */
public class FakeTaskManager implements TaskManager {

	private static List<TaskModel> mainData = new ArrayList<TaskModel>();
	private static List<TaskModel> delegatedToMeData = new ArrayList<TaskModel>();
	private static List<TaskModel> delegatedToOthersData = new ArrayList<TaskModel>();

	public FakeTaskManager() {
		if (mainData.size() == 0) {
			int dataCounts = 20;
			for (int i = 1; i <= dataCounts; i++) {
				Long id = Long.valueOf(i);
				Long categId = Long.valueOf(1);
				String name = "Ukol " + i;
				String desc = "Popisek ukolu " + i;
				Timestamp finishToDate = new Timestamp(new Date().getTime());
				Timestamp finishedDate = null;
				Timestamp insDate = null;
				Timestamp updDate = null;
				boolean updatable = true;
				boolean deletable = false;
				boolean closeable = false;

				mainData.add(new TaskModel(id, categId, "Kategorie " + i, name, desc, finishToDate, finishedDate, Long.valueOf(1), "Lukas", Long.valueOf(1), "Lukas", insDate, updDate, updatable, deletable, closeable));
				delegatedToMeData.add(new TaskModel(id*dataCounts, categId, "Kategorie " + i, name + "_toMe", desc, finishToDate, finishedDate, Long.valueOf(1), "Lukas", Long.valueOf(2), "Karel", insDate, updDate, updatable, deletable, closeable));
				delegatedToOthersData.add(new TaskModel(id*dataCounts*2, categId, "Kategorie " + i, name + "_toOthers", desc, finishToDate, finishedDate, Long.valueOf(2), "Karel", Long.valueOf(1), "Lukas", insDate, updDate, updatable, deletable, closeable));
			}
		}
	}

	@Override
	public void delete(Long id) {
		for (int i = 0; i < mainData.size(); i++) {
			if (mainData.get(i).getId().equals(id)) {
				mainData.remove(i);
				return;
			}
		}
		for (int i = 0; i < delegatedToMeData.size(); i++) {
			if (delegatedToMeData.get(i).getId().equals(id)) {
				delegatedToMeData.remove(i);
				return;
			}
		}
		for (int i = 0; i < delegatedToOthersData.size(); i++) {
			if (delegatedToOthersData.get(i).getId().equals(id)) {
				delegatedToOthersData.remove(i);
				return;
			}
		}
	}

//	@Override
//	public void update(TaskModel task) {
//		for (int i = 0; i < mainData.size(); i++) {
//			if (mainData.get(i).getId().equals(task.getId())) {
//				mainData.remove(i);
//				mainData.add(task);
//				return;
//			}
//		}
//		for (int i = 0; i < delegatedToMeData.size(); i++) {
//			if (delegatedToMeData.get(i).getId().equals(task.getId())) {
//				delegatedToMeData.remove(i);
//				delegatedToMeData.add(task);
//				return;
//			}
//		}
//		for (int i = 0; i < delegatedToOthersData.size(); i++) {
//			if (delegatedToOthersData.get(i).getId().equals(task.getId())) {
//				delegatedToOthersData.remove(i);
//				delegatedToOthersData.add(task);
//				return;
//			}
//		}
//	}

	@Override
	public void getAllMain(AsyncTaskCallBack<TaskModel[]> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllDelegatedToMe(AsyncTaskCallBack<TaskModel[]> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllDelegatedToOthers(AsyncTaskCallBack<TaskModel[]> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(Long id, AsyncTaskCallBack<TaskModel> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		// TODO Auto-generated method stub
		
	}

}
