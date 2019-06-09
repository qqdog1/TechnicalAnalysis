package name.qd.analysis.tech.analyzer.impl.P;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.StringCombineUtil;

public class PriceSpeed implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(PriceSpeed.class);
	
	@Override
	public String getCacheName(String product) {
		return StringCombineUtil.combine(PriceSpeed.class.getSimpleName(), product);
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = new ArrayList<>();
		
		try {
			List<ProductClosingInfo> lstProduct = dataSource.getProductClosingInfo(product, from, to);
			for(int i = 1; i < lstProduct.size(); i++) {
				ProductClosingInfo lastInfo = lstProduct.get(i-1);
				ProductClosingInfo info = lstProduct.get(i);
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				result.setValue(info.getClosePrice() - lastInfo.getClosePrice());
				lst.add(result);
			}
		} catch (Exception e) {
			log.error("PriceSpeed analyze failed.", e);
			throw e;
		}
		return lst;
	}

	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to,	String... inputs) throws Exception {
		int ma = Integer.parseInt(inputs[0]);
		List<AnalysisResult> lst = TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, TechAnalyzers.PriceSpeed, product, from, to);
		return AnalystUtils.simpleMovingAverageByResult(lst, ma);
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Simple-MA:");
		return lst;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.PRODUCT;
	}
}
