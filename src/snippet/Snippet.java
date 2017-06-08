package snippet;

import java.io.IOException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
/**
 * jacob 一个 java-com 中间组件，通过它可以在java应用程序中调用COM组件和Win32程序库
 * @author tan si xiang
 *
 */
public class Snippet implements Runnable{
	
	private String content;
	private void read(String content)
	{
		//System.out.println(content);
		ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		// Dispatch是做什么的？
		Dispatch sapo = sap.getObject();
		try {
	
		    // 音量 0-100
		    sap.setProperty("Volume", new Variant(100));
		    // 语音朗读速度 -10 到 +10
		    sap.setProperty("Rate", new Variant(-2));
	
		    Variant defalutVoice = sap.getProperty("Voice");
	
		    Dispatch dispdefaultVoice = defalutVoice.toDispatch();
		    Variant allVoices = Dispatch.call(sapo, "GetVoices");
		    Dispatch dispVoices = allVoices.toDispatch();
	
		    Dispatch setvoice = Dispatch.call(dispVoices, "Item", new Variant(1)).toDispatch();
		    ActiveXComponent voiceActivex = new ActiveXComponent(dispdefaultVoice);
		    ActiveXComponent setvoiceActivex = new ActiveXComponent(setvoice);
	
		    Variant item = Dispatch.call(setvoiceActivex, "GetDescription");
		    // 执行朗读
		    Dispatch.call(sapo, "Speak", new Variant(content));
	
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    sapo.safeRelease();
		    sap.safeRelease();
		}
	}
	public Snippet(String content)
	{
		this.content = content;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		read(content);
	}


}

