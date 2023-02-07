// package amos.specitemdatabase.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import amos.specitemdatabase.model.Category;
// import amos.specitemdatabase.model.CompareResult;
// import amos.specitemdatabase.model.CompareResultMarkup;
// import amos.specitemdatabase.model.SpecItem;
// import amos.specitemdatabase.model.TagInfo;
// import java.time.LocalDateTime;
// import java.time.Month;
// import java.util.List;
// import org.junit.jupiter.api.Test;

// class SpecitemsComparatorTest {

//     @Test
//     void compare() throws IllegalAccessException {
//         SpecItem s1 = new SpecItem();
//         SpecItem s2 = new SpecItem();
//         s1.setShortName("ID1");
//         s2.setShortName("ID1");

//         s1.setCommitTime(LocalDateTime.of(2020, Month.AUGUST, 13, 0, 0));
//         s2.setCommitTime(LocalDateTime.now());

//         s1.setLongName("Specitem1");
//         s2.setLongName("Specitem1");

//         s1.setCategory(Category.CATEGORY1);
//         s2.setCategory(Category.CATEGORY2);

//         s1.setContent("This is old content.");
//         s2.setContent("This is new content.");

//         TagInfo t1 = new TagInfo();
//         t1.setTags("Tag1");
//         TagInfo t2 = new TagInfo();
//         t2.setTags("Tag1");
//         s1.setTagInfo(t1);
//         s2.setTagInfo(t2);

//         List<CompareResult> results = SpecitemsComparator.compare(s1, s2);
//         System.out.println(results);
//         assertEquals(2, results.size());
//         assertEquals("category", results.get(0).getField());
//         assertEquals("content", results.get(1).getField());
//     }

//     @Test
//     void compareMarkup() throws IllegalAccessException {
//         SpecItem s1 = new SpecItem();
//         SpecItem s2 = new SpecItem();
//         s1.setShortName("ID1");
//         s2.setShortName("ID1");

//         s1.setCommitTime(LocalDateTime.of(2020, Month.AUGUST, 13, 0, 0));
//         s2.setCommitTime(LocalDateTime.now());

//         s1.setLongName("Specitem1");
//         s2.setLongName("Specitem1");

//         s2.setCategory(Category.CATEGORY2);

//         s1.setContent("This is old content.");
//         s2.setContent("This is new content.");

//         TagInfo t1 = new TagInfo();
//         t1.setTags("Tag1");
//         TagInfo t2 = new TagInfo();
//         t2.setTags("Tag1");
//         s1.setTagInfo(t1);
//         s2.setTagInfo(t2);
//         List<CompareResultMarkup> results = SpecitemsComparator.compareMarkup(s1, s2);
//         results.forEach(a -> System.out.println(a.getMarkupText()));
//     }
// }
