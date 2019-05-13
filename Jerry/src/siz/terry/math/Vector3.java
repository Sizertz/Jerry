package siz.terry.math;

public class Vector3 extends Vector{

	public Vector3(double x, double y, double z) {
		super(new double[] {x,y,z});
	}
	
	public Vector3() {
		super(3);
	}
	
	public static Vector3 cast(Matrix m) {
		if(m.getNColumns()==1 && m.getNRows()==3) {
			return new Vector3(m.getCoeff(0, 0),m.getCoeff(1, 0),m.getCoeff(2, 0));
		}
		if(m.getNRows()==1 && m.getNColumns()==3) {
			return new Vector3(m.getCoeff(0, 0),m.getCoeff(0, 1),m.getCoeff(0, 2));
		}
		return null;
	}
	
	@Override
	public Vector3 scalarDot(double scalar) {
		return cast(super.scalarDot(scalar));
	}
	
	@Override
	public Vector3 minus(Matrix other) {
		return cast(super.minus(other));
	}
	
	@Override
	public Vector3 dot(Matrix other) {
		return cast(super.dot(other));
	}
}
