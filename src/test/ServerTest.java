package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.signaflo.timeseries.TestData;
import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.model.arima.Arima;

import server.Server;

class ServerTest {

	private Server server = new Server();

	@Test
	public void loadUCRTest() {
		String fileName = "./resources/Car_TEST.tsv";
		String delimeter = "\t";
		int lineNo = 1;
		double[] secondLineOfFile = 
			new double[] { 1.6273729,1.5902346,1.553101,1.5159833,1.4757299,1.431632,1.3921917,1.3550057,1.3178379,1.2806747,1.24353,1.2063931,1.1692707,1.1321616,1.0950626,1.0579832,1.0209093,0.9838608,0.94681902,0.90979786,0.87279027,0.83579794,0.79882681,0.76186495,0.72493273,0.68800802,0.65111248,0.61423027,0.57737084,0.54053417,0.50371305,0.46004158,0.41806652,0.44752957,0.47904631,0.5161812,0.55332903,0.59049021,0.62766915,0.6648551,0.70206304,0.73927601,0.77650737,0.81374687,0.85099906,0.88464695,0.91604214,0.94818763,0.98081269,1.0183112,1.0558128,1.0933216,1.0900438,1.0481774,1.0623017,1.096256,1.133773,1.1567337,1.1161057,1.0757123,1.0356535,1.0440933,1.0801798,1.0843198,1.0833826,1.0830511,1.0711818,1.0337235,0.99663959,0.95988043,0.92368122,0.88765885,0.88951153,0.88422755,0.84973452,0.81388253,0.77625714,0.74130441,0.70773797,0.67497246,0.64179998,0.6043767,0.5669585,0.5295505,0.49215154,0.45475977,0.4173815,0.38004535,0.34890245,0.31636193,0.27916269,0.24197577,0.20480359,0.17274879,0.14310216,0.10686063,0.06992906,0.033033555,-0.0038462234,-0.040697284,-0.077520709,-0.114324,-0.15108597,-0.18756925,-0.21462271,-0.24402922,-0.28040883,-0.31143263,-0.33658234,-0.3692801,-0.4051891,-0.42916001,-0.45190269,-0.47308876,-0.49937346,-0.53414196,-0.55923115,-0.57786747,-0.60910929,-0.64213997,-0.65794087,-0.67842179,-0.71131264,-0.7341203,-0.74678252,-0.77316989,-0.8048137,-0.81519831,-0.82396438,-0.83059217,-0.84609135,-0.87506957,-0.8877376,-0.89047419,-0.91379902,-0.9388521,-0.93744646,-0.94380701,-0.96869313,-0.97765211,-0.97190914,-0.98715341,-1.0095958,-1.031098,-1.0461001,-1.0341942,-1.0350826,-1.0537513,-1.0716724,-1.0891786,-1.1056182,-1.1217755,-1.1367563,-1.1513055,-1.1649243,-1.1777711,-1.1899642,-1.1730396,-1.1472276,-1.1556966,-1.1641549,-1.1714105,-1.1779749,-1.1836597,-1.18828,-1.1923633,-1.1950023,-1.197275,-1.1980962,-1.1983616,-1.1975403,-1.1745916,-1.134473,-1.1228717,-1.1189378,-1.1134847,-1.1076737,-1.1007098,-1.0930642,-1.0846232,-1.0751958,-1.0653173,-1.0541702,-1.0427167,-1.0301009,-1.0170197,-1.0031114,-0.98847893,-0.97333203,-0.95722779,-0.94085973,-0.92340296,-0.90566947,-0.88714204,-0.86812428,-0.84858183,-0.82835969,-0.8078568,-0.78650803,-0.76496353,-0.74269744,-0.72012365,-0.69705107,-0.6735183,-0.64968651,-0.62526141,-0.60067826,-0.57546086,-0.55006677,-0.52421835,-0.49807077,-0.47162948,-0.42549179,-0.37204066,-0.34383171,-0.31675429,-0.28926319,-0.26158092,-0.23361554,-0.20536973,-0.17695757,-0.14818661,-0.11932296,-0.090093039,-0.060750311,-0.031146276,-0.0013565717,0.028600345,0.042219003,0.050413895,0.080877836,0.11196076,0.14327162,0.17469666,0.20627639,0.25131697,0.30363022,0.33792924,0.36965102,0.40158707,0.43359587,0.46575707,0.49803514,0.53040972,0.54849068,0.56235758,0.59539106,0.62852202,0.66178763,0.69512648,0.72855625,0.75075318,0.76743674,0.79964065,0.8317813,0.85015991,0.86896221,0.88857027,0.9088583,0.92966089,0.95138346,0.97336567,0.99644997,1.0110425,0.99919256,1.0045661,1.0295426,1.0300088,1.0195815,1.0432941,1.0702392,1.0980294,1.1126293,1.1043957,1.1184518,1.1479482,1.1478108,1.1394963,1.0959579,1.0723421,1.1040928,1.0995475,1.0571082,1.0418534,1.0374959,1.0337609,1.0363237,1.0709977,1.105964,1.141392,1.1541306,1.152503,1.1514199,1.1528087,1.1902786,1.2062109,1.1684153,1.1307889,1.0933248,1.0563654,1.0195876,0.98350373,0.94758922,0.91229519,0.87602047,0.83833002,0.80064212,0.7629557,0.72527294,0.68759116,0.64991352,0.61223736,0.57456451,0.53877219,0.50463213,0.47120719,0.4380119,0.44248098,0.44013998,0.40884716,0.40997709,0.45395137,0.49344322,0.53057811,0.56773613,0.60489991,0.64208586,0.6792795,0.71648881,0.75371032,0.79094168,0.82818926,0.86544151,0.90271342,0.94145428,0.98517768,1.025949,1.0631456,1.1003562,1.1375733,1.1748081,1.2128825,1.2567598,1.2982829,1.3354488,1.3726262,1.4098121,1.4470135,1.482387,1.4451818,1.4097546,1.3796831,1.3500168,1.3208006,1.2862207,1.2493251,1.2124576,1.1755972,1.138764,1.1019434,1.0651438,1.0283654,0.99160124,0.95486773,0.91814268,0.88145514,0.84477883,0.80813278,0.77150844,0.73490621,0.69833735,0.66178147,0.62527192,0.58877387,0.55231899,0.51588581,0.47948603,0.44312243,0.40678116,0.36178491,0.3158663,0.27943173,0.24301504,0.20664166,0.17030206,0.13399402,0.097737764,0.061499565,0.025333601,-0.010814541,-0.046897822,-0.082945357,-0.11894258,-0.15488059,-0.19078528,-0.22660381,-0.2612356,-0.28389222,-0.30353099,-0.31657188,-0.34844649,-0.39646431,-0.43515919,-0.47095945,-0.50664303,-0.54229786,-0.5778514,-0.61334363,-0.64875687,-0.68406862,-0.71932845,-0.75443929,-0.78951053,-0.8244175,-0.85925624,-0.89395975,-0.92853744,-0.96301626,-0.99729987,-1.0315273,-1.0654811,-1.0951183,-1.1106136,-1.1339582,-1.1665746,-1.1988976,-1.2310844,-1.2628354,-1.2944709,-1.3256393,-1.3565918,-1.3871553,-1.4173227,-1.447205,-1.4764674,-1.505554,-1.5337981,-1.5617908,-1.5890507,-1.6157753,-1.6419202,-1.667175,-1.6920566,-1.71561,-1.7388139,-1.760652,-1.781803,-1.8018257,-1.8206238,-1.8386159,-1.8547426,-1.8703515,-1.8836136,-1.8961316,-1.9067066,-1.9158409,-1.9235433,-1.9290539,-1.933738,-1.9354633,-1.9364839,-1.9349118,-1.9320949,-1.9274109,-1.9208415,-1.9131391,-1.9029972,-1.8922238,-1.8789617,-1.8649605,-1.8492221,-1.8323048,-1.8143127,-1.7948024,-1.7747797,-1.7529918,-1.7308359,-1.7073837,-1.6833289,-1.6584472,-1.6327493,-1.6066045,-1.5794887,-1.552135,-1.5238909,-1.4876274,-1.4343225,-1.3942789,-1.3653239,-1.3358074,-1.3061224,-1.275789,-1.245324,-1.2144024,-1.1832486,-1.1518029,-1.1200427,-1.0881259,-1.0558303,-1.0234443,-0.99071987,-0.95787703,-0.92480588,-0.89155673,-0.85817116,-0.81078853,-0.75997421,-0.72655656,-0.69307565,-0.65939447,-0.62560101,-0.59167817,-0.55760232,-0.52345749,-0.48912594,-0.45475072,-0.42021321,-0.38561254,-0.35090107,-0.31609504,-0.28122265,-0.24622914,-0.21120254,-0.17604267,-0.14084916,-0.10556067,-0.070213988,-0.034805785,0.0006817379,0.036201539,0.071818782,0.10745834,0.14317969,0.17893609,0.21474858,0.2506131,0.286511,0.32247572,0.35845816,0.39451148,0.43058484,0.46670897,0.50286693,0.53905772,0.57529452,0.61154814,0.64785849,0.68418156,0.72055046,0.75694085,0.79336269,0.82981613,0.86628806,0.91133168,0.95701642,0.99340864,1.0298156,1.0662561,1.1027209,1.1392074,1.1757268,1.2122569,1.2488276,1.2854073,1.3220185,1.3586458,1.3952948,1.4319671,1.468652,1.505367,1.5412287,1.5695035,1.6005945,1.6375037,1.6293944,1.5845662,1.5845662 };

		assertTrue(Arrays.equals(server.loadUCR(fileName, delimeter, lineNo).asArray(), secondLineOfFile));
	}

