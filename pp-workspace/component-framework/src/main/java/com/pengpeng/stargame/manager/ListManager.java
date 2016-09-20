package com.pengpeng.stargame.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import com.pengpeng.stargame.model.Indexable;

public abstract class ListManager<Index extends Serializable,Type extends Indexable<Index>> extends Observable implements IManager<Index,Type>{
		protected List<Type> items = new ArrayList<Type>();
		
		public Collection<Type> values(){
			return Collections.unmodifiableCollection(items);
		}
		public void clear(){
			items.clear();
		}
		public int size(){
			return items.size();
		}
		public Type getElement(Index index){
			return items.get((Integer)index);
		}
		public void addElement(Type element){
			items.add(element);
		}
		public void removeElement(Type element){
			items.remove(element);
		}
		public void removeElement(Index index){
			items.remove((Integer)index);
		}

        public void addElement(Collection<Type> colls) {
            for(Type item:colls){
                addElement(item);
            }
        }
		@Override
		public List<Type> find(IFinder<Type> finder) {
			Collection<Type> collections = items;
			List<Type> list = new ArrayList<Type>();
			int index = 0;
			for(Type item:collections){
				try {
					if (finder.match(index,item))
						list.add(item);
				} catch (BreakException e) {
					list.add(item);
				}
				index++;
			}
			return list;
		}	

        public abstract void sort();
}
