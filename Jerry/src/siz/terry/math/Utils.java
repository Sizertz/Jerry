package siz.terry.math;

public class Utils {
	public static double roundWithSignificantNumbers(final double x, final int nSignificant) {
		if (nSignificant > 17)
			return x;
		if (x == 0)
			return 0;
		final double signedScale = x < 0 ? -Math.pow(10, Math.floor(Math.log10(-x)) - nSignificant + 1)
				: Math.pow(10, Math.floor(Math.log10(x)) - nSignificant + 1);
		return Math.floor(x / signedScale + 0.5) * signedScale;
	}

	public static double round5(double x) {
		return roundWithSignificantNumbers(x, 5);
	}

	public static boolean approxEquals(double errorMargin, double x, double y) {
		return Math.abs(x - y) < errorMargin;
	}
	
	public static boolean approxEquals(double errorMargin, double x, double... others) {
		for(double y:others) {
			if(!approxEquals(errorMargin, x, y)) {
				return false;
			}
		}
		return true;
	}
}
