package siz.terry.math;

public class Matrix3D extends MatrixMN {
	public Matrix3D(Vector3 xCol, Vector3 yCol, Vector3 zCol) {
		super(3, 3);
		this.setByColumns(new Vector[] { xCol, yCol, zCol });
	}
	
	public Matrix3D() {
		super(3, 3);
	}

	public Matrix3D(Matrix mat) {
		super(3, 3);
		if (mat.getNColumns() == 3 && mat.getNRows() == 3)
			this.setByColumns(new Vector[] { mat.getColumn(0), mat.getColumn(1), mat.getColumn(2) });
	}
	
	public static Matrix3D diagonal(Vector3 diagonalCoeffs) {
		return diagonal(diagonalCoeffs.get(0),diagonalCoeffs.get(1),diagonalCoeffs.get(2));
	}
	
	public static Matrix3D diagonal(double x, double y, double z) {
		Vector3 colX = new Vector3(x, 0, 0);
		Vector3 colY = new Vector3(0, y, 0);
		Vector3 colZ = new Vector3(0, 0, z);
		return new Matrix3D(colX, colY, colZ);
	}

	public static Matrix3D rotationX(double angle) {
		double theta = (double) (angle * Math.PI / 180f);
		Vector3 colX = new Vector3(1, 0, 0);
		Vector3 colY = new Vector3(0, (double) Math.cos(theta), (double) Math.sin(theta));
		Vector3 colZ = new Vector3(0, (double) -Math.sin(theta), (double) Math.cos(theta));
		return new Matrix3D(colX, colY, colZ);
	}

	public static Matrix3D rotationY(double angle) {
		double theta = (double) (angle * Math.PI / 180f);
		Vector3 colX = new Vector3((double) Math.cos(theta), 0, (double) -Math.sin(theta));
		Vector3 colY = new Vector3(0, 1, 0);
		Vector3 colZ = new Vector3((double) Math.sin(theta), 0, (double) Math.cos(theta));
		return new Matrix3D(colX, colY, colZ);
	}

	public static Matrix3D rotationZ(double angle) {
		double theta = (double) (angle * Math.PI / 180f);
		Vector3 colX = new Vector3((double) Math.cos(theta), (double) Math.sin(theta), 0);
		Vector3 colY = new Vector3((double) -Math.sin(theta), (double) Math.cos(theta), 0);
		Vector3 colZ = new Vector3(0, 0, 1);
		return new Matrix3D(colX, colY, colZ);
	}

	
	public static Matrix3D terryInverseRotationMatrix(double x, double y, double z) {
		return new Matrix3D(rotationX(-x).dot(rotationY(-y).dot(rotationZ(-z))));
	}
	
	public static Matrix3D terryRotationMatrix(double x, double y, double z) {
		return new Matrix3D(rotationZ(z).dot(rotationY(y).dot(rotationX(x))));
	}
	
	public  Vector3 anglesFromRotationMatrix() {
		return anglesFromRotationMatrix(this);
	}

	public static Vector3 anglesFromRotationMatrix(Matrix3D mat) {
		double sinY = -mat.getCoeff(2, 0);

		if (sinY * sinY != 1) {
			double y1 = Math.asin(sinY);

			double cosY1 = Math.cos(y1);
			double cosX1 = Utils.round5(mat.getCoeff(2, 2) / cosY1);
			double sinX1 = mat.getCoeff(2, 1) / cosY1;
			double x1 = sinX1 > 0 ? Math.acos(cosX1) : -Math.acos(cosX1);

			double cosZ1 = Utils.round5(mat.getCoeff(0, 0) / cosY1);
			double sinZ1 = mat.getCoeff(1, 0) / cosY1;
			double z1 = sinZ1 > 0 ? Math.acos(cosZ1) : -Math.acos(cosZ1);

			return new Vector3(x1 * 180 / Math.PI, y1 * 180 / Math.PI, z1 * 180 / Math.PI);
		}

		else {
			if (sinY > 0) {
				double y1 = Math.PI / 2;

				double xPlusZ = (mat.getCoeff(1, 2) > 0 ? 1 : -1) * Math.acos(Utils.round5(mat.getCoeff(0, 2)));
				double xMinusZ = (mat.getCoeff(0, 1) > 0 ? 1 : -1) * Math.acos(mat.getCoeff(1, 1));

				double x1 = (xPlusZ + xMinusZ) / 2;

				double z1 = (xPlusZ - xMinusZ) / 2;
				
				return new Vector3(x1 * 180 / Math.PI, y1 * 180 / Math.PI, z1 * 180 / Math.PI);
			} else {
				
				double y1 = -Math.PI / 2;

				double xPlusZ = (mat.getCoeff(0, 1) < 0 ? 1 : -1) * Math.acos(Utils.round5(mat.getCoeff(1, 1)));
				double xMinusZ = (mat.getCoeff(1, 2) > 0 ? 1 : -1) * Math.acos(Utils.round5(-mat.getCoeff(0, 2)));

				double x1 = (xPlusZ + xMinusZ) / 2;
				
				double z1 = (xPlusZ - xMinusZ) / 2;
				
				return new Vector3(x1 * 180 / Math.PI, y1 * 180 / Math.PI, z1 * 180 / Math.PI);
			}
		}
	}
	

	public static Matrix3D cast(Matrix mat) {
		if(mat.getNColumns()==3 && mat.getNRows()==3) {
			Matrix3D res = new Matrix3D();
			res.setByColumns(new Vector[] { mat.getColumn(0), mat.getColumn(1), mat.getColumn(2) });
			return res;
		}
		return null;
	}
	

	public Matrix3D dot(Matrix3D other) {
		return cast(super.dot(other));
	}
	
	@Override
	public Matrix3D transpose() {
		return cast(super.transpose());
	}

}