	@Test
	public void loadColumnTest() {
		String fileName = "./resources/household_preview.txt";
		String delimeter = ";";
		boolean hasHeaderLine = true;
		int column = 5;
		double[] sixthColumnOfFile = 
			new double[] { 18.400,23.000,23.000,23.000,15.800,15.000,15.800,15.800,15.800,15.800,19.600,23.200,22.400,22.600,17.600,14.200,13.800,14.400,13.800,16.400,25.400,33.200,30.600,22.000,19.400,13.600,13.600,13.600,13.800,13.400,11.600,16.400,18.400,19.200,17.600,10.400,11.800,11.000,11.600,16.4000,21.0000,26.2000,29.0000,27.8000,27.0000,19.0000,15.0000,13.8000,16.4000,9.600,10.000,21.4000,19.600,17.800,19.200,12.000,12.400,12.400,12.400,12.400,15.200,20.800,20.800,20.800,13.800,12.400,12.400,12.400,11.000,11.600,15.600,26.4000,19.400,18.800,13.000,9.800,9.600,9.600,9.600,9.200,13.200,17.800,17.800,18.000,12.000,10.800,10.600,10.000,9.800,10.600,18.400,18.200,18.200,17.000,18.000,18.200,17.400,15.600,14.800 };

		assertTrue(Arrays.equals(server.loadColumn(fileName, delimeter, hasHeaderLine, column).asArray(), sixthColumnOfFile));
	}
	
