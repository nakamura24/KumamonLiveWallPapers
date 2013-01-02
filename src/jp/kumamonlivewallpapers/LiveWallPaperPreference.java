package jp.kumamonlivewallpapers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class LiveWallPaperPreference extends PreferenceActivity 
		implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
		// 設定が変更された時に呼び出されるListenerを登録
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setting.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(setting, null);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		ListPreference select = (ListPreference)getPreferenceScreen().findPreference("select");
		select.setSummary(select.getEntry());
		ListPreference locate = (ListPreference)getPreferenceScreen().findPreference("locate");
		locate.setSummary(locate.getEntry());
	}
}
