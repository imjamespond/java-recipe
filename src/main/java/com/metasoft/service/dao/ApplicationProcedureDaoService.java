package com.metasoft.service.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.PrivilegeType;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.security.ExtDomainPrivilege;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.model.GenericDao;
import com.metasoft.model.dao.ApplicationDao;
import com.metasoft.model.dao.ApplicationInnerDbDao;
import com.metasoft.model.dao.ApplicationObjectDao;
import com.metasoft.model.dao.ApplicationProcedureDao;
import com.metasoft.service.DaoHelperFactory;
import com.metasoft.util.Attr;
import com.metasoft.util.DbUtil;
import com.metasoft.util.GenericDaoHelper;
import com.metasoft.util.GenericDaoService;

@Service
public class ApplicationProcedureDaoService extends GenericDaoService<ApplicationProcedureDao> {
	static Logger log = LoggerFactory.getLogger(ApplicationProcedureDaoService.class);
	final String kReplaceIntoSql;
	
	@Autowired
	private ApplicationDaoService aaDaoService;
	@Autowired
	private ApplicationInnerDbDaoService aiDaoService;
	@Autowired
	private ApplicationObjectDaoService aoDaoService;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private PlatformTransactionManager transactionManager;
	//@Autowired
    //private MessageService messageService;
	@Autowired
	private DaoHelperFactory daoHelperFactory;
	
	private GenericDaoHelper daoHelper;
	
	ApplicationProcedureDaoService(){
		super();
		kReplaceIntoSql = initReplaceInto();
	}
	
