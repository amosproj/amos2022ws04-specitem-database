package amos.specitemdatabase.service;

import amos.specitemdatabase.model.CompareResult;
import amos.specitemdatabase.model.CompareResultMarkup;
import amos.specitemdatabase.model.SpecItem;
import com.github.difflib.text.DiffRowGenerator;
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
            String oldValue = attribute.get(old) == null? "" : attribute.get(old).toString();
            String newValue = attribute.get(updated) == null? "" : attribute.get(updated).toString();
            if(!Objects.equals(oldValue, newValue)) {
                results.add(new CompareResult(attName, oldValue, newValue));
            }
        }
        return results;
    }

    public static List<CompareResultMarkup> compareMarkup(SpecItem old, SpecItem updated) throws IllegalAccessException {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .mergeOriginalRevised(true)
                .inlineDiffByWord(true)
                .build();
        List<CompareResultMarkup> results = new LinkedList<>();
        Field[] attributes = SpecItem.class.getDeclaredFields();
        for (Field attribute : attributes) {
            attribute.setAccessible(true);
            String attName = attribute.getName();
            if (attName.equals("shortName") || attName.equals("time") || attName.equals("commit"))
                continue;
            String oldValue = attribute.get(old) == null ? "" : attribute.get(old).toString();
            String newValue = attribute.get(updated) == null ? "" : attribute.get(updated).toString();
            if (!Objects.equals(oldValue, newValue)) {
                String markupText = generator.generateDiffRows(List.of(oldValue), List.of(newValue)).get(0).getOldLine();
                results.add(new CompareResultMarkup(attName, markupText));
            }
        }
        return results;
    }
}
