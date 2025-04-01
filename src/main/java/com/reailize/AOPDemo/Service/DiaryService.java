package com.reailize.AOPDemo.Service;

import com.reailize.AOPDemo.Annotation.CreateEntry;
import com.reailize.AOPDemo.Annotation.UpdateEntry;
import com.reailize.AOPDemo.Model.Entry;
import com.reailize.AOPDemo.Repository.DiaryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public Entry getEntry(Long id) {

        if(id == null) return null;

        return diaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Entry not found with id %d", id)));
    }

    public List<Entry> getEntries() {
        return (List<Entry>) diaryRepository.findAll();
    }

    @CreateEntry
    public Entry createEntry(Entry entry) {
        Entry existingEntry = diaryRepository.getEntryByName(entry.getName());
        if(existingEntry != null) {
            throw new RuntimeException(String.format("Entry with name %s already exists", entry.getName()));
        }
        return diaryRepository.save(entry);
    }

    @UpdateEntry
    public Entry updateEntry(Entry entry) {
        getEntry(entry.getId());
        return diaryRepository.save(entry);
    }

    public Entry deleteEntry(Long id) {
        Entry entry = getEntry(id);
        diaryRepository.delete(entry);
        return entry;
    }
}
