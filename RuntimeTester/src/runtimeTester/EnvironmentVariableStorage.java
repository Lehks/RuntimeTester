package runtimeTester;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnvironmentVariableStorage
{
	public static final int 	INVALID_INDEX = -1;
	public static final String 	INVALID_TYPE_EXCEPTION_MSG = "An Object of an invalid type was passed.";
	
	private ObservableList<StorageEntry> entries = FXCollections.observableArrayList();
	
	public <T> boolean add(String name, T data)
	{
		StorageEntry newEntry = new StorageEntry(name, data.getClass(), data);
		
		if(!entries.contains(newEntry))
		{
			entries.add(newEntry);
			return true;
		}
		
		return false;
	}
	
	public boolean remove(String name)
	{
		int index = getIndexOf(name);
		
		if(index != INVALID_INDEX)
		{
			entries.remove(index);
			return true;
		}
		
		return false;
	}
	
	public ObjectProperty<Class<?>> getType(String name)
	{
		int index = getIndexOf(name);
		
		if(index != INVALID_INDEX)
		{
			return entries.get(index).type;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(String name)
	{
		int index = getIndexOf(name);
		
		if(index != INVALID_INDEX)
		{
			return (T) entries.get(index).type.get().cast(entries.get(index).data.get());
		}
		
		return null;
	}

	public <T> void setData(String name, T data)
	{
		int index = getIndexOf(name);
		
		if(index != INVALID_INDEX)
		{
			entries.get(index).setData(data);
		}
	}
	
	public StorageEntry getEntry(String name)
	{
		int index = getIndexOf(name);
		
		if(index != INVALID_INDEX)
		{
			return entries.get(index);
		}
		
		return null;
	}
	
	public ObservableList<StorageEntry> entriesProperty()
	{
		return entries;
	}
	
	private int getIndexOf(String name)
	{
		for(int i = 0; i < entries.size(); i++)
		{
			if(name.equals(entries.get(i).name.get()))
			{
				return i;
			}
		}
		
		return INVALID_INDEX;
	}
	
	public class StorageEntry
	{
		private SimpleStringProperty name;
		private ObjectProperty<Class<?>> type;
		private ObjectProperty<Object> data;
		
		public StorageEntry(String name, Class<?> type, Object data)
		{
			this.name = new SimpleStringProperty(name);
			this.type = new SimpleObjectProperty<Class<?>>(type);
			this.data = new SimpleObjectProperty<>(data);
		}
		
		public Object getData()
		{
			return data.get();
		}

		public void setData(Object data)
		{
			this.data.set(data);
			
			if(!type.get().isInstance(data))
			{
				type.set(data.getClass());
			}
		}
		
		public ObjectProperty<Object> dataProperty()
		{
			return data;
		}

		public String getName()
		{
			return name.get();
		}

		public StringProperty nameProperty()
		{
			return name;
		}
		
		public Class<?> getType()
		{
			return type.get();
		}
		
		public ObjectProperty<Class<?>> typeProperty()
		{
			return type;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj == null)
				return false;
			
			if(!(obj instanceof StorageEntry))
				return false;
			
			StorageEntry sE = (StorageEntry) obj;
			
			return name.equals(sE.name);
		}
	}
}
