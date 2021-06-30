package parser;

import parser.*;
import java.util.*;

// Types
public enum TYPE
{
	INT,
	REAL,
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