	public void init() {
		super.init();
		this.daoHelper = daoHelperFactory.getDaoHelper();
	}
	
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}
	
	public String initReplaceInto(){
		List<Attr> attrList = DbUtil.GetFieldList(this.kDaoClass);
		String insertFields = this.kId.field;
		String mergeFields = "merge."+this.kId.attr;
        String updateFields = "";
        String insertHolders = ":"+this.kId.attr;
		for(Attr attr : attrList){
        	insertFields+=","+attr.attr;
        	mergeFields+=",merge."+attr.attr;
        	
        	if(updateFields.length()>0)
        		updateFields+=",";
        	updateFields+="tab."+attr.attr+" = merge."+attr.attr;

			insertHolders+=",:"+attr.attr;
		}
		
		return "MERGE INTO "+this.kTable+" AS tab "
		+ "USING (VALUES "
		+ "("+insertHolders+") ) AS merge ("+insertFields+") "
		+ "ON tab.APPL_STATE = merge.APPL_STATE AND tab.AUDITOR_ID = merge.AUDITOR_ID "
		+ "WHEN MATCHED THEN "
		+ "UPDATE SET "+updateFields
		+ " WHEN NOT MATCHED THEN "
		+ "INSERT ("+insertFields+") "
		+ "VALUES ("+mergeFields+")";
	}
	
	public int replaceInto(ApplicationProcedureDao dao){
		try {
			MapSqlParameterSource params = this.getMapedParams(dao);
			return this.getNamedParameterJdbcTemplate().update(kReplaceIntoSql, params);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return 0; 
	}
	
	public List<GenericDao> getProceduresByApplId(String appl_id) throws DataSharingMgrError{
		Object[] objs = {appl_id};
		List<GenericDao> procedures = this.select(" where appl_id=?", objs);
		for(GenericDao dao:procedures){
			ApplicationProcedureDao apDao = (ApplicationProcedureDao) dao;
			ExtUser user = dataSharingMgrService.getExtUserById(apDao.getAuditorId());
			Tenant tenant = dataSharingMgrService.getTenantById(apDao.getDomain_id());
			if(null!=user)
				apDao.auditor = user.getName();
			if(null!=tenant)
				apDao.domain = tenant.getName();
		}
		return procedures;
	}
	public String getTenantAuditorId(String appl_id) throws DataSharingMgrError{
		Object[] objs = {appl_id, ApplicationProcedureDao.State.TOPROCESS.name};
		List<GenericDao> procedures = this.select(" where appl_id=? and proc_state=? and proc_token=1", objs);
		Assert.isTrue(procedures.size()==1,"租户内审核出错");
		ApplicationProcedureDao dao = (ApplicationProcedureDao) procedures.get(0);
		Assert.notNull(dao.getAuditorId(),"租户内审核出错");
		return dao.getAuditorId();
	}

	@Transactional(propagation=Propagation.REQUIRED )
	public void testTransation(){
		this.getJdbcTemplate().update("insert into foobar (id,name) values('foo','bar')");
		this.getJdbcTemplate().update("create table foobar(foo int)");
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void audit(String user_id, String domain_id, ApplicationDao appl, String opinion, String state, int token, Map<String, List<Integer>> approved_objs) 
			throws DataSharingMgrError, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Assert.notNull(kId);		
		Object[] objs = {user_id, opinion, token, System.currentTimeMillis(), appl.getAppl_id(), state, domain_id};
		final String sql = "update "+this.kTable+" set auditor_id=?, opinion=?,proc_token=?,audit_date=? where "+kId.field+"=? and proc_state=? and domain_id=?";
		this.getJdbcTemplate().update(sql, objs);
		
		if(token==2){
			//本租户拒绝并结束
			if (appl.getDomain_id().equals(domain_id) ) {	
				aaDaoService.finish(appl);
			}
			// 跨租户拒绝并结束
		}
		else if(token==1){
			// 本租户同意并结束
			// 本租户同意并发给跨租户审核
			// 跨租户同意并结束
			if(appl.getAppl_type().equals(ApplicationDao.Type.APPLYDATA.name)){
				if(approve(domain_id, user_id, appl, approved_objs)){
					aaDaoService.finish(appl);
				}
			}
			/*else if(appl.getAppl_type().equals(ApplicationDao.Type.INNERDATA.name)){
				if(approveInner(domain_id, user_id, appl, state)){
					aaDaoService.finish(appl);
				}
			}*/
		}

	}
	
	private String getDomainIdByDbAddressId(String dbAddressId) throws DataSharingMgrError {
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		Tenant tenant = dataSharingMgrService.getTenantById(dbAddress.getTenantId());
		
		return tenant.getName();
	}
	
	private boolean approve(String grantorDomainId, String grantorId, ApplicationDao appl, Map<String, List<Integer>> approved_objs) throws DataSharingMgrError, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if (approved_objs == null) {
			approved_objs = new HashMap<String, List<Integer>>();
			
			List<GenericDao> applObjs = aoDaoService.selectByApplId(appl.getAppl_id());
			for (GenericDao applObj : applObjs) {
				String objId = ((ApplicationObjectDao) applObj).getObj_id();
				approved_objs.put(objId, new ArrayList<Integer>());
			}
		}
		
		List<TableModel> approvingTableModels = new ArrayList<>();		
		for (String approvingObj : approved_objs.keySet()) {
			if (approvingObj.startsWith("TableModel=")) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(approvingObj);
				if (tableModel != null)
					approvingTableModels.add(tableModel);
			}
		}
	
		Map<TableModel, List<Integer>> unauthorizedTableModels = new HashMap<TableModel, List<Integer>>();
		boolean isFromTenantUser = appl.getDomain_id().equals(grantorDomainId);
		ExtUser grantee = dataSharingMgrService.getExtUserById(appl.getUser_id());
		for (TableModel approvingTableModel : approvingTableModels) {
			List<Integer> approvedColumns = approved_objs.get(approvingTableModel.getTableModelId());
			if (approvingTableModel.getTenantId().equals(grantorDomainId)) {				
				if (isFromTenantUser) {
					if (approvedColumns.size() == 0)
						dataSharingMgrService.grantPrivilege(grantorId, grantee.getUserId(), approvingTableModel.getTableModelId(), grantorDomainId);
					else 
						dataSharingMgrService.grantPrivilege(grantorId, grantee.getUserId(), approvingTableModel.getTableModelId(), grantorDomainId, PrivilegeType.R, computeColumnsMask(approvedColumns));
				} else {
					if (approvedColumns.size() == 0)
						dataSharingMgrService.grantDomainPrivilege(grantorDomainId, grantee.getTenantId(), approvingTableModel.getTableModelId());
					else
						dataSharingMgrService.grantDomainPrivilege(grantorDomainId, grantee.getTenantId(), approvingTableModel.getTableModelId(), PrivilegeType.R, computeColumnsMask(approvedColumns));
				}
			} else {
				if (isFromTenantUser) {
					Map<String, ExtDomainPrivilege> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(grantorDomainId);
					ExtDomainPrivilege domainPrivilege = domainPrivileges.get(approvingTableModel.getTableModelId());
					if (domainPrivilege != null) {
						if (approvedColumns.size() == 0)
							dataSharingMgrService.grantPrivilege(getTenantAuditorId(appl.getAppl_id()), appl.getUser_id(), domainPrivilege.getDomainPrivilegeId(), appl.getDomain_id(), PrivilegeType.R, domainPrivilege.getMask());
						else {
							if ((computeColumnsMask(approvedColumns) | domainPrivilege.getMask()) == domainPrivilege.getMask())
								dataSharingMgrService.grantPrivilege(getTenantAuditorId(appl.getAppl_id()), appl.getUser_id(), domainPrivilege.getDomainPrivilegeId(), appl.getDomain_id(), PrivilegeType.R, computeColumnsMask(approvedColumns));
							else 
								unauthorizedTableModels.put(approvingTableModel, null);
						}
					} else {
						unauthorizedTableModels.put(approvingTableModel, null);
					}
				} else {
					// illegal
				}
			}
		}
		
		if (unauthorizedTableModels.isEmpty())
			return true;
		
		for (TableModel unauthorized : unauthorizedTableModels.keySet()) {
			ApplicationProcedureDao procedureDao = 
					new ApplicationProcedureDao(appl.getAppl_id(), appl.getUser_id(), unauthorized.getTenantId(), ApplicationProcedureDao.State.CROSS_DOMAIN, 0);
			this.insert(procedureDao);
		}
		return false;
				
		/*
		Map<String, ExtDomainPrivilege> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(grantorDomainId);
		Map<String, Boolean> crossDomains = new HashMap<String, Boolean>();
		// todo: check columns privileges
		for(String approved:approved_objs.keySet()){
			ApplicationObjectDao aDao = (ApplicationObjectDao) aoDaoService.selectById(approved);
			Assert.notNull(aDao, "数据对象不存在："+approved);
			//是否为租户内申请单
			if (appl.getDomain_id().equals(grantorDomainId) ) {	
				//审核自己的对象
				if(aDao.getDomain_id().equals(grantorDomainId)){
					if (domainPrivileges.containsKey(aDao.getObj_id())){
						if (aDao.getObj_id().startsWith("TableModel=")) {
							dataSharingMgrService.grantPrivilege(grantorId, aDao.getUser_id(), 
									domainPrivileges.get(aDao.getObj_id()).getDomainPrivilegeId(), grantorDomainId);
							aDao.setObj_mode(ApplicationObjectDao.Mode.Approved.value);
							aoDaoService.updateById(aDao);
						}
						if (aDao.getObj_id().startsWith("DataArea=")) {
							dataSharingMgrService.grantPrivilege(grantorId, aDao.getUser_id(), 
									domainPrivileges.get(aDao.getObj_id()).getDomainPrivilegeId(), grantorDomainId, PrivilegeType.getPrivilegeType(aDao.getObj_mode()));
						}
					}else{
						if (aDao.getObj_id().startsWith("DataArea=")) {
							dataSharingMgrService.grantPrivilege(grantorId, aDao.getUser_id(), aDao.getObj_id(), grantorDomainId, PrivilegeType.getPrivilegeType(aDao.getObj_mode()));
						} else {
							dataSharingMgrService.grantPrivilege(grantorId, aDao.getUser_id(), aDao.getObj_id(), grantorDomainId);
							aDao.setObj_mode(ApplicationObjectDao.Mode.Approved.value);
							aoDaoService.updateById(aDao);
						}
					}
				//跨租户请求
				}else{
					crossDomains.put(aDao.getDomain_id(), true);
				}
			} else {
				//审核自己的对象
				if(aDao.getDomain_id().equals(grantorDomainId)){
					if (aDao.getObj_id().startsWith("TableModel=")) {
						String domainPrivilegeId = dataSharingMgrService.grantDomainPrivilege(grantorDomainId, appl.getDomain_id(), aDao.getObj_id());
						ExtUser user = dataSharingMgrService.getExtUserById(appl.getUser_id());
						if (user.getACL() == 0) {
							dataSharingMgrService.grantPrivilege(getTenantAuditorId(appl.getAppl_id()), appl.getUser_id(), domainPrivilegeId, appl.getDomain_id());
						}
					} 
					if (aDao.getObj_id().startsWith("DataArea=")) {
						dataSharingMgrService.grantDomainPrivilege(grantorDomainId, appl.getDomain_id(), aDao.getObj_id(), PrivilegeType.getPrivilegeType(aDao.getObj_mode()));
					}
				}
				//检测结束
			}
		}//for daos
		if(crossDomains.size()>0){
			//生成跨租户流程
			for(Entry<String, Boolean> domain:crossDomains.entrySet()){
				ApplicationProcedureDao procedureDao = 
						new ApplicationProcedureDao(appl.getAppl_id(), appl.getUser_id(), domain.getKey(), ApplicationProcedureDao.State.CROSS_DOMAIN, 0);
				this.insert(procedureDao);
				//messageService.sendMsgToProcess(domain.getKey(), "待处理", appl);
			}
			return false;//未完成
		}
		return true;
		*/
	}
	
	private int computeColumnsMask(List<Integer> columnIndeces) {
		int mask = 0;
		for (Integer index : columnIndeces) {
			mask += Math.pow(2, index);
		}
	 	return mask;
	}
	
