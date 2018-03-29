package com.metasoft.service.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.copycat.framework.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.controller.ManagementController;
import com.metasoft.model.GenericDao;
import com.metasoft.model.dao.ApplicationDao;
import com.metasoft.model.dao.ApplicationInnerDbDao;
import com.metasoft.model.dao.ApplicationProcedureDao;
import com.metasoft.service.DaoHelperFactory;
import com.metasoft.util.DBType;
import com.metasoft.util.GenericDaoHelper;
import com.metasoft.util.GenericDaoService;
import com.metasoft.util.PrivilegeCheckingHelper;
@Service
public class ApplicationDaoService extends GenericDaoService<ApplicationDao> {
	private Logger logger = LoggerFactory.getLogger(ManagementController.class);

	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private ApplicationProcedureDaoService apDaoService;
	@Autowired
	private ApplicationObjectDaoService aoDaoService;
	@Autowired
	private ApplicationInnerDbDaoService aidDaoService;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;

	//@Autowired
    //private MessageService messageService;

	@Autowired
	private DaoHelperFactory daoHelperFactory;

	private GenericDaoHelper daoHelper;
	private DBType dbType;

	public void init() {
		super.init();
		this.daoHelper = daoHelperFactory.getDaoHelper();
		this.dbType = daoHelperFactory.getDBType();
	}

