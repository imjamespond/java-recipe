import java.util.ArrayList;

import com.metasoft.empire.common.GeneralRequest;
import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.utils.JsonUtils;
import com.metasoft.empire.vo.ChatRequest;
import com.metasoft.empire.vo.GameAnimVO;


public class VOTest {

	public static void main(String[] args) {
		GeneralRequest req = new GeneralRequest();
		req.setParam(new ChatRequest());
		String json = JsonUtils.toJson(req);
		System.out.println(json);
		try {
			GeneralRequest request = JsonUtils.toObject(GeneralRequest.class, json);
			ChatRequest vo = (ChatRequest) request.getParam();
			System.out.println(JsonUtils.toJson(vo));
			
			ArrayList<GameAnimVO> list = new ArrayList<GameAnimVO>();
			list.add(new Hehe());
			System.out.println(GeneralResponse.newJsonObj(list));
		}catch(Exception e){
			System.out.println(e);
		}
	}
}

class Hehe extends GameAnimVO{
	public int id = 1;
	private int test1 = 1;
	private String test2 = "hehe";
	public int getTest1() {
		return test1;
	}
	public void setTest1(int test1) {
		this.test1 = test1;
	}
	public String getTest2() {
		return test2;
	}
	public void setTest2(String test2) {
		this.test2 = test2;
	}
	
}