//	public boolean approveInner(String grantorDomainId, String grantorId, ApplicationDao appl, String state) throws DataSharingMgrError, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
//		ApplicationInnerDbDao inner = (ApplicationInnerDbDao) aiDaoService.selectById(appl.getAppl_id());
//		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(inner.getDb_id());
//		Tenant tenant = dataSharingMgrService.getTenantByName(grantorDomainId);
//		// 发起跨租户申请
//		if (appl.getDomain_id().equals(grantorDomainId) && !tenant.getTenantId().equals(dbAddress.getTenantId())) {	
//			String approvorDomainId = getDomainIdByDbAddressId(inner.getDb_id());
//			ApplicationProcedureDao procedureDao = 
//					new ApplicationProcedureDao(appl.getAppl_id(), appl.getUser_id(), approvorDomainId, ApplicationProcedureDao.State.CROSS_DOMAIN_INNER, 0);
//			this.insert(procedureDao);
//			return false;
//		}
//		Object[] objs = {ApplicationProcedureDao.Token.INNER_DB.val, appl.getAppl_id(), state, grantorDomainId};
//		final String sql = "update "+this.kTable+" set proc_token="+getBITOR()+" where "+kId.field+"=? and proc_state=? and domain_id=?";
//		this.getJdbcTemplate().update(sql, objs);
//		// 如果是跨租户，批准 or deny
//		return true;//finished
//	}
	
	private String getBITOR() {
		return daoHelper.getBITORFunction("proc_token","?");
	}
	
	public void setProcToken(String appl_id, ApplicationProcedureDao.Token token){
		Assert.notNull(kId);		
		Object[] objs = {token.val, appl_id, ApplicationProcedureDao.Token.INNER_DB.val};
		//if mysql select 3&2 from dual;
		final String sql = "update "+this.kTable+" set proc_token=? where "+kId.field+"=? and "+getBITOR()+">0";
		this.getJdbcTemplate().update(sql, objs);
	}
	
	public boolean checkAllCrossDomain(ApplicationDao appl){
		final String sql = "select count(*) from "+this.kTable+" where appl_id=? and proc_state=? and proc_token=0";
		Object[] objs = {appl.getAppl_id(), ApplicationProcedureDao.State.CROSS_DOMAIN.name};
		Integer count = this.getJdbcTemplate().queryForObject( sql, objs, Integer.class);
		return count==0;
	}
}
