package application;

public class Main
{
	public boolean palindrome(String element)
	{
		element = element.toLowerCase().replaceAll(" ", "");
		
		for(int i = 0; i < element.length() / 2; i++)
		{
			if(element.charAt(i) != element.charAt(element.length() - 1 - i))
				return false;
		}
		
		return true;
	}
}
