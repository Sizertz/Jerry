package siz.terry.math;

public class MatrixMN implements Matrix {
	double[][] coeffs;

	public MatrixMN(int nRows, int nColumns) {
		this.coeffs = new double[nRows][nColumns];
	}

	@Override
	public double getCoeff(int row, int col) {
		return this.coeffs[row][col];
	}

	@Override
	public void setCoeff(int row, int col, double value) {
		this.coeffs[row][col] = value;
	}

	@Override
	public int getNColumns() {
		return coeffs[0].length;
	}

	@Override
	public int getNRows() {
		return coeffs.length;
	}
	
	@Override
	public String toString() {
		return prettyString();
	}
}
