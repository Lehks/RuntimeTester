package runtimeTester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class RuntimeTester
{
	private int iterations;
	private int precision;
	private IIncrementationPhase incrementationRule;
	private IPreRunPhase preRunPhase;
	
	private FloatProperty progress;
	
	private Result result;
	
	public RuntimeTester(int iterations)
	{
		this(iterations, null);
	}

	public RuntimeTester(int iterations, IIncrementationPhase incrementationRule)
	{
		this(iterations, 0, incrementationRule);
	}

	public RuntimeTester(int iterations, int precision, IIncrementationPhase incrementationRule)
	{
		this(iterations, precision, incrementationRule, null);
	}
	
	public RuntimeTester(int iterations, int precision, IIncrementationPhase incrementationRule, IPreRunPhase preRunPhase)
	{
		this.iterations = iterations;	
		this.precision = precision;
		this.incrementationRule = incrementationRule;
		this.preRunPhase = preRunPhase;
		
		this.progress = new SimpleFloatProperty();
	}
	
	public <T> void run(T obj, Object incrementObj, Object preRunObject, Method method, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		result = new Result(iterations, precision);
		
		int currentStep = 0;
		int totalSteps = precision * iterations;
		
		progress.set(0);
		
		for(int i = 0; i < iterations; i++)
		{
			ArrayList<Long> iterationResults = new ArrayList<>(precision);
			
			for(int j = 0; j < precision; j++)
			{
				if(preRunPhase != null)
					preRunPhase.preRun(obj, preRunObject, params);
				
				long singleResult = runSingleTest(obj, method, params);
				
				iterationResults.add(singleResult);
				
				currentStep++;
				
				progress.set(((float) currentStep) / ((float) totalSteps));
			}

			if(incrementationRule != null)
				incrementationRule.increment(incrementObj);
			
			result.addIteration(iterationResults);
		}
	}
	
	private long runSingleTest(Object obj, Method method, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		long startTime = System.nanoTime();
		
		method.invoke(obj, params);
		
		return System.nanoTime() - startTime;
	}

	public int getIterations()
	{
		return iterations;
	}

	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}

	public int getPrecision()
	{
		return precision;
	}

	public void setPrecision(int precision)
	{
		this.precision = precision;
	}

	public IIncrementationPhase getIncrementationRule()
	{
		return incrementationRule;
	}

	public void setIncrementationRule(IIncrementationPhase incrementationRule)
	{
		this.incrementationRule = incrementationRule;
	}
	
	public void setPreRunPhase(IPreRunPhase preRunPhase)
	{
		this.preRunPhase = preRunPhase;
	}
	
	public Result getResult()
	{
		return result;
	}
	
	public FloatProperty getProgress()
	{
		return progress;
	}
}