	//FIXME use transaction with serialize isolation level
	public String insertWithId(ApplicationDao dao){
		final String sql = getMergeIntoSql();
		try {
			String type = dao.getAppl_type().equals(ApplicationDao.Type.APPLYDATA.name)?"A":"B";
			String counter_id = getApplId();
			this.getJdbcTemplate().update(sql,counter_id);
			Integer counter = this.getJdbcTemplate().
				queryForObject("select COUNTER from DS_APPLICATION_COUNTER where COUNTER_ID='"+counter_id+"'", Integer.class);
			dao.setAppl_id(String.format("%s%s-%05d", type, counter_id, counter));
			if( super.insert(dao) > 0)
				return dao.getAppl_id();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage());
		}
		return null;
	}

	private String getMergeIntoSql() {
		String mergeIntoSql = "";
		if (dbType.isDB2()) {
			mergeIntoSql = "MERGE INTO DS_APPLICATION_COUNTER as tb "
					+ "USING (VALUES (?,1) ) AS merge (COUNTER_ID, COUNTER) "
					+ "on tb.COUNTER_ID = merge.COUNTER_ID "
					+ "WHEN MATCHED THEN update set COUNTER=COUNTER+1 "
					+ "WHEN NOT MATCHED THEN INSERT (COUNTER_ID, COUNTER) VALUES(COUNTER_ID, COUNTER)";
		} else if (dbType.isMySql()) {
			mergeIntoSql = "insert into DS_APPLICATION_COUNTER(COUNTER_ID,COUNTER) values(?,1) on DUPLICATE key update COUNTER = COUNTER + 1;";
		}
		return mergeIntoSql;
	}

	private String getApplId(){
		SimpleDateFormat f = new SimpleDateFormat("yyMMdd");
		return f.format(new Date());
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public List<GenericDao> selectUserApplByState(String userId, String state, Page page) throws DataSharingMgrError{
		String sql = "select * from "+this.kTable + " where USER_ID=? and APPL_STATE=? "
				+ "order by case when FINISH_DATE=0 then 1 else 2 end, APPL_ID desc ";
		final String count = "select count(1) from "+this.kTable + " where USER_ID=? and APPL_STATE=? ";
		Object[] objs = {userId,state};
		Object[] countObjs = {userId,state};
		List<GenericDao> list = selectByPagination(page, sql, objs);
		int total = this.getJdbcTemplate().queryForObject(count, Integer.class, countObjs);
		page.setTotal(total);
		setExtra(list);
		return list;
	}
	public List<GenericDao> selectApplToProcess(String domainId, Page page) throws DataSharingMgrError{
		String sql = "select b.* from DS_APPLICATION_PROCEDURE as a inner join "+this.kTable+" as b on a.APPL_ID = b.APPL_ID "
				+ "where a.DOMAIN_ID=? and a.PROC_TOKEN=0 order by a.APPL_ID desc ";
		final String count = "select count(1) from DS_APPLICATION_PROCEDURE a where a.DOMAIN_ID=? and a.PROC_TOKEN=0";//FIXME replace with table variable
		List<GenericDao> list = selectByPagination(page, sql, domainId);
		int total = this.getJdbcTemplate().queryForObject(count, Integer.class, domainId);
		page.setTotal(total);
		setExtra(list);
		return list;
	}
	public List<GenericDao> selectApplProcessed(String domainId, int token, Page page) throws DataSharingMgrError{
		String sql = "select b.* from DS_APPLICATION_PROCEDURE as a inner join "+this.kTable+" as b on a.APPL_ID = b.APPL_ID "
				+ "where a.DOMAIN_ID=? and "+getBITANDFunction("a.PROC_TOKEN","?")+">0 order by a.APPL_ID desc ";
		final String count = "select count(1) from DS_APPLICATION_PROCEDURE a where a.DOMAIN_ID=? and a.PROC_TOKEN>0";//FIXME replace with table variable
		Object[] objs = {domainId,token};
		Object[] countObjs = {domainId};
		List<GenericDao> list = selectByPagination(page, sql, objs);
		int total = this.getJdbcTemplate().queryForObject(count, Integer.class, countObjs);
		page.setTotal(total);
		setExtra(list);
		return list;
	}

	private String getBITANDFunction(String firstParam,String secondParam) {
		return daoHelper.getBITANDFunction(firstParam,secondParam);
	}

	private void setExtra(List<GenericDao> list) throws DataSharingMgrError{
		for(GenericDao dao:list){
			ApplicationDao aDao = (ApplicationDao) dao;
			ExtUser user = dataSharingMgrService.getExtUserById(aDao.getUser_id());
			if(null!=user)
				aDao.username = user.getName();
			Tenant tenant = dataSharingMgrService.getTenantById(aDao.getDomain_id());
			if(null!=tenant)
				aDao.domain = tenant.getName();
		}
	}

	public void newApplication(HttpServletRequest request, ApplicationDao appl) throws Exception {
		//TODO do some privilege check
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		String domainId = PrivilegeCheckingHelper.getTenantId(request.getSession());
		appl.setDomain_id(domainId);
		appl.setUser_id(userId);
		appl.setCreate_date(System.currentTimeMillis());
		String appl_id = insertWithId(appl);
		appl.setAppl_id(appl_id);//for return
		if(appl.getAppl_state().equals(ApplicationDao.State.APPLY.name)){

			ApplicationProcedureDao procedureDao =
					new ApplicationProcedureDao(appl.getAppl_id(), userId, domainId, ApplicationProcedureDao.State.TOPROCESS, 0);
			apDaoService.insert(procedureDao);
			//messageService.sendMsgToProcess(domainId, "", appl);
		}
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void newApply(HttpServletRequest request,
			ApplicationDao dao, String applReqs) throws Exception{
		newApplication(request, dao);
		aoDaoService.batchUpdateMode(applReqs,dao.getAppl_id());
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void applyInnerDb(HttpServletRequest request,
			ApplicationDao dao, ApplicationInnerDbDao innerDbDao) throws Exception {
		newApplication(request, dao);
		innerDbDao.setAppl_id(dao.getAppl_id());
		aidDaoService.insert(innerDbDao);
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delApplication(String appl_id){
		this.deleteById(appl_id);
		apDaoService.deleteById(appl_id);
		aoDaoService.deleteByApplId(appl_id);
		aidDaoService.deleteById(appl_id);
	}

	public void finish( ApplicationDao appl) throws DataSharingMgrError {
		final String sql = "update "+this.kTable+" set FINISH_DATE=? where APPL_ID=? ";
		Object[] objs = {System.currentTimeMillis(), appl.getAppl_id()};
		this.getJdbcTemplate().update(sql, objs);

		//messageService.sendMsgFinished(appl.getUser_id(), appl.getTitle(), appl.getAppl_id());
	}

	public void updateApplication(HttpServletRequest request, ApplicationDao appl) throws Exception {
		//TODO do some privilege check
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		String domainId = PrivilegeCheckingHelper.getTenantId(request.getSession());
		appl.setDomain_id(domainId);
		appl.setUser_id(userId);
		appl.setCreate_date(System.currentTimeMillis());
		this.updateById(appl);
		if(appl.getAppl_state().equals(ApplicationDao.State.APPLY.name)){

		ApplicationProcedureDao procedureDao =
				new ApplicationProcedureDao(appl.getAppl_id(), userId, domainId, ApplicationProcedureDao.State.TOPROCESS, 0);
		apDaoService.insert(procedureDao);
		//messageService.sendMsgToProcess(domainId, "", appl);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateAppl(HttpServletRequest request, ApplicationDao dao, String applReqs) throws Exception {
		updateApplication( request, dao);
		aoDaoService.batchUpdateMode(applReqs, dao.getAppl_id());
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateApplInnerDb(HttpServletRequest request, ApplicationDao dao, ApplicationInnerDbDao innerDbDao) throws Exception {
		updateApplication( request, dao);
		aidDaoService.updateById(innerDbDao);
	}
}
