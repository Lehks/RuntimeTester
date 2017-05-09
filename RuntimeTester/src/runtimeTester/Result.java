package runtimeTester;

import java.util.ArrayList;

public class Result
{
	public static int TO_STRING_COLUMN_WIDTH = 12;
	
	private int iterations;
	private int precision;
	private ArrayList<IterationResult> iterationResults;
	
	public Result(int iterations, int precision)
	{
		this.iterations = iterations;
		this.precision = precision;
		
		iterationResults = new ArrayList<>(iterations);
	}
	
	public void addIteration(ArrayList<Long> iterationResult)
	{
		iterationResults.add(new IterationResult(iterationResult));
	}
	
	public int getIterations()
	{
		return iterations;
	}

	public int getPrecision()
	{
		return precision;
	}

	public ArrayList<IterationResult> getIterationResults()
	{
		return iterationResults;
	}
	
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		
		makeHead(ret);
		
		ret.append(String.format("Iteration # | %" + TO_STRING_COLUMN_WIDTH +"s | ", "Average"));
		
		for(int i = 0; i < precision; i++)
		{
			String caption = String.format("Run #%d", i);
			
			ret.append(String.format("%" + TO_STRING_COLUMN_WIDTH +"s |", caption));
		}

		makeSecondRow(ret);
		
		for(int i = 0; i < iterationResults.size(); i++)
		{
			IterationResult ir = iterationResults.get(i);
			
			ret.append("\n").append(String.format("%11d | ", i)).append(ir);
		}
		
		return ret.toString();
	}
	
	private void makeHead(StringBuilder sb)
	{
		sb.append("Precision:  ").append(precision);
		sb.append("\n\n");
		sb.append("Iterations: ").append(iterations);
		sb.append("\n\n");
	}
	
	private void makeSecondRow(StringBuilder sb)
	{
		StringBuffer secondRowParts = new StringBuffer(TO_STRING_COLUMN_WIDTH);
		
		sb.append("\n");

		sb.append("------------+");
		
		for(int i = 0; i < TO_STRING_COLUMN_WIDTH + 2; i++)
			sb.append("-");
		
		sb.append("+-");
		
		for(int i = 0; i < TO_STRING_COLUMN_WIDTH + 1; i++)
			secondRowParts.append("-");

		secondRowParts.append("+");

		for(int i = 0; i < precision; i++)
			sb.append(secondRowParts);
	}
	
	public class IterationResult
	{
		private ArrayList<Long> results;
		private long average;
		
		public IterationResult(ArrayList<Long> results)
		{
			this.results = results;
			
			long total = 0;
			
			for(Long result: results)
				total += result;
			
			average = total / results.size();
		}

		public ArrayList<Long> getResults()
		{
			return results;
		}

		public long getAverage()
		{
			return average;
		}
		
		@Override
		public String toString()
		{
			StringBuilder format = new StringBuilder();
			
			for(int i = 0; i < precision; i++)
			{
				format.append("%" + TO_STRING_COLUMN_WIDTH +"s |");
			}
			
			StringBuilder ret = new StringBuilder();
			
			ret.append(String.format("%" + TO_STRING_COLUMN_WIDTH +"d | ", average));
			ret.append(String.format(format.toString(), results.toArray()));
			
			return ret.toString();
		}
	}
}
