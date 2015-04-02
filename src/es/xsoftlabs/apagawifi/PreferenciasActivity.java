package es.xsoftlabs.apagawifi;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferenciasActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		addPreferencesFromResource(R.xml.preferencias);
	}
}
