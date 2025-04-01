package com.reailize.AOPDemo.Controller;


import com.reailize.AOPDemo.Model.Entry;
import com.reailize.AOPDemo.Service.DiaryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/")
    public String GetEntry(Model model, @RequestParam(required = false, defaultValue = "-1") Long id) {
        System.out.println("Home page");
        Entry entry = new Entry();
        if (id != -1d) {
            try{
                entry = diaryService.getEntry(id);
            }
            catch(RuntimeException e){

            }

        }
        model.addAttribute("entry", entry);
        return "entry";
    }

    @PostMapping("/handleform")
    public String handleForm(@Valid Entry entry, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return "entry";

        Entry existingEntry = diaryService.getEntry(entry.getId());

        if (existingEntry != null) {
            diaryService.updateEntry(entry);
        } else {
            try{
                diaryService.createEntry(entry);
            }
            catch (RuntimeException e){
                bindingResult.rejectValue("name", "entry.exists", "entry exists, try again");
                return "entry";
            }
        }

        return "redirect:/all";
    }

    @GetMapping("/all")
    public String GetDairyEntries(Model model) {
        System.out.println("Here are your diary entries");
        model.addAttribute("entries", diaryService.getEntries());
        return "entries";
    }

    @DeleteMapping("/delete/{id}")
    public String GetDairyEntry(@PathVariable(required = true, value = "id") Long id) {
        System.out.printf("Diary entry deleted. %d%n", id);

        diaryService.deleteEntry(id);

        return "redirect:/all";
    }
}
