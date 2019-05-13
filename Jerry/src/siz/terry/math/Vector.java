package siz.terry.math;

public class Vector implements Matrix {
	private double[] coords;

	@Override
	public double getCoeff(int row, int col) {
		return coords[row];
	}

	@Override
	public void setCoeff(int row, int col, double value) {
		coords[row] = value;

	}

	@Override
	public int getNColumns() {
		return 1;
	}

	@Override
	public int getNRows() {
		return coords.length;
	}

	public double get(int i) {
		return coords[i];
	}

	public void set(int i, double value) {
		coords[i] = value;
	}

	public Vector(double[] coords) {
		this.coords = coords;
	}

	public Vector(int n) {
		this.coords = new double[n];
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.coords.length; i++) {
			builder.append(this.coords[i]);
			builder.append(" ");
		}
		builder = builder.replace(builder.length() - 1, builder.length(), "");
		return builder.toString();
	}
	
	@Override
	public Vector minus(Matrix other) {
		return cast(Matrix.super.minus(other));
	}

	private Vector cast(Matrix m) {
		
		if(m.getNColumns()==1) {
			return new Vector(m.getColumn(0).coords);
		}
		return null;
	}

	public boolean equals(Vector other) {
		Vector diff = this.minus(other);
		return diff.transpose().dot(diff).get(0) < 1E-4;
	}
}
