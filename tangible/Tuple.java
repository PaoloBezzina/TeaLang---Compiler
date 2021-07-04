package tangible;

public final class Tuple<T1, T2, T3>
{
	public T1 first;
	public T2 second;
	public T3 third;

	public Tuple()
	{
		first = null;
		second = null;
		third = null;
	}

	public Tuple(T1 firstValue, T2 secondValue, T3 thirdvalue)
	{
		first = firstValue;
		second = secondValue;
		third = thirdvalue;
	}

	public Tuple(Tuple<T1, T2, T3> tupleToCopy)
	{
		first = tupleToCopy.first;
		second = tupleToCopy.second;
		third = tupleToCopy.third;
	}
}