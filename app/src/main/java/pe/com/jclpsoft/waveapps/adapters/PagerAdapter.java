package pe.com.jclpsoft.waveapps.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;

import pe.com.jclpsoft.waveapps.DashboardFragment;
import pe.com.jclpsoft.waveapps.MainFragment;
import pe.com.jclpsoft.waveapps.TransactionFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private String mTabHeader;
    private int mSection;

    public PagerAdapter(FragmentManager fm, String mTabHeader, int mSection) {
        super(fm);
        this.mTabHeader = mTabHeader;
        this.mSection=mSection;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                switch (mSection){
                    case 1:
                        return new MainFragment();
                    case 2:
                        return new DashboardFragment();
                    case 3:
                        return new TransactionFragment();
                    case 4:
                        return null;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeader;
    }
}
