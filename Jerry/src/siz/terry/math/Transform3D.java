package siz.terry.math;

public class Transform3D {
	private Matrix3D rotation;
	private Vector3 rotationAngles;
	private Vector3 translations;
	private Vector3 scalingFactors;

	public Matrix3D getRotation() {
		return rotation;
	}

	public void setRotation(Matrix3D rotation) {
		this.rotation = rotation;
		rotationAngles = Matrix3D.anglesFromRotationMatrix(rotation);
	}

	public Vector3 getRotationAngles() {
		return rotationAngles;
	}

	public void setRotationAngles(Vector3 rotationAngles) {
		this.rotationAngles = rotationAngles;
		computeRotationMatrix();
	}

	public Vector3 getTranslations() {
		return translations;
	}

	public void setTranslations(Vector3 translations) {
		this.translations = translations;
	}

	public Vector3 getScalingFactors() {
		return scalingFactors;
	}

	public void setScalingFactors(Vector3 scalingFactors) {
		this.scalingFactors = scalingFactors;
	}

	public Transform3D(Vector3 rotationAngles, Vector3 translations, Vector3 scalingFactors) {
		super();
		this.rotationAngles = rotationAngles;
		this.translations = translations;
		this.scalingFactors = scalingFactors;
		computeRotationMatrix();
	}

	public Transform3D(String rotationAngles, String translations, String scalingFactors) {
		super();
		this.rotationAngles = new Vector3(Double.parseDouble(rotationAngles.split(" ")[0]),
				Double.parseDouble(rotationAngles.split(" ")[1]), Double.parseDouble(rotationAngles.split(" ")[2]));
		this.translations = new Vector3(Double.parseDouble(translations.split(" ")[0]),
				Double.parseDouble(translations.split(" ")[1]), Double.parseDouble(translations.split(" ")[2]));
		this.scalingFactors = new Vector3(Double.parseDouble(scalingFactors.split(" ")[0]),
				Double.parseDouble(scalingFactors.split(" ")[1]), Double.parseDouble(scalingFactors.split(" ")[2]));
		computeRotationMatrix();
	}
	
	private Transform3D() {
		
	}
	
	public Transform3D inverse() {
		Transform3D inverse=new Transform3D();
		inverse.scalingFactors=new Vector3(1/this.scalingFactors.get(0),1/this.scalingFactors.get(1),1/this.scalingFactors.get(2));
		inverse.translations=new Vector3(-this.translations.get(0), -this.translations.get(1), -this.translations.get(2));
		inverse.rotation=Matrix3D.rotationX(-this.rotationAngles.get(0)).dot(Matrix3D.rotationY(-this.rotationAngles.get(1))).dot(Matrix3D.rotationZ(-this.rotationAngles.get(2)));
		inverse.rotationAngles = Matrix3D.anglesFromRotationMatrix(rotation);
		return inverse;
	}

	private void computeRotationMatrix() {
		this.rotation = Matrix3D.terryRotationMatrix(this.rotationAngles.get(0), this.rotationAngles.get(1),
				this.rotationAngles.get(2));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transform3D\n");
		builder.append("rotation angles='");
		builder.append(rotationAngles);
		builder.append("', translations='");
		builder.append(translations);
		builder.append("', scaling factors='");
		builder.append(scalingFactors);
		builder.append("'\nrotation matrix=\n");
		builder.append(rotation.prettyString());
		return builder.toString();
	}
	

}
