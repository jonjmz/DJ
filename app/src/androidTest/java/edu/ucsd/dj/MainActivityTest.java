package edu.ucsd.dj;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Switch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.dj.activities.MainActivity;
import edu.ucsd.dj.managers.Settings;

/**
 * Created by jonathanjimenez on 5/9/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    @UiThreadTest
//    public void testPrioritizeRecent() throws Exception {
//        Switch s = (Switch) rule.getActivity().findViewById(R.id.recency); // Get toggle by id
//
//        // It should match the current setting
//        assert(s.isChecked() == Settings.isConsideringRecency());
//
//        // Flip the toggle
//        boolean currentValue = s.isChecked();
//        s.toggle();
//
//        // It should have changed
//        assert(currentValue != s.isChecked());
//
//        // It should match the current setting
//        assert(currentValue == Settings.getInstance().isConsideringRecency());
//    }

    @Test
    @UiThreadTest
    public void testPrioritizeProximity() throws Exception {
        Switch s = (Switch) rule.getActivity().findViewById(R.id.proximitySwitch); // Get toggle by id

        // It should match the current setting
        assert(s.isChecked() == Settings.getInstance().isConsideringProximity());

        // Flip the toggle
        boolean currentValue = s.isChecked();
        s.toggle();

        // It should have changed
        assert(currentValue != s.isChecked());

        // It should match the current setting
        assert(currentValue == Settings.getInstance().isConsideringProximity());
    }

    @Test
    @UiThreadTest
    public void testPrioritizeTOD() throws Exception {
        Switch s = (Switch) rule.getActivity().findViewById(R.id.timeOfDaySwitch); // Get toggle by id

        // It should match the current setting
        assert(s.isChecked() == Settings.getInstance().isConsideringTOD());

        // Flip the toggle
        boolean currentValue = s.isChecked();
        s.toggle();

        // It should have changed
        assert(currentValue != s.isChecked());

        // It should match the current setting
        assert(currentValue == Settings.getInstance().isConsideringTOD());
    }
}
