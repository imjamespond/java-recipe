import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.keymobile.auth.core.persistence.Schema;
import com.keymobile.common.persistence.metadata.Model;
import com.keymobile.common.persistence.metadata.ModelAttribute;

import elements.GenBean;

public class TestFM {
	private static final Logger logger = LoggerFactory.getLogger(FreeMakerUtil.class);
	private static final String kSchema = "D:\\Project\\java\\AuthServices\\resources\\schema-ext.xml";
	public List<GenBean> beans = new ArrayList<GenBean>();
	
	public static void main(String[] args) {
		logger.info(FreeMakerUtil.class.toString());
		Schema schema = new Schema(new File(kSchema));
		TestFM fm = new TestFM();
		fm.addBean(schema.getDoaminModel());
		FreeMakerUtil.genFiles(fm.beans);
	}
	
	public void addBean(Model model ){
		GenBean bean = new GenBean();
		bean.setPkg("");//com.pkg
		bean.setClazz(model.getName());//getClass().getSimpleName();
		bean.setParentClazz("SuperClazz");//getClass().getSuperclass().getSimpleName()
		bean.setClazzDesc("clazz annotation");
		bean.setEvent(null);
		
		for(ModelAttribute attr : model.getAttributes()){
			bean.addItem(attr.getType().getName(), attr.getName(), null);
		}
		
		beans.add(bean);
	}

}
