package com.example.geolocationlist;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int countTab;
    public PageAdapter(FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Gnss gnss = new Gnss();
                return gnss;
            case 1:
                Mapa mapa = new Mapa();
                return mapa;
            case 2:
                Historico historico = new Historico();
                return historico;
            case 3:
                Configuracoes configuracoes = new Configuracoes();
                return configuracoes;
            case 4:
                Creditos creditos = new Creditos();
                return creditos;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
