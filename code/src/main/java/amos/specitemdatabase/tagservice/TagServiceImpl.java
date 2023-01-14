package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    @PersistenceContext
    EntityManager entityManager;

    private final TagsRepo tagsRepo;
    @Autowired
    public TagServiceImpl(final TagsRepo tagsRepo) {
        this.tagsRepo = tagsRepo;
    }

    @Override
    public String fetchTags(final SpecItem specItem) {
        List<TagInfo> tagInfos = this.tagsRepo.getLatestTagInfo(specItem.getShortName());
        if (tagInfos.size() > 1) {
            // TODO: A correct mechanism must be implemented here
            return tagInfos.get(tagInfos.size() -1).getTags();
        } else if (tagInfos.size() == 1) {
            return tagInfos.get(0).getTags();
        } else {
            return "";
        }
    }
}
