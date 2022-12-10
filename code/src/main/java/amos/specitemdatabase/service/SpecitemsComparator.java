package amos.specitemdatabase.service;

import amos.specitemdatabase.model.CompareResult;
import amos.specitemdatabase.model.SpecItem;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SpecitemsComparator {
    public static List<CompareResult> compare(SpecItem old, SpecItem updated) throws IllegalAccessException {
        List<CompareResult> results = new LinkedList<>();
        Field[] attributes = SpecItem.class.getDeclaredFields();
        for (Field attribute : attributes) {
            attribute.setAccessible(true);
            String attName = attribute.getName();
            if(attName.equals("shortName") || attName.equals("time") || attName.equals("commit"))
                continue;
            String oldValue = attribute.get(old) == null? null: attribute.get(old).toString();
            String newValue = attribute.get(updated) == null? null: attribute.get(updated).toString();
            if(!Objects.equals(oldValue, newValue)) {
                results.add(new CompareResult(attName, oldValue, newValue));
            }
        }
        return results;
    }
}
