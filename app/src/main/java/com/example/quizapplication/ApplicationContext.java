package com.example.quizapplication;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.quizapplication.Data.Option;
import com.example.quizapplication.Data.OptionViewModel;
import com.example.quizapplication.Data.UriTypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplicationContext extends Application {


    public OptionViewModel optionViewModel;
    private AtomicBoolean HardMode;
    private Option Current = null;
    public boolean saveState = false;

    public Option[] SavedOptions = new Option[3];
    public ArrayList<Option> options = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        HardMode = new AtomicBoolean(false);

    }
    public void setCurrent(Option option) {
        this.Current = option;
    }
    public Option getCurrent() {
        return this.Current;
    }
    public void setViewModelHolder(OptionViewModel ovm) {
        this.optionViewModel = ovm;

    }
    public void update(List<Option> list) {
        this.options.clear();
        this.options.addAll(list);
    }
    public ArrayList<Option> getList() {
         ArrayList list = new ArrayList<>(optionViewModel.getAllOptions().getValue());
         return list;
    }
    public boolean removeFromList(Option c) {
        optionViewModel.delete(c);
        return true;
    }
    public boolean addToList(Option c) {
        AtomicBoolean nameIsDupe = new AtomicBoolean(false);
        AtomicBoolean uriIsDupe = new AtomicBoolean(false);

        // Assuming getAllOptions() can provide a synchronous result; otherwise, you need a different approach
        List<Option> list = optionViewModel.getAllOptions().getValue(); // Make sure this is not null
        if (list != null) {
            list.forEach(x -> {
                if (x.getName().equalsIgnoreCase(c.getName())) {
                    nameIsDupe.set(true);
                }
                if (x.getUri().equals(c.getUri())) {
                    uriIsDupe.set(true);
                }
            });

            if (!nameIsDupe.get() && !uriIsDupe.get()) {

                Log.i("length", this.getList().size()+"");
                this.optionViewModel.insert(c);
                Log.i("inserted", "inserted "+c.getName());
                Log.i("length", this.getList().size()+"");
                try {
                    wait(200);
                } catch (Exception e) {
                    Log.i("Exception", e.toString());
                }
                Log.i("length", this.getList().size()+"");
                return true; // Item was added successfully
            }
        }

        return false; // Either list is null, or duplicates were found
    }


    // get hardcore
    public boolean isHardMode() {
        return HardMode.get();
    }
    // set hardcore
    public void setHardMode(boolean hardMode) {
        HardMode.set(hardMode);
    }

}
