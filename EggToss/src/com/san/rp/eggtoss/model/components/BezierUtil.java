package com.san.rp.eggtoss.model.components;

public class BezierUtil {
	
	/**
	 * Calculate the position on a quadratic bezier curve given three points and
	 * the percentage of time passed. from
	 * http://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * 
	 * @param interpolatedTime
	 *            - the fraction of the duration that has passed where
	 *            0<=time<=1
	 * @param p0
	 *            - a single dimension of the starting point
	 * @param p1
	 *            - a single dimension of the middle point
	 * @param p2
	 *            - a single dimension of the ending point
	 */
	public static long calcBezier(float interpolatedTime, float p0, float p1, float p2) {
		return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
				+ (2 * (1 - interpolatedTime) * interpolatedTime * p1)
				+ (Math.pow(interpolatedTime, 2) * p2));
	}
}
