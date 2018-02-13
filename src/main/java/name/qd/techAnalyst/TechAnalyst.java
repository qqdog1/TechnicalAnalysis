package name.qd.techAnalyst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg5Day;
import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.dataSource.TWSE.TWSEDataManager;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalyst {
	private TechAnalyzerManager analyzerManager;
	private DataManager twseDataManager;
	
	private TechAnalyst() {
		// 要分析哪一檔商品  時間  哪種分析方式
		// 檢查檔案 分析 回傳結果?
		
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		Logger logger = LogManager.getLogger(TechAnalyst.class);
		
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = new TWSEDataManager();
		
		try {
			Date from = TimeUtil.getDateTimeFormat().parse("20180201-00:00:00:000");
			Date to = TimeUtil.getDateTimeFormat().parse("20180212-00:00:00:000");
			String product = "2453";
			String analyzerName = MovingAvg5Day.class.getSimpleName();
			
			List<AnalysisResult> lst = analyzerManager.getAnalysisResult(twseDataManager, analyzerName, product, from, to);
			for(AnalysisResult result : lst) {
				System.out.println(result.getKeyString() + ":" + result.getValue());
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
