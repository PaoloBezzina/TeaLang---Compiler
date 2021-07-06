package parser;

// Types
public enum TYPE
{
	INT,
	FLOAT,
	BOOLEAN,
	STRING;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TYPE forValue(int value)
	{
		return values()[value];
	}
}