	@Test
	public void transformLogarithmTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		double[] logFromR =
			new double[] { 5.241747,5.433722,5.517453,5.666427,5.560682,6.066108,6.49224,6.65544,6.818924,6.418365,6.184149,5.624018,5.497168,5.690359,5.765191,5.913503,5.746203,6.320768,6.72263,6.866933,7.049255,6.632002,6.408529,5.916202,5.697093,5.934894,5.921578,6.09357,5.924256,6.49224,6.911747,7.050123,7.235619,6.806829,6.572283,6.089045 };
		double[] series = server.transformLogarithm(timeSeries).asArray();

		for(int i=0; i<series.length; i++)
			series[i] = (double)Math.round(series[i] * 1000000d) / 1000000d;

		assertTrue(Arrays.equals(series, logFromR));
	}
	
	@Test
	public void transformDifferenceTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		double[] differenceOneFromR =
			new double[] { 40,20,40,-29,171,229,117,138,-302,-128,-208,-33,52,23,51,-57,243,275,129,192,-393,-152,-236,-73,80,-5,70,-69,286,344,149,235,-484,-189,-274 };
		double[] series = server.transformDifference(timeSeries, 1).asArray();

		for(int i=0; i<series.length; i++)
			series[i] = (double)Math.round(series[i]);

		assertTrue(Arrays.equals(series, differenceOneFromR));
	}

	@Test
	public void getSeasonalityTest() {
		//TODO test this
	}
	
	@Test
	public void minusTest() {
		TimeSeries timeSeries1 = TimeSeries.from(new double[] {5, 5, 5, 5, 5});
		TimeSeries timeSeries2 = TimeSeries.from(new double[] {0, 1, 2, 3, 4});
		double[] resultArray =
			new double[] { 5, 4, 3, 2, 1 };	

		assertTrue(Arrays.equals(server.minus(timeSeries1, timeSeries2).asArray(), resultArray));
	}

	@Test
	public void transformDemeanTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		double[] demeanFromR =
			new double[] { -374.4167,-334.4167,-314.4167,-274.4167,-303.4167,-132.4167,96.5833,213.5833,351.5833,49.5833,-78.4167,-286.4167,-319.4167,-267.4167,-244.4167,-193.4167,-250.4167,-7.4167,267.5833,396.5833,588.5833,195.5833,43.5833,-192.4167,-265.4167,-185.4167,-190.4167,-120.4167,-189.4167,96.5833,440.5833,589.5833,824.5833,340.5833,151.5833,-122.4167 };
		double[] series = server.transformDemean(timeSeries).asArray();

		for(int i=0; i<series.length; i++)
			series[i] = (double)Math.round(series[i] * 10000d) / 10000d;

		assertTrue(Arrays.equals(series, demeanFromR));
	}
	
	@Test
	public void convertToArrayTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		double[] doubleArray = server.convertToArray(timeSeries);
		
		for(int i=0; i<timeSeries.size(); i++)
			if(doubleArray[i] != timeSeries.at(i))
				assertTrue(false);

		assertTrue(true);
	}
	
	@Test
	public void sizeTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		double[] doubleArray = timeSeries.asArray();
		int size = server.size(timeSeries);

		assertEquals(doubleArray.length, size);
	}
	
	@Test
	public void getAutoCorrelationUpToLag() {
		//TODO test this	
	}
	
	@Test
	public void splitTimeSeriesTest() {
		TimeSeries timeSeries = TimeSeries.from(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		double[] fortyPercent =
			new double[] { 0, 1, 2, 3 }; 
		double[] sixtyPercent = 
			new double[] { 4, 5, 6, 7, 8, 9 };	

		TimeSeries[] resultSeries = server.splitTimeSeries(timeSeries, 0.4);

		assertTrue(Arrays.equals(resultSeries[0].asArray(), fortyPercent));
		assertTrue(Arrays.equals(resultSeries[1].asArray(), sixtyPercent));
	}
	
	@Test
	public void createArimaTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		int orders[] = { 1, 1, 1 };

		Arima arima = server.createArima(timeSeries, orders);

		String str = arima.order().toString();
		int start = str.indexOf("(");
		int end = str.indexOf(")");
		String substr = str.substring(start+1, end);
		String[] tokens = substr.split(", ");

		for(int i=0; i<tokens.length; i++)
			assertEquals(Integer.parseInt(tokens[i]), orders[i]);
	}
	
	@Test
	public void getModelFittedSeriesTest() {
		//my_fitted <- my_ts - my_arima$residuals	
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		Arima arima = server.createArima(timeSeries, new int[] {1, 1, 1});
		double[] fittedSeriesR =
			new double[] { 188.811,191.4316,242.6721,257.0095,303.0102,251.9002,486.4972,741.2093,823.9791,965.3437,518.7293,433.0406,203.0767,225.4523,311.2063,328.1565,387.7533,296.0639,634.3011,929.7324,1012.752,1220.734,636.6576,544.2188,286.7281,265.1993,400.9514,373.7357,466.1388,353.6695,752.1156,1126.9,1214.851,1471.846,753.2455,637.0787 };	
		double[] series = server.getModelFittedSeries(arima).asArray();

		for(int i=0; i<series.length; i++) {
			series[i] = (double)Math.round(series[i]);
			fittedSeriesR[i] = (double)Math.round(fittedSeriesR[i]);
		}

		assertTrue(Arrays.equals(series, fittedSeriesR));
	}

	@Test
	public void getModelNonFittedSeriesTest() { 
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		Arima arima = server.createArima(timeSeries, new int[] {1, 1, 1});
		double[] series = server.getModelNonFittedSeries(arima).asArray();
		
		assertTrue(Arrays.equals(series, timeSeries.asArray()));
	}
	
	@Test
	public void getModelResidualsTest() {
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		Arima arima = server.createArima(timeSeries, new int[] {1, 1, 1});
		double[] fittedSeriesR =
			new double[] { 0.1889999,37.56839,6.32786,31.99053,-43.01017,179.0998,173.5028,35.79067,91.02087,-352.3437,-33.72928,-156.0406,40.92327,70.54771,7.793715,41.84348,-74.75334,259.9361,196.6989,30.26764,139.2479,-461.7342,-29.6576,-173.2188,11.27186,112.8007,-27.95137,69.26428,-92.13881,306.3305,251.8844,26.10033,173.1494,-567.8458,-38.24548,-196.0787 };
		double[] series = server.getModelResiduals(arima).asArray();

		for(int i=0; i<series.length; i++) {
			series[i] = (double)Math.round(series[i]);
			fittedSeriesR[i] = (double)Math.round(fittedSeriesR[i]);
		}

		assertTrue(Arrays.equals(series, fittedSeriesR));
	}
	
	@Test
	public void createModelForecastTest() {
		//my_forecast <- predict(my_arima, n.ahead=5)$pred
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		Arima arima = server.createArima(timeSeries, new int[] {1, 1, 1});

		TimeSeries[] forecastValues = server.createModelForecast(arima, 5);
		TimeSeries forecastPointEstimates = forecastValues[0];
		//TimeSeries forecastLowerPredictionInterval = forecastValues[1];
		//TimeSeries forecastUpperPredictionInterval = forecastValues[2];

		double[] predictionR =
			new double[] { 342.6365,300.0164,281.5493,273.5477,270.0807 };
		double[] series = forecastPointEstimates.asArray();

		for(int i=0; i<series.length; i++) {
			series[i] = (double)Math.round(series[i]);
			predictionR[i] = (double)Math.round(predictionR[i]);
		}

		assertTrue(Arrays.equals(series, predictionR));
	}

	@Test
	public void validateModelTest() {
		double[] testS = {0.3979927, 1.311269, 0.6239756, -0.4258832, -0.5265587, -0.4989824, -0.1823278, -0.685791, -0.08392203, -0.2387654, -1.237047, -1.056209, -0.6114126, -0.2104292, -0.2829568, 0.2338701, 2.269214, -1.305414, -0.5025786, -0.6507393, -0.5923028, 1.708815, 0.8842577, -1.557335, -0.4656443, 0.7206226, 1.28818, 0.4142365, -0.5256184, -1.524502 };
		TimeSeries timeSeries = TimeSeries.from(testS);
		TimeSeries[] timeSeriesArray = server.splitTimeSeries(timeSeries, 0.8);
		TimeSeries trainingSet = timeSeriesArray[0];
		TimeSeries testSet = timeSeriesArray[1];
		Arima arima = server.createArima(trainingSet, new int[]{1, 1, 1});

		TimeSeries[] forecastValues = server.createModelForecast(arima, testSet.asArray().length);
		//TimeSeries forecastPointEstimates = forecastValues[0];
		//TimeSeries forecastLowerPredictionInterval = forecastValues[1];
		//TimeSeries forecastUpperPredictionInterval = forecastValues[2];
		boolean pass = server.validateModel(testSet, forecastValues);
		
		assertEquals(pass, true);
	}
	
	@Test
	public void getAicTest() {
		//my_aic <- my_arima$aic	
		TimeSeries timeSeries = TimeSeries.from(TestData.sodaSalesArray);
		Arima arima = server.createArima(timeSeries, new int[] {1, 1, 1});
		double value = (double)Math.round(arima.aic() * 10000d) / 10000d;
		
		assertEquals(value, 470.5765);
	}
	
	@Test
	public void formPartialsForPacfTest() {
		//TODO test this
	}
}
