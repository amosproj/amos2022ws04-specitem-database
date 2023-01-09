package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;

public interface TagService {

    String fetchTags(final SpecItem specItem);
}
