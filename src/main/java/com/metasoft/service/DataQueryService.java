package com.metasoft.service;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.metasoft.model.ExtTableModel;
import com.metasoft.util.JsonTreeHelper.JsonNode;

@Service
public class DataQueryService {
	
	private Logger logger = LoggerFactory.getLogger(DataQueryService.class);

	@Autowired
	private DataSharingMgrService dataSharingMgrService;	
	
	public JsonNode queryPrivilegeKpiAsTree(List<ExtTableModel> tableModel) { 
		List<TableModelWithSchema> categories = getCategorys(tableModel, "kpi");
		return queryPrivilegesTableModelsAsTree(categories);
	}
	
	public JsonNode queryPrivilegeAnalyticalModelAsTree(List<ExtTableModel> tableModel) { 
		List<TableModelWithSchema> categories = getCategorys(tableModel, "analyticalModel");
		return queryPrivilegesTableModelsAsTree(categories);
	}
	
	public JsonNode queryPrivilegesTableModelsAsTree(List<TableModelWithSchema> categories) {
		final String separator = "/";

		JsonNode root = new JsonNode("root");

		for (TableModelWithSchema category : categories) {
			JsonNode current = root;
			String[] parts = category.tableNameInSource.split(separator);
			for (String part : parts) {
				JsonNode subNode = null;
				if (current.getChildSize() == 0) {
					subNode = new JsonNode(part, category.schema, category.tableModelId);
					current.addChild(subNode);
				} else {
					for (JsonNode node : current.nodes) {
						if (node.text.equals(part))
							subNode = node;
					}
					if (subNode == null) {
						subNode = new JsonNode(part, category.schema, category.tableModelId);
						current.addChild(subNode);
					}
				}
				current = subNode;
			}
		}
		return root;
	}
	
	public static class TableModelWithSchema {
		public String schema;
		public String tableNameInSource;
		public String tableModelId;
		
		public TableModelWithSchema(String schema, String tableNameInSource, String tableModelId) {
			this.schema = schema;
			this.tableNameInSource = tableNameInSource;
			this.tableModelId = tableModelId;
		}
	}
	
	private List<TableModelWithSchema> getCategorys(List<ExtTableModel> tableModel, String type) {
		List<TableModelWithSchema> categorys = new ArrayList<>();
		for (TableModel model : tableModel) {
			if (model.getType().equals(type) && model.isEnabled()) {
				String schema = "";
				try {
					DBAddress address = dataSharingMgrService.getDBAddressById(model.getDBAddressId());
					if (address != null) 
						schema = address.getName();
				} catch (DataSharingMgrError e) {
					logger.error("DBAddress not found.", e);
				}
				String nameInsource = model.getNameInSource();
				nameInsource = nameInsource.split("\\.")[0];
				nameInsource = nameInsource + "/" + model.getName();
				categorys.add(new TableModelWithSchema(schema, nameInsource, model.getTableModelId()));
			}
		}
		return categorys;
	}
	
}
