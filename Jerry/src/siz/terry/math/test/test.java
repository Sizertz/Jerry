package siz.terry.math.test;

import java.util.Random;

import siz.terry.math.Matrix3D;
import siz.terry.math.Vector3;

public class test {

	public static void main(String[] args) {
		//testAnglesFromRotation();
		//testInverseRotation();
		testTransposeEqualsInverse();
	}

	private static void testTransposeEqualsInverse() {
		int errors = 0;
		int iterations = 1000000;
		double maxError = 0;
		Random rng = new Random();
		for (int i = 0; i < iterations; i++) {
			Double x = rng.nextDouble() * 360 - 180;
			Double y = rng.nextDouble() * 360 - 180;
			Double z = rng.nextDouble() * 360 - 180;
			Matrix3D rot = Matrix3D.terryRotationMatrix(x, y, z).transpose();
			Matrix3D newMat = Matrix3D.terryInverseRotationMatrix(x, y, z);

			if (newMat.minus(rot).norm() > 1E-3) {
				errors++;
				System.out.println("original");
				Matrix3D.terryRotationMatrix(x, y, z).prettyPrint();
				System.out.println("transpose");
				System.out.println(x + " " + y + " " + z);
				rot.prettyPrint();
				System.out.println("inverse");
				newMat.prettyPrint();
				System.out.println("diff " + newMat.minus(rot).norm());

			}
			if (newMat.minus(rot).norm() > maxError) {
				maxError = newMat.minus(rot).norm();
			}
		}
		System.out.println("Finished " + iterations + " iterations. Found " + errors + " anomalies.");
		System.out.println("Max error is " + maxError);	
	}

	private static void testInverseRotation() {
		int errors = 0;
		int iterations = 1000000;
		double maxError = 0;
		Random rng = new Random();
		for (int i = 0; i < iterations; i++) {
			Double x = rng.nextDouble() * 360 - 180;
			Double y = rng.nextDouble() * 360 - 180;
			Double z = rng.nextDouble() * 360 - 180;
			Matrix3D rot = Matrix3D.terryRotationMatrix(x, y, z).dot(Matrix3D.terryInverseRotationMatrix(x, y, z));
			Matrix3D newMat = Matrix3D.diagonal(1, 1, 1);
			if (newMat.minus(rot).norm() > maxError) {
				maxError = newMat.minus(rot).norm();
			}
			
			rot = Matrix3D.terryInverseRotationMatrix(x, y, z).dot(Matrix3D.terryRotationMatrix(x, y, z));
			if (newMat.minus(rot).norm() > maxError) {
				maxError = newMat.minus(rot).norm();
			}
		}
		System.out.println("Finished " + iterations + " iterations. Found " + errors + " anomalies.");
		System.out.println("Max error is " + maxError);		
	}

	private static void testAnglesFromRotation() {
		int errors = 0;
		int iterations = 1000000;
		double maxError = 0;
		Random rng = new Random();
		for (int i = 0; i < iterations; i++) {
			Double x = rng.nextDouble() * 360 - 180;
			Double y = rng.nextDouble() * 360 - 180;
			Double z = rng.nextDouble() * 360 - 180;
			Matrix3D rot = Matrix3D.terryRotationMatrix(x, y, z);
			Matrix3D newMat = Matrix3D.terryRotationMatrix(rot.anglesFromRotationMatrix().get(0),
					rot.anglesFromRotationMatrix().get(1), rot.anglesFromRotationMatrix().get(2));

			if (newMat.minus(rot).norm() > 1E-3) {
				errors++;
				System.out.println(x + " " + y + " " + z);
				rot.prettyPrint();
				System.out.println("computed angles " + rot.anglesFromRotationMatrix());
				System.out
						.println("difference with input " + rot.anglesFromRotationMatrix().minus(new Vector3(x, y, z)));
				System.out.println("\nnew matrix");
				newMat.prettyPrint();
				System.out.println("\ndiff with old matrix " + newMat.minus(rot).norm());

			}
			if (newMat.minus(rot).norm() > maxError) {
				maxError = newMat.minus(rot).norm();
			}
		}
		System.out.println("Finished " + iterations + " iterations. Found " + errors + " anomalies.");
		System.out.println("Max error is " + maxError);		
	}

}
