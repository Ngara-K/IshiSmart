package app.web.ishismart.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimelineViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments = new ArrayList<>();
    List<String> fragment_title = new ArrayList<>();

    public TimelineViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /*adding a fragment*/
    public void addFragment(Fragment fragment, String tab_title) {
        fragments.add(fragment);
        fragment_title.add(tab_title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragment_title.get(position);
    }
}
