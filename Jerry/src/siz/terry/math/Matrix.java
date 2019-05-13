package siz.terry.math;

public interface Matrix {

	public abstract double getCoeff(int row, int col);

	public abstract void setCoeff(int row, int col, double value);

	public abstract int getNColumns();

	public abstract int getNRows();

	public default void setByColumns(Vector[] columns) {
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < columns[i].getNRows(); j++) {
				setCoeff(j, i, columns[i].get(j));
			}
		}
	}

	public default Vector getColumn(int j) {
		Vector result = new Vector(this.getNRows());
		for (int i = 0; i < this.getNRows(); i++) {
			result.set(i, this.getCoeff(i, j));
		}
		return result;
	}

	public default Vector dot(Vector vec) {
		Vector result = new Vector(this.getNRows());
		for (int i = 0; i < this.getNRows(); i++) {
			double coord = 0;
			for (int j = 0; j < this.getNColumns(); j++) {
				coord += vec.get(j) * this.getCoeff(i, j);
			}
			result.set(i, coord);
		}
		return result;
	}

	public default Matrix dot(Matrix other) {
		MatrixMN result = new MatrixMN(this.getNRows(), other.getNColumns());
		for (int i = 0; i < result.getNRows(); i++) {
			for (int j = 0; j < result.getNColumns(); j++) {
				double coord = 0;
				for (int k = 0; k < this.getNColumns(); k++) {
					coord += this.getCoeff(i, k) * other.getCoeff(k, j);
				}
				result.setCoeff(i, j, coord);
			}
		}
		return result;
	}

	public default Matrix minus(Matrix other) {
		MatrixMN result = new MatrixMN(this.getNRows(), this.getNColumns());
		for (int i = 0; i < result.getNRows(); i++) {
			for (int j = 0; j < result.getNColumns(); j++) {
				result.setCoeff(i, j, this.getCoeff(i, j) - other.getCoeff(i, j));
			}
		}
		return result;
	}

	public default double norm() {
		double res = 0;
		for (int i = 0; i < this.getNRows(); i++) {
			for (int j = 0; j < this.getNColumns(); j++) {
				res += this.getCoeff(i, j) * this.getCoeff(i, j);
			}
		}
		return res;
	}

	public default void prettyPrint() {
		System.out.println(prettyString());
	}

	public default String prettyString() {
		int[] longestInColumn = new int[this.getNRows()];
		for (int j = 0; j < this.getNColumns(); j++) {
			longestInColumn[j] = 0;
			for (int i = 0; i < this.getNRows(); i++) {
				int lengthOfCoeff = String.valueOf(this.getCoeff(i, j)).length();
				if (lengthOfCoeff > longestInColumn[j])
					longestInColumn[j] = lengthOfCoeff;
			}
		}

		String str = "";
		for (int i = 0; i < getNRows(); i++) {
			for (int j = 0; j < getNColumns(); j++) {
				String coeff = String.valueOf(this.getCoeff(i, j));
				int k = 0;
				while (k < (longestInColumn[j] - coeff.length()) / 2) {
					str += " ";
					k++;
				}
				str += coeff + " ";
				while (k < longestInColumn[j] - coeff.length()) {
					str += " ";
					k++;
				}
			}
			str += "\n";
		}
		return str;
	}
	
	public default Matrix scalarDot(double scalar) {
		MatrixMN result = new MatrixMN(this.getNRows(), this.getNColumns());
		for (int i = 0; i < result.getNRows(); i++) {
			for (int j = 0; j < result.getNColumns(); j++) {
				result.setCoeff(i, j, this.getCoeff(i, j)*scalar);
			}
		}
		return result;
	}

	public default Matrix transpose() {
		MatrixMN result = new MatrixMN(this.getNColumns(), this.getNRows());
		for (int i = 0; i < result.getNRows(); i++) {
			for (int j = 0; j < result.getNColumns(); j++) {
				result.setCoeff(i, j, this.getCoeff(j, i));
			}
		}
		return result;
	}
}
